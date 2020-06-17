package degubi;

import static java.lang.System.*;

import com.mongodb.client.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.mongo.*;
import org.springframework.data.mongodb.core.*;

@SpringBootApplication(exclude = MongoAutoConfiguration.class)
public /*non-final*/ class Main {
    //public static final MongoTemplate database = new MongoTemplate(MongoClients.create(getenv("DB_CONNECTION")), getenv("DB_NAME"));

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}