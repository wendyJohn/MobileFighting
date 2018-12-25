package com.sanleng.mobilefighting.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.baidu.platform.comapi.walknavi.widget.ArCameraView;
import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.adapter.StationAdapter;
import com.sanleng.mobilefighting.bean.StationBean;
import com.sanleng.mobilefighting.myview.FullVideoView;
import com.sanleng.mobilefighting.myview.LinearLayoutForListView;
import com.sanleng.mobilefighting.net.NetCallBack;
import com.sanleng.mobilefighting.net.RequestUtils;
import com.sanleng.mobilefighting.net.URLs;
import com.sanleng.mobilefighting.util.E_StationDialog;
import com.sanleng.mobilefighting.util.FireFormationDialog;
import com.sanleng.mobilefighting.video.activity.MonitorVideoActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * 应急救援
 *
 * @author qiaoshi
 */
public class EmergencyRescueActivity extends Activity implements OnClickListener {
    private LocationClient mLocationClient = null; // 定位对象
    private BDLocationListener myListener = new MyLocationListener(); // 定位监听
    private double S_mylatitude;// 纬度
    private double S_mylongitude;// 经度

    private double E_mylatitude;// 纬度
    private double E_mylongitude;// 经度
    private LocationManager locationManager;
    private MapView mMapView;  // 地图应用
    private MyLocationData locData;
    private BaiduMap mBaiduMap;
    private List<OverlayOptions> list;
    private LatLng latLng;
    private boolean isFirstLoc = true; // 是否首次定位
    BitmapDescriptor bdAs = BitmapDescriptorFactory.fromResource(R.drawable.e_station);//应急站标识

    private FireFormationDialog fireFormationDialog;
    private String AlarmTime;
    private String AlarmEquipment;
    private String AlarmUnit;
    private int i = 0;// 开锁次数
    private String str;
    private E_StationDialog e_stationDialog;
    private List<String> mylist = new ArrayList<>();//应急门的标识
    private RelativeLayout myr_back;
    private FullVideoView video;
    private static final double EARTH_RADIUS = 6378137.0;
    private List<StationBean> slist;
    private LinearLayoutForListView stationlistview;
    private StationAdapter stationAdapter;
    WalkNaviLaunchParam walkParam;
    /*导航起终点Marker，可拖动改变起终点的坐标*/
    private Marker mStartMarker;
    private Marker mEndMarker;
    BitmapDescriptor bdStart = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_start);
    BitmapDescriptor bdEnd = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_end);
    private static boolean isPermissionRequested = false;
    private LatLng startPt, endPt;

    public EmergencyRescueActivity() {
    }

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        this.setContentView(R.layout.emergencyrescue_activity);
        requestPermission();
        initview();
        initMap();
    }


    private void initMap() {
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
        //获取地图控件引用
        mBaiduMap = mMapView.getMap();
        // 普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
        mBaiduMap.setMyLocationEnabled(true);
        // 开启交通图
        mBaiduMap.setTrafficEnabled(true);
        // 开启热力图
        mBaiduMap.setBaiduHeatMapEnabled(false);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient = new LocationClient(getApplicationContext());
        // 声明LocationClient类 //配置定位SDK参数
        initLocation();
        mLocationClient.registerLocationListener(myListener);// 注册监听函数
        // 开启定位
        mLocationClient.start();
        // 图片点击事件，回到定位点
        mLocationClient.requestLocation();

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker arg0) {
                // 获得marker中的数据
//                e_stationDialog = new E_StationDialog(EmergencyRescueActivity.this, clickListener,
//                        AlarmEquipment, AlarmUnit, null, null);
//                e_stationDialog.show();
                return true;

            }

        });

    }

    //配置定位SDK参数
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps
        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
        // .getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);
        option.setOpenGps(true);// 打开gps
        // 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，
        // 设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    //实现BDLocationListener接口,BDLocationListener为结果监听接口，异步获取定位结果
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            S_mylatitude = location.getLatitude();
            S_mylongitude = location.getLongitude();
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            // 当不需要定位图层时关闭定位图层
            mBaiduMap.setMyLocationEnabled(false);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                if (location.getLocType() == BDLocation.TypeGpsLocation) {
                    // GPS定位结果
                    Toast.makeText(EmergencyRescueActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    // 网络定位结果
                    Toast.makeText(EmergencyRescueActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                    // 离线定位结果
                    Toast.makeText(EmergencyRescueActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    Toast.makeText(EmergencyRescueActivity.this, "服务器错误，请检查", Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    Toast.makeText(EmergencyRescueActivity.this, "网络错误，请检查", Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    Toast.makeText(EmergencyRescueActivity.this, "手机模式错误，请检查是否飞行", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setVideo();
        NearbyEmergencyStation();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        WalkNavigateHelper.getInstance().resume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        WalkNavigateHelper.getInstance().pause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        // 当不需要定位图层时关闭定位图层
        WalkNavigateHelper.getInstance().quit();
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        super.onDestroy();
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
        mMapView = (MapView) findViewById(R.id.bmapView);
        mylist.add("A");
        mylist.add("B");
        mylist.add("C");
        mylist.add("D");
        myr_back = (RelativeLayout) findViewById(R.id.r_back);
        myr_back.setOnClickListener(this);
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
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
        beana.setE_mylatitude(31.87308);
        beana.setE_mylongitude(118.83488);

        StationBean beanb = new StationBean();
        beanb.setName("应急站B");
        beanb.setAddress("南京市-江宁区-秣周东路12号");
        beanb.setDistance("20m");
        beanb.setE_mylatitude(31.87308);
        beanb.setE_mylongitude(118.83488);

        StationBean beanc = new StationBean();
        beanc.setName("应急站C");
        beanc.setAddress("南京市-江宁区-秣周东路12号");
        beanc.setDistance("30m");
        beanc.setE_mylatitude(31.87308);
        beanc.setE_mylongitude(118.83488);

        StationBean beand = new StationBean();
        beand.setName("应急站D");
        beand.setAddress("南京市-江宁区-秣周东路12号");
        beand.setDistance("40m");
        beand.setE_mylatitude(31.87308);
        beand.setE_mylongitude(118.83488);

        slist.add(beana);
        slist.add(beanb);
        slist.add(beanc);
        slist.add(beand);

        // 构建MarkerOption，用于在地图上添加Marker
        LatLng llA = new LatLng(31.87308, 118.83488);
        MarkerOptions option = new MarkerOptions().position(llA).icon(bdAs);
        Marker marker = (Marker) mBaiduMap.addOverlay(option);
        // 将信息保存
        Bundle bundle = new Bundle();
        bundle.putSerializable("marker", beana);
        marker.setExtraInfo(bundle);
        mBaiduMap.addOverlays(list);

        stationlistview = findViewById(R.id.stationlistview);
        stationAdapter = new StationAdapter(EmergencyRescueActivity.this, slist);
        stationlistview.setAdapter(stationAdapter);

        stationlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                E_mylatitude = slist.get(position).getE_mylatitude();
                E_mylongitude = slist.get(position).getE_mylongitude();

                //初始化导航数据
                initOverlay();
                startPt = new LatLng(S_mylatitude, S_mylongitude);
                endPt = new LatLng(E_mylatitude, E_mylongitude);
                /*构造导航起终点参数对象*/
                walkParam = new WalkNaviLaunchParam().stPt(startPt).endPt(endPt);
                walkParam.extraNaviMode(0);
                startWalkNavi();
                mBaiduMap.clear();
            }
        });
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

    /**
     * 初始化导航起终点Marker
     */
    public void initOverlay() {
        LatLng llA = new LatLng(S_mylatitude, S_mylongitude);
        LatLng llB = new LatLng(E_mylatitude, E_mylongitude);
        MarkerOptions ooA = new MarkerOptions().position(llA).icon(bdStart)
                .zIndex(9).draggable(true);
        mStartMarker = (Marker) (mBaiduMap.addOverlay(ooA));
        mStartMarker.setDraggable(true);
        MarkerOptions ooB = new MarkerOptions().position(llB).icon(bdEnd)
                .zIndex(5);
        mEndMarker = (Marker) (mBaiduMap.addOverlay(ooB));
        mEndMarker.setDraggable(true);

        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {
            }

            public void onMarkerDragEnd(Marker marker) {
                if (marker == mStartMarker) {
                    startPt = marker.getPosition();
                } else if (marker == mEndMarker) {
                    endPt = marker.getPosition();
                }
                walkParam.stPt(startPt).endPt(endPt);
            }

            public void onMarkerDragStart(Marker marker) {
            }
        });
    }

    /**
     * 开始步行导航
     */
    private void startWalkNavi() {
        try {
            WalkNavigateHelper.getInstance().initNaviEngine(this, new IWEngineInitListener() {
                @Override
                public void engineInitSuccess() {
                    routePlanWithWalkParam();

                }

                @Override
                public void engineInitFail() {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发起步行导航算路
     */
    private void routePlanWithWalkParam() {
        WalkNavigateHelper.getInstance().routePlanWithParams(walkParam, new IWRoutePlanListener() {
            @Override
            public void onRoutePlanStart() {
            }

            @Override
            public void onRoutePlanSuccess() {
                Intent intent = new Intent();
                intent.setClass(EmergencyRescueActivity.this, WNaviGuideActivity.class);
                startActivity(intent);
            }

            @Override
            public void onRoutePlanFail(WalkRoutePlanError error) {
            }

        });

    }

    /**
     * Android6.0之后需要动态申请权限
     */
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23 && !isPermissionRequested) {
            isPermissionRequested = true;
            ArrayList<String> permissions = new ArrayList<>();
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (permissions.size() == 0) {
                return;
            } else {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ArCameraView.WALK_AR_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(EmergencyRescueActivity.this, "没有相机权限,请打开后重试", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
