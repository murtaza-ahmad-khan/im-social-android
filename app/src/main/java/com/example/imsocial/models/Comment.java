package com.example.imsocial.models;

import com.example.imsocial.services.FirebaseService;
import com.example.imsocial.utils.OnResponseListener;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class Comment {
    OnResponseListener onResponseListener;

    public void addComment(String postId, String userId, String comment) {
        FirebaseService firebaseService = new FirebaseService();
        Map<String, Object> commentMap = new HashMap();
        Map<String, Object> response = new HashMap<>();

        String username =  firebaseService.getAuth().getCurrentUser().getEmail().split("@")[0];

        commentMap.put("uid", userId);
        commentMap.put("comment", comment);
        commentMap.put("username", username);
        commentMap.put("createdAt", firebaseService.getTimestamp());


        firebaseService.getFirestore()
                .collection(FirebaseService.POSTS_COLLECTION)
                .document(postId)
                .collection(FirebaseService.COMMENTS_COLLECTION)
                .add(commentMap)
                .addOnCompleteListener((task) -> {
                    if(task.isSuccessful()) {
                        response.put("success", true);
                        response.put("data", task.getResult().get());
                    } else {
                        response.put("success", false);
                        response.put("data", "Could not add the comment");
                    }
                    this.onResponseListener.onResponse(response);
                });
    }

    public void getComments(String postId) {
        FirebaseService firebaseService = new FirebaseService();
        Map<String, Object> response = new HashMap<>();

//       Get Real time data
        firebaseService.getFirestore()
                .collection(FirebaseService.POSTS_COLLECTION)
                .document(postId)
                .collection(FirebaseService.COMMENTS_COLLECTION)
                .addSnapshotListener((snapshot, exception) -> {
                    if(snapshot != null) {
                        response.put("success", true);
                        response.put("data", snapshot.getDocuments());
                    }
                    onResponseListener.onResponse(response);
                });
    }

    public void setOnResponseListener(OnResponseListener onResponseListener) {
        this.onResponseListener = onResponseListener;
    }
}
