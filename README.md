# CS310_Team2_Project
/Introduction/

This repository is a group project that was done in my CS 310 Software Engineering 1 course. This project was completed over the course of approximately two months. We were split into teams of about five people and the focus of this group work was to utilize the Scrum process to complete this project. This project was split up into five different features and every two weeks we had a goal to achieve. Each feature was done through the process of pair programming and someone in our group usually took the lead on each feature. I will be mainly discussing the fifth feature because that is the feature that I completed.

/Description/

- This project was given to us by Professor Jay Snellen. It involves connecting to a database called “tas” which held raw information of workers’ badge ids, shift clock ins, shift clock outs, and breaks.
- We created an algorithm that pulled this data from the database and sorted it with different functions we created, but these functions were from the different features that I will not go into detail about.
- This overall project was split into five different classes.
- The classes include: “Badge.java”, “Punch.java”, “Shift.java”, “CS310_Team2_Project.java”, and “TASDatabase.java”.
- Feature five, the feature that I will describe in detail, dealed with taking information from previous features, putting it into a hash map, adding the hash map to an array list, and returning that array list as a JSON string.
- This feature involved creating the method called “getPunchListAsJSON()” and this method is under the “TASDatabase.java” class.
- This method is not under the master branch. To view it you need to change to the branch called "Final".

/Algorithm/

- The first step in creating this feature was to define the method as “getPunchListAsJSON()”.
- The next step was to declare the variables and their types used in this method. They include: “json” of type String, “adjustedPunch” of type Punch, “badge” of type String, and “date” of type String.
- We retrieve badge using the getter method “getBadgeid()” defined earlier in previous features.
- We retrieve date by importing the library called “SimpleDateFormat”.
- We define json as an empty string because nothing is inserted into it yet.
- We then define an empty array list of type string called “jsonData” and an empty hash map called “totalMinutes”.
- Referencing an array list called “punches”(this held every punch from the database), we made a for loop that looped through the entire array list.
- We then put each punch from the “punches” array list into the variable adjustedPunch.
- We then use a function defined in feature three called “adjust” to adjust each punch as they loop through the array list.
- Under the for loop, we make another empty hash map called “jsonData”
- This is the hashmap that is going to hold each item that we are going to store in the json string.
- The items that are needed to store in the json string include: “id”, “terminalid”, “eventtypeid”, “badgeid”, “eventdata”, “originaltimestamp”, “adjustedtimestamp”, and “originaltimestamp”.
- To store these items into the hash map we use the built in function called “put” which simply puts the item into the hash map as a string.
- After each item is put into the hash map, you use the built in function called “add” to put this information into the array list called “jsonData”.
- Now outside of the for loop we use the hash map defined earlier, “totalMinutes”, to put the total amount of minutes accrued (this is information from feature 4) into the hash map.
- You then add this hash map to the jsonData array list.
- The final step is to take this array list, “jsonData”, that holds all the information you want to return and put it in the empty variable “json”.
- To do this we use a built in function, from the json library that is implemented, called “toJSONString”
- Finally, we return the variable “json”.
