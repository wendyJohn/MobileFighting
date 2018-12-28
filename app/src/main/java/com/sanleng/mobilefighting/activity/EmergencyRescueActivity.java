package com.sanleng.mobilefighting.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNRoutePlanManager;
import com.baidu.navisdk.adapter.IBNTTSManager;
import com.baidu.navisdk.adapter.IBaiduNaviManager;
import com.baidu.platform.comapi.walknavi.widget.ArCameraView;
import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.adapter.BottomMenuAdapter;
import com.sanleng.mobilefighting.adapter.StationAdapter;
import com.sanleng.mobilefighting.baidumap.DemoGuideActivity;
import com.sanleng.mobilefighting.baidumap.NormalUtils;
import com.sanleng.mobilefighting.bean.StationBean;
import com.sanleng.mobilefighting.net.NetCallBack;
import com.sanleng.mobilefighting.net.RequestUtils;
import com.sanleng.mobilefighting.net.URLs;
import com.sanleng.mobilefighting.util.E_StationDialog;
import com.sanleng.mobilefighting.util.FireFormationDialog;
import com.sanleng.mobilefighting.util.ScreenUtil;
import com.sanleng.mobilefighting.video.activity.MonitorVideoActivity;
import com.yinglan.scrolllayout.ScrollLayout;

import java.io.File;
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
    BitmapDescriptor bdA = BitmapDescriptorFactory.fromResource(R.drawable.ico_sos);//求救标识
    private FireFormationDialog fireFormationDialog;
    private String AlarmTime;
    private String AlarmEquipment;
    private String AlarmUnit;
    private int i = 0;// 开锁次数
    private String str;
    private E_StationDialog e_stationDialog;
    private List<String> mylist = new ArrayList<>();//应急门的标识
    private RelativeLayout myr_back;
    private static final double EARTH_RADIUS = 6378137.0;
    private List<StationBean> slist;
    private List<StationBean> slistsos;
    private List<StationBean> slists;//底部菜单选项
    private ListView stationlistview;
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

    //底部详情菜单
    private ScrollLayout mScrollLayout;
    private BottomMenuAdapter bottomMenuAdapter;
    private RelativeLayout foot;
    private TextView walkingnavigation;
    private RelativeLayout viewdetails;
    private TextView name;
    private TextView address;
    private TextView distance;
    //驾车导航
    private static final String[] authBaseArr = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int authBaseRequestCode = 1;
    private boolean hasInitSuccess = false;
    private static final String APP_FOLDER_NAME = "BNSDKSimpleDemo";
    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    private String mSDCardPath = null;
    private BNRoutePlanNode mStartNode = null;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        this.setContentView(R.layout.emergencyrescue_activity);
        RequestPermission();
        initview();
        initMap();
        if (initDirs()) {
            initNavi();
        }
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
                StationBean bean = (StationBean) arg0.getExtraInfo().get("marker");
                E_mylatitude = bean.getE_mylatitude();
                E_mylongitude = bean.getE_mylongitude();
                String ids = bean.getId();
                String names = bean.getName();
                String addresss = bean.getAddress();
                double distances = bean.getDistance();
                name.setText(names);
                address.setText(addresss);
                distance.setText("距您 " + distances + " m");
                BottomMenu(names, addresss, distances, ids);
                mScrollLayout.setVisibility(View.VISIBLE);

                LatLng llA = new LatLng(E_mylatitude, E_mylongitude);
                showInfoWindow(llA, names);

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
            mBaiduMap.setMyLocationEnabled(true);
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
        new Handler().postDelayed(new Runnable() {
            public void run() {
                // 等待1000毫秒后获取数据
                NearbyEmergencyStation();
                NearbyEmergencySOS();
            }
        }, 1000);

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

    // 初始化数据
    private void initview() {
        mScrollLayout = (ScrollLayout) findViewById(R.id.scroll_down_layout);
        name = (TextView) findViewById(R.id.name);
        address = (TextView) findViewById(R.id.address);
        distance = (TextView) findViewById(R.id.distance);
        /**设置 setting*/
        mScrollLayout.setMinOffset(0);
        mScrollLayout.setMaxOffset((int) (ScreenUtil.getScreenHeight(EmergencyRescueActivity.this) * 1));
        mScrollLayout.setExitOffset(ScreenUtil.dip2px(EmergencyRescueActivity.this, 110));
        mScrollLayout.setIsSupportExit(true);
        mScrollLayout.setAllowHorizontalScroll(true);
        mScrollLayout.setOnScrollChangedListener(mOnScrollChangedListener);
        mScrollLayout.setToExit();
        mScrollLayout.getBackground().setAlpha(0);
        mMapView = (MapView) findViewById(R.id.bmapView);
        foot = findViewById(R.id.foot);
        walkingnavigation = findViewById(R.id.walkingnavigation);
        viewdetails = findViewById(R.id.viewdetails);
        mylist.add("A");
        mylist.add("B");
        mylist.add("C");
        mylist.add("D");
        myr_back = (RelativeLayout) findViewById(R.id.r_back);
        myr_back.setOnClickListener(this);
        walkingnavigation.setOnClickListener(this);
        viewdetails.setOnClickListener(this);
        foot.setOnClickListener(this);
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
            // 步行导航
            case R.id.walkingnavigation:
                //初始化导航数据
                initOverlay();
                startPt = new LatLng(S_mylatitude, S_mylongitude);
                endPt = new LatLng(E_mylatitude, E_mylongitude);
                /*构造导航起终点参数对象*/
                walkParam = new WalkNaviLaunchParam().stPt(startPt).endPt(endPt);
                walkParam.extraNaviMode(0);
                startWalkNavi();
                mBaiduMap.clear();
                break;
//                查看详情
            case R.id.viewdetails:
                routeplanToNavi(CoordinateType.BD09LL);
//                mScrollLayout.setToOpen();
                break;
            default:
                break;
        }
    }

    //附近的应急站
    private void NearbyEmergencyStation() {
        slist = new ArrayList<>();
        StationBean beana = new StationBean();
        beana.setId("1234567890");
        beana.setName("应急站A");
        beana.setAddress("南京市-江宁区-秣周东路12号");
        beana.setE_mylatitude(31.87308);
        beana.setE_mylongitude(118.83488);
        beana.setDistance(gps_m(S_mylatitude, S_mylongitude, 31.87308, 118.83488));
        slist.add(beana);
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

                String ids = slist.get(position).getId();
                String names = slist.get(position).getName();
                String addresss = slist.get(position).getAddress();
                double distances = slist.get(position).getDistance();

                name.setText(names);
                address.setText(addresss);
                distance.setText("距您 " + distances + "m");

                ChooseMyLocation(E_mylatitude, E_mylongitude);
                BottomMenu(names, addresss, distances, ids);
                mScrollLayout.setVisibility(View.VISIBLE);

                LatLng llA = new LatLng(E_mylatitude, E_mylongitude);
                showInfoWindow(llA, names);
            }
        });
    }

    //附近的SOS
    private void NearbyEmergencySOS() {
        slistsos = new ArrayList<>();
        StationBean bean = new StationBean();
        bean.setId("1234567890");
        bean.setName("SOS求救");
        bean.setAddress("南京市-江宁区-秣周东路12号");
        bean.setE_mylatitude(31.87368);
        bean.setE_mylongitude(118.83358);
        bean.setDistance(gps_m(S_mylatitude, S_mylongitude, 31.87368, 118.83358));
        slistsos.add(bean);
        // 构建MarkerOption，用于在地图上添加Marker
        LatLng llA = new LatLng(31.87368, 118.83358);
        MarkerOptions option = new MarkerOptions().position(llA).icon(bdA);
        Marker marker = (Marker) mBaiduMap.addOverlay(option);
        // 将信息保存
        Bundle bundle = new Bundle();
        bundle.putSerializable("marker", bean);
        marker.setExtraInfo(bundle);
        mBaiduMap.addOverlays(list);
    }

    // 返回单位是米
    private double gps_m(double lat_a, double lng_a, double lat_b, double lng_b) {
        double radLat1 = (lat_a * Math.PI / 180.0);
        double radLat2 = (lat_b * Math.PI / 180.0);
        double a = radLat1 - radLat2;
        double b = (lng_a - lng_b) * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(
                Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
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

    //底部菜单
    private void BottomMenu(String name, String address, double distance, String id) {
        slists = new ArrayList<>();
        StationBean beana = new StationBean();
        beana.setType(0);
        slists.add(beana);
        StationBean beanb = new StationBean();
        beanb.setType(1);
        slists.add(beanb);
        StationBean beanc = new StationBean();
        beanc.setName("防火服");
        beanc.setNumber("2件");
        beanc.setImage_type("1");
        beanc.setType(2);
        slists.add(beanc);
        StationBean beand = new StationBean();
        beand.setName("灭火器");
        beand.setNumber("2件");
        beand.setImage_type("2");
        beand.setType(2);
        slists.add(beand);
        ListView listView = (ListView) findViewById(R.id.list_view);
        bottomMenuAdapter = new BottomMenuAdapter(EmergencyRescueActivity.this, slists, name, address, distance, id, m_Handler);
        listView.setAdapter(bottomMenuAdapter);
    }

    private ScrollLayout.OnScrollChangedListener mOnScrollChangedListener = new ScrollLayout.OnScrollChangedListener() {
        @Override
        public void onScrollProgressChanged(float currentProgress) {
            if (currentProgress >= 0) {
                float precent = 255 * currentProgress;
                if (precent > 255) {
                    precent = 255;
                } else if (precent < 0) {
                    precent = 0;
                }
                mScrollLayout.getBackground().setAlpha(255 - (int) precent);
//                if (foot.getVisibility() == View.VISIBLE)
//                foot.setVisibility(View.GONE);
            }
        }

        @Override
        public void onScrollFinished(ScrollLayout.Status currentStatus) {
            if (currentStatus.equals(ScrollLayout.Status.EXIT)) {
//                foot.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onChildScroll(int top) {
        }
    };

    //快速找到所需要的站点位置
    private void ChooseMyLocation(double la, double lo) {
        // 开启定位功能
        mBaiduMap.setMyLocationEnabled(true);
        // 构造定位数据
        MyLocationData locationData = new MyLocationData.Builder()
                .latitude(la)
                .longitude(lo)
                .build();
        // 设置定位数据
        mBaiduMap.setMyLocationData(locationData);
        // 设置定位图层的配置，设置图标跟随状态（图标一直在地图中心）
        MyLocationConfiguration config = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.FOLLOWING, true, null);
        mBaiduMap.setMyLocationConfigeration(config);
        // 当不需要定位时，关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);

        StationBean bean = new StationBean();
        bean.setE_mylatitude(E_mylatitude);
        bean.setE_mylongitude(E_mylongitude);
        // 将信息保存
        Bundle bundle = new Bundle();
        bundle.putSerializable("marker", bean);
    }

    @SuppressLint("HandlerLeak")
    private Handler m_Handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                //一键开门
                case 5859590:
                    i = 0;
                    handler.postDelayed(runnable, 2000);// 每两秒执行一次runnable.
                    break;
                //还物资
                case 5859591:
                    Intent intent_Warehousing = new Intent(EmergencyRescueActivity.this, MaterialManagementCapture.class);
                    intent_Warehousing.putExtra("mode", "Warehousing");
                    startActivity(intent_Warehousing);
                    break;
                //取物质
                case 5859592:
                    Intent intent_OutOfStock = new Intent(EmergencyRescueActivity.this, MaterialManagementCapture.class);
                    intent_OutOfStock.putExtra("mode", "OutOfStock");
                    startActivity(intent_OutOfStock);
                    break;
                //报损
                case 5859593:
                    Intent intent_Reportloss = new Intent(EmergencyRescueActivity.this, MaterialManagementCapture.class);
                    intent_Reportloss.putExtra("mode", "Reportloss");
                    startActivity(intent_Reportloss);
                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 显示弹出窗
     */
    private void showInfoWindow(LatLng ll, String name) {
        //创建InfoWindow展示的view
        View contentView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.infowindow_item, null);
        TextView tvCount = (TextView) contentView.findViewById(R.id.tv_count);
        tvCount.setText(name);
        //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
        InfoWindow infoWindow = new InfoWindow(contentView, ll, -80);
        //显示InfoWindow
        mBaiduMap.showInfoWindow(infoWindow);

    }

    private boolean hasBasePhoneAuth() {
        PackageManager pm = this.getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    //============驾车导航===================
    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private void initNavi() {
        // 申请权限
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (!hasBasePhoneAuth()) {
                this.requestPermissions(authBaseArr, authBaseRequestCode);
                return;
            }
        }
        BaiduNaviManagerFactory.getBaiduNaviManager().init(this,
                mSDCardPath, APP_FOLDER_NAME, new IBaiduNaviManager.INaviInitListener() {
                    @Override
                    public void onAuthResult(int status, String msg) {
                        String result;
                        if (0 == status) {
                            result = "key校验成功!";
                        } else {
                            result = "key校验失败, " + msg;
                        }
                    }

                    @Override
                    public void initStart() {
                    }

                    @Override
                    public void initSuccess() {
                        hasInitSuccess = true;
                        // 初始化tts
                        initTTS();
                    }

                    @Override
                    public void initFailed() {
                    }
                });
    }

    private void initTTS() {
        // 使用内置TTS
        BaiduNaviManagerFactory.getTTSManager().initTTS(getApplicationContext(),
                getSdcardDir(), APP_FOLDER_NAME, NormalUtils.getTTSAppID());
        // 注册同步内置tts状态回调
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedListener(
                new IBNTTSManager.IOnTTSPlayStateChangedListener() {
                    @Override
                    public void onPlayStart() {
                        System.out.println("==============111");
                    }

                    @Override
                    public void onPlayEnd(String speechId) {
                        System.out.println("==============2222");
                    }

                    @Override
                    public void onPlayError(int code, String message) {
                        System.out.println("==============3333" + message);
                    }
                }
        );

        // 注册内置tts 异步状态消息
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedHandler(
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                    }
                }
        );
    }


    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    private void routeplanToNavi(final int coType) {
        if (!hasInitSuccess) {
            Toast.makeText(EmergencyRescueActivity.this, "还未初始化!", Toast.LENGTH_SHORT).show();
        }

        BNRoutePlanNode sNode = new BNRoutePlanNode(S_mylongitude, S_mylatitude, "悠谷小镇", "悠谷小镇", coType);
        BNRoutePlanNode eNode = new BNRoutePlanNode(E_mylongitude, E_mylatitude, "新街口", "新街口", coType);
        switch (coType) {
            case CoordinateType.BD09LL: {
                sNode = new BNRoutePlanNode(S_mylongitude, S_mylatitude, "悠谷小镇", "悠谷小镇", coType);
                eNode = new BNRoutePlanNode(E_mylongitude, E_mylatitude, "新街口", "新街口", coType);
                break;
            }
            default:
                break;
        }

        mStartNode = sNode;
        List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
        list.add(sNode);
        list.add(eNode);

        BaiduNaviManagerFactory.getRoutePlanManager().routeplanToNavi(
                list,
                IBNRoutePlanManager.RoutePlanPreference.ROUTE_PLAN_PREFERENCE_DEFAULT,
                null,
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_START:
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_SUCCESS:
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_FAILED:
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_TO_NAVI:
                                Intent intent = new Intent(EmergencyRescueActivity.this,
                                        DemoGuideActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(ROUTE_PLAN_NODE, mStartNode);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                break;
                            default:
                                // nothing
                                break;
                        }
                    }
                });
    }

    //=================================================================================

    /**
     * Android6.0之后需要动态申请权限
     */
    private void RequestPermission() {
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
        if (requestCode == authBaseRequestCode) {
            for (int ret : grantResults) {
                if (ret == 0) {
                    continue;
                } else {
                    Toast.makeText(EmergencyRescueActivity.this, "缺少导航基本的权限!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            initNavi();
        }
    }

}
