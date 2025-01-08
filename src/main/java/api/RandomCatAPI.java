package api;
import config.OkHttpClientSingleton;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class RandomCatAPI {
    private static final String randomCatURL = "https://api.thecatapi.com/v1/images/search";
    private static final OkHttpClient client = OkHttpClientSingleton.getInstance();

    public static String fetchRandomCat() {
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

                System.out.println("Random Cat URL: " + catImageURL);

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
    }
}
