package com.claudio.androidii;

import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthSingleton {

    private static FirebaseAuth mAuthInstance;

    // Construtor privado para evitar instanciação direta
    private FirebaseAuthSingleton() {
        // Aqui você pode inicializar a instância do FirebaseAuth conforme necessário
        mAuthInstance = FirebaseAuth.getInstance();
    }

    // Método para obter a instância única do FirebaseAuth
    public static FirebaseAuth getInstance() {
        // Se a instância ainda não foi criada, inicialize-a
        if (mAuthInstance == null) {
            synchronized (FirebaseAuthSingleton.class) {
                if (mAuthInstance == null) {
                    new FirebaseAuthSingleton();
                }
            }
        }
        return mAuthInstance;
    }
}
