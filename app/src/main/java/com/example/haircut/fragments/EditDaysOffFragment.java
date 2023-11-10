package com.example.haircut.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.haircut.R;
import com.example.haircut.activities.MainActivity;
import com.example.haircut.classes.Settings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditDaysOffFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditDaysOffFragment extends Fragment {

    MainActivity mainActivity;
    public FirebaseDatabase database;


    String currentDate;

    public EditDaysOffFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EditDaysOffFragment.
     */
    public static EditDaysOffFragment newInstance() {
        return new EditDaysOffFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_days_off, container, false);
        CalendarView dayOffCalender = view.findViewById(R.id.calendarViewDaysOff);
        dayOffCalender.setOnDateChangeListener(this::onSelectedDayChange);

        Button addDaysOff = view.findViewById(R.id.buttonAddDayOff);
        addDaysOff.setOnClickListener(v -> {
            mainActivity = (MainActivity) getActivity();
            if (currentDate == null)
            {
                Toast.makeText(mainActivity, "\n" +
                        "Vui lòng chọn Ngày", Toast.LENGTH_SHORT).show();
            }
            else
            {
                addDayOff(currentDate);
            }
        });

        Button cancelDaysOff = view.findViewById(R.id.buttonCancelDayOff);
        cancelDaysOff.setOnClickListener(v -> {
            mainActivity = (MainActivity) getActivity();
            if (currentDate == null)
            {
                Toast.makeText(mainActivity, "Vui lòng chọn Ngày", Toast.LENGTH_SHORT).show();
            }
            else
            {
                cancelDayOff(currentDate);
            }
        });

        return view;
    }

    public void addDayOff(String date) {
        DatabaseReference myRef = database.getReference("settings");
        myRef.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
            }
            else {
                Settings settings = new Settings();
                settings.DayOffList = (HashMap<String, String>) ((HashMap)task.getResult().getValue(Object.class)).get("DayOffList");
                if(settings.DayOffList == null || !settings.DayOffList.containsValue(date)) {
                    Toast.makeText(mainActivity, "Day Off updated successfully on " + date, Toast.LENGTH_LONG).show();
                    myRef.child("DayOffList").push().setValue(date);
                }
                else{
                    Toast.makeText(mainActivity, "This is already Day Off " + date, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void cancelDayOff(String date) {
        DatabaseReference myRef = database.getReference("settings");
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                }
                else {
                    Settings settings = new Settings();
                    settings.DayOffList = (HashMap<String, String>) ((HashMap)task.getResult().getValue(Object.class)).get("DayOffList");
                    if(settings.DayOffList == null || !settings.DayOffList.containsValue(date)) {
                        Toast.makeText(mainActivity, "This is not Day Off " + date, Toast.LENGTH_LONG).show();
                    }
                    else{
                        String keyToRemove = "";
                        for (String key: settings.DayOffList.keySet())
                        {
                            if (date.equals(settings.DayOffList.get(key))) {
                                keyToRemove = key;
                                break;
                            }
                        }
                        myRef.child("DayOffList").child(keyToRemove).removeValue();
                        Toast.makeText(mainActivity, "Day Off "+date +" Removed", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void onSelectedDayChange(CalendarView view1, int year, int month, int dayOfMonth) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yy");
        currentDate = format.format(new Date(year, month, dayOfMonth));
    }
}