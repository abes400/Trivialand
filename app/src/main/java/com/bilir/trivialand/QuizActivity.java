package com.bilir.trivialand;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QuizActivity extends AppCompatActivity {

    // Score Texts
    TextView time, correct, wrong, status;
    TextView question;

    // Answers
    Button ans1, ans2, ans3, ans4;

    // Buttons
    Button quit, next;

    // Correct and wrong answer count
    int correctC = 0, wrongC = 0;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    // Database reference
    DatabaseReference dbRef;

    DatabaseReference authRef = database.getReference();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    // Current questions and answers
    String qQuestion, ansA, ansB, ansC, ansD, ansCorrect, userAns;
    int questionCount
            ,qNumber = 1; // holds the index of the current question

    // Topic also used for accessing the quesitons of different categories
    String topic;

    // Count down timer object
    CountDownTimer timer;
    private static final long TOTAL_TIME = 25000;
    Boolean timerCont;
    long timeLeft = TOTAL_TIME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Intent topicIntent = getIntent();
        topic = topicIntent.getStringExtra("Category");
        dbRef = database.getReference().child(topic);

        getSupportActionBar().setTitle(topic);

        // Textviews
        time = findViewById(R.id.time);
        correct = findViewById(R.id.correct);
        wrong = findViewById(R.id.wrong);
        status = findViewById(R.id.status);

        question = findViewById(R.id.questionText);


        // Buttons
        ans1 = findViewById(R.id.ans1);
        ans2 = findViewById(R.id.ans2);
        ans3 = findViewById(R.id.ans3);
        ans4 = findViewById(R.id.ans4);

        quit = findViewById(R.id.end);
        next = findViewById(R.id.next);


        ans1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAns = "a";
                checkAns(ans1);
            }
        });

        ans2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAns = "b";
                checkAns(ans2);
            }
        });

        ans3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAns = "c";
                checkAns(ans3);
            }
        });

        ans4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAns = "d";
                checkAns(ans4);
            }
        });

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { quitGame(); }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status.setText(R.string.question);
                status.setTextColor(getResources().getColor(R.color.primary));
                fetchQuestion();
            }
        });
        setChoicesClickable(false);

        fetchQuestion();

    }

    // Fetching questions and answers from database
    public void fetchQuestion() {

        // Read from the database
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                // reset colors
                ans1.setBackgroundColor(getResources().getColor(R.color.transparent_black));
                ans2.setBackgroundColor(getResources().getColor(R.color.transparent_black));
                ans3.setBackgroundColor(getResources().getColor(R.color.transparent_black));
                ans4.setBackgroundColor(getResources().getColor(R.color.transparent_black));

                setChoicesClickable(true);

                // Get the question count
                questionCount = (int) dataSnapshot.getChildrenCount();

                if(qNumber <= questionCount) {


                    String index = String.valueOf(qNumber);
                    // Get the question
                    qQuestion = dataSnapshot.child(index).child("q").getValue().toString();
                    ansA = dataSnapshot.child(index).child("a").getValue().toString();
                    ansB = dataSnapshot.child(index).child("b").getValue().toString();
                    ansC = dataSnapshot.child(index).child("c").getValue().toString();
                    ansD = dataSnapshot.child(index).child("d").getValue().toString();
                    ansCorrect = dataSnapshot.child(index).child("ans").getValue().toString();

                    question.setText(qQuestion);
                    ans1.setText(ansA);
                    ans2.setText(ansB);
                    ans3.setText(ansC);
                    ans4.setText(ansD);
                    qNumber++;

                    startTimer();
                } else quitGame();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                ToastMaker.makeToastShort(QuizActivity.this, "Question fetching failed");
            }
        });
    }

    public void checkAns(Button button) {

        // Stop timer
        timer.cancel();

        setChoicesClickable(false);

        if(userAns.equals(ansCorrect)) {

            // The user selects the correct answer
            button.setBackgroundColor(Color.GREEN);
            correct.setText("" + (++correctC));

            status.setText(R.string.correct);

        } else {

            // The user selects the wrong answer
            button.setBackgroundColor(Color.RED);
            wrong.setText("" + (++wrongC));

            status.setText(R.string.wrong);
            status.setTextColor(Color.RED);

            findCorrectAnswer();

        }

    }

    void findCorrectAnswer() {
        // Finding the button containing the correct answer and highlighting it
        if(ansCorrect.equals("a")) ans1.setBackgroundColor(Color.GREEN);
        else if(ansCorrect.equals("b")) ans2.setBackgroundColor(Color.GREEN);
        else if(ansCorrect.equals("c")) ans3.setBackgroundColor(Color.GREEN);
        else if(ansCorrect.equals("d")) ans4.setBackgroundColor(Color.GREEN);
    }

    void setChoicesClickable(boolean clickable) {
        ans1.setClickable(clickable);
        ans2.setClickable(clickable);
        ans3.setClickable(clickable);
        ans4.setClickable(clickable);
    }

    public void startTimer() {
        timeLeft = TOTAL_TIME;
        timer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished/1000%60;
                time.setText("" + timeLeft);
            }

            @Override
            public void onFinish() {
                findCorrectAnswer();
                setChoicesClickable(false);

                status.setText(R.string.time_up);
                status.setTextColor(getResources().getColor(R.color.gray));
            }
        }.start();
    }

    public void quitGame() {
        submitScore();

        // Sending the correct reference child key so that result activity knows
        // where to look on the database.
        Intent resultIntent = new Intent(QuizActivity.this, ResultActivity.class);

        resultIntent.putExtra("category", topic);
        resultIntent.putExtra("correct", correctC);
        resultIntent.putExtra("wrong", wrongC);

        startActivity(resultIntent);

        finish();
    }

    // Submitting the score of the user to the database
    public void submitScore() {
        // Getting the UID of the user
        String UID = user.getUid();

        authRef.child("Scores").child(UID).child(topic).child("correct").setValue(correctC);
        authRef.child("Scores").child(UID).child(topic).child("wrong").setValue(wrongC);
    }
}