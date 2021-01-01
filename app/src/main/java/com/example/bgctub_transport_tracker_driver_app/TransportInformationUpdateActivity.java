package com.example.bgctub_transport_tracker_driver_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.bgctub_transport_tracker_driver_app.data_secure.DataSecure;
import com.example.bgctub_transport_tracker_driver_app.model.TransportInformation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class TransportInformationUpdateActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText scheduleTimeEditText,scheduleDateEditText,scheduleRoadEditText, start_loc_EditText,destinitionEditText;
    private EditText transportNameEditText, transportNumberEditText;
    private EditText  driverNameEditText,driverAddressEditText;
    private Button updateButton;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference transportInfoDatabaseRef;
    private ProgressDialog progressDialog;
    private DataSecure dataSecure;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_information_update);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Using for keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //for encoding and decoding
        dataSecure=new DataSecure();

        driverNameEditText=(EditText) findViewById(R.id.driver_name_editText);
        driverAddressEditText=(EditText) findViewById(R.id.driver_address_editText);

        transportNameEditText=(EditText) findViewById(R.id.transport_company_name_editText);
        transportNumberEditText=(EditText)findViewById(R.id.transport_number_editText);

        scheduleTimeEditText=(EditText) findViewById(R.id.schedule_time_editText);
        scheduleDateEditText=(EditText) findViewById(R.id.schedule_date_editText);
        scheduleRoadEditText=(EditText) findViewById(R.id.schedule_road_editText);
        start_loc_EditText=(EditText) findViewById(R.id.schedule_start_loc_editText);
        destinitionEditText=(EditText) findViewById(R.id.schedule_destinition_editText);

        updateButton=(Button) findViewById(R.id.update_button_transport_info) ;
        progressDialog=new ProgressDialog(this);


        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        String userId=mUser.getUid();
        final String DATABASE_PATH="driver_app" + "/" +"transport_info_location"+"/"+ userId + "/" + "transport_information";
        transportInfoDatabaseRef= FirebaseDatabase.getInstance().getReference(DATABASE_PATH);

        //check user logon or not, if not go to signUp activity
        if(mUser==null){
            startActivity(new Intent(this,SignupActivity.class));
            finish();
        }

        //add information to editText
        addInformation();

        scheduleTimeEditText.setOnClickListener(this);
        scheduleDateEditText.setOnClickListener(this);
        updateButton.setOnClickListener(this);
    }

    //if data available data add to edittext
    public void addInformation(){


        transportInfoDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    String name = dataSecure.dataDecode(snapshot.child("driver_name").getValue().toString());
                    String address = dataSecure.dataDecode(snapshot.child("driver_address").getValue().toString());
                    String transName = dataSecure.dataDecode(snapshot.child("vehicle_name").getValue().toString());
                    String transNumber = dataSecure.dataDecode(snapshot.child("vehicle_number").getValue().toString());
                    String schTime = dataSecure.dataDecode(snapshot.child("start_time_schedule").getValue().toString());
                    String schDate = dataSecure.dataDecode(snapshot.child("start_date_schedule").getValue().toString());
                    String schRoad = dataSecure.dataDecode(snapshot.child("travel_road").getValue().toString());
                    String startLoc = dataSecure.dataDecode(snapshot.child("start_location").getValue().toString());
                    String destinition = dataSecure.dataDecode(snapshot.child("destinition").getValue().toString());

                    driverNameEditText.setText(name);
                    driverAddressEditText.setText(address);
                    transportNameEditText.setText(transName);
                    transportNumberEditText.setText(transNumber);
                    scheduleTimeEditText.setText(schTime);
                    scheduleDateEditText.setText(schDate);
                    scheduleRoadEditText.setText(schRoad);
                    start_loc_EditText.setText(startLoc);
                    destinitionEditText.setText(destinition);
                }catch(Exception exception){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TransportInformationUpdateActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }




    //information validation  and upload to database
    public void transportInfoUpdate(){

        String userId=mUser.getUid();
        String name=driverNameEditText.getText().toString().trim();
        String contact=mUser.getPhoneNumber();
        String address=driverAddressEditText.getText().toString().trim();
        String transName=transportNameEditText.getText().toString().trim();
        String transNumber=transportNumberEditText.getText().toString().trim();
        String schTime=scheduleTimeEditText.getText().toString().trim();
        String schDate=scheduleDateEditText.getText().toString().trim();
        String schRoad=scheduleRoadEditText.getText().toString().trim();
        String startLoc=start_loc_EditText.getText().toString().trim();
        String destinition=destinitionEditText.getText().toString().trim();


        if(TextUtils.isEmpty(name)){
            driverNameEditText.setError("দয়া করে আপনার নাম দিন");
            return;
        }
        if(TextUtils.isEmpty(address)){
            driverAddressEditText.setError("দয়া করে আপনার ঠিকানা দিন");
            return;
        }
        if(TextUtils.isEmpty(transName)){
            transportNameEditText.setError("দয়া করে আপনার গাড়ীর কোম্পানি নাম দিন");
            return;
        }
        if(TextUtils.isEmpty(transNumber)){
            transportNumberEditText.setError("দয়া করে আপনার গাড়ীর নম্বর দিন");
            return;
        }
        if(TextUtils.isEmpty(schTime)){
            scheduleTimeEditText.setError("দয়া করে যাত্রা শুরুর সময় দিন");
            return;
        }
        if(TextUtils.isEmpty(schDate)){
            scheduleDateEditText.setError("দয়া করে যাত্রা শুরুর তারিখ দিন");
            return;
        }
        if(TextUtils.isEmpty(startLoc)){
            start_loc_EditText.setError("দয়া করে যাত্রা শুরুর জায়গার নাম দিন");
            return;
        }
        if(TextUtils.isEmpty(destinition)){
            destinitionEditText.setError("দয়া করে আপনার গন্তব্য দিন");
            return;
        }
        if(TextUtils.isEmpty(schRoad)){
            scheduleRoadEditText.setError("দয়া করে রোডের নাম দিন");
            return;
        }

        progressDialog.setMessage("তথ্য আপডেট করা হচ্ছে");
        progressDialog.show();


        //encoding and add data
        TransportInformation transportInformation=new TransportInformation(userId,
                dataSecure.dataEncode(name),dataSecure.dataEncode(contact),
                dataSecure.dataEncode(address),dataSecure.dataEncode(schTime),
                dataSecure.dataEncode(schDate),dataSecure.dataEncode(startLoc),
                dataSecure.dataEncode(destinition),dataSecure.dataEncode(transName),
                dataSecure.dataEncode(transNumber),dataSecure.dataEncode(schRoad));


        try {
            transportInfoDatabaseRef.setValue(transportInformation).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(TransportInformationUpdateActivity.this,"তথ্য আপডেট করা হয়েছে",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TransportInformationUpdateActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    Toast.makeText(TransportInformationUpdateActivity.this,"দয়া করে আবার চেষ্টা করুন",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });
        }catch(Exception exception){
            Toast.makeText(this,"দয়া করে আবার চেষ্টা করুন",Toast.LENGTH_LONG).show();
        }
    }




    //choose time from time picker to click time editText**
    public void timePicker(){

        final int hour,minute;
        final Calendar calendar=Calendar.getInstance();
        hour=calendar.get(Calendar.HOUR_OF_DAY);
        minute=calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                //convert 24 hours time style to 12 hours style**
                if(hourOfDay>12){
                    hourOfDay=hourOfDay-12;

                    //add '0' with 0-9 values**

                    String time=String.valueOf(hourOfDay);
                    String min=String.valueOf(minute);
                    if(hourOfDay<10){
                        time="0".concat(time);
                    }
                    if(minute<10){
                        min="0".concat(min);
                    }
                    scheduleTimeEditText.setText(time+" : "+min+" PM");
                }
                else if(hourOfDay==12){

                    //add '0' with 0-9 values**

                    String time=String.valueOf(hourOfDay);
                    String min=String.valueOf(minute);
                    if(hourOfDay<10){
                        time="0".concat(time);
                    }
                    if(minute<10){
                        min="0".concat(min);
                    }
                    scheduleTimeEditText.setText(time+" : "+min+" PM");
                }
                else if(hourOfDay==0){
                    hourOfDay=12;
                    //add '0' with 0-9 values**

                    String time=String.valueOf(hourOfDay);
                    String min=String.valueOf(minute);
                    if(hourOfDay<10){
                        time="0".concat(time);
                    }
                    if(minute<10){
                        min="0".concat(min);
                    }
                    scheduleTimeEditText.setText(time+" : "+min+" AM");
                }
                else{

                    //add '0' with 0-9 values**
                    String time=String.valueOf(hourOfDay);
                    String min=String.valueOf(minute);
                    if(hourOfDay<10){
                        time="0".concat(time);
                    }
                    if(minute<10){
                        min="0".concat(min);
                    }

                    scheduleTimeEditText.setText(time+" : "+min+" AM");
                }

            }
        },hour,minute,false);
        timePickerDialog.show();
    }

    //choose time from date picker to click date editText**
    public void datePicker(){
        final int day,month,year;
        final Calendar calendar=Calendar.getInstance();
        day=calendar.get(Calendar.DAY_OF_MONTH);
        month=calendar.get(Calendar.MONTH);
        year= calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                scheduleDateEditText.setText(dayOfMonth+"-"+(month+1)+"-"+year);
            }
        },year,month,day);
        datePickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        //choose time from time picker
        if(v==scheduleTimeEditText){
            timePicker();
        }

        //choose date from date picker
        if(v==scheduleDateEditText){
            datePicker();
        }

        //upload data to click button
        if(v==updateButton){
            transportInfoUpdate();
        }
    }
}