package com.example.imsocial.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imsocial.R;
import com.example.imsocial.activities.Comments;
import com.example.imsocial.activities.Profile;
import com.example.imsocial.models.Post;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostAdaptor extends RecyclerView.Adapter<PostAdaptor.PostAdaptorViewHolder> {
    List<DocumentSnapshot> posts;
    Context context;

    public PostAdaptor(Context context, List<DocumentSnapshot> posts) {
        this.posts = posts;
        this.context = context;
    }

    @NonNull
    @Override
    public PostAdaptor.PostAdaptorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout, parent, false);
        return new PostAdaptorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdaptorViewHolder holder, int position) {
        Map postMap = posts.get(position).getData();

        String postId = posts.get(position).getId();
        String userId = String.valueOf(postMap.get("uid"));
        String post_content = String.valueOf(postMap.get("content"));
        String post_author = String.valueOf(postMap.get("username"));
        Timestamp post_date = (Timestamp) postMap.get("createdAt");

//          Likes Map
        Map likesMap = new HashMap();
        likesMap.put("isLiked", false);
        likesMap.put("likeCount", 0);

        if(postMap.get("likeCount") != null) {
            likesMap.put("likeCount", postMap.get("likeCount"));
        }

//          if post has an image
        if(postMap.get("imageUrl") != null) {
            Uri post_imageUrl = Uri.parse(String.valueOf(postMap.get("imageUrl")));
            //  Load image from Url using Picasso Lib
            Picasso.get().load(post_imageUrl).into( holder.post_image_view);
            holder.post_image_view.setVisibility(View.VISIBLE);
        }

//          Set views text
        holder.post_content_view.setText(post_content);
        holder.post_date_view.setText(post_date.toDate().toString().split("GMT")[0]);
        holder.posts_likes_count_view.setText(String.valueOf(likesMap.get("likeCount")));
        holder.post_user_name_view.setText(post_author);

//          Open Comments Activity
        navigateToCommentsActivity(postId, holder.post_comments_view);

//        Open Profile Activity
        navigateToProfileActivity(userId, holder.user_view);

//          Checking if user liked a post or not
        isLikedPost(postId, likesMap, holder.posts_likes_icon_view);

//          Add/remove post like
        handlePostLikes(postId, likesMap, holder.post_likes_view, holder.posts_likes_icon_view, holder.posts_likes_count_view);

    }

    @Override
    public int getItemCount() {
        if(posts != null) {
            return posts.size();
        }
        return 0;
    }

    private void navigateToCommentsActivity(String postId, View post_comments_view) {
        post_comments_view.setOnClickListener((v)-> {
            Intent intent = new  Intent(context, Comments.class);
            intent.putExtra("postId", postId);
            context.startActivity(intent);
        });
    }

    private void navigateToProfileActivity(String userId, View user_view) {
        user_view.setOnClickListener((v)-> {
            Intent intent = new  Intent(context, Profile.class);
            intent.putExtra("userId", userId);
            context.startActivity(intent);
        });
    }

    private void  isLikedPost(String postId, Map likesMap, ImageView posts_likes_icon_view) {
        Post post = new Post();
        post.setOnResponseListener((response) -> {
            if(response.get("success").equals(true) && response.get("data").equals(true)) {
                posts_likes_icon_view.setImageResource(R.drawable.ic_heart_fill);
                likesMap.put("isLiked", true);
            }
        });
        post.isLiked(postId);
    }

    public void handlePostLikes (String postId, Map likesMap,View post_likes_view ,ImageView like_icon_view, TextView like_count_view) {
        Post post = new Post();
        post_likes_view.setOnClickListener((v)-> {
//              Toggle like
            boolean liked = (boolean) likesMap.get("isLiked");
            likesMap.put("isLiked", !liked);

            int likes =  Integer.parseInt(String.valueOf(likesMap.get("likeCount")));

            if(likesMap.get("isLiked").equals(true)) {
                likes ++;
                like_icon_view.setImageResource(R.drawable.ic_heart_fill);
                like_count_view.setText(likes +"");
            } else {
                likes--;
                like_icon_view.setImageResource(R.drawable.ic_heart);
                like_count_view.setText(likes +"");
            }

            likesMap.put("likeCount", likes);

            post.likePost(postId);
        });
    }

    class PostAdaptorViewHolder extends RecyclerView.ViewHolder {
        TextView post_content_view;
        ImageView post_image_view;
        TextView post_date_view;
        TextView post_user_name_view;
        View post_comments_view;
        View post_likes_view;
        ImageView posts_likes_icon_view;
        TextView posts_likes_count_view;
        View user_view;

        public PostAdaptorViewHolder(@NonNull View itemView) {
            super(itemView);

            post_content_view =  itemView.findViewById(R.id.post_content);
            post_image_view = itemView.findViewById(R.id.post_image);
            post_date_view = itemView.findViewById(R.id.post_date);
            post_user_name_view = itemView.findViewById(R.id.post_user_name);
            post_comments_view = itemView.findViewById(R.id.post_comments);
            post_likes_view = itemView.findViewById(R.id.post_likes);
            posts_likes_icon_view = itemView.findViewById(R.id.post_likes_icon);
            posts_likes_count_view = itemView.findViewById(R.id.post_likes_count);
            user_view = itemView.findViewById(R.id.user);
        }
    }
}
