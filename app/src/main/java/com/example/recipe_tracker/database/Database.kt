package com.example.recipe_tracker.database
import androidx.room.*
import kotlinx.coroutines.flow.Flow

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

@Dao
interface IngredientDao {
    @Query("SELECT * FROM ingredients ORDER BY name COLLATE NOCASE")
    fun viewAll(): Flow<List<IngredientEntity>>

    @Insert
    suspend fun insert(ingredient: IngredientEntity)

    @Delete
    suspend fun delete(ingredient: IngredientEntity)
}


@Database(entities = [IngredientEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ingredientDao(): IngredientDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: android.content.Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "RecipeData.db"
                ).build().also { INSTANCE = it }
            }
    }
}