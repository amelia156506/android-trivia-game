/**
 * Author: Amelia Lee (chichenl)
 * Last Modified: Nov 17, 2022
 * <p>
 * The Data received from API will wrap several question together as list.
 * This class serve as the unit to parse the list in data.
 * Identical to the Question class in Project4 Web Application.
 */
package com.example.androidapp;

import java.util.List;

public class Question {
    private String category;
    private String difficulty;
    private String question;
    private String correct_answer;
    private List<String> incorrect_answers;

    public String getQuestion() {
        return question;
    }

    public String getCorrect_answer() {
        return correct_answer;
    }

    public List<String> getIncorrect_answers() {
        return incorrect_answers;
    }
}
