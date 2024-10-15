package api;

import models.PlayerStats;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Call;
import okhttp3.Callback;
import java.io.IOException;
import static api.FortniteStatsParser.parsePlayerStats;

public class FortniteAPI {
    // TODO: implement string concatenation to allow multiple parameters for API_URL
    private static String API_URL_TEMPLATE = "https://fortnite-api.com/v2/stats/br/v2?name=%s";
    private static final OkHttpClient client = new OkHttpClient();

    public static void main(String[] args) {
        //fetchPlayerStats();
    }

    public interface PlayerStatsCallback {
        // Callback interface
        // Abstract methods, must implement if this interface is used
        void onSuccess(PlayerStats playerStats);
        void onError(String errorMessage);
    }

    public static void fetchPlayerStats(String playerName, PlayerStatsCallback callback) {

        // Explanation of how the API url works
        // https://i.ibb.co/1GyTXH6/url-structure-1.webp

        // To construction URL we must add more variables and use the & to seperate the parameters
        // EXAMPLE: String API_URL = String.format(API_URL_TEMPLATE, playerName + "&timeWindow=season");
        String API_URL = String.format(API_URL_TEMPLATE, playerName);

        System.out.println(API_URL);

        Request request = new Request.Builder()
                .url(API_URL)
                .header("Authorization", "3b83ac55-313f-4330-a361-55bacf777a6b")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Error during request: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    PlayerStats playerStats = FortniteStatsParser.parsePlayerStats(responseData);

                    // Reference interface above
                    // On success, the onSuccess method is called containing playerStats
                    callback.onSuccess(playerStats);
                } else {
                    // Reference interface above
                    // On failure, the onError method is called containing the String below
                    callback.onError("Request failed: " + response.code());
                }
            }
        });

    }

//    public static String buildAPIURL(String playerName, String accountType, String timeWindow, String image) {
//        return "";
//    } TODO: Implement proper building API URL method.

}
