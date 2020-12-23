package com.example.bgctub_transport_tracker_driver_app.ui.report;

import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bgctub_transport_tracker_driver_app.BuildConfig;
import com.example.bgctub_transport_tracker_driver_app.R;
import com.example.bgctub_transport_tracker_driver_app.model.ReportFeedback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportFragment extends Fragment implements View.OnClickListener {

    private ReportViewModel mViewModel;
    private EditText reportTitleEditText, reportInfoEditText;
    private Button reportSubmitButton;
    private FirebaseAuth mAuth;
    private DatabaseReference driverReportDatabaseRef;
    ProgressDialog progressDialog;

    public static ReportFragment newInstance() {
        return new ReportFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.report_fragment, container, false);
        reportTitleEditText=root.findViewById(R.id.report_title_editText);
        reportInfoEditText=root.findViewById(R.id.report_info_editText);
        reportSubmitButton=root.findViewById(R.id.report_submit_button);

        mAuth=FirebaseAuth.getInstance();
        String userId=mAuth.getCurrentUser().getUid();
        String database_path="driver_app"+"/"+"report_feedback";
        driverReportDatabaseRef= FirebaseDatabase.getInstance().getReference(database_path);

        reportSubmitButton.setOnClickListener(this);
        progressDialog=new ProgressDialog(getActivity());
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        // TODO: Use the ViewModel
    }

    //data validation and upload report and feedback to database
    public void updateReportFeedback(){
        //for system configuration
        String model = Build.MODEL;
        String manufacture = Build.MANUFACTURER;
        String brand = Build.BRAND;
        String product = Build.PRODUCT;
        String version = String.valueOf(Build.VERSION.SDK_INT);
        String version_rel = Build.VERSION.RELEASE;
        String configuration = "[Model: " + model + "] [" + "Manufacturer: " + manufacture + "] " +
                "[Brand: " + brand + "] [" + "Product: " + product + "] " +
                "[Version: " + version + "] [" + "Version Release: " + version_rel + "]";


        //for current date and time
        Date date=new Date();
        SimpleDateFormat ft=new SimpleDateFormat("E dd.MM.yyyy 'at' hh:mm:ss a zzz");
        String timePost=ft.format(date);


        //other fields
        String userId=mAuth.getCurrentUser().getUid();
        String report_title=reportTitleEditText.getText().toString().trim();
        String report_info=reportInfoEditText.getText().toString().trim();
        String version_name= BuildConfig.VERSION_NAME;
        String app_name_version="Driver App version: "+version_name;
        String userPhone=mAuth.getCurrentUser().getPhoneNumber();


        //input validation**

        if(TextUtils.isEmpty(report_title)){
            reportTitleEditText.setError("দয়া করে আপনার সমস্যা অথবা ফিডব্যাকের টাইটেল দিন");
            return;
        }
        if(TextUtils.isEmpty(report_info)){
            reportInfoEditText.setError("দয়া করে আপনার সমস্যা অথবা ফিডব্যাক সম্বন্ধে জানান");
            return;
        }

        progressDialog.setMessage("দয়া করে অপেক্ষা করুন");
        progressDialog.show();
        try{
            ReportFeedback reportFeedback=new ReportFeedback(userId,report_title,report_info,userPhone,app_name_version,timePost,configuration);
            //used pushed id
            driverReportDatabaseRef.push().setValue(reportFeedback);
            Toast.makeText(getActivity(),"ধন্যবাদ তথ্য সাবমিট করা হয়েছে",Toast.LENGTH_LONG).show();
        }catch (Exception exception){
            Toast.makeText(getActivity(),"দুঃখিত আবার চেষ্টা করুন",Toast.LENGTH_LONG).show();
        }
        progressDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        if(v==reportSubmitButton){
            updateReportFeedback();
        }
    }

}