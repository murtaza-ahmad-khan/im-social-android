package com.example.imsocial.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.imsocial.R;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;
import java.util.Map;

public class CommentAdaptor extends BaseAdapter {
    List<DocumentSnapshot> commentsList;
    Context context;

    public CommentAdaptor(Context context, List<DocumentSnapshot> commentsList) {
        this.commentsList = commentsList;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(commentsList != null) return commentsList.size();

        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view =  ((Activity) context).getLayoutInflater().inflate(R.layout.comment_layout, null);

        TextView comment_text_view =  view.findViewById(R.id.comment_text);
        TextView comment_date_view = view.findViewById(R.id.comment_date);
        TextView comment_user_name_view = view.findViewById(R.id.comment_user_name);

        Map commentMap = commentsList.get(i).getData();

        String comment_text = String.valueOf(commentMap.get("comment"));
        String comment_author = String.valueOf(commentMap.get("username"));

        if(commentMap.get("createdAt") != null) {
            Timestamp comment_date = (Timestamp) commentMap.get("createdAt");
            comment_date_view.setText(comment_date.toDate().toString().split("GMT")[0]);
        }

        comment_text_view.setText(comment_text);
        comment_user_name_view.setText(comment_author);

        return view;
    }
}
