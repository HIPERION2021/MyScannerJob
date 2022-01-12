package com.example.myscannerjob;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Looper;


import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.myscannerjob.receiver.MyErrorHandler;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;


public class MyUpload extends Worker {


    com.example.myscannerjob.MyDAO myDAO;
    protected final String URL = "http://192.168.1.30:7500";

    public MyUpload(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        myDAO = new MyDAO(getApplicationContext());
        Thread.setDefaultUncaughtExceptionHandler(new MyErrorHandler(getApplicationContext()));
    }

    @NonNull
    @Override
    public Result doWork() {
        // her we upload the data
        com.example.myscannerjob.MyDAO myDao = new MyDAO(getApplicationContext());
        List<DataModel> myData = myDao.getRecords();
        AsyncHttpClient client = new AsyncHttpClient();
        Gson gson = new Gson();
        String out = gson.toJson(myData);



        Thread thread = new Thread(new Runnable() {
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

                client.post(getApplicationContext(), URL, entity, "application/json", new AsyncHttpResponseHandler() {


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        SQLiteDatabase db = myDAO.getWritableDatabase();
                        db.execSQL("delete FROM "+ MyDAO.TABLE);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });


            }
        });
        thread.start();


        return Result.success();
    }
}
