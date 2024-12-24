import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskTrackerCLI {
    private static final String FILE_NAME = "tasks.json";
    private static JSONArray tasks;

    public static void main(String[] args){
        loadTasks();

        if(args.length ==0){
            System.out.println("Usage: tasks-cli <command> [options]");
            return;
        }

        switch(args[0].toLowerCase()){
            case "add":
                if(args.length < 2){
                    System.out.println("Error: please provide a task description");
                }
                else{
                    addTask(args[1]);
                }
                break;
            case "update":
                if(args.length < 3){
                    System.out.println("Error: please provide a task Id and new description.");
                }
                else{
                    updateTask(Integer.parseInt(args[1]), args[2]);
                }
                break;
            case "delete":
                if(args.length < 2){
                    System.out.println("Error: please provide a task Id");
                }
                else{
                    deleteTask(Integer.parseInt(args[1]));
                }
                break;
            case "mark-done":
                if(args.length < 2){
                    System.out.println("Error: please provide a task Id");
                }
                else{
                    updateStatus(Integer.parseInt(args[1]), "done");
                }
                break;
            case "list":
                if(args.length == 1){
                    listTasks(null);
                }
                else{
                    listTasks(args[1]);
                }
                break;
            default:
                System.out.println("Unknown command.");
        }
    }

    private static void loadTasks(){
        try{
            File file = new File(FILE_NAME);
            if(!file.exists()){
                tasks = new JSONArray();
                saveTasks();
            }
            else{
                String content = new String(Files.readAllBytes(Paths.get(FILE_NAME)));
                tasks = new JSONArray(content);
            }
        }catch (IOException e){
            System.err.println("Error loading tasks: "+e.getMessage());
            tasks = new JSONArray();
        }
    }

    private static void saveTasks(){
        try(FileWriter writer = new FileWriter(FILE_NAME)){
            writer.write(tasks.toString(4));
        }
        catch (IOException e){
            System.err.println("Error saving tasks: "+e.getMessage());
        }
    }

    private static void addTask(String description){
        JSONObject task = new JSONObject();
        int id = tasks.length() == 0 ? 1: tasks.getJSONObject(tasks.length()-1).getInt("id") +1;
        task.put("id",id);
        task.put("description", description);
        task.put("status","todo");
        task.put("createdAt",getCurrentDateTime());
        task.put("updatedAt",getCurrentDateTime());
        tasks.put(task);
        saveTasks();
        System.out.println("Task added successfully (ID: "+ id + ")");
    }

    private static void updateTask(int id, String description){
        JSONObject task = findTaskById(id);
        if(task != null){
            task.put("description", description);
            task.put("updatedAt",getCurrentDateTime());
            saveTasks();
            System.out.println("Task updated successfully.");
        }
        else{
            System.out.println("Task not found");
        }
    }

    private static void deleteTask(int id){
        for(int i = 0; i< tasks.length(); i++){
            if(tasks.getJSONObject(i).getInt("id") ==id){
                tasks.remove(i);
                saveTasks();
                System.out.println("Task deleted successfully");
                return;
            }
        }
        System.out.println("Task not found");
    }

    private static void updateStatus(int id, String status){
        JSONObject task = findTaskById(id);
        if(task != null){
            task.put("status", status);
            task.put("updateAt", getCurrentDateTime());
            saveTasks();
            System.out.println("Task status updated to "+ status + ".");
        }
        else{
            System.out.println("Task not found.");
        }
    }

    private static void listTasks(String statusFilter){
        for(int i= 0; i< tasks.length(); i++){
            JSONObject task = tasks.getJSONObject(i);
            String status = task.getString("status");
            if(statusFilter == null || status.equalsIgnoreCase(statusFilter)){
                System.out.println("ID: "+ task.getInt("id") +
                        ",Description: " + task.getString("description") +
                        ",Status: " + task.getString("status") +
                        ",CreatedAt: " + task.getString("createdAt") +
                        ",UpdatedAt: " + task.getString("updatedAt"));
            }
        }
    }

    private static JSONObject findTaskById(int id){
        for(int i = 0; i< tasks.length(); i++){
            if(tasks.getJSONObject(i).getInt("id") == id){
                return tasks.getJSONObject(i);
            }
        }
        return null;
    }
    private static String getCurrentDateTime(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}

