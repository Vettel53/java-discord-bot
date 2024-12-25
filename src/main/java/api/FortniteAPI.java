package api;

import models.PlayerStats;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import services.FortniteStatsParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class FortniteAPI {
    private static String API_URL_TEMPLATE = "https://fortnite-api.com/v2/stats/br/v2?name=";
    private static final OkHttpClient client = new OkHttpClient();
    private static String API_KEY = getAPIKey();

//    public static void main(String[] args) {
//        //fetchPlayerStats();
//    }

    public static String getAPIKey() {
        try {
            File myToken = new File("src/fortniteToken.txt");
            Scanner myReader = new Scanner(myToken);
            while (myReader.hasNextLine()) {
                API_KEY = myReader.nextLine();
                System.out.println("Your token is: " + API_KEY);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the file...");
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.out.println("Error getting API key: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        return API_KEY;
    }

    public static CompletableFuture<PlayerStats> fetchPlayerStats(String StringUserName, String StringPlaylist, String StringPlatform, String StringTimeWindow, String StringInput) {

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
                .header("Authorization", API_KEY)
                .build();

        return CompletableFuture.supplyAsync(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new RuntimeException("API error: " + response.code());
                }

                if (response.body() == null) {
                    throw new RuntimeException("Response body is null.");
                }

                String responseData = response.body().string();
                return responseData; // return just the string instead of 'response'
            } catch (IOException e) {
                throw new RuntimeException("Network error occurred", e);
            }
        }).thenApply(responseData -> {
            // Now 'responseData' is a fully-read string, safe to parse
            return FortniteStatsParser.parsePlayerStats(responseData, StringPlaylist);
        }).exceptionally(e -> {
            System.out.println("Error parsing response: " + e.getMessage());
            return null;
        });


//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                callback.onError("Error during request: " + e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//
//                    // put switch statement here for picking overall, solo, or squad
//
//                    String responseData = response.body().string();
//                    PlayerStats playerStats = FortniteStatsParser.parsePlayerStats(responseData, StringPlaylist);
//                    // This is where we can edit to get solo, duo, squads
//
//
//                    // Reference interface above
//                    // On success, the onSuccess method is called containing playerStats
//                    callback.onSuccess(playerStats);
//                } else {
//                    // Reference interface above
//                    // On failure, the onError method is called containing the String(s) below
//                    if (response.code() == 400) {
//                        callback.onError("Invalid or missing parameter(s)...");
//                    } else if (response.code() == 403) {
//                        callback.onError("This players account is private...");
//                    } else if (response.code() == 404) {
//                        callback.onError("Account does not exist or has no stats...");
//                    }
//                }
//
//            }
//        });

    }

    public static CompletableFuture<PlayerStats> fetchItemShop(String StringUserName, String StringPlaylist, String StringPlatform, String StringTimeWindow, String StringInput) {

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

        return CompletableFuture.supplyAsync(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new RuntimeException("API error: " + response.code());
                }

                if (response.body() == null) {
                    throw new RuntimeException("Response body is null.");
                }

                String responseData = response.body().string();
                return responseData; // return just the string instead of 'response'
            } catch (IOException e) {
                throw new RuntimeException("Network error occurred", e);
            }
        }).thenApply(responseData -> {
            // Now 'responseData' is a fully-read string, safe to parse
            return FortniteStatsParser.parsePlayerStats(responseData, StringPlaylist);
        }).exceptionally(e -> {
            System.out.println("Error parsing response: " + e.getMessage());
            return null;
        });
    }

}
