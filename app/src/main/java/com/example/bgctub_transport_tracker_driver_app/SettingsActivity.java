package com.example.bgctub_transport_tracker_driver_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bgctub_transport_tracker_driver_app.services.TrackerService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView deleteTextView;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference userIdDatabaseRef;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String userId = mUser.getUid();
        final String DATABASE_PATH = "driver_app" + "/" +"transport_info_location"+"/"+ userId;
        userIdDatabaseRef = FirebaseDatabase.getInstance().getReference(DATABASE_PATH);

        //check user logon or not, if not go signup activity
        if (mUser == null) {
            startActivity(new Intent(this, SignupActivity.class));
            finish();
        }

        mProgressDialog = new ProgressDialog(this);
        deleteTextView = (TextView) findViewById(R.id.deleteAccountTextView);
        deleteTextView.setOnClickListener(this);
    }

    //delete a user and user information

    public void deleteAccountAndInfo() {
        mProgressDialog.setMessage("দয়া করে অপেক্ষা করুন");
        mProgressDialog.show();
        assert mUser != null;
        if (mUser != null) {
            mUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        userIdDatabaseRef.removeValue();
                        //stop tracker service after delete account**
                        stopService(new Intent(SettingsActivity.this, TrackerService.class));
                        startActivity(new Intent(SettingsActivity.this, SignupActivity.class));
                        finish();
                        Toast.makeText(SettingsActivity.this, "আপনার অ্যাকাউন্টটি সঠিকভাবে ডিলেট করা হয়েছে", Toast.LENGTH_LONG).show();

                    }
                    mProgressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(SettingsActivity.this, "দয়া করে পুনরায় লগইন করে আবার চেষ্টা করুন", Toast.LENGTH_LONG).show();
                    Toast.makeText(SettingsActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    mProgressDialog.dismiss();
                }
            });
        }
    }

    //alert dialog builder for confirm account delete

    AlertDialog.Builder buildDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("আপনি কি আপনার অ্যাকাউন্টটি ডিলেট করতে চান?");
        builder.setPositiveButton("ঠিকআছে", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //delete if ok
                deleteAccountAndInfo();
            }
        });
        builder.setNegativeButton("বাদ দিন", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        return builder;
    }


    @Override
    public void onClick(View v) {
        if (v == deleteTextView) {
            buildDialog(SettingsActivity.this).show();
        }
    }
}
