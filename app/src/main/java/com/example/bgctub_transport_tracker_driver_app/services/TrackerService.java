package com.example.bgctub_transport_tracker_driver_app.services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TrackerService extends Service {
    private static final String TAG = TrackerService.class.getSimpleName();
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference userLocationDatabaseRef;
    private FusedLocationProviderClient client;
    private LocationRequest locationRequest;


    public TrackerService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String userId = mUser.getUid();
        String DATABASE_PATH_location = "driver_app" + "/" +"transport_info_location"+"/"+ userId + "/" + "locations";
        userLocationDatabaseRef = FirebaseDatabase.getInstance().getReference(DATABASE_PATH_location);

        client = LocationServices.getFusedLocationProviderClient(this);


        //update location**
        requestLocationUpdates();
        //    buildAppNotification();
    }


/*
    private void buildAppNotification() {
        String stop = "stop";
        registerReceiver(stopReceiver, new IntentFilter(stop));

        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder appNotificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_text))
                .setOngoing(true)
                .setContentIntent(broadcastIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(getString(R.string.notification_text)))
                .setSmallIcon(R.drawable.logo1);
        startForeground(1, appNotificationBuilder.build());
    }


    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        // app stop after tapped notifications
            unregisterReceiver(stopReceiver);
            stopSelf();
        }
    };

*/

    //location callback check and get last location and update to database****

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Location location = locationResult.getLastLocation();
            if (location != null) {
                userLocationDatabaseRef.setValue(location);
            }
        }
    };

    // request location check location in time period continously**

    private void requestLocationUpdates(){
        locationRequest=new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            client.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
        //stop request location updates
        client.removeLocationUpdates(locationCallback);
    }


    /*
        //update location to database after a time interval
        private void requestLocationUpdates() {

            final LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(5000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            final FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

            int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (permission == PackageManager.PERMISSION_GRANTED) {
                client.requestLocationUpdates(locationRequest,new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        if (mUser != null) {
                            Location location = locationResult.getLastLocation();
                            if (location != null) {
                                userLocationDatabaseRef.setValue(location);
                            }
                        }
                    }
                }, null);
            }
        }
    */
}
