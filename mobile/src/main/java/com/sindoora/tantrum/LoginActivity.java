package com.sindoora.tantrum;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sindoora.tantrum.Doctor.Doctor;
import com.sindoora.tantrum.Patient.Patient;
import com.sindoora.tantrum.Patient.PatientListActivity;

public class LoginActivity extends AppCompatActivity {

    private Button mBtnGoogleLogin;
    private Button mBtnSkipLogin;
    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "LoginActivity";
    private Switch aSwitch;
    private DatabaseReference mDatabase;
    private Patient patient;
    private Doctor doctor;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        aSwitch = (Switch)findViewById(R.id.switch1);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null) {

                } else {

                }
            }
        };

        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mBtnGoogleLogin = (Button) findViewById(R.id.google_button);
        mBtnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser() == null) {
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                } else {
                    Toast.makeText(getApplicationContext(), "You are already signed in!", Toast.LENGTH_LONG).show();
                }
            }
        });

        mBtnSkipLogin = (Button) findViewById(R.id.btnSkipLogin);
        mBtnSkipLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(aSwitch.isChecked()) {
                    Intent intPatientListActivity = new Intent(getApplicationContext(), PatientListActivity.class);
                    startActivity(intPatientListActivity);
                } else {
                    Intent intMonitorActivity = new Intent(getApplicationContext(), MonitorActivity.class);
                    startActivity(intMonitorActivity);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            saveInfo();
                            String[] userName = mAuth.getCurrentUser().getDisplayName().split(" ");
                            sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                            if(aSwitch.isChecked()){
                                doctor = new Doctor(mAuth.getCurrentUser().getUid(), userName[0], userName[1], true);
                                mDatabase.child("doctors").child(mAuth.getCurrentUser().getUid()).setValue(doctor);
                                editor = sharedPref.edit();
                                editor.putBoolean("isDoctor", true);
                                editor.apply();
                                startActivityForResult(new Intent(getApplicationContext(),PatientListActivity.class), RC_SIGN_IN);
                            } else
                                patient = new Patient(mAuth.getCurrentUser().getUid(), userName[0], userName[1], false);
                                mDatabase.child("patients").child(mAuth.getCurrentUser().getUid()).setValue(patient);
                                editor = sharedPref.edit();
                                editor.putBoolean("isDoctor", false);
                                editor.apply();
                                startActivityForResult(new Intent(getApplicationContext(),MonitorActivity.class), RC_SIGN_IN);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveInfo() {
        String[] userName = mAuth.getCurrentUser().getDisplayName().split(" ");
        sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.putString("uid", mAuth.getCurrentUser().getUid());
        editor.putString("firstName", userName[0]);
        editor.putString("lastName", userName[1]);
        editor.putString("email", mAuth.getCurrentUser().getEmail());
        editor.putString("photoURL", mAuth.getCurrentUser().getPhotoUrl().toString());
        editor.apply();
        Toast.makeText(getApplicationContext(),"Saved User Data", Toast.LENGTH_SHORT).show();
    }
}
