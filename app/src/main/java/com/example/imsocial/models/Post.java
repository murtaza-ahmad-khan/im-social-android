package com.example.imsocial.models;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.imsocial.services.FirebaseService;
import com.example.imsocial.utils.OnResponseListener;
import com.example.imsocial.utils.UploadFile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Post {
    private String uid;
    private String content;
    private Uri imageUri;
    static private FirebaseService firebaseService  = new FirebaseService();;
    private OnResponseListener onResponseListener;

    public Post() {
    }

    public Post(String uid, String content, Uri imageFile) {
        this.uid = uid;
        this.content = content;
        this.imageUri = imageFile;
    }

    public Map toMap() {
        HashMap<String, Object> post = new HashMap<>();

        String username =  firebaseService.getAuth().getCurrentUser().getEmail().split("@")[0];

        post.put("content", this.content);
        post.put("uid", this.uid);
        post.put("createdAt", firebaseService.getTimestamp());
        post.put("username", username);

        return post;
    }

    public void save() {
        HashMap<String, Object> response = new HashMap<>();

        Task docTask =  firebaseService.getFirestore()
                .collection(FirebaseService.POSTS_COLLECTION).add(toMap());

        docTask.addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()) {
                    if(imageUri != null) {
                        uploadImage(task, response);
                    } else {
                        response.put("success", true);
                        response.put("data", "Post created Successfully");
                        onResponseListener.onResponse(response);
                    }
                } else {
                    response.put("success", false);
                    response.put("data", "Error! Could not create the post");
                    onResponseListener.onResponse(response);
                }
            }
        });

    }

    public void find(List<String> q) {
        HashMap<String, Object> response = new HashMap<>();
        CollectionReference posts_collections =  firebaseService.getFirestore().collection(FirebaseService.POSTS_COLLECTION);

        Query query = posts_collections
                    .orderBy("createdAt", Query.Direction.DESCENDING);

        if(q != null) {
            query = posts_collections
                    .orderBy("createdAt", Query.Direction.DESCENDING).whereEqualTo(q.get(0), q.get(1));
        }

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    response.put("success", true);
                    response.put("data", task.getResult().getDocuments());
                } else {
                    response.put("success", false);
                    response.put("data", "Error! Please Try again later");
                }
                onResponseListener.onResponse(response);
            }
        });

    }

    public void likePost(String postId) {
        HashMap<String, Object> response = new HashMap<>();
         String userId = firebaseService.getAuth().getCurrentUser().getUid();

//      Post Document Reference
        DocumentReference postRef = firebaseService.getFirestore()
                .collection(FirebaseService.POSTS_COLLECTION)
                .document(postId);
//      Like Document Ref
        DocumentReference likeRef = postRef.collection("Likes")
                .document(userId);
//      Transaction (Should revert to old the state if one of these operations failed
        WriteBatch batch =  firebaseService.getFirestore().batch();
//      Data to update
        Map<String, Object> likeMap = new HashMap();
        Map<String, Object> postMap = new HashMap();

        likeRef.get().addOnCompleteListener((likeTask) -> {
            if(likeTask.isSuccessful()) {
                // If user already liked a post
                if(likeTask.getResult().getData() != null) {
                    postMap.put("likeCount", firebaseService.getIncrement(-1));
                    batch.delete(likeRef);
                } else {
                    // Creating a new like document in Likes collection
                    likeMap.put("uid", userId);
                    postMap.put("likeCount", firebaseService.getIncrement(1));
                    batch.set(likeRef, likeMap);
                }
                batch.update(postRef, postMap);
                // run transaction
                batch.commit();
            }

        });


    }


    public void isLiked(String postId) {
        HashMap<String, Object> response = new HashMap<>();
        String userId = firebaseService.getAuth().getCurrentUser().getUid();

//      Like Document Reference
        DocumentReference likeRef = firebaseService.getFirestore()
                .collection(FirebaseService.POSTS_COLLECTION)
                .document(postId)
                .collection("Likes")
                .document(userId);

        likeRef.get().addOnCompleteListener((task) -> {
           if(task.isSuccessful()) {
//             if user liked a post
               if(task.getResult().getData() != null) {
                   response.put("success", true);
                   response.put("data", true);
               } else {
                   response.put("success", true);
                   response.put("data", false);
               }
           } else {
               response.put("success", false);
               response.put("data", false);
           }
           onResponseListener.onResponse(response);
        });
    }

    public void findByIdAndUpdate() {

    }

    public void findByIdAndDelete() {

    }

    private void uploadImage(Task<DocumentReference> task, Map response) {
        UploadFile uploadFile = new UploadFile();

        uploadFile.setOnResponseListener((onResponse)-> {
            if(onResponse.get("success").equals(true)) {
//                                Update the imageUrl field in Post
                firebaseService.getFirestore()
                        .collection("Posts")
                        .document(task.getResult().getId())
                        .update("imageUrl", String.valueOf(onResponse.get("data")));

                response.put("success", true);
                response.put("data", "Post created Successfully");
            } else {
                response.put("success", false);
                response.put("data", "Could not upload the image");
            }
            onResponseListener.onResponse(response);

        });
        uploadFile.uploadImage(imageUri);
    }


    public void setOnResponseListener(OnResponseListener onResponseListener) {
        this.onResponseListener = onResponseListener;
    }
}
