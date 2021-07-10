package com.example.imsocial.services;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class FirebaseService {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    public static String  USERS_COLLECTION = "Users";
    public static String  POSTS_COLLECTION = "Posts";
    public static String  COMMENTS_COLLECTION = "Comments";
    public static String  LIKES_COLLECTION = "Likes";
    public static String  STORAGE_REF = "images";

    public FirebaseService() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public FirebaseFirestore getFirestore() {
        return firebaseFirestore;
    }

    public FirebaseStorage getStorage() {return FirebaseStorage.getInstance();}

    public FirebaseAuth getAuth() {
        return firebaseAuth;
    }

    public FieldValue getTimestamp() {
        return FieldValue.serverTimestamp();
    }

    public FieldValue getIncrement(long i) {
        return  FieldValue.increment(i);
    }

}
