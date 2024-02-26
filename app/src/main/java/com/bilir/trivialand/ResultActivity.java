package com.bilir.trivialand;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    // Result stat numbers
    TextView correctRes, wrongRes;

    // Option buttons
    Button playAgain, endGame;

    String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        getSupportActionBar().setTitle(R.string.title_result);

        Intent quizIntent = getIntent();
        topic = quizIntent.getStringExtra("category");

        // Assigning components
        correctRes = findViewById(R.id.correctRes);
        wrongRes = findViewById(R.id.wrongRes);
        playAgain = findViewById(R.id.buttonResultRestart);
        endGame = findViewById(R.id.buttonResultQuit);

        correctRes.setText(String.valueOf(quizIntent.getIntExtra("correct", 0)));
        wrongRes.setText(String.valueOf(quizIntent.getIntExtra("wrong", 0)));


        // Assigning functions to buttons
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent quizIntent = new Intent(ResultActivity.this, QuizActivity.class);
                quizIntent.putExtra("Category", topic);
                startActivity(quizIntent);
                finish();
            }
        });

        endGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}