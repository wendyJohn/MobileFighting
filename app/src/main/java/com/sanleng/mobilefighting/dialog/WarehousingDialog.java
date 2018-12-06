package com.sanleng.mobilefighting.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.util.MonStationDialog;
import com.sanleng.mobilefighting.util.StorageDialog;

/**
 * 入库提示
 *
 * @author qiaoshi
 *
 */
public class WarehousingDialog extends Dialog implements android.view.View.OnClickListener {

	private Context context;
	private TextView notice;
	private TextView cancle;
	private Handler mHandler;
	private RelativeLayout spinner_rootViewa;
	private RelativeLayout spinner_rootViewb;
	private TextView spinner_name_rootViewa;
	private TextView spinner_name_rootViewb;
	private MonStationDialog monStationDialog;
	private StorageDialog storageDialog;

	private String ids = null;
	private String stationAddress = null;
	private String stationId = null;
	private String stationName = null;
	private String storageLocation = null;
	private String storagename = null;

	public WarehousingDialog(Context context, String ids, Handler mHandler) {
		super(context);
		this.context = context;
		this.ids = ids;
		this.mHandler = mHandler;
	}

	public WarehousingDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		this.setContentView(R.layout.warehousingdialog);
		this.setCancelable(false);// 设置点击屏幕Dialog不消失
		notice = (TextView) findViewById(R.id.notice);
		cancle = (TextView) findViewById(R.id.cancle);
		spinner_name_rootViewa = (TextView) findViewById(R.id.spinner_name_rootViewa);
		spinner_name_rootViewb = (TextView) findViewById(R.id.spinner_name_rootViewb);
		spinner_rootViewa = (RelativeLayout) findViewById(R.id.spinner_rootViewa);
		spinner_rootViewb = (RelativeLayout) findViewById(R.id.spinner_rootViewb);

		spinner_rootViewa.setOnClickListener(this);
		spinner_rootViewb.setOnClickListener(this);

		notice.setOnClickListener(this);
		cancle.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			// 获取站列表
			case R.id.spinner_rootViewa:
				monStationDialog = new MonStationDialog(context, m_Handler);
				monStationDialog.show();
				break;
			// 获取存储位置
			case R.id.spinner_rootViewb:
				storageDialog = new StorageDialog(context, m_Handler);
				storageDialog.show();
				break;
			// 添加入库
			case R.id.notice:
				if ("".equals(stationName) || stationName == null) {
					Toast.makeText(context, "站点位置不能为空", Toast.LENGTH_SHORT).show();
				} else if ("".equals(storagename) || storagename == null) {
					Toast.makeText(context, "存储位置不能为空", Toast.LENGTH_SHORT).show();
				} else {
					Message mymsg = new Message();
					Bundle data = new Bundle();
					data.putString("ids", ids);
					data.putString("stationAddress", stationAddress);
					data.putString("stationId", stationId);
					data.putString("stationName", stationName);
					data.putString("storageLocation", storageLocation);

					mymsg.setData(data);
					mymsg.what = 25267;
					mHandler.sendMessage(mymsg);
					dismiss();
				}
				break;
			// 取消入库
			case R.id.cancle:
				ids = null;
				stationAddress = null;
				stationId = null;
				stationName = null;
				storageLocation = null;
				storagename = null;

				Message msg = new Message();
				msg.what = 25266;
				mHandler.sendMessage(msg);
				dismiss();
				break;
			default:
				break;
		}
	}

	private Handler m_Handler = new Handler() {
		@SuppressLint("HandlerLeak")
		@Override
		public void handleMessage(Message message) {
			switch (message.what) {
				// 站点信息
				case 78323:
					monStationDialog.dismiss();
					Bundle bundle = message.getData();
					stationAddress = bundle.getString("address");
					stationId = bundle.getString("id");
					stationName = bundle.getString("name");
					spinner_name_rootViewa.setText(stationName);
					break;
				// 存储位置
				case 78326:
					storageDialog.dismiss();
					Bundle bundles = message.getData();
					storageLocation = bundles.getString("id");
					storagename = bundles.getString("name");
					spinner_name_rootViewb.setText(storagename);
					break;
				default:
					break;
			}
		}
	};
}
