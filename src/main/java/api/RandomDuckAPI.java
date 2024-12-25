package api;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;

public class RandomDuckAPI {

    private static final String randomDuckURL = "https://random-d.uk/api/v2/random";
    private static final OkHttpClient client = new OkHttpClient();

    // TODO: Implement random choosing of jpg or gif?
    public static String fetchRandomDuck() {
        Request request = new Request.Builder()
                .url(randomDuckURL)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body()!= null) {
                String responseBody = response.body().string();

                JSONObject json = new JSONObject(responseBody);
                String duckImageURL = json.getString("url");

                System.out.println("Random Duck URL: " + duckImageURL);

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
    }

}
