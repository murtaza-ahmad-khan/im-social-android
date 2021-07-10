package com.example.imsocial.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.imsocial.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    final Fragment home_fragment = new Home_Fragment();
    final Fragment new_post_fragment =new New_Post_Fragment();
    final Fragment profile_fragment = new Profile_Fragment();
    final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment active = home_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager.beginTransaction()
                .add(R.id.main_fragment_container, profile_fragment, "profile").hide(profile_fragment).commit();
        fragmentManager.beginTransaction()
                .add(R.id.main_fragment_container, new_post_fragment, "new_post").hide(new_post_fragment).commit();
        fragmentManager.beginTransaction()
                .add(R.id.main_fragment_container, home_fragment, "home").commit();


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home_screen:
                        fragmentManager.beginTransaction().hide(active).show(home_fragment).commit();
                        active = home_fragment;
                        break;
                    case R.id.new_post_screen:
                        fragmentManager.beginTransaction().hide(active).show(new_post_fragment).commit();
                        active = new_post_fragment;
                        break;
                    case R.id.profile_screen:
                        fragmentManager.beginTransaction().hide(active).show(profile_fragment).commit();
                        active = profile_fragment;
                        break;
                }

                return true;
            }
        });


    }
}