package com.example.bgctub_transport_tracker_driver_app.ui.transportInformation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bgctub_transport_tracker_driver_app.R;
import com.example.bgctub_transport_tracker_driver_app.TransportInformationUpdateActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TransportInformationFragment extends Fragment implements View.OnClickListener {

    private TransportInformationViewModel mViewModel;
    private ImageButton openEditTransportInfoImgBtn;
    private TextView driverNameTextView, driverContactTextView, driverAddressTextView;
    private TextView transportNameTextView, transportNumberTextView;
    private TextView schedule_time_TextView, schedule_day_TextView, scheduleRoadTextView, start_loc_TextView, destinitionTextView;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference transportInfoDatabaseRef;

    public static TransportInformationFragment newInstance() {
        return new TransportInformationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.transport_information_fragment, container, false);
        openEditTransportInfoImgBtn = root.findViewById(R.id.transInfo_View_edit_imgBtn);

        driverNameTextView = root.findViewById(R.id.driver_name_textview);
        driverContactTextView = root.findViewById(R.id.driver_contact_textview);
        driverAddressTextView = root.findViewById(R.id.driver_address_textview);

        transportNameTextView = root.findViewById(R.id.transport_name_textview);
        transportNumberTextView = root.findViewById(R.id.transport_number_textview);

        schedule_time_TextView = root.findViewById(R.id.schedule_time_textview);
        schedule_day_TextView = root.findViewById(R.id.schedule_day_textview);
        scheduleRoadTextView = root.findViewById(R.id.shedule_road_textview);
        start_loc_TextView = root.findViewById(R.id.schedule_start_loc_textview);
        destinitionTextView = root.findViewById(R.id.schedule_destinition_textview);


        openEditTransportInfoImgBtn.setOnClickListener(this);

        //firebase instance,database
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String userId = mUser.getUid();
        final String DATABASE_PATH = "driver_app" + "/" + "transport_info_location" + "/" + userId + "/" + "transport_information";
        transportInfoDatabaseRef = FirebaseDatabase.getInstance().getReference(DATABASE_PATH);

        //add information to textview
        addInformation();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TransportInformationViewModel.class);
        // TODO: Use the ViewModel
    }


    //if data available data add to textview
    public void addInformation() {


        transportInfoDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    String name = snapshot.child("driver_name").getValue().toString();
                    String contact = snapshot.child("driver_contact").getValue().toString();
                    String address = snapshot.child("driver_address").getValue().toString();
                    String transName = snapshot.child("vehicle_name").getValue().toString();
                    String transNumber = snapshot.child("vehicle_number").getValue().toString();
                    String schTime = snapshot.child("start_time_schedule").getValue().toString();
                    String schDate = snapshot.child("start_date_schedule").getValue().toString();
                    String schRoad = snapshot.child("travel_road").getValue().toString();
                    String startLoc = snapshot.child("start_location").getValue().toString();
                    String destinition = snapshot.child("destinition").getValue().toString();

                    driverNameTextView.setText(name);
                    driverContactTextView.setText(contact);
                    driverAddressTextView.setText(address);
                    transportNameTextView.setText(transName);
                    transportNumberTextView.setText(transNumber);
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

    @Override
    public void onClick(View v) {
        if (v == openEditTransportInfoImgBtn) {
            startActivity(new Intent(getActivity(), TransportInformationUpdateActivity.class));
        }
    }
}

