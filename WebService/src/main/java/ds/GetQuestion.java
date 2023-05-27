/**
 * Author: Amelia Lee (chichenl)
 * Last Modified: Nov 17, 2022
 * <p>
 * This program retrieve data from the trivia API based on user request inputs
 */
package ds;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class GetQuestion {
    static Gson gson = new Gson();

    public String search(int category, String difficulty) {
        // get data from trivia API
        String url = "https://opentdb.com/api.php?amount=1&category=" + category + "&difficulty=" + difficulty + "&type=multiple";

        // Get the question element
        Data data = getRemoteJSON(url);

        // return null if nothing is retrieved
        if (data != null) {
            // use response code from API to decide if retrieve was success
            int responseCode = data.getResponse_code();
            switch (responseCode) {
                case 0:
                    System.out.println("Success");
                    // return retrieve JSON result as String
                    return gson.toJson(data);
                case 1:
                    System.out.println("No Results");
                    break;
                case 2:
                    System.out.println("invalid Parameters");
                    break;
            }
        }
        return null;

    }

    /**
     * Given an url that will request JSON, return a Document with that JSON, else null
     */
    private static Data getRemoteJSON(String url) {
        StringBuilder sb = new StringBuilder();
        try {
            String line;
            InputStream in = new URL(url).openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
            in.close();
        } catch (Exception e) {
            System.out.println("GetQuestion.getRemoteJSON error: " + e);
        }
        return gson.fromJson(sb.toString(), Data.class);
    }
}

