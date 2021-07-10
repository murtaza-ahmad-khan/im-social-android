package com.example.imsocial.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.imsocial.R;
import com.example.imsocial.models.User;
import com.example.imsocial.utils.ButtonWithSpinner;
import com.example.imsocial.utils.OnResponseListener;

import java.util.Map;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

//      Init Views
        View signUp_button_view = findViewById(R.id.signUp_button_view);
        EditText name_view = findViewById(R.id.signUp_name);
        EditText email_view = findViewById(R.id.signUp_email);
        EditText password_view = findViewById(R.id.signUp_password);

        ButtonWithSpinner button  = new ButtonWithSpinner("Sign Up", signUp_button_view);



        signUp_button_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = name_view.getText().toString();
                String email = email_view.getText().toString();
                String password = password_view.getText().toString();

                button.setLoading(true);

                User user  = new User(name, email, password);
                user.save();

                user.setOnResponseListener(new OnResponseListener() {
                    @Override
                    public void onResponse(Map response) {
                        button.setLoading(false);
                        if(response.get("success").equals(true)) {
                            navigateToHome();
                        }
                    }
                });

            }
        });

    }
    
    public void navigateToLogin(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void navigateToHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}