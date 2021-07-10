package com.example.imsocial.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.imsocial.R;
import com.example.imsocial.services.FirebaseService;
import com.example.imsocial.utils.ButtonWithSpinner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//      Init Views
        View signIn_button_view = findViewById(R.id.signIn_button);
        EditText email_view = findViewById(R.id.signIn_email);
        EditText password_view = findViewById(R.id.signIn_password);
        TextView error_view = findViewById(R.id.signIn_error);

        ButtonWithSpinner button = new ButtonWithSpinner("Sign In", signIn_button_view);

        signIn_button_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_view.getText().toString();
                String password = password_view.getText().toString();
                FirebaseService firebaseService = new FirebaseService();

                error_view.setText("");
                button.setLoading(true);

                firebaseService.getAuth().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    navigateToHome();
                                } else {
                                    String error = task.getException().getMessage();
                                    error_view.setText(error);
                                    button.setLoading(false);
                                }
                            }
                        });

            }
        });
    }

    public void navigateToSignUp(View view) {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    public void navigateToHome() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(intent);
    }
}