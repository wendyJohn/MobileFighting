package com.sanleng.mobilefighting.net;

/**
 * @author qiaoshi
 */
public class URLs {
    public static String HOST_IP = "10.101.80.113";
    public static String HOST_PORT = "8080";

    public static String HOST = "http://" + HOST_IP + ":" + HOST_PORT + "/";
    // 登陆
    public static String BULOGIN_URL = HOST + "kspf/app/user/login";
    // 获取首页问题统计数量
    public static String ProblemStatistics = HOST + "kspf/app/fpindex/fpStatistics";
    // 巡查任务列表
    public static String InspectionTask = HOST + "kspf/app/patroltask/list";
    // 巡查记录
    public static String InspectionRecord = HOST + "kspf/app/patrol/list";
    // 整改任务列表
    public static String RectificationTask = HOST + "kspf/app/rectification/list";
    // 隐患上传
    public static String RectificationTaskUpload = HOST + "kspf/app/rectification/upload";
    // 火警统计
    public static String FireAlarmStatistics = HOST + "kspf/app/ferecord/count";
    // 火警
    public static String FireAlarm_URL = HOST + "kspf/app/ferecord/firealertlist";
    // 确认火警
    public static String ConfirmFireAlarm_URL = HOST + "kspf/app/ferecord/isTrueFire";
    // 水系统列表
    public static String WaterSystem_URL = HOST + "kspf/app/water/list";
    // 水系数据统列表
    public static String WaterSystemStatistics_URL = HOST + "kspf/app/water/count";
    // 电气火灾建筑列表
    public static String Architecture_URL = HOST + "kspf/app/electricalfire/buildList";
    // 电气火灾楼层列表
    public static String Floor_URL = HOST + "kspf/app/electricalfire/floorList";
    // 电气火灾设备名列表
    public static String Device_URL = HOST + "kspf/app/electricalfire/deviceNames";
    // 电气火灾设备列表
    public static String DeviceItem_URL = HOST + "kspf/app/electricalfire/deviceList";
    // 电气火灾设备实时数据列表
    public static String RealTimeData_URL = HOST + "kspf/app/electricalfire/deviceRealTimeData";
    // 首页轮播图
    public static String BANGIMAGE_URL = HOST + "kspf/app/publicityedu/banner";
    // 文章视频
    public static String Article_URL = HOST + "kspf/app/publicityedu/list?page=";
    // 文章详情
    public static String ArticleLtem_URL = HOST + "kspf/app/publicityedu/info";
    // NFC是否绑定
    public static String NFC_ISBinding = HOST + "kspf/app/fpequipment/isbind";
    // 巡查记录上传
    public static String PatorlRecordUplaod_URL = HOST + "kspf/app/patrol/upload";
    // 应急站列表
    public static String EmergencyStation_URL = HOST + "kspf/app/station/list";
    // 应急开锁
    public static String ORDER_BASE_URL = "http://10.101.208.157:8091/emergencystation";// Order消息发送,心跳包uri
    // 物资入库
    public static String Warehousing_URL = HOST + "kspf/app/station/state";
    // 物资出库
    public static String Outofstock_URL = HOST + "kspf/app/station/state";
    // 物资列表
    public static String Material_URL = HOST + "kspf/app/station/materiallist";
    // 物资详情
    public static String MaterialDetails_URL = HOST + "kspf/app/station/detail";
}
