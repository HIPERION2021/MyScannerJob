package com.example.myscannerjob.receiver;

import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.os.Process;
import android.provider.Settings;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.myscannerjob.DataModel;
import com.example.myscannerjob.MyUpload;
import com.example.myscannerjob.MyTask;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;

public class MyErrorHandler implements
        Thread.UncaughtExceptionHandler{
    private final Context myContext;
    static final String TASK_ID = "MyWork";
    static final String UPLOAD_ID = "MyUpload";
    protected final String URL = "http://192.168.1.30:7500";
    protected String manufacturer;
    protected String brand;
    protected String model;
    protected String Board_value;
    protected String Hardware_value;
    protected String Serial_nO_value;
    protected int android_id;
    protected String BootLoader_value;
    protected String User_value;
    protected String Host_value;


    public MyErrorHandler(Context context) {
        myContext = context;

    }

    public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        System.err.println(stackTrace);
        getIdentifiers();
        //make a post for the log!!!
             DataModel crashdata = new DataModel("crash",manufacturer,brand,model ,Board_value,Hardware_value,Serial_nO_value,android_id,BootLoader_value,User_value,Host_value,"crash","crash","crash","crash","crash","crash","crash","crash","crash","crash","crash","crash",stackTrace.toString(), "crash", "crash", "crash", "crash", 3);
             Gson gson = new Gson();
             String out = gson.toJson(crashdata);
             AsyncHttpClient client = new AsyncHttpClient();
             Thread thread1 = new Thread(new Runnable() {
                 @Override
                 public void run() {
                     Looper.prepare();
                     HttpEntity entity = null;
                     try {
                         entity = new StringEntity(out);
                     } catch (UnsupportedEncodingException e) {
                         e.printStackTrace();
                     }
                     assert entity != null;

                     client.post(myContext, URL, entity, "application/json", new AsyncHttpResponseHandler() {
                         @Override
                         public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                         }

                         @Override
                         public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                         }

                     });

                 }
             });
            thread1.start();


        //end post()!!

        Data data = new Data.Builder()
                .putInt("android_id", android_id)
                .putString("manufacturer", manufacturer)
                .putString("brand", brand)
                .putString("model", model)
                .putString("Board_value", Board_value)
                .putString("Hardware_value", Hardware_value)
                .putString("Serial_nO_value", Serial_nO_value)
                .putString("BootLoader_value", BootLoader_value)
                .putString("User_value", User_value)
                .putString("Host_value", Host_value)
                .build();


        PeriodicWorkRequest mywork = new PeriodicWorkRequest.Builder(MyTask.class, 1, TimeUnit.HOURS).setInputData(data).build();
        WorkManager.getInstance(myContext).enqueueUniquePeriodicWork(TASK_ID, ExistingPeriodicWorkPolicy.KEEP, mywork);

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest myupload = new PeriodicWorkRequest.Builder(MyUpload.class, 4,TimeUnit.HOURS).setInitialDelay(1, TimeUnit.MINUTES).setConstraints(constraints).build();
        WorkManager.getInstance(myContext).enqueueUniquePeriodicWork(UPLOAD_ID, ExistingPeriodicWorkPolicy.KEEP, myupload);

        Process.killProcess(Process.myPid());
        System.exit(1);

    }

    public void getIdentifiers(){
        android_id = Settings.Secure.getString(myContext.getContentResolver(),
                Settings.Secure.ANDROID_ID).hashCode();
        manufacturer = Build.MANUFACTURER;
        brand = Build.BRAND;
        model = Build.MODEL;
        Board_value = Build.BOARD;
        Hardware_value = Build.HARDWARE;
        Serial_nO_value = Build.SERIAL;
        BootLoader_value = Build.BOOTLOADER;
        User_value = Build.USER;
        Host_value = Build.HOST;

    }


}


