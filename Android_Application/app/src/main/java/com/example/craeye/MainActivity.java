package com.example.craeye;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.LoginFilter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference doRef;

    private BluetoothAdapter bluetoothAdapter;
    private ArrayList bluetoothDevices = new ArrayList<String>();

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                bluetoothDevices.add("" + device.getName());

                try {
                    if (device.getName().equals("BEACON_01")) {
                        showAd();
                    }
                }

                catch (Exception error) {

                }
                Log.i("custom", bluetoothDevices.toString());
            }
        }
    };

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

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        final IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

        startBluetooth();
        updateDailyOffer();

        final Handler handler = new Handler();
        final int delay = 5000;

        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                searchBeacon();
                startBluetooth();
                handler.postDelayed(this, 5000);
            }
        }, delay);
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

    void startBluetooth(){
        bluetoothAdapter.startDiscovery();

        if(bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();
        }
    }

    void searchBeacon() {
        if(!bluetoothDevices.contains("BEACON_01")){
            TextView beacon_name = findViewById(R.id.info_text_3);
            beacon_name.setText("No available offer.");
        }

        bluetoothDevices.clear();
    }

    void showAd() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("beacon_locations");
        final String[] rack_number = new String[3];

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rack_number[0] = dataSnapshot.child("BEACON_1").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRef = database.getReference("camera_01");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rack_number[1] = dataSnapshot.child("age").getValue().toString();
                rack_number[2] = dataSnapshot.child("gender").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRef = database.getReference("rack").child("02").child("Electronics");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView beacon_name = findViewById(R.id.info_text_3);

                try {
                    beacon_name.setText("BEACON_1 \n" + dataSnapshot.child(rack_number[2].substring(2, rack_number[2].length() - 2)).child(rack_number[1].substring(2, rack_number[1].length() - 2)).getValue().toString());
                }

                catch (Exception error) {
                    beacon_name.setText("BEACON_1");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    public void generateCode1(View view) {
        FirebaseDatabase fb_database = FirebaseDatabase.getInstance();
        DatabaseReference db_ref = fb_database.getReference();
    }
}
