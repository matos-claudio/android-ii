package com.claudio.androidii.aulai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.claudio.androidii.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateContactActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPhone;
    private Button btUpdate;

    private final String DATABASE_REFERENCE = "contact";
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        btUpdate = findViewById(R.id.btUpdate);

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateContact();
            }
        });

        loadData();
    }

    private void loadData() {
        Contact myContact = getContactData();
        etName.setText(myContact.getName());
        etEmail.setText(myContact.getEmail());
        etPhone.setText(myContact.getPhone());
    }

    private Contact getContactData() {
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("CONTACT_ID")) {
            Contact contact = new Contact();
            contact.setContactId(intent.getStringExtra("CONTACT_ID"));
            contact.setName(intent.getStringExtra("CONTACT_NAME"));
            contact.setEmail(intent.getStringExtra("CONTACT_EMAIL"));
            contact.setPhone(intent.getStringExtra("CONTACT_PHONE"));
            return contact;
        }
        return null;
    }

    private void updateContact() {
        Contact contact = new Contact();
        contact.setName(etName.getText().toString());
        contact.setEmail(etEmail.getText().toString());
        contact.setPhone(etPhone.getText().toString());

        String id = getContactData().getContactId();

        databaseReference = FirebaseDatabase.getInstance().getReference(DATABASE_REFERENCE).child(id);
        databaseReference.setValue(contact);
        finish();
    }
}