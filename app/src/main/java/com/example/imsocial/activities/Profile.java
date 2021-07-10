package com.example.imsocial.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.imsocial.R;
import com.example.imsocial.adapters.PostAdaptor;
import com.example.imsocial.models.Post;
import com.example.imsocial.models.User;
import com.example.imsocial.services.FirebaseService;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profile extends AppCompatActivity {
    List<DocumentSnapshot> posts;
    Post post;

    TextView user_name_view;
    TextView user_email_view;
    ImageView user_image_view;
    Button user_logout_button_view;
    RecyclerView posts_list_view;
    SwipeRefreshLayout swipe_refresh_layout;
    ProgressBar progress_bar_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String userId = getIntent().getStringExtra("userId");

//        init view
        user_name_view =  findViewById(R.id.user_profile_name);
        user_email_view =  findViewById(R.id.user_profile_email);
        user_image_view =  findViewById(R.id.user_profile_pic);
        user_logout_button_view = findViewById(R.id.user_profile_logout);
        progress_bar_view = findViewById(R.id.progress_bar);
        swipe_refresh_layout = findViewById(R.id.posts_pull_to_refresh);

        posts_list_view = findViewById(R.id.posts_list_view);
        posts_list_view.setLayoutManager(new LinearLayoutManager(this));

        post = new Post();

//      Get User
        getUser();
//      Get User's Posts
        getUserPosts(userId);

//      Pull to refresh data
        swipe_refresh_layout.setOnRefreshListener(()-> {
            getUserPosts(userId);
        });

    }

    private void getUser() {
        User user = new User();
        user.setOnResponseListener((response)-> {
            if(response.get("success").equals(true)) {
                Map userMap = (HashMap) response.get("data");

                user_name_view.setText(String.valueOf(userMap.get("name")));
                user_email_view.setText(String.valueOf(userMap.get("email")));
//              Picasso.get().load((Uri) userMap.get("photoUrl")).into(user_image_view);
            } else {

            }
        });
        user.getUserById(User.getUid());
    }

    private void getUserPosts(String userId) {
        post.setOnResponseListener((response)-> {
            if(response.get("success").equals(true)) {
                swipe_refresh_layout.setRefreshing(false);
                posts = (List) response.get("data");
                posts_list_view.setAdapter(new PostAdaptor(getApplicationContext(), posts));
                progress_bar_view.setVisibility(View.GONE);
            }
        });
//      Query for getting user specific posts
        List<String> query = new ArrayList<>();
        query.add("uid");
        query.add(userId);

        post.find(query);
    }

}