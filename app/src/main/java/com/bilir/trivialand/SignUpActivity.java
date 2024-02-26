package com.bilir.trivialand;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText emailEditTextSignUp, passwordEditTextSignUp;
    Button signupButtonSignUp;
    ProgressBar progressBar;

    // Getting the authenticator singleton instance for the sign-up operations.
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setTitle(R.string.title_signup);

        // Binding EditText components (email, password)
        emailEditTextSignUp = findViewById(R.id.editTextSignUpEmail);
        passwordEditTextSignUp = findViewById(R.id.editTextSignUpPassword);

        // Binding buttons (Sign up)
        signupButtonSignUp = findViewById(R.id.buttonSignUpSignUp);

        // Binding progress bar
        progressBar = findViewById(R.id.progressBar);

        signupButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFirebaseUser();
            }
        });
    }

    void createFirebaseUser( ) {
        String emailNew = emailEditTextSignUp.getText().toString(),
                passwordNew = passwordEditTextSignUp.getText().toString();

        if(!emailNew.isEmpty() && !passwordNew.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);

            // Create user w/ email and pw
            auth.createUserWithEmailAndPassword(emailNew, passwordNew)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.INVISIBLE);
                            signupButtonSignUp.setEnabled(false);

                            if (task.isSuccessful()) { // Creation succeeds
                                ToastMaker.makeToastLong(getApplicationContext(), getString(R.string.create_successful));
                                createUserData();
                                finish();

                            } else { // Creation fails
                                ToastMaker.makeToastLong(getApplicationContext(), getString(R.string.create_fail));
                                signupButtonSignUp.setEnabled(true);
                            }

                        }
                    });
        } else
            ToastMaker.makeToastLong(getApplicationContext(), getString(R.string.missing_info));

    }

    void createUserData() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference();
        FirebaseUser user = auth.getCurrentUser();

        String UID = user.getUid();

        dbRef.child("Scores").child(UID).child("Questions").child("correct").setValue(0);
        dbRef.child("Scores").child(UID).child("Questions").child("wrong").setValue(0);

        dbRef.child("Scores").child(UID).child("Movies and TV Shows").child("correct").setValue(0);
        dbRef.child("Scores").child(UID).child("Movies and TV Shows").child("wrong").setValue(0);

        dbRef.child("Scores").child(UID).child("Anime").child("correct").setValue(0);
        dbRef.child("Scores").child(UID).child("Anime").child("wrong").setValue(0);
    }
}