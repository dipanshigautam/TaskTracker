#Submission done at 
https://roadmap.sh/projects/task-tracker as well

# TaskTracker
Task tracker is a project used to track and manage tasks.

## Dependency
- `json-java` library is required to handle JSON files.
- Add the `.jar` file to the classpath while compiling and running the program.

# Task Tracker CLI
A simple command-line interface (CLI) to manage tasks. Tasks are stored in a JSON file.

## Features
- Add, update, and delete tasks.
- Mark tasks as in-progress or done.
- List tasks by status.

## Command to run in CL
- Add a task:
  ```bash
  javac -cp .:json-*<putYourJarVersionHere>.jar TaskTrackerCLI.java
  java -cp libs/json-*<putYourJarVersionHere>.jar:src TaskTrackerCLI add "Task description"

## Tip for easy development
- Keep json jar in same folder as your main class eg. json-20240303.jar
- Check tasks.json file for sample payload creation by program
- Complete program is just one java file.

