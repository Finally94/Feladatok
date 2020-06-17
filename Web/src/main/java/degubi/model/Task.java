package degubi.model;

import javax.json.bind.annotation.*;
import org.springframework.data.annotation.*;

public final class Task {
    
    @Id
    public final String name;
    public final String pdfURL;
    public final String consoleInput;
    
    @JsonbCreator
    @PersistenceConstructor
    public Task(@JsonbProperty("name") String name,
                @JsonbProperty("pdfURL") String pdfURL,
                @JsonbProperty("consoleInput") String consoleInput) {
        
        this.name = name;
        this.pdfURL = pdfURL;
        this.consoleInput = consoleInput;
    }
}