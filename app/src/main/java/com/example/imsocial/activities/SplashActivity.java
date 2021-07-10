package com.example.imsocial.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.imsocial.R;
import com.example.imsocial.services.FirebaseService;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseService firebaseService = new FirebaseService();
        FirebaseUser firebaseUser =  firebaseService.getAuth().getCurrentUser();

        new Thread() {
          public void run() {
              try {
                  sleep(3*1000);

                  if(firebaseUser != null) {
                      startActivity(new Intent(getApplicationContext(), MainActivity.class));
                  } else {
                      startActivity(new Intent(getApplicationContext(), Welcome.class));
                  }

                  finish();
              } catch (Exception e) {

              }
          }
        }.start();

    }
}