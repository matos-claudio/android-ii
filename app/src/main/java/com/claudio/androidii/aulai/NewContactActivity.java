package com.claudio.androidii.aulai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.claudio.androidii.FirebaseAuthSingleton;
import com.claudio.androidii.R;
import com.claudio.androidii.aulaiii.LoginActivity;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class NewContactActivity extends AppCompatActivity implements ContactAdapter.OnDeleteClickListener, ContactAdapter.OnUpdateClickListener{

    private EditText etName, etEmail, etPhone;
    private Button btAdd;

    private DatabaseReference databaseReference;

    private List<Contact> contacts;
    private ContactAdapter contactAdapter;

    private RecyclerView recyclerView;

    private FirebaseAnalytics firebaseAnalytics;

    private FirebaseAuth firebaseAuth;

    private final String DATABASE_REFERENCE = "contact";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);

        firebaseAuth = FirebaseAuthSingleton.getInstance();

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            String token = task.getResult();
            System.out.println("TOKEN: " + token);
        });

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        btAdd = findViewById(R.id.btAdd);
        recyclerView = findViewById(R.id.recyclerView);

        contacts = new ArrayList<>();
        contactAdapter = new ContactAdapter(this, contacts, this, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(contactAdapter);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        databaseReference = FirebaseDatabase.getInstance().getReference(DATABASE_REFERENCE);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                contacts.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    Contact contact = snapshot1.getValue(Contact.class);
                    contact.setContactId(snapshot1.getKey());
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
                    String contactId = databaseReference.push().getKey();
                    Contact contact = new Contact();
                    contact.setName(name);
                    contact.setEmail(email);
                    contact.setPhone(phone);
                    databaseReference.child(contactId).setValue(contact);
                    trackClickEvent();
                }
            }
        });
    }

    private void trackClickEvent() {
        Bundle params = new Bundle();
        params.putString("save_contact_click", "true");
        firebaseAnalytics.logEvent("button_click", params);
    }

    @Override
    public void onDeleteClick(int position) {
        deleteContact(position);
    }

    private void deleteContact(int position) {
        System.out.println("POSITION " + contacts.get(position).getContactId());
        databaseReference.child(contacts.get(position).getContactId()).removeValue();
    }

    @Override
    public void onUpdateClick(int position) {
        openUpdateScreen(position);
    }

    private void openUpdateScreen(int position) {
        String contactId = contacts.get(position).getContactId();
        String contactName = contacts.get(position).getName();
        String contactEmail = contacts.get(position).getEmail();
        String contactPhone = contacts.get(position).getPhone();

        Intent intent = new Intent(this, UpdateContactActivity.class);
        intent.putExtra("CONTACT_ID", contactId);
        intent.putExtra("CONTACT_NAME", contactName);
        intent.putExtra("CONTACT_EMAIL", contactEmail);
        intent.putExtra("CONTACT_PHONE", contactPhone);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection.
        switch (item.getItemId()) {
            case R.id.exit_user:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        firebaseAuth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

}