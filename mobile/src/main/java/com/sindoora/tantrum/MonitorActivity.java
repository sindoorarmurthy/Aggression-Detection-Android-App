package com.sindoora.tantrum;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import me.denley.courier.Courier;
import me.denley.courier.ReceiveData;

public class MonitorActivity extends AppCompatActivity {

    private TextView heartRateText, nameHeader, diagnosisText;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private DatabaseReference mDatabase;
    private final String lowAgr = "Calm";
    private final String highAgr = "Agitated";
    SharedPreferences sharedPref;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        heartRateText.setText("0 bpm");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Courier.stopReceiving(this);
        heartRateText.setText("0 bpm");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
        Courier.startReceiving(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //If logged out
                if(firebaseAuth.getCurrentUser() != null) {

                } else {

                }
            }
        };

        nameHeader = (TextView)findViewById(R.id.nameHeader);
        nameHeader.setText(mAuth.getCurrentUser().getDisplayName());

        heartRateText = (TextView)findViewById(R.id.heartRateTextView);
        diagnosisText = (TextView)findViewById(R.id.diagnosisText);

        sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        FloatingActionButton mAccountBtn = (FloatingActionButton)findViewById(R.id.fabAccount);
        mAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSettings = new Intent(getApplicationContext(), AccountsActivity.class);
                startActivity(intSettings);
            }
        });
        heartRateText.setText("0 bpm");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_account:
                // User chose the "Account" action, show Account Details.
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    @ReceiveData("/heartRate")
    void onNewValue(final String value) {
        if(value==null) {
            heartRateText.setText("0 bpm");
        } else {
            heartRateText.setText(value + " bpm");
            mDatabase.child("patients").child(mAuth.getCurrentUser().getUid()).child("heartRate").setValue(value);
            if(Integer.parseInt(value) < Integer.parseInt(sharedPref.getString("threshold", ""))){
                diagnosisText.setText("You are calm. Keep it up! :)");
                mDatabase.child("patients").child(mAuth.getCurrentUser().getUid()).child("diagnosis").setValue(lowAgr);
            } else {
                diagnosisText.setText("You seem to be agitated. Please calm down. :)");
                mDatabase.child("patients").child(mAuth.getCurrentUser().getUid()).child("diagnosis").setValue(highAgr);
            }
        }
    }
}
