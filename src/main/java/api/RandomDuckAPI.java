package api;

import config.OkHttpClientSingleton;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class RandomDuckAPI {

    private static final String randomDuckURL = "https://random-d.uk/api/v2/random";
    private static final OkHttpClient client = OkHttpClientSingleton.getInstance();

    /**
     * Fetches a random duck image URL asynchronously
     * <p>
     *     Sends HTTP request to <b>random-d.uk</b> and parses the JSON response.
     * <p>
     *     The JSON response contains URL key (<b>"url"</b>) pointing to a URL of a random duck.
     * </p>
     *     If unsuccessful, returns <b>null</b>}.
     * </p>
     *
     * @return A CompletableFuture that holds a String (Random Duck URL).
     */
    public static CompletableFuture<String> fetchRandomDuck() {

        return CompletableFuture.supplyAsync(() -> {
            Request request = new Request.Builder()
                    .url(randomDuckURL)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();

                    JSONObject json = new JSONObject(responseBody);
                    String duckImageURL = json.getString("url");

                    return duckImageURL;
                } else {
                    System.out.println("Failed to fetch random duck: " + response.code());
                    return null;
                }
            } catch (IOException e) {
                System.err.println("Error fetching random duck: " + e.getMessage());
                return null;
            } catch (Exception e) {
                System.err.println("Error parsing JSON response: " + e.getMessage());
                return null;
            }
        });
    }

}
