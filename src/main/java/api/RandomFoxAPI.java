package api;
import config.OkHttpClientSingleton;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class RandomFoxAPI {
    private static final String randomFoxURL = "https://randomfox.ca/api";
    private static final OkHttpClient client = OkHttpClientSingleton.getInstance();

    /**
     * Fetches a random fox image URL asynchronously
     * <p>
     *     Sends HTTP request to <b>randomfox.ca</b> and parses the JSON response.
     * <p>
     *     The JSON response contains URL key (<b>"image"</b>) pointing to a URL of a random fox.
     * </p>
     *     If unsuccessful, returns <b>null</b>}.
     * </p>
     *
     * @return A CompletableFuture that holds a String (Random Fox URL).
     */
    public static CompletableFuture<String> fetchRandomFox() {


        return CompletableFuture.supplyAsync(() -> {
            Request request = new Request.Builder()
                    .url(randomFoxURL)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();

                    JSONObject json = new JSONObject(responseBody);
                    String foxImageURL = json.getString("image");

                    return foxImageURL;
                } else {
                    System.out.println("Failed to fetch random fox: " + response.code());
                    return null;
                }
            } catch (IOException e) {
                System.err.println("Error fetching random fox: " + e.getMessage());
                return null;
            } catch (Exception e) {
                System.err.println("Error parsing JSON response: " + e.getMessage());
                return null;
            }
        });
    }
}
