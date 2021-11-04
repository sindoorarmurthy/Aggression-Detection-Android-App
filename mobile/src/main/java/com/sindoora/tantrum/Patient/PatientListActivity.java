package com.sindoora.tantrum.Patient;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sindoora.tantrum.AccountsActivity;
import com.sindoora.tantrum.LoginActivity;
import com.sindoora.tantrum.R;

import java.util.ArrayList;
import java.util.List;

public class PatientListActivity extends AppCompatActivity {

    private ListView listViewPatients;
    private DatabaseReference dbPatients;
    private List<Patient> listPatients;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FloatingActionButton mBtnAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);
        mAuth = FirebaseAuth.getInstance();

        dbPatients = FirebaseDatabase.getInstance().getReference("patients");
        listViewPatients = (ListView)findViewById(R.id.listViewPatients);
        listPatients = new ArrayList<>();

        mBtnAccount = (FloatingActionButton) findViewById(R.id.fabAccount);
        mBtnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                Intent intLogin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intLogin);
                Toast.makeText(getApplicationContext(),"Signed Out", Toast.LENGTH_SHORT).show();
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //If logged out
                if(firebaseAuth.getCurrentUser() != null) {
                    //Nothing
                } else {
                    //Nothing
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        dbPatients.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listPatients.clear();
                for (DataSnapshot patientSnapShot : dataSnapshot.getChildren()) {
                    Patient patient = patientSnapShot.getValue(Patient.class);
                    listPatients.add(patient);
                }
                PatientListAdapter patientlistadapter = new PatientListAdapter(PatientListActivity.this, listPatients);
                listViewPatients.setAdapter(patientlistadapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
