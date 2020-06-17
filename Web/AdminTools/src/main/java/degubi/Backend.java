package degubi;

import java.io.*;
import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.*;
import java.net.http.HttpResponse.*;
import java.nio.charset.*;
import javax.json.*;
import javax.json.bind.*;
import org.eclipse.yasson.*;

public final class Backend {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Jsonb json = JsonbBuilder.create(new JsonbConfig().withPropertyVisibilityStrategy(new FieldAccessStrategy()));
    private static final String BACKEND_URL = "http://localhost:8080";
    private static final String PW = System.getenv("ADMIN_PW");
    
    private Backend() {}
    
    public static BodyHandler<JsonObject> ofJsonObject() {
        return info -> BodySubscribers.mapping(BodySubscribers.ofString(StandardCharsets.UTF_8), data -> json.fromJson(data, JsonObject.class));
    }
    
    public static BodyHandler<JsonArray> ofJsonArray() {
        return info -> BodySubscribers.mapping(BodySubscribers.ofString(StandardCharsets.UTF_8), data -> json.fromJson(data, JsonArray.class));
    }
    
    public static<T> HttpResponse<T> sendGetRequest(String apiPoint, BodyHandler<T> bodyHandler) {
        return sendRequest(newRequest(apiPoint).build(), bodyHandler);
    }
    
    public static void sendPutRequest(String apiPoint, Object bodyObject) {
        sendRequest(newRequest(apiPoint).method("PUT", BodyPublishers.ofString(json.toJson(bodyObject))).build(), BodyHandlers.discarding());
    }
    
    public static void sendDeleteRequest(String apiPoint, Object bodyObject) {
        sendRequest(newRequest(apiPoint).method("DELETE", BodyPublishers.ofString(bodyObject.toString())).build(), BodyHandlers.discarding());
    }
    
    
    private static HttpRequest.Builder newRequest(String apiPoint) {
        return HttpRequest.newBuilder(URI.create(BACKEND_URL + apiPoint)).header("Content-Type", "application/json").header("pw", PW);
    }
    
    private static<T> HttpResponse<T> sendRequest(HttpRequest request, BodyHandler<T> handler) {
        try {
            return client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new IllegalStateException("HUH?");
        }
    }
}