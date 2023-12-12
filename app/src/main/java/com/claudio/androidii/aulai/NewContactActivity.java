package com.claudio.androidii.aulai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.claudio.androidii.R;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class NewContactActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPhone;
    private Button btAdd;

    private DatabaseReference databaseReference;

    private List<Contact> contacts;
    private ContactAdapter contactAdapter;

    private RecyclerView recyclerView;

    private final String DATABASE_REFERENCE = "contact";

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        btAdd = findViewById(R.id.btAdd);
        recyclerView = findViewById(R.id.recyclerView);

        contacts = new ArrayList<>();
        contactAdapter = new ContactAdapter(this, contacts);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(contactAdapter);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String currentToken = task.getResult();
                        Log.d("TOKEN", "Current token: " + currentToken);

                        // ...
                    } else {
                        Log.w("TOKEN", "Fetching FCM registration token failed", task.getException());
                    }
                });

        databaseReference = FirebaseDatabase.getInstance().getReference(DATABASE_REFERENCE);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                contacts.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    Contact contact = snapshot1.getValue(Contact.class);
                    contacts.add(contact);
                }
                contactAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NewContactActivity.this, "Erro ao ler o banco", Toast.LENGTH_LONG).show();
            }
        });

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                if(!TextUtils.isEmpty(name) &&
                        !TextUtils.isEmpty(email) &&
                        !TextUtils.isEmpty(phone)) {
                    trackEvent();
                    String contactId = databaseReference.push().getKey();
                    Contact contact = new Contact();
                    contact.setName(name);
                    contact.setEmail(email);
                    contact.setPhone(phone);
                    databaseReference.child(contactId).setValue(contact);
                }
            }
        });
    }

    private void trackEvent() {
        Bundle params = new Bundle();
        params.putString("button_click", "true");
        mFirebaseAnalytics.logEvent("button_click", params);
    }
}