/**
 * Author: Amelia Lee (chichenl)
 * Last Modified: Nov 17, 2022
 * <p>
 * The starter of the Android App
 */
package com.example.androidapp;

import static java.util.Map.entry;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    MainActivity me = this;
    private RadioGroup radioDifficulties;
    private Spinner spinnerCategory;
    private TextView textView;

    // API only recognize category code, needs mapping to find code from user input
    private final Map<String, Integer> categoryMap = Map.ofEntries(
            entry("Animals", 27),
            entry("Books", 10),
            entry("Film", 11),
            entry("Television", 14),
            entry("General Knowledge", 9),
            entry("Geography", 22),
            entry("History", 23),
            entry("Mythology", 20),
            entry("Mathematics", 19),
            entry("Sports", 21)
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MainActivity ma = this;

        // spinner cite: https://stackoverflow.com/questions/1947933/how-to-get-spinner-value
        spinnerCategory = (Spinner) findViewById(R.id.spinner);

        // radio buttons cite: https://developer.android.com/develop/ui/views/components/radiobutton
        radioDifficulties = (RadioGroup) findViewById(R.id.radioGroup);
        radioDifficulties.clearCheck();

        Button generateButton = (Button) findViewById(R.id.generateButton);
        generateButton.setOnClickListener(new View.OnClickListener() {
            // retrieve user chosen difficulty and category on button click
            @Override
            public void onClick(View view) {

                int checkId = radioDifficulties.getCheckedRadioButtonId();
                String difficulty = findRadioButton(checkId);

                String category = spinnerCategory.getSelectedItem().toString();
                int categoryCode = categoryMap.getOrDefault(category, 9);

                // reference to lab 7 AndroidInterestingPicture
                GetQuestion gq = new GetQuestion();
                // Done asynchronously in another thread.
                // It calls MainActivity.resultReady() in this thread when complete.
                gq.search(difficulty,categoryCode, me, ma);
            }
        });

        textView = (TextView) findViewById(R.id.textView);
    }

    // find the Radio button that was selected
    private String findRadioButton(int checkId) {
        switch (checkId) {
            case R.id.radio_easy:
                return "easy";
            case R.id.radio_medium:
                return "medium";
            case R.id.radio_hard:
                return "hard";
        }
        return "easy";
    }

    // show the result on app after receiving result from web application
    public void resultReady(Data data, String difficulty, int categoryCode) {
        System.out.println("resultReady: " + Thread.currentThread());
        if (data != null) {
            List<Question> resultsList = data.getQuestions();
            StringBuilder sb = new StringBuilder();
            sb.append("Category: ").append(categoryCode).append(System.lineSeparator());
            sb.append("Difficulty: ").append(difficulty).append(System.lineSeparator());
            for(Question result: resultsList) {
                sb.append("Question: ").append(result.getQuestion()).append(System.lineSeparator());
                sb.append("Correct Answer: ").append(result.getCorrect_answer()).append(System.lineSeparator());
                sb.append("Wrong Answer: ").append(System.lineSeparator());
                for (String s : result.getIncorrect_answers()){
                    sb.append(s).append(System.lineSeparator());
                }
            }
            textView.setText(sb);
        } else {
            System.out.println("No Result");
            String errorText  = "Couldn't find a question of category " + categoryCode + " with difficulty " + difficulty;
            textView.setText(errorText);
        }
    }
}