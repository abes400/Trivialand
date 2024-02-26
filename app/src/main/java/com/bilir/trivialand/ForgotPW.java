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
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPW extends AppCompatActivity {

    EditText emailForgot;
    Button buttonForgot;
    ProgressBar progressBar;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pw);

        getSupportActionBar().setTitle(R.string.title_forgot);

        emailForgot = findViewById(R.id.editTextForgotUpEmail);
        buttonForgot = findViewById(R.id.buttonForgotSendMail);
        progressBar = findViewById(R.id.progressBar3);

        buttonForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResetPasswordRequest();

            }
        });
    }

    public void sendResetPasswordRequest() {
        String emailForgotPW = emailForgot.getText().toString();
        if(!emailForgotPW.isEmpty()) {
            auth.sendPasswordResetEmail(emailForgotPW).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        ToastMaker.makeToastShort(getApplicationContext(), getString(R.string.pw_rst_approve));
                    } else {
                        ToastMaker.makeToastShort(getApplicationContext(), getString(R.string.pw_rst_failure));
                    }
                }
            });
        }
    }
}