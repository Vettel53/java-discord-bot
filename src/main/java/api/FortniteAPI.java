package api;

import config.OkHttpClientSingleton;
import models.PlayerStats;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import services.FortniteStatsParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

/**
 * This class provides methods to interact with the Fortnite API.
 */
public class FortniteAPI {
    private static final String API_URL_TEMPLATE = "https://fortnite-api.com/v2/stats/br/v2?name=";
    private static final OkHttpClient client = OkHttpClientSingleton.getInstance();
    private static String API_KEY = getAPIKey();

    /**
     * Retrieves the API key from a file (fortniteToken.txt) and returns it.
     * Make sure the file is located in the <b>config package/folder</b>
     * @return The Fortnite API key.
     */

    public static String getAPIKey() {
        try {
            File myToken = new File("src/main/java/config/fortniteToken.txt");
            Scanner myReader = new Scanner(myToken);
            while (myReader.hasNextLine()) {
                API_KEY = myReader.nextLine();
                System.out.println("Your token is: " + API_KEY);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the file... " + e.getMessage());
            System.out.println("Make sure the file (fortniteToken.txt) is located in the config folder!");
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.out.println("Error getting API key (" + API_KEY + "): " + e.getMessage());
            System.out.println("Verify your API key is entered correctly by itself!");
            e.printStackTrace();
            System.exit(1);
        }
        return API_KEY;
    }

    /**
     * Fetches player statistics asynchronously using the provided parameters.
     *
     * @param StringUserName The username of the player.
     * @param StringPlaylist The playlist for which the stats should be fetched.
     * @param StringPlatform The platform (e.g., epic, steam).
     * @param StringTimeWindow The time window for which the stats should be fetched.
     * @param StringInput Additional input parameters for the API request.
     * @return A CompletableFuture that completes with the fetched player statistics.
     */
    public static CompletableFuture<PlayerStats> fetchPlayerStats(String StringUserName, String StringPlaylist, String StringPlatform, String StringTimeWindow, String StringInput) {

        // Explanation of how the API url works
        // https://i.ibb.co/1GyTXH6/url-structure-1.webp

        // To construct URL we must add more variables and use the & to separate the parameters
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


    }

//    public static CompletableFuture<PlayerStats> fetchItemShop(String StringUserName, String StringPlaylist, String StringPlatform, String StringTimeWindow, String StringInput) {
//
//        // Explanation of how the API url works
//        // https://i.ibb.co/1GyTXH6/url-structure-1.webp
//
//        // To construct URL we must add more variables and use the & to separate the parameters
//        // EXAMPLE: String API_URL = String.format(API_URL_TEMPLATE, playerName + "&timeWindow=season");
//        String queryParams = String.format(
//                "&accountType=%s&timeWindow=%s&image=%s",
//                StringPlatform,
//                StringTimeWindow,
//                StringInput
//        );
//
//        String API_URL = String.format("%s%s", API_URL_TEMPLATE, StringUserName + queryParams);
//        // EXAMPLE URL: https://fortnite-api.com/v2/stats/br/v2?name=AuroFN&accountType=epic&timeWindow=lifetime&image=none
//
//        System.out.println(API_URL); // Debugging purposes
//
//        Request request = new Request.Builder()
//                .url(API_URL)
//                .header("Authorization", "3b83ac55-313f-4330-a361-55bacf777a6b")
//                .build();
//
//        return CompletableFuture.supplyAsync(() -> {
//            try (Response response = client.newCall(request).execute()) {
//                if (!response.isSuccessful()) {
//                    throw new RuntimeException("API error: " + response.code());
//                }
//
//                if (response.body() == null) {
//                    throw new RuntimeException("Response body is null.");
//                }
//
//                String responseData = response.body().string();
//                return responseData; // return just the string instead of 'response'
//            } catch (IOException e) {
//                throw new RuntimeException("Network error occurred", e);
//            }
//        }).thenApply(responseData -> {
//            // Now 'responseData' is a fully-read string, safe to parse
//            return FortniteStatsParser.parsePlayerStats(responseData, StringPlaylist);
//        }).exceptionally(e -> {
//            System.out.println("Error parsing response: " + e.getMessage());
//            return null;
//        });
//    }

}
