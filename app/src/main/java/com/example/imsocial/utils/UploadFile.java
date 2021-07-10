package com.example.imsocial.utils;

import android.content.ContentResolver;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;

import com.example.imsocial.services.FirebaseService;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class UploadFile {
    OnResponseListener onResponseListener;

    public void uploadImage(Uri uri) {
        FirebaseService firebaseService = new FirebaseService();
        HashMap<String, Object> response = new HashMap<>();

        StorageReference reference =  firebaseService.getStorage()
                .getReference()
                .child("images/" + UUID.randomUUID().toString());
        UploadTask uploadTask =  reference.putFile(uri);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return reference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> uploadTask) {
                if (uploadTask.isSuccessful()) {
                    Uri downloadUri = uploadTask.getResult();

                    response.put("success", true);
                    response.put("data", downloadUri);
                } else {
                    response.put("success", false);
                    response.put("data", "Could not upload the image");
                }
                onResponseListener.onResponse(response);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                response.put("success", false);
                response.put("data", "Could not upload the image.");
                onResponseListener.onResponse(response);
            }
        });
    }

    public void setOnResponseListener(OnResponseListener onResponseListener) {
        this.onResponseListener = onResponseListener;
    }


}
