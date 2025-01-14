package api;
import config.OkHttpClientSingleton;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class RandomCatAPI {
    private static final String randomCatURL = "https://api.thecatapi.com/v1/images/search";
    private static final OkHttpClient client = OkHttpClientSingleton.getInstance();

    /**
     * Fetches a random cat image URL asynchronously
     * <p>
     *     Sends HTTP request to <b>api.thecatapi.com</b> and parses the JSON response.
     * <p>
     *     The JSON response contains URL key (<b>"url"</b>) pointing to a URL of a random cat.
     * </p>
     *     If unsuccessful, returns <b>null</b>}.
     * </p>
     *
     * @return A CompletableFuture that holds a String (Random Duck URL).
     */
    public static CompletableFuture<String> fetchRandomCat() {
        // TODO: Convert all animal apis to asynchronous
        return CompletableFuture.supplyAsync(() -> {

            Request request = new Request.Builder()
                    .url(randomCatURL)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body()!= null) {
                    String responseBody = response.body().string();

                    /*
                        NOTE: This cat api gives a JSONArray and not a regular JSONObject like the other API's
                        So, we need to parse the response as a JSONArray and then get the first object (the only one)
                        to get the "url" field.
                     */

                    JSONArray jsonArray = new JSONArray(responseBody);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String catImageURL = jsonObject.getString("url");

                    return catImageURL;
                } else {
                    System.out.println("Failed to fetch random cat: " + response.code());
                    return null;
                }
            } catch (IOException e) {
                System.err.println("Error fetching random cat: " + e.getMessage());
                return null;
            } catch (Exception e) {
                System.err.println("Error parsing JSON response: " + e.getMessage());
                return null;
            }

        });
    }
}
