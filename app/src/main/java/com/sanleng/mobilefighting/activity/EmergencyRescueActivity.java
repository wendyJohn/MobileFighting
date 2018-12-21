package com.sanleng.mobilefighting.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.adapter.StationAdapter;
import com.sanleng.mobilefighting.bean.FireAlarmBean;
import com.sanleng.mobilefighting.bean.StationBean;
import com.sanleng.mobilefighting.myview.FullVideoView;
import com.sanleng.mobilefighting.net.NetCallBack;
import com.sanleng.mobilefighting.net.RequestUtils;
import com.sanleng.mobilefighting.net.URLs;
import com.sanleng.mobilefighting.util.E_StationDialog;
import com.sanleng.mobilefighting.util.FireFormationDialog;
import com.sanleng.mobilefighting.video.activity.MonitorVideoActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 应急救援
 *
 * @author qiaoshi
 */
public class EmergencyRescueActivity extends Activity implements OnClickListener {
    // 定位对象
    private LocationClient mLocationClient = null;
    // 定位监听
    private BDLocationListener myListener = new MyLocationListener();
    // 纬度
    private double mylatitude;
    // 经度
    private double mylongitude;
    private LocationManager locationManager;
    // 地图应用
    private MapView mMapView;
    private MyLocationData locData;
    private BaiduMap mBaiduMap;
    private BitmapDescriptor mCurrentMarker;
    private List<OverlayOptions> list;
    private FireFormationDialog fireFormationDialog;
    private String AlarmTime;
    private String AlarmEquipment;
    private String AlarmUnit;
    private int i = 0;// 次数
    private String str;
    private E_StationDialog e_stationDialog;
    private List<String> mylist = new ArrayList<>();
    private RelativeLayout myr_back;
    BitmapDescriptor bdAs = BitmapDescriptorFactory.fromResource(R.drawable.e_station);
    private FullVideoView video;

    private static final double EARTH_RADIUS = 6378137.0;
    private List<StationBean> slist;
    private ListView stationlistview;
    private StationAdapter stationAdapter;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        this.setContentView(R.layout.emergencyrescue_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(EmergencyRescueActivity.this);
            dialog.setTitle("GPS未打开");
            dialog.setMessage("请打开GPS或WIFI，提高定位精度");
            dialog.setCancelable(false);
            dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    Intent setting_Intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(setting_Intent, 0);
                }
            });
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                }
            });
            dialog.show();
        }
        mLocationClient = new LocationClient(EmergencyRescueActivity.this); // 声明LocationClient类
        mLocationClient.registerLocationListener(myListener); // 注册监听函数
        // 获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 设置地图缩放级别
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(19).build()));// 设置缩放级别
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }

        mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                // 获得marker中的数据
                FireAlarmBean bean = (FireAlarmBean) arg0.getExtraInfo().get("marker");
                AlarmTime = bean.getAlarmTime();
                AlarmEquipment = bean.getAlarmEquipment();
                AlarmUnit = bean.getAlarmUnit();
                // TODO Auto-generated method stub
                // 实例化一个地理编码查询对象
                GeoCoder geoCoder = GeoCoder.newInstance();
                // 设置反地理编码位置坐标
                ReverseGeoCodeOption op = new ReverseGeoCodeOption();
                op.location(arg0.getPosition());
                // 发起反地理编码请求(经纬度->地址信息)
                geoCoder.reverseGeoCode(op);
                geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                    @Override
                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
                        // 获取点击的坐标地址
                        String address = arg0.getAddress();
                        e_stationDialog = new E_StationDialog(EmergencyRescueActivity.this, clickListener,
                                AlarmEquipment, AlarmUnit, address, null);
                        e_stationDialog.show();
                    }

                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult arg0) {
                    }
                });
                return true;

            }

        });

        initview();
        initLocation();
        setVideo();
        NearbyEmergencyStation();
        mLocationClient.start();

    }

    /**
     * 设置视频参数
     */
    private void setVideo() {
        video = findViewById(R.id.video);
        MediaController mediaController = new MediaController(this);
        mediaController.setVisibility(View.GONE);//隐藏进度条
        video.setMediaController(mediaController);
//        File file = new File(Environment.getExternalStorageDirectory() + "/" + "FireVideo", "1542178640266.mp4");
//        video.setVideoPath(file.getAbsolutePath());
//        video.start();
        video.setVideoURI(Uri.parse(URLs.HOST + "/RootFile/Platform/20181114/1542178640266.mp4"));
        video.start();
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
        });
    }

    // 初始化数据
    private void initview() {
        mylist.add("A");
        mylist.add("B");
        mylist.add("C");
        mylist.add("D");
        myr_back = (RelativeLayout) findViewById(R.id.r_back);
        myr_back.setOnClickListener(this);
        list = new ArrayList<OverlayOptions>();
        // 测试
        FireAlarmBean bean = new FireAlarmBean();
        double latitude = 31.87308;
        double longitude = 118.83488;
        bean.setAlarmTime("2018/11/30 00:00");
        bean.setAlarmEquipment("应急站");
        bean.setAlarmUnit("三棱科技");
        bean.setTaskId("123");
        bean.setType("应急站");

        bean.setLatitude(latitude);
        bean.setLongitude(longitude);
        // 构建MarkerOption，用于在地图上添加Marker
        LatLng llA = new LatLng(latitude, longitude);
        MarkerOptions option = new MarkerOptions().position(llA).icon(bdAs);
        Marker marker = (Marker) mBaiduMap.addOverlay(option);
        // 将信息保存
        Bundle bundle = new Bundle();
        bundle.putSerializable("marker", bean);
        marker.setExtraInfo(bundle);
        mBaiduMap.addOverlays(list);

    }

    //地图定位
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
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

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        // 当不需要定位图层时关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // 构造定位数据
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                // GPS定位结果
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                // 网络定位结果
            }
            mylatitude = location.getLatitude();
            mylongitude = location.getLongitude();
            // 构造定位数据
            locData = new MyLocationData.Builder().accuracy(0)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(0).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
            // 开启定位图层
            mBaiduMap.setMyLocationEnabled(true);
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
            mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.mypation);

            MyLocationConfiguration config = new MyLocationConfiguration(
                    MyLocationConfiguration.LocationMode.FOLLOWING, true, mCurrentMarker);
            mBaiduMap.setMyLocationConfigeration(config);
            LatLng mLatLng = new LatLng(mylatitude, mylongitude);
            // 实例化一个地理编码查询对象
            GeoCoder geoCoder = GeoCoder.newInstance();
            // 设置反地理编码位置坐标
            ReverseGeoCodeOption op = new ReverseGeoCodeOption();
            op.location(mLatLng);
            // 发起反地理编码请求(经纬度->地址信息)
            geoCoder.reverseGeoCode(op);
            geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                @Override
                public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
                    // 获取点击的坐标地址
                }

                @Override
                public void onGetGeoCodeResult(GeoCodeResult arg0) {
                    // TODO Auto-generated method stub
                }
            });
        }

        @Override
        public void onConnectHotSpotMessage(String arg0, int arg1) {
            // TODO Auto-generated method stub

        }

    }


    private OnClickListener clickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                // 现场监控
                case R.id.monitor:
                    Intent intent_Inspection = new Intent(EmergencyRescueActivity.this, MonitorVideoActivity.class);
                    startActivity(intent_Inspection);
                    break;
                // 取消
                case R.id.canle:
                    fireFormationDialog.dismiss();
                    break;
                case R.id.dooropening:
                    i = 0;
                    handler.postDelayed(runnable, 2000);// 每两秒执行一次runnable.
                    break;

                case R.id.warehousings:
                    Intent intent_Warehousing = new Intent(EmergencyRescueActivity.this, MaterialManagementCapture.class);
                    intent_Warehousing.putExtra("mode", "Warehousing");
                    startActivity(intent_Warehousing);
                    break;

                case R.id.outofstock:
                    Intent intent_OutOfStock = new Intent(EmergencyRescueActivity.this, MaterialManagementCapture.class);
                    intent_OutOfStock.putExtra("mode", "OutOfStock");
                    startActivity(intent_OutOfStock);
                    break;

                case R.id.reportloss:
                    Intent intent_Reportloss = new Intent(EmergencyRescueActivity.this, MaterialManagementCapture.class);
                    intent_Reportloss.putExtra("mode", "Reportloss");
                    startActivity(intent_Reportloss);
                    break;
                // 取消
                case R.id.canles:
                    e_stationDialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (i == 4) {
                handler.removeCallbacks(runnable);
            } else {
                str = mylist.get(i).toString();
                i++;
                Unlock(str, "54C9DFF77FAB");
                // 要做的事情
                handler.postDelayed(this, 2000);
            }

        }
    };

    // 开锁方式
    private void Unlock(String position, final String mac) {
        RequestUtils.ClientPost(URLs.ORDER_BASE_URL + "/" + position + "/" + mac, null, new NetCallBack() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onMySuccess(String result) {
                if (result == null || result.length() == 0) {
                    return;
                }
                System.out.println("数据请求成功" + result);
            }

            @Override
            public void onMyFailure(Throwable arg0) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.r_back:
                finish();
                break;

            default:
                break;
        }
    }

    //附近的应急站
    private void NearbyEmergencyStation() {
        slist = new ArrayList<>();
        StationBean beana = new StationBean();
        beana.setName("应急站A");
        beana.setAddress("南京市-江宁区-秣周东路12号");
        beana.setDistance("10m");

        StationBean beanb = new StationBean();
        beanb.setName("应急站B");
        beanb.setAddress("南京市-江宁区-秣周东路12号");
        beanb.setDistance("20m");

        StationBean beanc = new StationBean();
        beanc.setName("应急站C");
        beanc.setAddress("南京市-江宁区-秣周东路12号");
        beanc.setDistance("30m");

        StationBean beand = new StationBean();
        beand.setName("应急站D");
        beand.setAddress("南京市-江宁区-秣周东路12号");
        beand.setDistance("40m");
        slist.add(beana);
        slist.add(beanb);
        slist.add(beanc);
        slist.add(beand);
        stationlistview=findViewById(R.id.stationlistview);
        stationAdapter=new StationAdapter(EmergencyRescueActivity.this,slist);
        stationlistview.setAdapter(stationAdapter);
    }

    // 返回单位是米
    public static double getDistance(double longitude1, double latitude1,
                                     double longitude2, double latitude2) {
        double Lat1 = rad(latitude1);
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(Lat1) * Math.cos(Lat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

}
