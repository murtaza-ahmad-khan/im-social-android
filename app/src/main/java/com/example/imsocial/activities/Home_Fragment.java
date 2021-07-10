package com.example.imsocial.activities;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.imsocial.R;
import com.example.imsocial.adapters.PostAdaptor;
import com.example.imsocial.models.Post;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;


public class Home_Fragment extends Fragment {
    List<DocumentSnapshot> posts;


    public Home_Fragment() {
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
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        SwipeRefreshLayout swipe_refresh_layout = view.findViewById(R.id.posts_pull_to_refresh);
        RecyclerView posts_list_view = view.findViewById(R.id.posts_list_view);
        posts_list_view.setLayoutManager(new LinearLayoutManager(getContext()));



        Post post = new Post();
        post.setOnResponseListener((response)-> {
            if(response.get("success").equals(true)) {
                posts = (List) response.get("data");
                posts_list_view.setAdapter(new PostAdaptor(getContext(), posts));
                progressBar.setVisibility(View.GONE);
                swipe_refresh_layout.setRefreshing(false);
            }
        });
        post.find(null);

//      Pull to refresh data
        swipe_refresh_layout.setOnRefreshListener(()-> {
            post.find(null);
        });

        return view;
    }

}