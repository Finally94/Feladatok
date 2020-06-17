package degubi.services;

import static degubi.Main.*;

import com.mongodb.client.model.*;
import degubi.model.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
public final class TaskTestService {
    private static final String COLLECTION_TASKS = "Tasks";
    private static final int TEST_PROCESS_TIMEOUT_SECONDS = 5;

    @GetMapping("/tasks")
    public static String[] listTaskNames() {
        var query = database.getCollection(COLLECTION_TASKS)
                            .find()
                            .projection(Projections.include("_id"));
        
        return StreamSupport.stream(query.spliterator(), false)
                            .map(k -> k.getString("_id"))
                            .toArray(String[]::new);
    }
    
    @GetMapping("/tasks/full/{taskName}")
    public static Task getTaskData(@PathVariable String taskName, @RequestHeader String pw) {
        return checkPassword(pw) ? database.findById(taskName, Task.class, COLLECTION_TASKS) : null;
    }
    
    @PutMapping("/tasks")
    public static void addNewTask(@RequestBody Task task, @RequestHeader String pw) {
        if(checkPassword(pw)) {
            database.insert(task, COLLECTION_TASKS);
        }
    }
    
    @DeleteMapping("/tasks")
    public static void deleteTaskByID(@RequestBody String taskName, @RequestHeader String pw) {
        if(checkPassword(pw)) {
            database.remove(database.findById(taskName, Task.class, COLLECTION_TASKS), COLLECTION_TASKS);
        }
    }
    
    
    @PostMapping("/tasks/java")
    public static ResponseEntity<String> handleJavaTaskTest(@RequestBody UserTaskInput input) throws IOException {
        var workDirName = String.valueOf(System.currentTimeMillis());
        var workDirPath = Files.createDirectory(Path.of(workDirName));
        var workFileName = workDirPath + "/" + input.userFileName;
        
        Files.writeString(Path.of(workFileName), input.userSource);
        
        var testProcess = Runtime.getRuntime().exec("java -jar JavaRunner.jar " + workFileName + ' ' + workDirName);
        var output = getOutputForProcess(testProcess);
        
        deleteWorkDir(workDirPath);
        return ResponseEntity.ok(output);
    }
    
    
    private static boolean checkPassword(String pw) {
        return pw.equals(System.getenv("ADMIN_PW"));
    }
    
    @SuppressWarnings("resource")
    private static String getOutputForProcess(Process testProcess) {
        try {
            var completed = testProcess.waitFor(TEST_PROCESS_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            var result = completed ? new String(testProcess.getInputStream().readAllBytes()) : "Process timed out!";
            
            testProcess.destroy();
            return result;
        } catch (InterruptedException | IOException e) {
            throw new IllegalStateException("WTF Happened here?");
        }
    }
    
    private static void deleteWorkDir(Path workDir) throws IOException {
        try(var toDelete = Files.walk(workDir)) {
            toDelete.sorted(Comparator.reverseOrder()).forEach(TaskTestService::deleteFile);
        }
    }
    
    private static void deleteFile(Path toDelete) {
        try {
            Files.delete(toDelete);
        } catch (IOException e) {}
    }
}