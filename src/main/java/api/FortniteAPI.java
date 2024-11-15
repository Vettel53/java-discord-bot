package api;

import models.PlayerStats;
import okhttp3.*;

import java.io.IOException;

public class FortniteAPI {
    private static String API_URL_TEMPLATE = "https://fortnite-api.com/v2/stats/br/v2?name=";
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

    public static void fetchPlayerStats(String StringUserName, String StringPlaylist, String StringPlatform, String StringTimeWindow, String StringInput, PlayerStatsCallback callback) {

        // Explanation of how the API url works
        // https://i.ibb.co/1GyTXH6/url-structure-1.webp

        // To construct URL we must add more variables and use the & to seperate the parameters
        // EXAMPLE: String API_URL = String.format(API_URL_TEMPLATE, playerName + "&timeWindow=season");
        String queryParams = String.format(
                "&accountType=%s&timeWindow=%s&image=%s",
                StringPlatform,
                StringTimeWindow,
                StringInput
        );

        String API_URL = String.format("%s%s", API_URL_TEMPLATE, StringUserName + queryParams);
        // EXAMPLE URL: https://fortnite-api.com/v2/stats/br/v2?name=AuroFN&accountType=epic&timeWindow=lifetime&image=none

        System.out.println(API_URL); // Debugging purposes

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

                    // put switch statement here for picking overall, solo, or squad

                    String responseData = response.body().string();
                    PlayerStats playerStats = FortniteStatsParser.parsePlayerStats(responseData, StringPlaylist);
                    // This is where we can edit to get solo, duo, squads


                    // Reference interface above
                    // On success, the onSuccess method is called containing playerStats
                    callback.onSuccess(playerStats);
                } else {
                    // Reference interface above
                    // On failure, the onError method is called containing the String(s) below
                    if (response.code() == 400) {
                        callback.onError("Invalid or missing parameter(s)...");
                    } else if (response.code() == 403) {
                        callback.onError("This players account is private...");
                    } else if (response.code() == 404) {
                        callback.onError("Account does not exist or has no stats...");
                    }
                }

            }
        });

    }

}
