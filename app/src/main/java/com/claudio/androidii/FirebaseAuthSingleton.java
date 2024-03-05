package com.claudio.androidii;

import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthSingleton {
    private static FirebaseAuth firebaseAuth;

    private FirebaseAuthSingleton() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static FirebaseAuth getInstance() {
        // Se a instancia ainda nao foi criada, inicialize-a
        if(firebaseAuth == null) {
            synchronized (FirebaseAuthSingleton.class) {
                if(firebaseAuth == null) {
                    new FirebaseAuthSingleton();
                }
            }
        }
        return firebaseAuth;
    }
}
