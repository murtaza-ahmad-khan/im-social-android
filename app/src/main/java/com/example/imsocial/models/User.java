package com.example.imsocial.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.imsocial.services.FirebaseService;
import com.example.imsocial.utils.OnResponseListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String name;
    private String email;
    private String password;
    private OnResponseListener onResponseListener;

    public User() {

    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("name", this.name);

        return map;
    }

    public void save() {
        HashMap<String, Object> response = new HashMap<>();
        FirebaseService firebaseService = new FirebaseService();

        firebaseService.getAuth().createUserWithEmailAndPassword(this.email, this.password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser user = firebaseService.getAuth().getCurrentUser();

                        if(task.isSuccessful()) {
                            DocumentReference userRef =  firebaseService.getFirestore().collection(FirebaseService.USERS_COLLECTION).document(user.getUid());

                            Map userMap = toMap();
                            userMap.put("createdAt", firebaseService.getTimestamp());

                            userRef.set(userMap);

                            response.put("success", true);
                            response.put("message", "User account created successfully");

                            onResponseListener.onResponse(response);

                        } else {
                            String error =  String.valueOf(task.getException().getMessage());

                            response.put("success", false);
                            response.put("message", error);

                            onResponseListener.onResponse(response);
                        }
                    }
                });

    }

    public Map<String, Object> update() {
        HashMap<String, Object> response = new HashMap<>();
        // TODO
        response.put("success", true);
        response.put("message", "User updated successfully");

        return response;
    }

    public Map<String, Object> delete() {
        HashMap<String, Object> response = new HashMap<>();
        // TODO
        response.put("success", true);
        response.put("message", "User deleted successfully");

        return response;
    }

    public static String getUid() {
        FirebaseService firebaseService = new FirebaseService();
        return firebaseService.getAuth().getCurrentUser().getUid();
    }

    public void getUserById(String id) {
        Map<String, Object> userMap = new HashMap();
        Map<String, Object> response = new HashMap();

        FirebaseService firebaseService = new FirebaseService();
        FirebaseUser firebaseUser = firebaseService.getAuth().getCurrentUser();

        firebaseService.getFirestore()
                .collection("Users")
                .document(id).get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Map data = task.getResult().getData();

                        userMap.put("uid", firebaseUser.getUid());
                        userMap.put("email", firebaseUser.getEmail());
                        userMap.put("name", data.get("name"));

                        response.put("success", true);
                        response.put("data", userMap);
                    } else {
                        response.put("success", false);
                        response.put("data", "Error! could not get the user");
                    }
                    this.onResponseListener.onResponse(response);
        });

    }

    public void setOnResponseListener (OnResponseListener onResponseListener) {
        this.onResponseListener = onResponseListener;
    }

}
