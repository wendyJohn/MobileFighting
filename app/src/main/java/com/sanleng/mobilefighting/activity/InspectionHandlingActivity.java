package com.sanleng.mobilefighting.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.dialog.PromptDialog;
import com.sanleng.mobilefighting.util.Bimp;
import com.sanleng.mobilefighting.util.DateTimePickDialogUtil;
import com.sanleng.mobilefighting.util.FileUtils;
import com.sanleng.mobilefighting.util.PatrolRecordUplaod;
import com.sanleng.mobilefighting.util.PreferenceUtils;
import com.sanleng.mobilefighting.util.SVProgressHUD;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 巡查办理（NFC）
 *
 * @author qiaoshi
 */
public class InspectionHandlingActivity extends Activity implements OnClickListener {
    private RelativeLayout task_ac_back;
    private Button commit_task;
    private GridView noScrollgridview;
    private GridAdapter adapter;
    public File tempFile;
    private PromptDialog promptDialog;

    private TextView labelnumber;
    private TextView devicename;
    private TextView equipmentposition;

    private String devicenames;
    private String equipmentpositions;

    private int type;
    private String qrcode;
    private String equipmentids;
    private String R_status = "0";
    private String taskids = null;
    private String responsible_person;
    private String processingPeriod;
    private String desc;

    private EditText info_editText;
    private EditText r_info_editText;

    private EditText rectificationperiod;
    private EditText personliable;

    private LinearLayout youta, youtb, youtc, youtd, youte;
    private CheckBox rectification;
    private String isRectification = "否";
    private PatrolRecordUplaod post;
    private String initTime = "2018-00-00 00:00"; // 初始化结束时间
    private String rectificationperiodtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inspectionhandactivity);
        initview();
    }

    // 初始化数据
    private void initview() {
        // 创建对象
        promptDialog = new PromptDialog(this);
        // 设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(2000);
        task_ac_back = (RelativeLayout) findViewById(R.id.task_ac_back);
        task_ac_back.setOnClickListener(this);

        commit_task = (Button) findViewById(R.id.commit_task);
        commit_task.setOnClickListener(this);

        labelnumber = (TextView) findViewById(R.id.labelnumber);
        devicename = (TextView) findViewById(R.id.devicename);
        equipmentposition = (TextView) findViewById(R.id.equipmentposition);

        info_editText = (EditText) findViewById(R.id.insp_editText);
        r_info_editText = (EditText) findViewById(R.id.r_info_editText);

        rectificationperiod = (EditText) findViewById(R.id.rectificationperiod);
        rectificationperiod.setOnClickListener(this);

        personliable = (EditText) findViewById(R.id.personliable);

        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        adapter.update();
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg2 == Bimp.bmp.size()) {
                    new PopupWindows(InspectionHandlingActivity.this, noScrollgridview);
                } else {
                    Intent intent = new Intent(InspectionHandlingActivity.this, PhotoActivity.class);
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                }
            }
        });

        Intent intent = getIntent();
        type = intent.getExtras().getInt("type");
        if (type == 2) {
            taskids = intent.getStringExtra("taskId");
        }

        qrcode = intent.getStringExtra("labelnumber");
        devicenames = intent.getStringExtra("devicename");
        equipmentpositions = intent.getStringExtra("equipmentposition");
        equipmentids = intent.getStringExtra("equipmentids");
        labelnumber.setText(qrcode);

        devicename.setText(devicenames);
        equipmentposition.setText(equipmentpositions);

        youta = (LinearLayout) findViewById(R.id.youta);
        youtb = (LinearLayout) findViewById(R.id.youtb);
        youtc = (LinearLayout) findViewById(R.id.youtc);
        youtd = (LinearLayout) findViewById(R.id.youtd);
        youte = (LinearLayout) findViewById(R.id.youte);

        rectification = (CheckBox) findViewById(R.id.rectification);
        rectification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    isRectification = "是";
                    youte.setVisibility(View.GONE);
                } else {
                    isRectification = "否";
                    youte.setVisibility(View.GONE);
                }
            }
        });

        RadioGroup group = (RadioGroup) this.findViewById(R.id.radiogrou);
        // 绑定一个匿名监听器
        group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub
                // 获取变更后的选中项的ID
                int radioButtonId = arg0.getCheckedRadioButtonId();
                // 根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton) findViewById(radioButtonId);
                String s = rb.getText().toString();
                if (s.equals("正常")) {
                    R_status = "0";
                    youta.setVisibility(View.GONE);
                    youtb.setVisibility(View.GONE);
                    youtc.setVisibility(View.GONE);
                    youtd.setVisibility(View.GONE);
                    youte.setVisibility(View.GONE);
                }
                if (s.equals("故障（损坏）")) {
                    R_status = "1";
                    youta.setVisibility(View.VISIBLE);
                    youtb.setVisibility(View.VISIBLE);
                    youtc.setVisibility(View.VISIBLE);
                    youtd.setVisibility(View.GONE);
                }
                if (s.equals("瘫痪（丢失）")) {
                    R_status = "2";
                    youta.setVisibility(View.VISIBLE);
                    youtb.setVisibility(View.VISIBLE);
                    youtc.setVisibility(View.VISIBLE);
                    youtd.setVisibility(View.GONE);
                }
            }
        });
    }

    // 提交
    public void doCommit() {
        // 提交
        desc = info_editText.getText().toString().trim();
        if (R_status.equals("1") || R_status.equals("2")) {
            responsible_person = personliable.getText().toString().trim();
            String time = rectificationperiod.getText().toString().trim();
            if (time != null || !"".equals(time)) {
                rectificationperiodtime = time.substring(0, time.length() - 6);
            } else {
                rectificationperiodtime = null;
            }
        } else {
            rectificationperiodtime = null;
        }

        List<String> list = new ArrayList<String>();
        for (int i = 0; i < Bimp.drr.size(); i++) {
            String Str = Bimp.drr.get(i).substring(Bimp.drr.get(i).lastIndexOf("/") + 1,
                    Bimp.drr.get(i).lastIndexOf("."));
            list.add(FileUtils.SDPATH + Str + ".JPEG");
        }

        post = new PatrolRecordUplaod(InspectionHandlingActivity.this, list, qrcode, equipmentids, R_status, taskids,
                desc, responsible_person, rectificationperiodtime,
                PreferenceUtils.getString(InspectionHandlingActivity.this, "MobileFig_username"), m_handler);
        post.execute();
    }

    private Handler m_handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 567676:
                    SVProgressHUD.showSuccessWithStatus(InspectionHandlingActivity.this, "上传成功");
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            finish();
                        }
                    }, 2000);
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 执行提交操作(需要更新很多数据)
            case R.id.commit_task:
                doCommit();
                break;
            case R.id.task_ac_back:
                finish();
                break;
            case R.id.rectificationperiod:
                DateTimePickDialogUtil enddateTimePicKDialog = new DateTimePickDialogUtil(InspectionHandlingActivity.this,
                        initTime);
                enddateTimePicKDialog.dateTimePicKDialog(rectificationperiod);
                break;
            default:
                break;
        }

    }

    @SuppressLint("HandlerLeak")
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater; // 视图容器
        private int selectedPosition = -1;// 选中的位置
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {

            loading();
        }

        public int getCount() {
            return (Bimp.bmp.size() + 1);
        }

        public Object getItem(int arg0) {

            return null;
        }

        public long getItemId(int arg0) {

            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        /**
         * ListView Item设置
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            final int coord = position;
            ViewHolder holder = null;
            if (convertView == null) {

                convertView = inflater.inflate(R.layout.item_published_grida, parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == Bimp.bmp.size()) {
                holder.image
                        .setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused));
                if (position == 20) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.bmp.get(position));

            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.drr.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            try {
                                String path = Bimp.drr.get(Bimp.max);
                                System.out.println(path);
                                Bitmap bm = Bimp.revitionImageSize(path);
                                Bimp.bmp.add(bm);
                                String newStr = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
                                FileUtils.saveBitmap(bm, "" + newStr);
                                Bimp.max += 1;
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);
                            } catch (IOException e) {

                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
        }
    }

    public class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent) {

            View view = View.inflate(mContext, R.layout.item_popupwindows, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_bottom_in_2));

            setWidth(LayoutParams.FILL_PARENT);
            setHeight(LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
            Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
            Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
            bt1.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    photo();
                    dismiss();
                }
            });
            bt2.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {

                }
            });
            bt3.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });

        }

    }

    public static boolean hasSDcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    private static final int TAKE_PICTURE = 0x000000;
    private String path = "";

    @SuppressLint("SimpleDateFormat")
    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        StringBuffer sDir = new StringBuffer();
        if (hasSDcard()) {
            sDir.append(Environment.getExternalStorageDirectory() + "/HandlingPicture/" + equipmentids + "/");
        } else {
            String dataPath = Environment.getRootDirectory().getPath();
            sDir.append(dataPath + "/HandlingPicture/" + equipmentids + "/");
        }

        File fileDir = new File(sDir.toString());
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        tempFile = new File(fileDir, dateFormat.format(date) + ".jpg");

        path = tempFile.getPath();
        Uri imageUri = Uri.fromFile(tempFile);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case TAKE_PICTURE:
                if (Bimp.drr.size() < 20 && resultCode == -1) {
                    Bitmap bm = BitmapFactory.decodeFile(path);
                    try {
                        FileOutputStream fos = new FileOutputStream(tempFile);
                        byte[] temp = new byte[1024];
//					 添加时间水印
                        Bitmap newbm = addTimeFlag(bm);
                        newbm.compress(Bitmap.CompressFormat.JPEG, 100, fos);// 把数据写入文件
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Bimp.drr.add(path);
                }
                break;
        }
    }


    private Bitmap addTimeFlag(Bitmap src) {
        // 获取原始图片与水印图片的宽与高
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas mCanvas = new Canvas(newBitmap);
        // 往位图中开始画入src原始图片
        mCanvas.drawBitmap(src, 0, 0, null);
        // 添加文字
        Paint textPaint = new Paint();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date(System.currentTimeMillis()));
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(100);
//		mCanvas.drawText("经度:" + mylongitude + " ; " + " 纬度:" + mylatitude, 25, (h - 155), textPaint);
        mCanvas.drawText(time, (float) 25, (h - 285), textPaint);
//		mCanvas.drawText("详细地址:" + address, 25, (h - 25), textPaint);
//		mCanvas.drawText(name, (float) 25, (h - 415), textPaint);
        mCanvas.save();
        mCanvas.restore();
        return newBitmap;
    }

    public String getString(String s) {
        String path = null;
        if (s == null)
            return "";
        for (int i = s.length() - 1; i > 0; i++) {
            s.charAt(i);
        }
        return path;
    }

    protected void onRestart() {
        adapter.update();
        super.onRestart();
    }

}
