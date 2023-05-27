/**
 * Author: Amelia Lee (chichenl)
 * Last Modified: Nov 17, 2022
 * <p>
 * Request for question to web application and wait for result in background thread.
 * cite: lab 7 AndroidInterestingPicture
 */
package com.example.androidapp;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import com.google.gson.Gson;

public class GetQuestion {
    MainActivity mainActivity = null;   // for callback
    String difficulty = null;       // request web application to search for this category & difficulty
    int categoryCode = 0;
    Data searchResult = null;          // returned from web application

    /**
     * Call background thread to search for question
     *
     * @param difficulty   one of the requirement to search for on trivia
     * @param categoryCode one of the requirement to search for on trivia
     * @param activity     the UI thread activity
     * @param mainActivity the callback method's class; here, it will be mainActivity.resultReady()
     */
    public void search(String difficulty, int categoryCode, Activity activity, MainActivity mainActivity) {
        System.out.println("GetQuestion: " + Thread.currentThread());
        this.mainActivity = mainActivity;
        this.difficulty = difficulty;
        this.categoryCode = categoryCode;
        new BackgroundTask(activity).startBackground();
    }

    private class BackgroundTask {

        private final Activity activity; // The UI thread

        public BackgroundTask(Activity activity) {
            this.activity = activity;
        }

        private void startBackground() {
            new Thread(new Runnable() {
                public void run() {
                    doInBackground();
                    // Activity should be set to MainActivity.this then this method uses the UI thread
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            onPostExecute();
                        }
                    });
                }
            }).start();
        }

        /**
         * Implements whatever you need to do on the background thread.
         * Here, it implements the search function.
         */
        private void doInBackground() {
            System.out.println("doInBackground: " + Thread.currentThread());
            searchResult = search(difficulty, categoryCode);
        }
    }

    /**
     * Run on the UI thread after the background thread completes.
     */
    public void onPostExecute() {
        System.out.println("doPostExecute: " + Thread.currentThread());
        mainActivity.resultReady(searchResult, difficulty, categoryCode);
    }

    /**
     * Request web application to search for the fitting question,
     * and return Data that can be parse into paragraph to put into TextView.
     * The searching logic is similar to the logic in GetQuestion function in the web application.
     *
     * @param difficulty   one of the requirement to search for on trivia
     * @param categoryCode one of the requirement to search for on trivia
     * @return Data that contains question information
     */
    private Data search(String difficulty, int categoryCode) {
        System.out.println("BackgroundTask.search: " + Thread.currentThread());

        // heroku web application url
        String url = "https://afternoon-bayou-81851.herokuapp.com/getQuestion?category=" + categoryCode + "&difficulty=" + difficulty;

        // Get the question element
        Data data = getRemoteJSON(url);

        // return null if nothing is retrieved
        if (data != null) {
            // use response code from API to decide if retrieve was success
            int responseCode = data.getResponse_code();
            switch (responseCode) {
                case 0:
                    System.out.println("GetQuestion.search: Success");
                    return data;
                case 1:
                    System.out.println("No Results");
                case 2:
                    System.out.println("Invalid Parameters");
                case 3:
                    System.out.println("Token Not Found");
                case 4:
                    System.out.println("Token Empty");
            }
        }
        System.out.println("Get JSON from URL failed");
        return null;
    }

    /**
     * Given an url that will request JSON, return a Document with that JSON, else null
     */
    private Data getRemoteJSON(String url) {
        System.out.println("getRemoteJSON: " + Thread.currentThread());
        StringBuilder sb = new StringBuilder();
        try {
            String line;

            InputStream in = new URL(url).openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
            in.close();
            Gson g = new Gson();
            return g.fromJson(sb.toString(), Data.class);
        } catch (Exception e) {
            System.out.println("GetQuestion.getRemoteJSON error:" + e);
            return null;
        }
    }
}


