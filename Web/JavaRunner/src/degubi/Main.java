package degubi;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import javax.tools.ToolProvider;

public final class Main {

    public static void main(String[] args) throws Exception {
        var compilerOutput = new CharArrayWriter(0);
        var userClass = loadUserMainClass(args[0], args[1], compilerOutput);
        
        if(userClass == null) {
            System.out.println("Nem sikerült tesztelni a bemenetet fordítási hiba miatt:\n" + compilerOutput.toString());
        }else{
            var inputLines = new String[] { "hahi" };
            
            var userOutput = new ByteArrayOutputStream();
            var syso = new PrintStream(userOutput);
            var sysin = new ByteArrayInputStream(String.join("\n", inputLines).getBytes());

            userClass.getMethod("main", PrintStream.class, InputStream.class).invoke(null, syso, sysin);
            
            System.out.println(userOutput);
        }
    }
    
    private static Class<?> loadUserMainClass(String sourceFileName, String workDir, CharArrayWriter compilerOutput) {
        var compiler = ToolProvider.getSystemJavaCompiler();

        try(var fileManager = compiler.getStandardFileManager(null, null, null)) {
            var sourceFiles = fileManager.getJavaFileObjectsFromStrings(List.of(sourceFileName));
            var compileSuccessfull = compiler.getTask(compilerOutput, fileManager, null, null, null, sourceFiles).call().booleanValue();
            
            return compileSuccessfull ? loadUserClass(sourceFileName, workDir) : null;
        } catch (IOException e) {
            return null;
        }
    }
    
    @SuppressWarnings("resource")
    private static Class<?> loadUserClass(String sourceFileName, String workDir) throws MalformedURLException {
        var className = sourceFileName.substring(sourceFileName.indexOf('/') + 1, sourceFileName.lastIndexOf('.'));
        var loadDirectory = Path.of(workDir).toUri().toURL();

        try {
            return new URLClassLoader(new URL[]{ loadDirectory }).loadClass(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
    
    private Main() {}
}