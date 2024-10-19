package api;

import models.PlayerStats;
import org.json.JSONObject;

public class FortniteStatsParser {

    private static String jsonFortniteStats;

    /** TODO: proper commenting, /** then enter
     *
     * @param jsonDataToParse
     * @return
     */

    public static PlayerStats parsePlayerStats(String jsonDataToParse) {
        jsonFortniteStats = jsonDataToParse;
        JSONObject jsonObject = new JSONObject(jsonFortniteStats);

        // Retrieve playerName, store each JSON object in each variable for later retrieval
        JSONObject dataObject = jsonObject.getJSONObject("data");
        JSONObject accountObject = dataObject.getJSONObject("account");
        String playerName = accountObject.getString("name");

        // Same as above, retrieve battle pass level
        JSONObject battlePassObject = dataObject.getJSONObject("battlePass");
        int battlePassLevel = battlePassObject.getInt("level");

        // Stats JSONObjects where actual game stats are stored
        JSONObject statsObject = dataObject.getJSONObject("stats");
        JSONObject allStatsObject = statsObject.getJSONObject("all");
        JSONObject overallStatsObject = allStatsObject.getJSONObject("overall");

        // Extract and print relevant data from JSON
        int kills = overallStatsObject.getInt("kills");
        int wins = overallStatsObject.getInt("wins");
        int minutesPlayed = overallStatsObject.getInt("minutesPlayed");
        double killDeathRatio = overallStatsObject.getDouble("kd");
        String lastUpdatedData = overallStatsObject.getString("lastModified");


        System.out.println("Kills: " + kills);
        System.out.println("wins: " + wins);
        System.out.println("minutesPlayed: " + minutesPlayed);
        System.out.printf("K/D Ratio: %.2f%n", killDeathRatio);
        System.out.println("lastUpdatedData: " + lastUpdatedData);

        // Extract relevant data from JSON and return relevant data

        System.out.println("Player Name: " + playerName);
        System.out.println("Battle Pass Level: " + battlePassLevel);

        // Extract relevant data from JSON and return relevant data


        return new PlayerStats(playerName, battlePassLevel, kills, wins, minutesPlayed, killDeathRatio, lastUpdatedData);
    }

}
