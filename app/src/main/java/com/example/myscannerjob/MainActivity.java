package com.example.myscannerjob;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.myscannerjob.receiver.MyErrorHandler;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;

public class MainActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CODE = 123;
    Context context = this;
    private static final String TAG = "Main";
    private TextView myList;
    private Button btnShow;
    protected static final String TASK_ID = "MyWork";
    static final String UPLOAD_ID = "MyUpload";
    static final String URL = "http://192.168.1.30:7500";
    private Button btnPost;
    private Button btnClear;
    protected String manufacturer;
    protected String brand;
    protected String model;
    protected String Board_value;
    protected String Hardware_value;
    protected String Serial_nO_value;
    protected int android_id;
    protected String User_value;
    protected String Host_value;
    protected String myResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // prevent crash and  reset periodic work - post stacktrace.
        Thread.setDefaultUncaughtExceptionHandler(new MyErrorHandler(this));

        //hide title bar - cosmetic only
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        btnPost = findViewById(R.id.post);
        myList = findViewById(R.id.list);
        btnShow = findViewById(R.id.button);
        btnClear = findViewById(R.id.btnClear);
        myList.setMovementMethod(new ScrollingMovementMethod());
        myList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        getIdentifiers();

        Data data = new Data.Builder()
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

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) { // 23
            int permission1 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (permission1 != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_WIFI_STATE,
                                Manifest.permission.ACCESS_NETWORK_STATE,

                        }, MY_REQUEST_CODE);
            }else{setWork(data);}

        }else{setWork(data);}

        //button post
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDAO myDao = new MyDAO(context);
                List<DataModel> myData = myDao.getRecords();
                AsyncHttpClient client = new AsyncHttpClient();
                Gson gson = new Gson();
                String out = gson.toJson(myData);

                HttpEntity entity = null;
                try {
                    entity = new StringEntity(out.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                assert entity != null;

                //authentication headers
                //  String userName = "";
                //  String password = "";
                //  String userNamePassword = userName + ":" + password;
                //  byte[] loginString = userNamePassword.getBytes();
                //  String AuthString = Base64.encodeToString(loginString, Base64.DEFAULT);
                //  client.setAuthenticationPreemptive(true);
                //  client.addHeader("x-auth-token",AuthString);

                client.post(context, URL, entity, "application/json", new TextHttpResponseHandler() {

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {

                    }
                });

            }
        });

        //btn clear
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDAO myDAO = new MyDAO(MainActivity.this);
                SQLiteDatabase db = myDAO.getWritableDatabase();
                db.execSQL("delete FROM "+ MyDAO.TABLE);

            }
        });


        //btn show
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyDAO myDAO = new MyDAO(MainActivity.this);
                List<DataModel> restList = myDAO.getRecords();
                myList.setText(restList.toString());

            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_REQUEST_CODE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Data data = new Data.Builder()
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

                    setWork(data);

                } else {
                    Toast.makeText(getApplicationContext(), "Please Read Carefully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, allow.class);
                    startActivity(intent);
                    this.finish();
                }
                break;
            }
        }
    }

    public void setWork(Data data){
        PeriodicWorkRequest mywork = new PeriodicWorkRequest.Builder(MyTask.class, 15, TimeUnit.MINUTES).setInputData(data).build();
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(TASK_ID, ExistingPeriodicWorkPolicy.KEEP, mywork);


        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest myupload = new PeriodicWorkRequest.Builder(MyUpload.class, 4,TimeUnit.HOURS).setInitialDelay(30, TimeUnit.MINUTES).setConstraints(constraints).build();
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(UPLOAD_ID, ExistingPeriodicWorkPolicy.KEEP, myupload);
        Log.d(TAG, "Work Scheduled");
    }

    public void getIdentifiers(){
        android_id = Settings.Secure.getString(context.getContentResolver(),
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

}
