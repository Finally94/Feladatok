package degubi.model;

import javax.json.bind.annotation.*;

public final class UserTaskInput {
    
    public final String userSource;
    public final String userFileName;
    public final String taskName;

    @JsonbCreator
    public UserTaskInput(@JsonbProperty("userSource") String rawUserSource,
                         @JsonbProperty("userFileName") String userFileName,
                         @JsonbProperty("taskName") String taskName) {
        
        this.userSource = prepareUserSource(rawUserSource);
        this.userFileName = userFileName;
        this.taskName = taskName;
    }
    
    private static String prepareUserSource(String rawUserSource) {
        return rawUserSource.replace("String[] args", "java.io.PrintStream out, java.io.InputStream in")
                            .replace("System.out", "out")
                            .replace("System.in", "in");
    }
}