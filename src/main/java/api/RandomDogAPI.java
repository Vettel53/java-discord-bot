package api;
import config.OkHttpClientSingleton;
import org.json.JSONObject;
import okhttp3.*;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class RandomDogAPI {
    private static final String randomDogURL = "https://random.dog/woof.json?filter=mp4,webm"; // Filter blocks those extensions
    private static final OkHttpClient client = OkHttpClientSingleton.getInstance();

    /**
     * Fetches a random dog image URL asynchronously
     * <p>
     *     Sends HTTP request to <b>random.dog</b> and parses the JSON response.
     *
     *     The JSON response contains URL key (<b>"url"</b>) pointing to a URL of a random dog.
     *
     *     If unsuccessful, returns <b>null</b>}.
     * </p>
     *
     * @return A CompletableFuture that holds a String (Random Dog URL).
     */
    public static CompletableFuture<String> fetchRandomDog() {
        return CompletableFuture.supplyAsync(() -> {
            Request request = new Request.Builder()
                    .url(randomDogURL)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    JSONObject json = new JSONObject(responseBody);
                    String dogImageURL = json.getString("url");

                    return dogImageURL;
                } else {
                    System.out.println("Failed to fetch random dog: " + response.code());
                    return null;
                }
            } catch (IOException e) {
                System.err.println("Error fetching random dog: " + e.getMessage());
                return null;
            } catch (Exception e) {
                System.err.println("Error parsing JSON response: " + e.getMessage());
                return null;
            }
        });
    }

}
