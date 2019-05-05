package com.example.craeye;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        username.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                password = findViewById(R.id.password);
                username.setText("dheeraj1998");
                password.setText("1234");

                return false;
            }
        });
    }

    public void openRegister(View view) {
        Intent register_page = new Intent(this, RegisterActivity.class);
        startActivity(register_page);
    }

    public void signIn(View view) {
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if (password.getText().toString().equals(dataSnapshot.child(username.getText().toString()).child("password").getValue().toString())) {
                        SharedPreferences sharedpreferences = getSharedPreferences("user_profile", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("dob", dataSnapshot.child(username.getText().toString()).child("dob").getValue().toString());
                        editor.putString("gender", dataSnapshot.child(username.getText().toString()).child("gender").getValue().toString());
                        editor.apply();

                        Intent temp = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(temp);
                        Toast.makeText(getApplicationContext(), "Login successful.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                catch (Exception error) {
                    Toast.makeText(getApplicationContext(), "Error while login.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error while login.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
