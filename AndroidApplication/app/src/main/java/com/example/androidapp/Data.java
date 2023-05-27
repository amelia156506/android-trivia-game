/**
 * Author: Amelia Lee (chichenl)
 * Last Modified: Nov 17, 2022
 * <p>
 * This class is for designed to take in the JSON result from trivia API.
 * Identical to the Data class in Project4 Web Application.
 */
package com.example.androidapp;

import java.util.List;

public class Data {
    private int response_code;
    private List<Question> results;

    public int getResponse_code() {
        return response_code;
    }

    public List<Question> getQuestions() {
        return results;
    }
}

