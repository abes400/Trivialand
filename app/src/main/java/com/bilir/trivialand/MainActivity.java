package com.bilir.trivialand;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    TextView signedAs;

    // Stats
    TextView gcCorrect, gcFalse, mvCorrect, mvFalse, anmCorrect, anmFalse;

    // Getting the authenticator singleton instance for the sign-up operations.
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    // Database reference
    DatabaseReference dbRef = database.getReference();

    // Login button
    Button startGeneral, startMovies, startAnime, logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle(R.string.title_main);

        startGeneral = findViewById(R.id.buttonGeneral);
        startMovies = findViewById(R.id.buttonMovies);
        startAnime = findViewById(R.id.buttonAnime);
        logOut = findViewById(R.id.buttonSignOut);

        // Initializing Views showing statistics
        gcCorrect = findViewById(R.id.GC_Correct);
        gcFalse = findViewById(R.id.GC_False);
        mvCorrect = findViewById(R.id.MV_Correct);
        mvFalse = findViewById(R.id.MV_False);
        anmCorrect = findViewById(R.id.ANM_Correct);
        anmFalse = findViewById(R.id.ANM_False);

        // Showing email addr. on screen
        signedAs = findViewById(R.id.signedAs);
        signedAs.setText("" + getText(R.string.loginas) + " " + user.getEmail());


        startGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent quizIntent = new Intent(MainActivity.this, QuizActivity.class);
                quizIntent.putExtra("Category", "Questions");
                startActivity(quizIntent);
            }
        });

        startMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent quizIntent = new Intent(MainActivity.this, QuizActivity.class);
                quizIntent.putExtra("Category", "Movies and TV Shows");
                startActivity(quizIntent);
            }
        });

        startAnime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent quizIntent = new Intent(MainActivity.this, QuizActivity.class);
                quizIntent.putExtra("Category", "Anime");
                startActivity(quizIntent);
            }
        });

        // Logout
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SIGN OUT
                auth.signOut();
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);

                // Removing MainActivity from the backstack
                finish();
            }
        });

        updateInfo();
    }


    void updateInfo() {
        // Getting the UID of the user
        String UID = user.getUid();


        dbRef.addValueEventListener(new ValueEventListener() {

            String correct_gc = "0", wrong_gc = "0", correct_mv = "0", wrong_mv = "0", correct_anm = "0", wrong_anm = "0";
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    correct_gc = snapshot.child("Scores").child(UID).child("Questions").child("correct").getValue().toString();
                    gcCorrect.setText(correct_gc);

                    wrong_gc = snapshot.child("Scores").child(UID).child("Questions").child("wrong").getValue().toString();
                    gcFalse.setText(wrong_gc);

                } catch(RuntimeException e) {
                    dbRef.child("Scores").child(UID).child("Questions").child("correct").setValue(0);
                    dbRef.child("Scores").child(UID).child("Questions").child("wrong").setValue(0);
                }

                try {
                    correct_mv = snapshot.child("Scores").child(UID).child("Movies and TV Shows").child("correct").getValue().toString();
                    mvCorrect.setText(correct_mv);

                    wrong_mv = snapshot.child("Scores").child(UID).child("Movies and TV Shows").child("wrong").getValue().toString();
                    mvFalse.setText(wrong_mv);
                } catch (RuntimeException e) {
                    dbRef.child("Scores").child(UID).child("Movies and TV Shows").child("correct").setValue(0);
                    dbRef.child("Scores").child(UID).child("Movies and TV Shows").child("wrong").setValue(0);
                }

                try {
                    correct_anm = snapshot.child("Scores").child(UID).child("Anime").child("correct").getValue().toString();
                    anmCorrect.setText(correct_anm);

                    wrong_anm = snapshot.child("Scores").child(UID).child("Anime").child("wrong").getValue().toString();
                    anmFalse.setText(wrong_anm);
                } catch (RuntimeException e) {
                    dbRef.child("Scores").child(UID).child("Anime").child("correct").setValue(0);
                    dbRef.child("Scores").child(UID).child("Anime").child("wrong").setValue(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}