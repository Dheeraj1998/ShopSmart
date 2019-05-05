package com.example.craeye;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    Button register_btn;
    EditText email_address, password, dob;
    RadioGroup gender_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_btn = findViewById(R.id.r_registerbtn);
        email_address = findViewById(R.id.r_username);
        password = findViewById(R.id.r_password);
        dob = findViewById(R.id.r_dob);

        email_address.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                email_address.setText("dheeraj1998");
                password.setText("1234");
                dob.setText("14011998");
                return false;
            }
        });
    }

    public void openLogin(View view) {
        finish();
    }

    public void registerUser(View view) {
        email_address = findViewById(R.id.r_username);
        password = findViewById(R.id.r_password);
        dob = findViewById(R.id.r_dob);
        gender_group = findViewById(R.id.r_gender);

        int selected_id = gender_group.getCheckedRadioButtonId();
        RadioButton gender = findViewById(selected_id);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        try {
            myRef.child(email_address.getText().toString()).child("password").setValue(password.getText().toString());
            myRef.child(email_address.getText().toString()).child("dob").setValue(dob.getText().toString());
            myRef.child(email_address.getText().toString()).child("gender").setValue(gender.getText());

            Toast.makeText(getApplicationContext(), "Registration complete.",Toast.LENGTH_SHORT).show();
            finish();
        }

        catch (Exception error) {
            Toast.makeText(getApplicationContext(), "Please try again.",Toast.LENGTH_SHORT).show();
        }
    }
}
