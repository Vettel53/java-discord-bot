package services;

import models.PlayerStats;
import org.json.JSONObject;

public class FortniteStatsParser {

    private static String jsonFortniteStats;

     /**
     * Parses a JSON string containing Fortnite player statistics and returns a PlayerStats object.
     *
     * @param jsonDataToParse A JSON string containing Fortnite player statistics.
     *                        The JSON string should be in the format returned by the Fortnite API.
     * @param StringPlaylist A string representing the Fortnite playlist for which the statistics are to be parsed.
     *                       The supported values are "overall", "solo", "duo", "squad", and "ltm".
     * @return A PlayerStats object containing the correctly parsed statistics.
     */
    public static PlayerStats parsePlayerStats(String jsonDataToParse, String StringPlaylist) {
        jsonFortniteStats = jsonDataToParse;

        //System.out.println(jsonFortniteStats);

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

        // Variables to be stored in PlayerStats object
        int kills = 0;
        int wins = 0;
        int minutesPlayed = 0;
        double killDeathRatio = 0.0;
        String lastUpdatedData = "";
        switch (StringPlaylist) {
            case "overall":
                // Extract and print relevant data from **OVERALL** JSON object
                JSONObject overallStatsObject = allStatsObject.getJSONObject("overall");
                kills = overallStatsObject.getInt("kills");
                wins = overallStatsObject.getInt("wins");
                minutesPlayed = overallStatsObject.getInt("minutesPlayed");
                killDeathRatio = overallStatsObject.getDouble("kd");
                lastUpdatedData = overallStatsObject.getString("lastModified");
                break;
            case "solo":
                // Extract and print relevant data from **SOLO** JSON object
                JSONObject soloStatsObject = allStatsObject.getJSONObject("solo");
                kills = soloStatsObject.getInt("kills");
                wins = soloStatsObject.getInt("wins");
                minutesPlayed = soloStatsObject.getInt("minutesPlayed");
                killDeathRatio = soloStatsObject.getDouble("kd");
                lastUpdatedData = soloStatsObject.getString("lastModified");
                break;
            case "duo":
                // Extract and print relevant data from **DUO** JSON object
                JSONObject duoStatsObject = allStatsObject.getJSONObject("duo");
                kills = duoStatsObject.getInt("kills");
                wins = duoStatsObject.getInt("wins");
                minutesPlayed = duoStatsObject.getInt("minutesPlayed");
                killDeathRatio = duoStatsObject.getDouble("kd");
                lastUpdatedData = duoStatsObject.getString("lastModified");
                break;
            case "squad":
                // Extract and print relevant data from **SQUAD** JSON object
                JSONObject squadStatsObject = allStatsObject.getJSONObject("squad");
                kills = squadStatsObject.getInt("kills");
                wins = squadStatsObject.getInt("wins");
                minutesPlayed = squadStatsObject.getInt("minutesPlayed");
                killDeathRatio = squadStatsObject.getDouble("kd");
                lastUpdatedData = squadStatsObject.getString("lastModified");
                break;
            case "ltm":
                // Extract and print relevant data from **LTM (Limited Time Mode) ** JSON object
                JSONObject ltmStatsObject = allStatsObject.getJSONObject("squad");
                kills = ltmStatsObject.getInt("kills");
                wins = ltmStatsObject.getInt("wins");
                minutesPlayed = ltmStatsObject.getInt("minutesPlayed");
                killDeathRatio = ltmStatsObject.getDouble("kd");
                lastUpdatedData = ltmStatsObject.getString("lastModified");
                break;
        }

//        System.out.println("Kills: " + kills);
//        System.out.println("wins: " + wins);
//        System.out.println("minutesPlayed: " + minutesPlayed);
//        System.out.printf("K/D Ratio: %.2f%n", killDeathRatio);
//        System.out.println("lastUpdatedData: " + lastUpdatedData);
//
//        System.out.println("Player Name: " + playerName);
//        System.out.println("Battle Pass Level: " + battlePassLevel);

        return new PlayerStats(playerName, battlePassLevel, kills, wins, minutesPlayed, killDeathRatio, lastUpdatedData);
    }

}
