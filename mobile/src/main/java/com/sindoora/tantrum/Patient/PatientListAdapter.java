package com.sindoora.tantrum.Patient;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.sindoora.tantrum.R;

import java.util.List;

public class PatientListAdapter extends BaseAdapter {
    Activity context;
    List<Patient> listOfPatients;
    LayoutInflater inflater;

    public PatientListAdapter(Activity context, List<Patient> listOfPatients) {
        this.context = context;
        this.listOfPatients = listOfPatients;
    }

    @Override
    public int getCount() {
        return listOfPatients.size();
    }

    @Override
    public Object getItem(int position) {
        return listOfPatients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.patient_list_item, null, true);

        TextView textViewPatientName = (TextView) listViewItem.findViewById(R.id.patientNameText);
        TextView heartRate = (TextView) listViewItem.findViewById(R.id.heartRateText);
        TextView diagnosis = (TextView) listViewItem.findViewById(R.id.statusText);

        Patient patient = listOfPatients.get(position);

        textViewPatientName.setText(patient.getFirstName());
        heartRate.setText(patient.getHeartRate());
        diagnosis.setText(patient.getDiagnosis());

        return listViewItem;
    }
}
