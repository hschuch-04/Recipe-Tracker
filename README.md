# Recipe Tracker

<img wwidth="270" height="585" alt="Screenshot_20261108_154508_Recipe-Tracker" src="https://github.com/user-attachments/assets/ff364d34-5d9d-48a9-aad8-24d6ae085520" />
<img width="270" height="585" alt="Screenshot_20261108_154503_Recipe-Tracker" src="https://github.com/user-attachments/assets/97059ffb-ac78-4e70-a177-4bd0cda6998b" />
<img width="270" height="585" alt="Screenshot_20261108_154419_Recipe-Tracker" src="https://github.com/user-attachments/assets/70bc513b-1383-470d-8e06-57ba6cb81a4d" />

## Introduction
The Recipe Tracker is a native Android Application made with the Jetpack Compose framework and uses Rooms as local storage of the information. When deciding what application I wanted to make to learn how to use Rooms with Android Development, I chose to do a recipe styled application as I had recently got into experimenting with my cooking and my partner always talks about needing to save my recipes somewhere. While I can just save these in a word or google document, I thought this could be a fun learning exercise on how to create something similar on my phone.

When coming up with the needs for the application, it only needed a couple of requirements:
- It needed to have the ability to input your own ingredients and store them. As I don't want to write them out everytime like you would have to in a word document.
- And it would require recipes to grab those ingredients and have a simple textbox for me to input my steps

These were the main requirements and it didn't need to be super accurate. I didn't want to make the new MyFitnessPal, it just had to store and allow me to write things up to view later.

## My Learnings

This project greatly helped me understand the use of ViewModels and how they can hold logic and storing state. This was extremely important when it came to moving between different pages using the NavController keeping its state and kept the logic of the database not built into the screen itself. This was my first attempt at using Rooms also, and it took me sometime to get my head around creating the table, DAO, database and then using the ViewModel. What helped me understand this was comparing it to similar project using React and Node where you have your functions creating the database, tables, queries, and then use node as a way to interact with the Front End of the project.


## The Results

Overall, I am happy with the outcome of the application currently as a MVP since it meets the general requirements set. In the future I would like the update the Add Ingredients Dialog to its own screen (as I have added a placeholder in the nav) and then also when storing lots of ingredients creating a better system to pick and choose from, such as a search bar and turning it into a scrollable list to still keep it on the one page.



