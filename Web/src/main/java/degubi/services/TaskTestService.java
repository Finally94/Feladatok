package degubi.services;

import degubi.model.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
public final class TaskTestService {
    
    private static final int TEST_PROCESS_TIMEOUT_SECONDS = 5;
    
    @PostMapping("/java")
    public static ResponseEntity<String> handleJavaTask(@RequestBody TaskInput input) throws IOException {
        var workDirName = String.valueOf(System.currentTimeMillis());
        var workDirPath = Files.createDirectory(Path.of(workDirName));
        var workFileName = workDirPath + "/" + input.userFileName;
        
        Files.writeString(Path.of(workFileName), input.userSource);
        
        var testProcess = Runtime.getRuntime().exec("java -jar JavaRunner.jar " + workFileName + ' ' + workDirName);
        var output = getOutputForProcess(testProcess);
        
        deleteWorkDir(workDirPath);
        return ResponseEntity.ok(output);
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