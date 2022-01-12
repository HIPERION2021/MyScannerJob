package com.example.myscannerjob;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class allow extends AppCompatActivity {


    private TextView tvtext;
    private String mytext;
    private static final int MY_REQUEST_CODE = 123;
    private final String TAG = "allow";
    private Button allow;
    static final String TASK_ID = "MyWork";
    static final String UPLOAD_ID = "MyUpload";
    protected String manufacturer;
    protected String brand;
    protected String model;
    protected String Board_value;
    protected String Hardware_value;
    protected String Serial_nO_value;
    protected int android_id;
    protected String User_value;
    protected String Host_value;
    protected Data data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allow);
        allow = findViewById(R.id.btnAllow);
        tvtext = findViewById(R.id.tvText);

        mytext = "Please note: \n" + "In order for this application to work efficiently it needs the user to allow certain permissions. \n " +
                 "This application DOES NOT reads, save, send or otherwise manage personal information. Your privacy is one of our top concerns, this application will only analyze hardware and software data to help us understand the security level of your system." +
                 "All the information sent to our servers is encrypted. \n" +
                 "Please click on the button below and allow the required permissions for this app to work." +
                 "\n" +
                 "Thank you.";

        tvtext.setText(mytext);
        getIdentifiers();
        data = new Data.Builder()
                .putInt("android_id", android_id)
                .putString("manufacturer", manufacturer)
                .putString("brand", brand)
                .putString("model", model)
                .putString("Board_value", Board_value)
                .putString("Hardware_value", Hardware_value)
                .putString("Serial_nO_value", Serial_nO_value)
                .putString("User_value", User_value)
                .putString("Host_value", Host_value)
                .build();

        allow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_REQUEST_CODE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                   setWork(data);
                   Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                   startActivity(intent);
                   this.finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Please Restart the App and Allow Permissions", Toast.LENGTH_SHORT).show();

                }
                break;
            }
        }


    }

    private void getIdentifiers() {
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID).hashCode();
        manufacturer = Build.MANUFACTURER;
        brand = Build.BRAND;
        model = Build.MODEL;
        Board_value = Build.BOARD;
        Hardware_value = Build.HARDWARE;
        Serial_nO_value = Build.SERIAL;
        User_value = Build.USER;
        Host_value = Build.HOST;
    }

    private void setWork(Data data){
        PeriodicWorkRequest mywork = new PeriodicWorkRequest.Builder(MyTask.class, 15, TimeUnit.MINUTES).setInputData(data).build();
        WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork(TASK_ID, ExistingPeriodicWorkPolicy.KEEP, mywork);


        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest myupload = new PeriodicWorkRequest.Builder(MyUpload.class, 4,TimeUnit.HOURS).setInitialDelay(30, TimeUnit.MINUTES).setConstraints(constraints).build();
        WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork(UPLOAD_ID, ExistingPeriodicWorkPolicy.KEEP, myupload);

    }
    private void requestPermission(){

        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permission1 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.ACCESS_NETWORK_STATE,

                    }, MY_REQUEST_CODE);
        }else{setWork(data);}
    }


}

