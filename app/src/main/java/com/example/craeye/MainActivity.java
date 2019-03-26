package com.example.craeye;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference doRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        database = FirebaseDatabase.getInstance();
        doRef = database.getReference("daily_offer");
        SharedPreferences sharedpreferences = getSharedPreferences("user_profile", Context.MODE_PRIVATE);
        Log.i("custom", sharedpreferences.getString("dob", ""));
        Log.i("custom", sharedpreferences.getString("gender", ""));

        
        updateDailyOffer();
    }

    void updateDailyOffer(){
        doRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.child("of1").getValue(String.class);
                TextView do1_text = findViewById(R.id.info_text_1);

                if(!value.equals("NIL")) {
                    do1_text.setText(value);
                }

                else{
                    do1_text.setText("No available offer.");
                }

                value = dataSnapshot.child("of2").getValue(String.class);
                TextView do2_text = findViewById(R.id.info_text_2);

                if(!value.equals("NIL")) {
                    do2_text.setText(value);
                }

                else{
                    do2_text.setText("No available offer.");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("custom", "Failed to read value.", error.toException());
            }
        });
    }
}
