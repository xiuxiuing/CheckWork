package com.getui.checkwork;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.getui.checkwork.bean.CheckBean;
import com.getui.checkwork.bean.RecordBean;
import com.getui.checkwork.bean.SchemeBean;
import com.getui.checkwork.data.SharePreUtils;
import com.getui.checkwork.db.DBHelper;
import com.getui.checkwork.utils.LogUtils;
import com.getui.checkwork.utils.PermissionsCheckUtils;
import com.getui.checkwork.utils.SystemUtils;
import com.getui.checkwork.utils.ToastUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends AppCompatActivity {


    WifiManager wm;
    WifiInfo wifiInfo;

    TextView tvDate;
    TextView tvTime;
    Button btnCheck;
    FloatingActionButton floating;

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    LoadingDialog loadingDialog;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            tvTime.setText(getTimeShort());
            // 要做的事情
            handler.postDelayed(this, 1000);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bmob.initialize(this, Consts.APPID);

        Intent intent = getIntent();
        try{
            System.out.println(intent.getStringExtra("goto"));
            System.out.println(intent.getBundleExtra("goto"));
            System.out.println(intent.getDataString());
        }catch (Exception e){
            e.printStackTrace();
        }

        String action = intent.getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
            Uri uri = intent.getData();
            if (uri != null) {
                System.out.println(uri.toString());
                String imei = SystemUtils.getImei(MainActivity.this);
                SchemeBean schemeBean = new SchemeBean();
                schemeBean.setImei(imei);
                schemeBean.setUri(uri.toString());
                schemeBean.setBrand(Build.BRAND);
                schemeBean.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            LogUtils.d("数据添加成功");
                            Toast.makeText(MainActivity.this, "数据上传成功", Toast.LENGTH_SHORT).show();
                        } else {
                            LogUtils.d("数据添加失败:" + e.getMessage());
                        }
                    }
                });
            }
        }



        if (PermissionsCheckUtils.lacksPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            if (Build.VERSION.SDK_INT >= 23) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            }
        }

        mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
        mLocationClient.registerLocationListener(myListener); // 注册监听函数
        initLocation();

        tvDate = (TextView) findViewById(R.id.tv_date);
        tvTime = (TextView) findViewById(R.id.tv_time);
        btnCheck = (Button) findViewById(R.id.btn_check);
        floating = (FloatingActionButton) findViewById(R.id.floating);

        wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wm.getConnectionInfo();

        loadingDialog = new LoadingDialog(this, R.style.LoadingDialog);

        floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                startActivity(intent);
            }
        });

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();

                mLocationClient.start();

            }
        });

        tvDate.setText(getStringDateShort() + " " + getWeek());
        tvTime.setText(getTimeShort());
        handler.postDelayed(runnable, 1000);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (PermissionsCheckUtils.hasAllPermissionsGranted(grantResults)) {
                System.out.println("定位权限有了");
            } else {

            }
        }
    }

    public static String getStringDateShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static String getTimeShort() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date currentTime = new Date();
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static String getWeek() {
        // 再转换为时间
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return new SimpleDateFormat("E").format(c.getTime());
    }

    public static String getWeek(String time) {
        // 再转换为时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return new SimpleDateFormat("E").format(c.getTime());
    }



    private double rad(double d) {
        return d * Math.PI / 180.0;
    }

    private double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * Consts.EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
        int span = 0;
        option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            LogUtils.d("定位成功!!!");
            double distance = getDistance(Consts.LAT, Consts.LON, location.getLatitude(), location.getLongitude());
            if (distance < Consts.DISTANCE) {
                ContentValues cv = new ContentValues();
                cv.put("time", tvDate.getText() + " " + tvTime.getText());
                DBHelper.getInstance(MainActivity.this).insertTime("work", cv);

                String date = getStringDateShort();
                String time = getTimeShort();
                String imei = SystemUtils.getImei(MainActivity.this);

                if (TextUtils.isEmpty(imei)) {
                    ToastUtils.showShort(MainActivity.this, "Imei获取失败!!!");

                }

                RecordBean recordBean = new RecordBean();
                recordBean.setDate(date);
                recordBean.setWeek(getWeek());
                recordBean.setTime(time);
                recordBean.setImei(imei);
                recordBean.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            LogUtils.d("数据添加成功");
                            Toast.makeText(MainActivity.this, "签到成功", Toast.LENGTH_SHORT).show();
                        } else {
                            LogUtils.d("数据添加失败:" + e.getMessage());
                        }
                    }
                });


                String early = SharePreUtils.getEarlyTime(MainActivity.this);
                if (TextUtils.isEmpty(early)) {
                    SharePreUtils.saveEarliestTime(MainActivity.this, date + " " + time);

                } else {
                    if (early.contains(date)) {
                        SharePreUtils.saveLastestTime(MainActivity.this, date + " " + time);
                    } else {
                        SharePreUtils.saveEarliestTime(MainActivity.this, date + " " + time);
                        CheckBean checkBean = new CheckBean();
                        checkBean.setDate(early.split(" ")[0]);
                        checkBean.setWeek(getWeek(early));
                        checkBean.setEarly(early);
                        checkBean.setImei(imei);
                        checkBean.setLast(SharePreUtils.getLastTime(MainActivity.this));
                        checkBean.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {

                            }
                        });
                    }
                }

            }
            mLocationClient.stop();
            loadingDialog.dismiss();
        }
    }



}
