package com.sindoora.tantrum;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private Button mSighOutBtn, mbtnLoginScreen, mBtnUpdateDB, mBtnGetAge, mBtnGetTreshold;
    private TextView nameView;
    private EditText editAge, editThreshold;
    private DatabaseReference mDatabase;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        mSighOutBtn = (Button) findViewById(R.id.btnSignout);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mSighOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                Intent logoutIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(logoutIntent);
                Toast.makeText(getApplicationContext(),"Signed Out", Toast.LENGTH_SHORT).show();

            }
        });

        mbtnLoginScreen = (Button)findViewById(R.id.btnLoginScreen);
        mbtnLoginScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intLoginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intLoginActivity);
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //If logged out
                if(firebaseAuth.getCurrentUser() != null) {
                    mSighOutBtn.setVisibility(View.VISIBLE);
                } else {
                    mSighOutBtn.setVisibility(View.INVISIBLE);
                }
            }
        };

        sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        nameView = (TextView) findViewById(R.id.textName);
        nameView.setText(sharedPref.getString("firstName", "") + " " + sharedPref.getString("lastName", ""));

        mBtnGetAge = (Button) findViewById(R.id.btnSubmitAge);
        editAge = (EditText) findViewById(R.id.editTextAge);
        mBtnGetAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = sharedPref.edit();
                editor.putString("age", editAge.getText().toString());
                editor.apply();
                Toast.makeText(getApplicationContext(), "Updated Age Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        mBtnGetTreshold = (Button) findViewById(R.id.btnSubmitThreshold);
        editThreshold = (EditText) findViewById(R.id.editTextThreshold);
        mBtnGetTreshold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = sharedPref.edit();
                editor.putString("threshold", editThreshold.getText().toString());
                editor.apply();
                Toast.makeText(getApplicationContext(), "Updated Threshold Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        mBtnUpdateDB = (Button) findViewById(R.id.btnUpdateDB);
        mBtnUpdateDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sharedPref.getBoolean("isDoctor", true)) {
                    mDatabase.child("patients").child(mAuth.getCurrentUser().getUid()).child("age").setValue(sharedPref.getString("age",""));
                    mDatabase.child("patients").child(mAuth.getCurrentUser().getUid()).child("threshold").setValue(sharedPref.getString("threshold",""));
                    Toast.makeText(getApplicationContext(), "Updated Firebase DB Successfully", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void  signOut() {
        // Firebase sign out
        FirebaseAuth.getInstance().signOut();
        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {

                    }
                });
    }
}
