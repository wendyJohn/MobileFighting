package com.sanleng.mobilefighting.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.dialog.TipsDialog;
import com.sanleng.mobilefighting.net.NetCallBack;
import com.sanleng.mobilefighting.net.RequestUtils;
import com.sanleng.mobilefighting.net.URLs;
import com.sanleng.mobilefighting.util.PreferenceUtils;
import com.sanleng.mobilefighting.util.SVProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 巡检点扫描
 *
 * @author Qiaoshi
 */

public class PatrolPointScanningActivity extends Activity implements OnClickListener {
	private RelativeLayout r_back;
	private NfcAdapter mAdapter;
	private PendingIntent mPendingIntent;
	private TipsDialog tipsDialog;
	private int type;
	private String taskId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.patrolpointscanning_activity);
		initview();
		mynfcinitview();
	}

	// 初始化
	private void initview() {
		Intent intent = getIntent();
		type = intent.getExtras().getInt("type");
		if (type == 2) {
			taskId = intent.getStringExtra("taskId");
		}
		r_back = (RelativeLayout) findViewById(R.id.r_back);
		r_back.setOnClickListener(this);
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

	@Override
	protected void onResume() {
		try {
			mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onResume();
	}

	// NFC初始化
	private void mynfcinitview() {
		Intent nfcIntent = new Intent(this, getClass());
		nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		mPendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0);
		mAdapter = NfcAdapter.getDefaultAdapter(this);
		if (mAdapter == null) {
			Toast.makeText(this, "该设备不支持NFC", Toast.LENGTH_SHORT).show();
			return;
		} else if (!mAdapter.isEnabled()) {
			Toast.makeText(this, "NFC功能没打开，请打开", Toast.LENGTH_SHORT).show();
			return;
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		getTagInfo(intent);
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
	private void getTagInfo(Intent intent) {
		Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		byte[] tagId = tag.getId();
		String str = ByteArrayToHexString(tagId);
		str = flipHexStr(str);
		Isbinding(str);
	}

	private String flipHexStr(String s) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i <= s.length() - 2; i = i + 2) {
			result.append(new StringBuilder(s.substring(i, i + 2)).reverse());
		}
		return result.reverse().toString();
	}

	private String ByteArrayToHexString(byte[] inarray) {
		int i, j, in;
		String[] hex = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
		String out = "";

		for (j = 0; j < inarray.length; ++j) {
			in = (int) inarray[j] & 0xff;
			i = (in >> 4) & 0x0f;
			out += hex[i];
			i = in & 0x0f;
			out += hex[i];
		}
		return out;
	}

	//判断是否已绑定20180201015404376_00e31db02e024a05bfe8f91bf79d2be7
	private void Isbinding(String str) {
		RequestParams params = new RequestParams();
		params.put("unitcode", PreferenceUtils.getString(PatrolPointScanningActivity.this, "unitcode"));
		params.put("username", PreferenceUtils.getString(PatrolPointScanningActivity.this, "MobileFig_username"));
		params.put("platformkey", "app_firecontrol_owner");
		params.put("qrcode", "20180201015404376_00e31db02e024a05bfe8f91bf79d2be7");

		RequestUtils.ClientPost(URLs.NFC_ISBinding, params, new NetCallBack() {
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
				try {
					JSONObject jsonObject = new JSONObject(result);
					String data = jsonObject.getString("data");
					JSONObject object = new JSONObject(data);
					String message = object.getString("message");
					if (message.equals("qrcode已被绑定")) {

						String equipment = object.getString("equipment");
						JSONObject objects = new JSONObject(equipment);
						String qrcodeCode = objects.getString("qrcodeCode");
						String equipmentName = objects.getString("equipmentName");
						String ownedBuilding = objects.getString("ownedBuilding");
						String floorNumber = objects.getString("floorNumber");
						String positionAddr = objects.getString("positionAddr");
						String equipmentids = objects.getString("ids");

						if (type == 1) {
							Intent intent = new Intent(PatrolPointScanningActivity.this,
									InspectionHandlingActivity.class);
							intent.putExtra("type", type);
							intent.putExtra("labelnumber", qrcodeCode);
							intent.putExtra("devicename", equipmentName);
							intent.putExtra("equipmentposition", ownedBuilding + floorNumber + positionAddr);
							intent.putExtra("equipmentids", equipmentids);
							startActivity(intent);
							finish();
						} else {
							Intent intent = new Intent(PatrolPointScanningActivity.this,
									InspectionHandlingActivity.class);
							intent.putExtra("type", type);
							intent.putExtra("labelnumber", qrcodeCode);
							intent.putExtra("devicename", equipmentName);
							intent.putExtra("equipmentposition", ownedBuilding + floorNumber + positionAddr);
							intent.putExtra("taskId", taskId);
							intent.putExtra("equipmentids", equipmentids);
							startActivity(intent);
							finish();
						}

					} else {
						// 没有绑定，重新绑定设备
//						SVProgressHUD.showErrorWithStatus(PatrolPointScanningActivity.this, message);
						tipsDialog = new TipsDialog(PatrolPointScanningActivity.this, mHandler);
						tipsDialog.show();

					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onMyFailure(Throwable arg0) {
				SVProgressHUD.showErrorWithStatus(PatrolPointScanningActivity.this, "加载失败");
			}
		});
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 88886666:

					tipsDialog.dismiss();
					break;
				default:
					break;
			}
		};
	};

}