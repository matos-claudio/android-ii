package com.claudio.androidii.aulaiii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.claudio.androidii.FirebaseAuthSingleton;
import com.claudio.androidii.R;
import com.claudio.androidii.aulai.NewContactActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends Activity {

    private FirebaseAuth firebaseAuth;
    private Button btCreateUser, btLogin;
    private EditText edtEmail, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuthSingleton.getInstance();

        btCreateUser = findViewById(R.id.btCreateUser);
        btLogin = findViewById(R.id.btLogin);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        btCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, CreateUserActivity.class));
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                if(!email.isEmpty() && !password.isEmpty()) {
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        startActivity(new Intent(LoginActivity.this, NewContactActivity.class));
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Erro ao realizar o login", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null) {
            System.out.println("UsuarioLogadoAgora "+ currentUser.getEmail());
        }
    }
}