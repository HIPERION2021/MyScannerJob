package com.example.myscannerjob;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyDAO extends SQLiteOpenHelper {

    public static final String TABLE = "FENIX1";
    public static final String COLUMN_MANUFACTURER = "manufacturer";
    public static final String COLUMN_MY_IP = "myIP";
    public static final String COLUMN_BRAND = "brand";
    public static final String COLUMN_MODEL = "model";
    public static final String COLUMN_BOARD = "board";
    public static final String COLUMN_HARDWARE = "hardware";
    public static final String COLUMN_SERIAL = "serial";
    public static final String COLUMN_ANDROID_ID = "android_id";
    public static final String COLUMN_BOOTLOADER = "bootloader";
    public static final String COLUMN_USER = "user";
    public static final String COLUMN_HOST = "host";
    public static final String COLUMN_VERSION = "version";
    public static final String COLUMN_API_LV = "api_lv";
    public static final String COLUMN_BUILD_ID = "build_id";
    public static final String COLUMN_BUILD_TIME = "build_time";
    public static final String COLUMN_FINGER_PRINT = "finger_print";
    public static final String COLUMN_CARRIER = "carrier";
    public static final String COLUMN_APPS = "apps";
    public static final String COLUMN_PROC = "proc";
    public static final String COLUMN_BULE_DEV = "bule_dev";
    public static final String COLUMN_BLUE_STAT = "blue_stat";
    public static final String COLUMN_NET_PROP = "net_prop";
    public static final String COLUMN_NET_CAP = "net_cap";
    public static final String COLUMN_LOG = "log";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_WIFI = "wifi";
    public static final String COLUMN_SENSOR = "sensor";
    public static final String COLUMN_CPU   = "cpu";
    public static final String COLUMN_MEMORY = "memory";
    public static final String COLUMN_ROOT = "root";


    public MyDAO(@Nullable Context context) {

        super(context, "Fenix.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createtbl = "CREATE TABLE " + TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_MY_IP + " TEXT, " + COLUMN_MANUFACTURER + " TEXT, " + COLUMN_BRAND + " TEXT, " + COLUMN_MODEL + " TEXT, " + COLUMN_BOARD + " TEXT, " + COLUMN_HARDWARE + " TEXT, " + COLUMN_SERIAL + " TEXT, " + COLUMN_ANDROID_ID + " TEXT, " + COLUMN_BOOTLOADER + " TEXT, " + COLUMN_USER + " TEXT, " + COLUMN_HOST + " TEXT, " + COLUMN_VERSION + " TEXT, " + COLUMN_API_LV + " TEXT, " + COLUMN_BUILD_ID + " TEXT, " + COLUMN_BUILD_TIME + " TEXT, " + COLUMN_FINGER_PRINT + " TEXT, " + COLUMN_CARRIER + " TEXT, " + COLUMN_APPS + " TEXT, " + COLUMN_PROC + " TEXT, " + COLUMN_BULE_DEV + " TEXT, " + COLUMN_BLUE_STAT + " BOOL, " + COLUMN_NET_PROP + " TEXT, " + COLUMN_NET_CAP + " TEXT, " + COLUMN_LOG + " TEXT, " + COLUMN_WIFI + " TEXT, " + COLUMN_SENSOR + " TEXT, " + COLUMN_CPU + " TEXT,  " + COLUMN_MEMORY + " TEXT, " + COLUMN_ROOT + " TEXT)";
        sqLiteDatabase.execSQL(createtbl);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addRow(DataModel dataModel){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MY_IP, dataModel.getMyIP());
        values.put(COLUMN_MANUFACTURER, dataModel.getManufacturer());
        values.put(COLUMN_BRAND, dataModel.getBrand());
        values.put(COLUMN_MODEL, dataModel.getModel());
        values.put(COLUMN_BOARD, dataModel.getBoard_value());
        values.put(COLUMN_HARDWARE, dataModel.getHardware_value());
        values.put(COLUMN_SERIAL, dataModel.getSerial_nO_value());
        values.put(COLUMN_ANDROID_ID, dataModel.getAndroid_id());
        values.put(COLUMN_BOOTLOADER, dataModel.getBootLoader_value());
        values.put(COLUMN_USER, dataModel.getUser_value());
        values.put(COLUMN_HOST, dataModel.getHost_value());
        values.put(COLUMN_VERSION, dataModel.getVersion());
        values.put(COLUMN_API_LV, dataModel.getAPI_level());
        values.put(COLUMN_BUILD_ID, dataModel.getBuild_ID());
        values.put(COLUMN_BUILD_TIME, dataModel.getBuild_Time());
        values.put(COLUMN_FINGER_PRINT, dataModel.getFingerprint());
        values.put(COLUMN_CARRIER, dataModel.getCarrierName());
        values.put(COLUMN_APPS, dataModel.getApps());
        values.put(COLUMN_PROC, dataModel.getProc());
        values.put(COLUMN_BULE_DEV, dataModel.getBlueConnDev());
        values.put(COLUMN_BLUE_STAT, dataModel.getBlueStatus());
        values.put(COLUMN_NET_PROP, dataModel.getNetprop());
        values.put(COLUMN_NET_CAP, dataModel.getNetcap());
        values.put(COLUMN_LOG, dataModel.getLogPfinal());
        values.put(COLUMN_WIFI, dataModel.getwifi());
        values.put(COLUMN_SENSOR, dataModel.getSensors());
        values.put(COLUMN_CPU, dataModel.getCPU());
        values.put(COLUMN_MEMORY, dataModel.getMemory());
        values.put(COLUMN_ROOT, dataModel.getRootState());

        long insert = sqLiteDatabase.insert(TABLE, null, values);
        return insert != -1;

    }

    public List<DataModel> getRecords() {

        List<DataModel> returnList = new ArrayList<>();
        String query = "SELECT * FROM "+ TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        final Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                String ip = cursor.getString(1);
                String manu = cursor.getString(2);
                String brand = cursor.getString(3);
                String model = cursor.getString(4);
                String board = cursor.getString(5);
                String Hard = cursor.getString(6);
                String serial = cursor.getString(7);
                Integer and_id = cursor.getInt(8);
                String bool_l = cursor.getString(9);
                String user = cursor.getString(10);
                String Host = cursor.getString(11);
                String version = cursor.getString(12);
                String api_lv = cursor.getString(13);
                String build_id = cursor.getString(14);
                String build_time = cursor.getString(15);
                String fingerprint = cursor.getString(16);
                String carrier = cursor.getString(17);
                String apps = cursor.getString(18);
                String proc = cursor.getString(19);
                String blue_dev = cursor.getString(20);
                String blue_stat = cursor.getString(21);
                String net_prop = cursor.getString(22);
                String net_cap = cursor.getString(23);
                String log = cursor.getString(24);
                String wifi = cursor.getString(25);
                String sensors = cursor.getString(26);
                String cpu = cursor.getString(27);
                String memory = cursor.getString(28);
                int root = cursor.getInt(29);

                DataModel dataModel = new DataModel(ip, manu, brand, model, board, Hard, serial, and_id, bool_l, user,Host, version, api_lv, build_id, build_time, fingerprint, carrier, apps,proc, blue_dev, blue_stat,net_prop, net_cap, log, wifi, sensors, cpu, memory, root);
                returnList.add(dataModel);
            }while(cursor.moveToNext());


        }else{

        }

        return returnList;
    }


}
