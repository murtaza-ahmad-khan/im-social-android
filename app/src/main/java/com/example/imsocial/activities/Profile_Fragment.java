package com.example.imsocial.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.imsocial.R;
import com.example.imsocial.adapters.PostAdaptor;
import com.example.imsocial.models.Post;
import com.example.imsocial.models.User;
import com.example.imsocial.services.FirebaseService;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profile_Fragment extends Fragment {
    List<DocumentSnapshot> posts;
    Post post;

    TextView user_name_view;
    TextView user_email_view;
    ImageView user_image_view;
    Button user_logout_button_view;
    RecyclerView posts_list_view;
    SwipeRefreshLayout swipe_refresh_layout;

    public Profile_Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

//        init view
        user_name_view =  view.findViewById(R.id.user_profile_name);
        user_email_view =  view.findViewById(R.id.user_profile_email);
        user_image_view =  view.findViewById(R.id.user_profile_pic);
        user_logout_button_view = view.findViewById(R.id.user_profile_logout);
        swipe_refresh_layout = view.findViewById(R.id.posts_pull_to_refresh);

        posts_list_view = view.findViewById(R.id.posts_list_view);
        posts_list_view.setLayoutManager(new LinearLayoutManager(getContext()));

        post = new Post();

//      Get User
        getUser();
//      Get User's Posts
        getUserPosts();
//      Logout
        logout();

//      Pull to refresh data
        swipe_refresh_layout.setOnRefreshListener(()-> {
            getUserPosts();
        });

        return view;
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

    private void getUserPosts() {
        post.setOnResponseListener((response)-> {
            if(response.get("success").equals(true)) {
                swipe_refresh_layout.setRefreshing(false);
                posts = (List) response.get("data");
                posts_list_view.setAdapter(new PostAdaptor(getContext(), posts));
            }
        });
//      Query for getting user specific posts
        List<String> query = new ArrayList<>();
        query.add("uid");
        query.add(User.getUid());

        post.find(query);
    }


    private void logout() {
        user_logout_button_view.setOnClickListener((v)-> {
            FirebaseService firebaseService = new FirebaseService();
            firebaseService.getAuth().signOut();

//          Open Welcome Activity and clear navigation stack
            Intent intent = new Intent(getContext(), Welcome.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity(intent);

        });
    }
}