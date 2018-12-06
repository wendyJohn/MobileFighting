package com.sanleng.mobilefighting.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.sanleng.mobilefighting.net.URLs;
import com.sanleng.mobilefighting.util.CustomMultipartEntity.ProgressListener;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 隐患整改上传
 *
 * @author Qiaoshi
 *
 */
public class RectificationUplaod extends AsyncTask<String, Integer, String> {
	private Context context;
	private List<String> filePathList;
	private long totalSize;
	private MyProgressDialog dp;
	private String serverResponse;
	private Handler handler;
	private String taskId;
	private String desc;
	private String username;

	public RectificationUplaod(Context context, List<String> filePathList, String taskId, String desc, String username,
							   Handler handler) {
		this.context = context;
		this.filePathList = filePathList;
		this.taskId = taskId;
		this.desc = desc;
		this.username = username;
		this.handler = handler;
	}

	@Override
	protected void onPreExecute() {
		dp = new MyProgressDialog(context);
		dp.setMessage("正在上传...");
		dp.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dp.setCancelable(false);
		dp.show();
	}

	@Override
	protected String doInBackground(String... params) {
		serverResponse = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext httpContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(URLs.RectificationTaskUpload + "?id=" + taskId + "&desc=" + desc + "&username="
				+ username + "&platformkey=app_firecontrol_owner");
		try {
			CustomMultipartEntity multipartContent = new CustomMultipartEntity(new ProgressListener() {
				@Override
				public void transferred(long num) {
					publishProgress((int) ((num / (float) totalSize) * 100));
				}
			});
			// 把上传内容添加到MultipartEntity
			for (int i = 0; i < filePathList.size(); i++) {
				multipartContent.addPart("File", new FileBody(new File(filePathList.get(i))));
				multipartContent.addPart("data",
						new StringBody(filePathList.get(i), Charset.forName(org.apache.http.protocol.HTTP.UTF_8)));
			}
			totalSize = multipartContent.getContentLength();

			httpPost.setEntity(multipartContent);

			HttpResponse response = httpClient.execute(httpPost, httpContext);
			serverResponse = EntityUtils.toString(response.getEntity());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return serverResponse;
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		dp.setProgress((int) (progress[0]));
	}

	@Override
	protected void onPostExecute(String result) {
		try {
			JSONObject object = new JSONObject(result);
			String mymsg = object.getString("msg");
			if (serverResponse != null) {
				if (mymsg.equals("处理成功")) {
					Message msg = new Message();
					msg.what = 678678;
					handler.sendMessage(msg);
					Bimp bimp = new Bimp();
					bimp.bmp = new ArrayList<Bitmap>();
					bimp.drr = new ArrayList<String>();
					bimp.max = 0;
					FileUtils.deleteDir();
				} else {

					SVProgressHUD.showErrorWithStatus(context, "上传失败！" + mymsg,
							SVProgressHUD.SVProgressHUDMaskType.GradientCancel);
				}
			} else {
				SVProgressHUD.showErrorWithStatus(context, "上传失败！" + mymsg,
						SVProgressHUD.SVProgressHUDMaskType.GradientCancel);
			}
			dp.dismiss();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onCancelled() {

	}

}
