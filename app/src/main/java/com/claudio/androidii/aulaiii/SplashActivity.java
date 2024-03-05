package com.claudio.androidii.aulaiii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.claudio.androidii.FirebaseAuthSingleton;
import com.claudio.androidii.R;
import com.claudio.androidii.aulai.Contact;
import com.claudio.androidii.aulai.NewContactActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends Activity {

    private static final int SPLASH_DISPLAY_TIME = 2000;

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseAuth = FirebaseAuthSingleton.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null) {
            startActivity(new Intent(SplashActivity.this, NewContactActivity.class));
        } else {
            System.out.println("FirebaseAuth::: Não Logado");
            navigateToScreen();
        }
    }

    private void navigateToScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
        }, SPLASH_DISPLAY_TIME);
    }
}