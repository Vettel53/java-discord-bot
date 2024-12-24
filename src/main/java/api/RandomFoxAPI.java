package api;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;

public class RandomFoxAPI {
    private static final String randomFoxURL = "https://randomfox.ca/api";
    private static final OkHttpClient client = new OkHttpClient();

    public static String fetchRandomFox() {
        Request request = new Request.Builder()
               .url(randomFoxURL)
               .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body()!= null) {
                String responseBody = response.body().string();

                JSONObject json = new JSONObject(responseBody);
                String foxImageURL = json.getString("image");

                System.out.println("Random Fox URL: " + foxImageURL);

                return foxImageURL;
            } else {
                System.out.println("Failed to fetch random fox: " + response.code());
                return null;
            }
        } catch (IOException e) {
            System.err.println("Error fetching random fox: " + e.getMessage());
            return null;
        }
    }
}
