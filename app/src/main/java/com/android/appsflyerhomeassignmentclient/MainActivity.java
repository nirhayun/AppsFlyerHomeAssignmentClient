package com.android.appsflyerhomeassignmentclient;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    QuestionFetcher qf;
    TextView questionView;
    Button answer1_button;
    Button answer2_button;
    Button answer3_button;
    Button answer4_button;
    int currentQuestionCorrectIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        setButtons();
        newQuestion();
    }

    protected void onStart() {
        super.onStart();
        // The activity is about to become visible
    }

    protected void onResume() {
        super.onResume();
        // The activity has become visible (it is now "resumed")
    }

    protected void onPause() {
        super.onPause();
        //  Another activity is taking focus (this activity is about to be "paused").
    }

    protected void onStop() {
        super.onStop();
        //  The activity is no longer visible (it is now "stopped")
    }

    protected void onDestroy() { super.onDestroy();
        // The activity is about to be destroyed.
    }

    private void init() {
        questionView = findViewById(R.id.question);
        answer1_button = findViewById(R.id.answer1_button);
        answer2_button = findViewById(R.id.answer2_button);
        answer3_button = findViewById(R.id.answer3_button);
        answer4_button = findViewById(R.id.answer4_button);
    }

    private void setButtons() {
        View.OnClickListener myClickListener= v -> {
            int tag = Integer.parseInt((String)v.getTag());
            final TextView questionView = findViewById(R.id.question);
            if (tag == currentQuestionCorrectIndex) {
                questionView.setText("You got it !!!");
            } else {
                questionView.setText("Try Again !!!");
            }
        };
        answer1_button.setOnClickListener(myClickListener);
        answer2_button.setOnClickListener(myClickListener);
        answer3_button.setOnClickListener(myClickListener);
        answer4_button.setOnClickListener(myClickListener);
    }

    private void enableDisableButtons(Boolean isEnabled) {
        answer1_button.setEnabled(isEnabled);
        answer2_button.setEnabled(isEnabled);
        answer3_button.setEnabled(isEnabled);
        answer4_button.setEnabled(isEnabled);
    }

    private void newQuestion() {
        qf = new QuestionFetcher();
        qf.execute();
        try {
            JSONObject question = qf.get();
            currentQuestionCorrectIndex = question.getInt("correct_answer_index");
            enableDisableButtons(true);
            questionView.setText(question.getString("question"));
            JSONArray answers = (JSONArray) question.get("answers");
            answer1_button.setText(answers.getString(0));
            answer2_button.setText(answers.getString(1));
            answer3_button.setText(answers.getString(2));
            answer4_button.setText(answers.getString(3));
        } catch (ExecutionException e) {
            e.printStackTrace();
            handleError("Application error, report was sent");
        } catch (NullPointerException | InterruptedException | JSONException e) {
            handleError("Server is not available, please try again later");
        }

    }

    private void handleError(String error) {
        questionView.setText(error);
        enableDisableButtons(false);
        answer1_button.setText("");
        answer2_button.setText("");
        answer3_button.setText("");
        answer4_button.setText("");
    }
}