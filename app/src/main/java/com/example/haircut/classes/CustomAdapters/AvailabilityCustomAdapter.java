package com.example.haircut.classes.CustomAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.haircut.R;
import com.example.haircut.activities.MainActivity;
import com.example.haircut.classes.DataModels.HairStyleDataModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class AvailabilityCustomAdapter extends RecyclerView.Adapter<AvailabilityCustomAdapter.MyViewHolder>  {

    private final ArrayList<String> dataSet;
    private final HairStyleDataModel hairStyleDataModel;
    private final FirebaseDatabase database;
    private final FirebaseAuth mAuto;
    private final MainActivity mainActivity;

    public AvailabilityCustomAdapter(ArrayList<String> data, HairStyleDataModel hairStyleAppointment, MainActivity main) {
        this.dataSet = data;
        this.hairStyleDataModel = hairStyleAppointment;
        database = FirebaseDatabase.getInstance();
        mAuto = FirebaseAuth.getInstance();
        this.mainActivity = main;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cardViewAvailable;
        TextView textViewHour;
        Button set;

        public MyViewHolder(View itemView) {
            super(itemView);

            this.set = itemView.findViewById(R.id.buttonSet);
            this.cardViewAvailable = itemView.findViewById(R.id.available_card_view);
            this.textViewHour = itemView.findViewById(R.id.textViewHour);
        }
    }

    @NonNull
    @Override
    public AvailabilityCustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.available_card, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvailabilityCustomAdapter.MyViewHolder holder, int position) {

        TextView textViewHour = holder.textViewHour;


        Button btnSet = holder.set;

        String hour = dataSet.get(position);
        textViewHour.setText(hour);

        btnSet.setOnClickListener(v -> {
            hairStyleDataModel.setHour(hour);
            hairStyleDataModel.setUserId(Objects.requireNonNull(mAuto.getCurrentUser()).getUid());

            DatabaseReference myRef = database.getReference("appointments").child("appointmentsList");
            myRef.child(hairStyleDataModel.getDate()).child(hairStyleDataModel.getHour()).setValue(hairStyleDataModel);
            Toast.makeText(mainActivity,"\n" +
                    "Đã đặt lịch hẹn thành công",Toast.LENGTH_LONG).show();
            mainActivity.setMainFragment();
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
