package com.example.myscannerjob;
import static android.content.Context.ACTIVITY_SERVICE;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.myscannerjob.receiver.MyErrorHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import okhttp3.Headers;


public class MyTask extends Worker {

    private static final String TAG = "MyScan";
    private WifiManager wifiManager;
    protected String myscan;

    public static final String IP_URL = "https://api.ipify.org/?format=json";
    Context context = getApplicationContext();
    protected String myIP;
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
    protected String Version;
    protected String API_level;
    protected String Build_ID;
    protected String Build_Time;
    protected String Fingerprint;
    protected String carrierName;
    protected String apps;
    protected String proc;
    protected BluetoothAdapter blueTooth;
    protected String blueConnDev;
    protected String blueStatus;
    protected String netprop;
    protected String netcap;
    protected String logPfinal;
    protected ConnectivityManager connectivityManager;
    ActivityManager activityManager;
    private SensorManager sensorManager;
    protected String sensorList;
    protected String myMemory;
    private int rootState;
    LocationManager locationManager;
    protected String mySettings;

    ProcessBuilder processBuilder;
    InputStream inputStream;
    Process process ;
    byte[] byteArry ;
    protected String cpuInfo;

    protected volatile boolean locTest;

    public MyTask(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        Thread.setDefaultUncaughtExceptionHandler(new MyErrorHandler(getApplicationContext()));
        blueTooth = BluetoothAdapter.getDefaultAdapter();
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        cpuInfo = "";
        myscan = "false";
        netcap = "n/a";
        netprop = "n/a";

        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
           // test = true;
        }
        locationManager = (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locTest = true;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            locTest = locationManager.isLocationEnabled();
        }

    }

    @NonNull
    @Override
    public Result doWork() {

    // need to add if statement to ignore wifi scans on android 5 or lower
      //  if(!wifiManager.isWifiEnabled() && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
      //      wifiManager.setWifiEnabled(true);
      //      test = false;
      //  }

        BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean success = intent.getBooleanExtra(
                        WifiManager.EXTRA_RESULTS_UPDATED, false);
                if (success) {
                    scanSuccess();
                } else {
                    // scan failure handling
                    scanFailure();
                }
            }
        };

        getIdentifiers();
        getSettings();
        getSensors();
        getCarrier();
        getApps();
        getbluestate();
        getActiveNet();
        getRunningProc();
        getCPUInfo();
        getMemory();
        rootState = getRootState(context);
        Log.d("!!!ROOT!!","rooted is = "+ rootState);
        logPfinal = "Protected";


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (wifiManager.isWifiEnabled() && locTest) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
                getApplicationContext().registerReceiver(wifiScanReceiver, intentFilter);

                boolean success = wifiManager.startScan();
                if (!success) {
                    scanFailure();
                }
            }else{
                //here android 6.0 but wifi is off or loc is off - wont scan
                myscan = "null";
                NetworkInfo currentNetwork = connectivityManager.getActiveNetworkInfo();
                boolean conn = currentNetwork != null && currentNetwork.isAvailable() && currentNetwork.isConnected();
                if(conn){record();}else{myIP = "no conn"; save();}
            }
        }else{
            //here code for android 5.0 and lower
            myscan = "null";
            myIP = "no conn";
            NetworkInfo currentNetwork = connectivityManager.getActiveNetworkInfo();
            boolean conn = currentNetwork != null && currentNetwork.isAvailable() && currentNetwork.isConnected();
            if(conn){record();}else{myIP = "no conn"; save();}
        }


        return Result.success();
    }

    private void scanSuccess() {
        List<ScanResult> results = wifiManager.getScanResults();
        myscan = results.toString();
        record();

        //if(!test){wifiManager.setWifiEnabled(false);}

    }

    private void scanFailure() {
        List<ScanResult> results = wifiManager.getScanResults();
        myscan = results.toString();
        record();
        //if(!test){wifiManager.setWifiEnabled(false);}

    }


    private void getIdentifiers(){
        manufacturer = getInputData().getString("manufacturer");
        brand = getInputData().getString("brand");
        model = getInputData().getString("model");
        Board_value = getInputData().getString("Board_value");
        Hardware_value = getInputData().getString("Hardware_value");
        Serial_nO_value = getInputData().getString("Serial_nO_value");
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID).hashCode();
        BootLoader_value = Build.BOOTLOADER;;
        User_value = getInputData().getString("User_value");
        Host_value = getInputData().getString("Host_value");
        Version = Build.VERSION.RELEASE;
        API_level = Build.VERSION.SDK_INT + "";
        Build_ID = Build.ID;
        Build_Time = Build.TIME + "";
        Fingerprint = Build.FINGERPRINT;

    }

    private void getCarrier() {
        TelephonyManager manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        carrierName = manager.getNetworkOperatorName();

    }

    private void getApps() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = context.getPackageManager().queryIntentActivities( mainIntent, 0);
        apps = pkgAppsList.toString();

    }

    private void getbluestate() {
        if(blueTooth.equals(null)){
            blueConnDev = "N/a";
        }else{
            Set<BluetoothDevice> paired = blueTooth.getBondedDevices();
            StringBuilder pairedblue = new StringBuilder();
            if(paired.size()>0){
                for(BluetoothDevice device: paired){
                    pairedblue.append(device.getName());
                    pairedblue.append(device.getAddress());
                    pairedblue.append(",");
                    pairedblue.append("\n");

                }
                blueConnDev = pairedblue.toString();
            }
        }
        assert blueTooth != null;
        if(blueTooth.isEnabled()){
            blueStatus = "Active";
        }else{
            blueStatus = "OFF";
        }

    }

    private void getActiveNet() {
        Network currentNetwork = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            currentNetwork = connectivityManager.getActiveNetwork();
                if(currentNetwork != null){
                    NetworkCapabilities caps = connectivityManager.getNetworkCapabilities(currentNetwork);
                    LinkProperties linkProperties = connectivityManager.getLinkProperties(currentNetwork);
                    netcap = caps.toString();
                    netprop = linkProperties.toString();
               }

        }
    }

    private void getRunningProc() {
        activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> runningProcesses = activityManager.getRunningAppProcesses();
        StringBuilder b = new StringBuilder();
        for (ActivityManager.RunningAppProcessInfo process: runningProcesses) {
            b.append(process.processName);
            b.append("\n");
        }
        proc = b.toString();
    }

    private void getSensors(){
        List<Sensor> deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        sensorList = deviceSensors.toString();

    }

    private void getCPUInfo(){
        String[] DATA = {"/system/bin/cat", "/proc/cpuinfo"};
        byteArry = new byte[1024];
        try{
            processBuilder = new ProcessBuilder(DATA);
            process = processBuilder.start();
            inputStream = process.getInputStream();
            while(inputStream.read(byteArry) != -1){
                cpuInfo = cpuInfo + new String(byteArry);

            }
            inputStream.close();
            } catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void getMemory(){
        ActivityManager.MemoryInfo memory = new ActivityManager.MemoryInfo();
        activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memory);
        List<Long> list = new ArrayList<>();
        list.add(memory.availMem);
        list.add(memory.totalMem);
        if(memory.lowMemory){list.add(1L);}else{list.add(0L);}
        myMemory = list.toString();
        Log.d("!!!memory!!!","list is = "+ myMemory);


    }

    private void record(){

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(IP_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d("!!!","onSuccess status code" + statusCode);
                JSONObject jsonObject = json.jsonObject;
                try {
                    myIP = jsonObject.getString("ip");
                    save();

                } catch (JSONException e) {
                    Log.e(TAG, "error getting ip = "+ e);
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                myIP = "no connection";
                save();
            }
        });
    }

    public static int getRootState(Context context) {
        String buildTags = Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            return 1;
        } else {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                return 1;
            } else {
                file = new File("/system/xbin/su");
                if(file.exists()){return 1;}else{return 0;}
            }
        }

    }

    private void save(){
        MyDAO myDAO = new MyDAO(getApplicationContext());
        DataModel dataModel;
        try{
            dataModel = new DataModel(myIP,manufacturer,brand,model,Board_value,Hardware_value,Serial_nO_value,android_id,BootLoader_value,User_value,Host_value,Version,API_level,Build_ID,Build_Time,Fingerprint,carrierName,apps,proc,blueConnDev,blueStatus,netprop,netcap,logPfinal,myscan, sensorList,cpuInfo, myMemory, rootState);
            myDAO.addRow(dataModel);

        }catch(Exception e){
            Log.e("Task", "Error MyDao"+ e.getMessage() );
        }

    }
    private void getSettings(){
        String default_input =  Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
        String roaming = Settings.Global.getString(getApplicationContext().getContentResolver(), Settings.Global.DATA_ROAMING);
        //On a multiuser device with a separate system user, the screen may be locked as soon as this is set to true and further activities cannot be launched on the system user unless they are marked to show over keyguard
        String provisioned = Settings.Global.getString(getApplicationContext().getContentResolver(), Settings.Global.DEVICE_PROVISIONED);
        String non_market = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.INSTALL_NON_MARKET_APPS);
        String USB_mass = Settings.Global.getString(getApplicationContext().getContentResolver(), Settings.Global.USB_MASS_STORAGE_ENABLED);
        String loc_prov = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        String airplane_mode = Settings.Global.getString(getApplicationContext().getContentResolver(), Settings.Global.AIRPLANE_MODE_ON);
        // 1 if it finishes activities as soon as they are not needed / 0 for normal extended life behavior
        int finish_act = Settings.Global.getInt(getApplicationContext().getContentResolver(), Settings.Global.ALWAYS_FINISH_ACTIVITIES, 0);



        StringBuilder sb = new StringBuilder();
        sb.append("[input="+default_input+",");
        sb.append("airplane="+airplane_mode+",");
        sb.append("finish="+finish_act+",");
        sb.append("roaming="+roaming+",");
        sb.append("provisioned="+provisioned+",");
        sb.append("non_market="+non_market+",");
        sb.append("loc_providers="+loc_prov+",");
        sb.append("USB_mass="+USB_mass+"]");

        mySettings = sb.toString();

    }


}
