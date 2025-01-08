package api;
import config.OkHttpClientSingleton;
import org.json.JSONObject;
import okhttp3.*;
import java.io.IOException;

public class RandomDogAPI {
    private static final String randomDogURL = "https://random.dog/woof.json";
    private static final OkHttpClient client = OkHttpClientSingleton.getInstance();

    public static String fetchRandomDog() {

        Request request = new Request.Builder()
                .url(randomDogURL)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JSONObject json = new JSONObject(responseBody);
                String dogImageURL = json.getString("url");

                System.out.println("Random Dog URL: " + dogImageURL);
                if (dogImageURL.endsWith(".mp4") || dogImageURL.endsWith(".webm")) { // Discord embed messages don't support MP4/webm files
                    System.out.println("Dog API called"  + dogImageURL);
                    return fetchRandomDog(); // Recursively generate another random dog url
                } else {
                    return dogImageURL;
                }
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
    }
}
