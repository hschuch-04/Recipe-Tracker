package com.example.recipe_tracker.database
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow
import androidx.room.Index
import androidx.room.Junction
import androidx.room.Relation
import androidx.room.Transaction
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// Ingredients Table
@Entity (tableName = "ingredients")
data class IngredientEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val grams: Int,
    val calories: Int,
    val protein: Double,
    val fat: Double,
    val carbs: Double
)

// Recipes Table
@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val instructions: String
)

// The Linking Table for recipes which stores the ingredients
@Entity(
    tableName = "recipe_ingredients",
    primaryKeys = ["recipeId", "ingredientId"],
    foreignKeys = [
        ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = ["id"],
            childColumns = ["recipeId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = IngredientEntity::class,
            parentColumns = ["id"],
            childColumns = ["ingredientId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("ingredientId")]
)
data class RecipeIngredientRef(
    val recipeId: Long,
    val ingredientId: Long,
)

data class RecipeWithIngredients(
    @Embedded val recipe: RecipeEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = RecipeIngredientRef::class,
            parentColumn = "recipeId",
            entityColumn = "ingredientId"
        )
    )
    val ingredients: List<IngredientEntity>
)


// Ingredients interface endpoints
@Dao
interface IngredientDao {
    @Query("SELECT * FROM ingredients ORDER BY name COLLATE NOCASE")
    fun viewAll(): Flow<List<IngredientEntity>>

    @Insert
    suspend fun insert(ingredient: IngredientEntity)

    @Delete
    suspend fun delete(ingredient: IngredientEntity)
}

// Recipes Database endpoints
@Dao
abstract class RecipeDao {
    @Transaction
    @Query("SELECT * FROM recipes ORDER BY name COLLATE NOCASE")
    abstract fun viewAll(): Flow<List<RecipeWithIngredients>>

    @Insert
    abstract suspend fun insertRecipe(recipe: RecipeEntity): Long

    @Insert
    abstract suspend fun insertRef(refs: List<RecipeIngredientRef>)

    @Transaction
    open suspend fun insertRecipeWithIngredients(recipe: RecipeEntity, ingredientsIds: List<Long>) {
        val recipeId = insertRecipe(recipe)
        insertRef(ingredientsIds.map { RecipeIngredientRef(recipeId, it) })
    }

    @Delete
    abstract suspend fun deleteRecipe(recipe: RecipeEntity)
}

/// The Database Class Setup and Creation
@Database(
    entities = [IngredientEntity::class, RecipeEntity::class, RecipeIngredientRef::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ingredientDao(): IngredientDao
    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `recipes` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `instructions` TEXT NOT NULL)"
                )
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `recipe_ingredients` (`recipeId` INTEGER NOT NULL, `ingredientId` INTEGER NOT NULL, PRIMARY KEY(`recipeId`, `ingredientId`), FOREIGN KEY(`recipeId`) REFERENCES `recipes`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE, FOREIGN KEY (`ingredientId`) REFERENCES `ingredients`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)"
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_recipe_ingredients_ingredientId` ON `recipe_ingredients` (`ingredientId`)")
            }
        }

        fun get(context: android.content.Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "RecipeData.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also { INSTANCE = it }
            }
    }
}