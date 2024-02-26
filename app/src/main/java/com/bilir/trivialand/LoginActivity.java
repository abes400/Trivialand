package com.bilir.trivialand;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    // Edit Text Components
    EditText emailEditText, passwordEditText;
    Button loginButton;


    // Bottom texts
    TextView signUpText, forgotText;

    // Progress bar
    ProgressBar progressBar;

    // Getting the authenticator singleton instance for the sign-up operations.
    FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Binding EditText components (email, password)
        emailEditText = findViewById(R.id.editTextLoginEmail);
        passwordEditText = findViewById(R.id.editTextLoginPassword);

        // Binding buttons
        loginButton = findViewById(R.id.buttonLoginLogin); //



        // Binding "sign-up" and "forgot" labels
        signUpText = findViewById(R.id.textViewLoginSignUp); //
        forgotText = findViewById(R.id.textViewLoginForgot); //

        // Binding progressbar //
        progressBar = findViewById(R.id.progressBar2);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInFirebaseUser();
            }
        });


        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // This activity to backstack and Sign Up act. to screen
                Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);

            }
        });

        forgotText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotIntent = new Intent(LoginActivity.this, ForgotPW.class);
                startActivity(forgotIntent);

            }
        });
    }

    public void signInFirebaseUser() {
        String emailLogin = emailEditText.getText().toString(),
                passwordLogin = passwordEditText.getText().toString();

        if(!emailLogin.isEmpty() && !passwordLogin.isEmpty()) {

            // show up progressbar and gray-out the buttons for fool-proofing
            progressBar.setVisibility(View.VISIBLE);
            loginButton.setEnabled(false);
            signUpText.setEnabled(false);
            forgotText.setEnabled(false);

            // Create user w/ email and pw
            auth.signInWithEmailAndPassword(emailLogin, passwordLogin)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.INVISIBLE);

                            if (task.isSuccessful()) {
                                ToastMaker.makeToastShort(getApplicationContext(), getString(R.string.welcome_back));

                                // Initiating main activity
                                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(mainIntent);

                                // Removing this activity from backstack
                                finish();
                            } else {
                                ToastMaker.makeToastLong(getApplicationContext(), getString(R.string.login_fail));

                                // show up progressbar and gray-out the buttons for fool-proofing
                                loginButton.setEnabled(true);
                                signUpText.setEnabled(true);
                                forgotText.setEnabled(true);
                            }

                        }
                    });
        } else
            ToastMaker.makeToastLong(getApplicationContext(), getString(R.string.missing_info));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();

        // AUTO LOGIN WHEN RELAUNCHED
        if(user != null) { // Logged in before
            // Initiating main activity
            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}