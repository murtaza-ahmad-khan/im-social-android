package com.example.imsocial.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.imsocial.R;
import com.example.imsocial.models.Post;
import com.example.imsocial.models.User;
import com.example.imsocial.utils.ButtonWithSpinner;
import com.example.imsocial.utils.OnResponseListener;

import java.util.Map;


public class New_Post_Fragment extends Fragment {
    View button_post_view;
    View image_picker_view;
    ImageView image_view;
    ButtonWithSpinner button_post;
    EditText post_content_view;
    int REQ_CODE = 2;
    Uri imageFile = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_post, container, false);

//      Init views
        post_content_view = view.findViewById(R.id.new_post_content);
        image_picker_view = view.findViewById(R.id.post_image_picker);
        image_view = view.findViewById(R.id.post_image);
        button_post_view = view.findViewById(R.id.button_post);
        button_post = new ButtonWithSpinner("Post", button_post_view);

//      Pick Image From Gallery
        image_picker_view.setOnClickListener((v)-> {
            pickImage();
        });

//      Add Post to the Database
        button_post_view.setOnClickListener(publishPost());

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_CODE && data != null) {
            imageFile = data.getData();

            image_view.setImageURI(data.getData());
            image_view.setScaleType(ImageView.ScaleType.FIT_XY);
            image_view.getLayoutParams().height= ViewGroup.LayoutParams.MATCH_PARENT;
            image_view.getLayoutParams().width= ViewGroup.LayoutParams.MATCH_PARENT;
        }
    }

    private void pickImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Pick Image"),
                REQ_CODE);
    }

    public View.OnClickListener publishPost() {
        return (v)-> {
            button_post.setLoading(true);

            String content = post_content_view.getText().toString();
            String uid = User.getUid();

            Post post = new Post(uid, content, imageFile);

            post.setOnResponseListener((response)-> {
                if(response.get("success").equals(true)) {
                    Toast.makeText(getContext(), response.get("data") +"", Toast.LENGTH_LONG).show();

                //  Clear form data
                    post_content_view.setText("");
                    imageFile = null;
                    image_view.setImageResource(R.drawable.ic_baseline_camera_alt_24);
                    image_view.getLayoutParams().height= 110;
                    image_view.getLayoutParams().width= 110;
                } else {
                    Toast.makeText(getContext(), response.get("data") +"", Toast.LENGTH_LONG).show();
                }
                button_post.setLoading(false);
            });
            post.save();
        };
    }


}