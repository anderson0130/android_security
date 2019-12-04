package com.example.security_essentials;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
class Usuario {

    public String uid;
    public String email;

    public Usuario() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Usuario(String uid, String email) {
        this.uid = uid;
        this.email = email;
    }

}