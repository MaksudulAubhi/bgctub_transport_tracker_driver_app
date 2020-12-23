package com.example.bgctub_transport_tracker_driver_app.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.bgctub_transport_tracker_driver_app.R;
import com.example.bgctub_transport_tracker_driver_app.services.TrackerService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private HomeViewModel homeViewModel;
    private SwitchCompat activeButton;
    private TextView schedule_time_TextView, schedule_day_TextView, scheduleRoadTextView, start_loc_TextView, destinitionTextView;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference transportInfoDatabaseRef, activeStatusDatabaseRef;
    private static final int PERMISSION_REQUEST = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
       /* final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        }); */

        activeButton = root.findViewById(R.id.activeSwitchButton);
        schedule_time_TextView = root.findViewById(R.id.home_schedule_time_textview);
        schedule_day_TextView = root.findViewById(R.id.home_schedule_day_textview);
        scheduleRoadTextView = root.findViewById(R.id.home_shedule_road_textview);
        start_loc_TextView = root.findViewById(R.id.home_schedule_start_loc_textview);
        destinitionTextView = root.findViewById(R.id.home_schedule_destinition_textview);

        //firebase instance,database
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String userId = mUser.getUid();
        //transport info database path
        final String DATABASE_PATH = "driver_app" + "/" +"transport_info_location"+"/"+userId+ "/" + "transport_information";
        transportInfoDatabaseRef = FirebaseDatabase.getInstance().getReference(DATABASE_PATH);

        //active status databasepath
        final String DATABASE_PATH_STATUS = "driver_app" + "/"+"transport_info_location"+"/" + userId + "/" + "status";
        activeStatusDatabaseRef = FirebaseDatabase.getInstance().getReference(DATABASE_PATH_STATUS);

        //add information to textview
        addInformation();
        //status info
        activeStatus();
        activeButton.setOnCheckedChangeListener(this);
        return root;
    }

    //if data available data add to textView
    public void addInformation() {

        transportInfoDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {

                    String schTime = snapshot.child("start_time_schedule").getValue().toString();
                    String schDate = snapshot.child("start_date_schedule").getValue().toString();
                    String schRoad = snapshot.child("travel_road").getValue().toString();
                    String startLoc = snapshot.child("start_location").getValue().toString();
                    String destinition = snapshot.child("destinition").getValue().toString();

                    schedule_time_TextView.setText(schTime);
                    schedule_day_TextView.setText(schDate);
                    scheduleRoadTextView.setText(schRoad);
                    start_loc_TextView.setText(startLoc);
                    destinitionTextView.setText(destinition);
                } catch (Exception exception) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //active status info from database**
    public void activeStatus() {
        activeStatusDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    String status = snapshot.getValue().toString();
                    if (status.equals("active")) {
                        activeButton.setChecked(true);
                    }
                    if (status.equals("inactive")) {
                        activeButton.setChecked(false);
                    }

                } catch (Exception exception) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    //*****************Start User Tracker task**************************

    //tracker service call method**
    public void startTrackerService() {
        getActivity().startService(new Intent(getActivity(), TrackerService.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startTrackerService();
        } else {
            //    finish();
        }
    }

    //final check and call service to onCreate: check request is granted if ok start service otherwise request permission **
    public void callTrackerService() {

        int permission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            startTrackerService();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST);
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                activeButton.setChecked(false);
                Toast.makeText(getActivity(),"দয়া করে আপনার ফোনের লোকেশান চালু করুন।",Toast.LENGTH_LONG).show();
            } else {
                //if active then start service and status will upload
                activeStatusDatabaseRef.setValue("active");
                callTrackerService();
            }
        }
        if (!isChecked) {
            //if inactive then close tracker service
            activeStatusDatabaseRef.setValue("inactive");
            getActivity().stopService(new Intent(getActivity(), TrackerService.class));
        }

    }
}