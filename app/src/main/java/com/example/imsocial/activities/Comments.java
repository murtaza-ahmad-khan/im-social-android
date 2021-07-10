package com.example.imsocial.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.imsocial.R;
import com.example.imsocial.adapters.CommentAdaptor;
import com.example.imsocial.models.Comment;
import com.example.imsocial.models.User;
import com.example.imsocial.utils.ButtonWithSpinner;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class Comments extends AppCompatActivity {
    List<DocumentSnapshot> commentsList;
    View comment_button_view ;
    ListView comment_list_view;
    EditText comment_input_view;
    ButtonWithSpinner button;
    String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        postId =  getIntent().getStringExtra("postId");

//      Init Views
        comment_button_view = findViewById(R.id.comment_button);
        comment_list_view = findViewById(R.id.comment_list);
        comment_input_view = findViewById(R.id.comment_input);

        button = new ButtonWithSpinner("Comment", comment_button_view);

//      Get Comments of a post
        getComments();

//      Comment on a Post
        postComment();

    }

    public void postComment() {
        Comment comment = new Comment();
        comment_button_view.setOnClickListener((v)-> {
            button.setLoading(true);
            comment.setOnResponseListener((response)-> {
                if(response.get("success").equals(true)) {
                    comment_input_view.setText("");
                }
                button.setLoading(false);
            });
            comment.addComment(postId, User.getUid(), comment_input_view.getText().toString());
        });
    }

    public void getComments() {
        Comment comment = new Comment();
        comment.setOnResponseListener((response)-> {
            if(response.get("success").equals(true)) {
                commentsList = (List) response.get("data");

                CommentAdaptor commentAdaptor = new CommentAdaptor(this, commentsList);
                comment_list_view.setAdapter(commentAdaptor);
            }
        });
        comment.getComments(postId);
    }

}