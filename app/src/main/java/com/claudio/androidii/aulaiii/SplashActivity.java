package com.claudio.androidii.aulaiii;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.claudio.androidii.FirebaseAuthSingleton;
import com.claudio.androidii.R;
import com.claudio.androidii.aulai.NewContactActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends Activity {

    FirebaseAuth firebaseAuth;
    private static final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        firebaseAuth = FirebaseAuthSingleton.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null) {
            navigateToScreen(this, NewContactActivity.class);
        } else {
            navigateToScreen(this, LoginActivity.class);
        }
    }

    private void navigateToScreen(Activity activity, Class<?> cls) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(activity, cls));
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}