package com.example.myscannerjob;



public class DataModel {

    private int ID;
    protected String myIP;
    private String manufacturer;
    private String brand;
    private String model;
    private String Board_value;
    private String Hardware_value;
    private String Serial_nO_value;
    private int android_id;
    private String BootLoader_value;
    private String User_value;
    private String Host_value;
    private String Version;
    private String API_level;
    private String Build_ID;
    private String Build_Time;
    private String Fingerprint;
    private String carrierName;
    private String apps;
    private String proc;
    private String blueConnDev;
    private String blueStatus;
    private String netprop;
    private String netcap;
    private String logPfinal;
    private String wifi;
    private String sensors;
    private String cpu;
    private String memory;
    private int root;


    public DataModel(String myIP1, String manufacturer1, String brand1, String model, String board_value, String hardware_value, String serial_nO_value, int android_id, String bootLoader_value, String user_value, String host_value, String version, String API_level, String build_ID, String build_Time, String fingerprint, String carrierName, String apps, String proc, String blueConnDev, String blueStatus, String netprop, String netcap, String logPfinal, String wifi1, String sensors1, String cpu1, String memory1, int root1) {
        myIP = myIP1;
        manufacturer = manufacturer1;
        brand = brand1;
        this.model = model;
        this.Board_value = board_value;
        this.Hardware_value = hardware_value;
        this.Serial_nO_value = serial_nO_value;
        this.android_id = android_id;
        this.BootLoader_value = bootLoader_value;
        this.User_value = user_value;
        this.Host_value = host_value;
        this.Version = version;
        this.API_level = API_level;
        this.Build_ID = build_ID;
        this.Build_Time = build_Time;
        this.Fingerprint = fingerprint;
        this.carrierName = carrierName;
        this.apps = apps;
        this.proc = proc;
        this.blueConnDev = blueConnDev;
        this.blueStatus = blueStatus;
        this.netprop = netprop;
        this.netcap = netcap;
        this.logPfinal = logPfinal;
        this.wifi = wifi1;
        this.sensors = sensors1;
        this.cpu = cpu1;
        this.memory = memory1;
        this.root = root1;
    }

    @Override
    public String toString() {
        return "DataModel{" +
                "ID=" + ID +
                ", myIP='" + myIP + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", Board_value='" + Board_value + '\'' +
                ", Hardware_value='" + Hardware_value + '\'' +
                ", Serial_nO_value='" + Serial_nO_value + '\'' +
                ", android_id=" + android_id +
                ", BootLoader_value='" + BootLoader_value + '\'' +
                ", User_value='" + User_value + '\'' +
                ", Host_value='" + Host_value + '\'' +
                ", Version='" + Version + '\'' +
                ", API_level='" + API_level + '\'' +
                ", Build_ID='" + Build_ID + '\'' +
                ", Build_Time='" + Build_Time + '\'' +
                ", Fingerprint='" + Fingerprint + '\'' +
                ", carrierName='" + carrierName + '\'' +
                ", apps='" + apps + '\'' +
                ", proc='" + proc + '\'' +
                ", blueConnDev='" + blueConnDev + '\'' +
                ", blueStatus='" + blueStatus + '\'' +
                ", netprop='" + netprop + '\'' +
                ", netcap='" + netcap + '\'' +
                ", logPfinal='" + logPfinal + '\'' +
                ", wifi='" + wifi + '\'' +
                ", sensors='" + sensors + '\'' +
                ", cpu='" + cpu + '\'' +
                ", memory='" + memory + '\'' +
                ", root='" + root + '\'' +
                '}';
    }

  //  public DataModel(String ip, String manu, String brand, String model, String board, String hard, String serial, Integer and_id, String bool_l, String user, String version, String api_lv, String build_id, String build_time, String fingerprint, String carrier, String apps, String blue_dev, String blue_stat, String net_prop, String net_cap, String log) {



   // }

    public String getMyIP() {
        return myIP;
    }

    public void setMyIP(String myIP) {
        this.myIP = myIP;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBoard_value() {
        return Board_value;
    }

    public void setBoard_value(String board_value) {
        Board_value = board_value;
    }

    public String getHardware_value() {
        return Hardware_value;
    }

    public void setHardware_value(String hardware_value) {
        Hardware_value = hardware_value;
    }

    public String getSerial_nO_value() {
        return Serial_nO_value;
    }

    public void setSerial_nO_value(String serial_nO_value) {
        Serial_nO_value = serial_nO_value;
    }

    public int getAndroid_id() {
        return android_id;
    }

    public void setAndroid_id(int android_id) {
        this.android_id = android_id;
    }

    public String getBootLoader_value() {
        return BootLoader_value;
    }

    public void setBootLoader_value(String bootLoader_value) {
        BootLoader_value = bootLoader_value;
    }

    public String getUser_value() {
        return User_value;
    }

    public void setUser_value(String user_value) {
        User_value = user_value;
    }

    public String getHost_value() {
        return Host_value;
    }

    public void setHost_value(String host_value) {
        Host_value = host_value;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getAPI_level() {
        return API_level;
    }

    public void setAPI_level(String API_level) {
        this.API_level = API_level;
    }

    public String getBuild_ID() {
        return Build_ID;
    }

    public void setBuild_ID(String build_ID) {
        Build_ID = build_ID;
    }

    public String getBuild_Time() {
        return Build_Time;
    }

    public void setBuild_Time(String build_Time) {
        Build_Time = build_Time;
    }

    public String getFingerprint() {
        return Fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        Fingerprint = fingerprint;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getApps() {
        return apps;
    }

    public void setApps(String apps) {
        this.apps = apps;
    }

    public String getProc() {
        return proc;
    }

    public void setProc(String proc) {
        this.proc = proc;
    }

    public String getBlueConnDev() {
        return blueConnDev;
    }

    public void setBlueConnDev(String blueConnDev) {
        this.blueConnDev = blueConnDev;
    }

    public String getBlueStatus() {
        return blueStatus;
    }

    public void setBlueStatus(String blueStatus) {
        this.blueStatus = blueStatus;
    }

    public String getNetprop() {
        return netprop;
    }

    public void setNetprop(String netprop) {
        this.netprop = netprop;
    }

    public String getNetcap() {
        return netcap;
    }

    public void setNetcap(String netcap) {
        this.netcap = netcap;
    }

    public String getLogPfinal() {
        return logPfinal;
    }

    public void setLogPfinal(String logPfinal) {
        this.logPfinal = logPfinal;
    }

    public String getwifi() {  return wifi; }

    public String getSensors() {  return sensors; }

    public String getCPU() { return cpu; }

    public String getMemory(){return memory;}

    public int getRootState(){return root;}
}
