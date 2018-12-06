package com.sanleng.mobilefighting.util;

import java.text.NumberFormat;

import com.sanleng.mobilefighting.R;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MyProgressDialog extends AlertDialog {
	private ProgressBar mProgress;
	private TextView mProgressNumber;
	private TextView mProgressPercent;
	private TextView mProgressMessage;
	private Handler mViewUpdateHandler;
	private int mMax;
	private CharSequence mMessage;
	private boolean mHasStarted;
	private int mProgressVal;
	private String TAG = "CommonProgressDialog";
	private String mProgressNumberFormat;
	private NumberFormat mProgressPercentFormat;

	public MyProgressDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initFormats();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myprogress_dialog);
		mProgress = (ProgressBar) findViewById(R.id.progress);
		mProgressNumber = (TextView) findViewById(R.id.progress_number);
		mProgressPercent = (TextView) findViewById(R.id.progress_percent);
		mProgressMessage = (TextView) findViewById(R.id.progress_message);
		mViewUpdateHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				int progress = mProgress.getProgress();
				int max = mProgress.getMax();
				double dProgress = (double) progress / (double) (1024 * 1024);
				double dMax = (double) max / (double) (1024 * 1024);
				if (mProgressNumberFormat != null) {
					String format = mProgressNumberFormat;
					mProgressNumber.setText(String.format(format, progress, max));
				} else {
					mProgressNumber.setText("");
				}
				if (mProgressPercentFormat != null) {
					double percent = (double) progress / (double) max;
					SpannableString tmp = new SpannableString(mProgressPercentFormat.format(percent));
					tmp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, tmp.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					mProgressPercent.setText(tmp);
				} else {
					mProgressPercent.setText("");
				}
			}
		};

		onProgressChanged();
		if (mMessage != null) {
			setMessage(mMessage);
		}
		if (mMax > 0) {
			setMax(mMax);
		}
		if (mProgressVal > 0) {
			setProgress(mProgressVal);
		}
	}

	private void initFormats() {
		mProgressNumberFormat = "%1d/%2d";
		mProgressPercentFormat = NumberFormat.getPercentInstance();
		mProgressPercentFormat.setMaximumFractionDigits(0);
	}

	private void onProgressChanged() {
		mViewUpdateHandler.sendEmptyMessage(0);
	}

	public void setProgressStyle(int style) {
		// mProgressStyle = style;
	}

	public int getMax() {
		if (mProgress != null) {
			return mProgress.getMax();
		}
		return mMax;
	}

	public void setMax(int max) {
		if (mProgress != null) {
			mProgress.setMax(max);
			onProgressChanged();
		} else {
			mMax = max;
		}
	}

	public void setIndeterminate(boolean indeterminate) {
		if (mProgress != null) {
			mProgress.setIndeterminate(indeterminate);
		}
		// else {
		// mIndeterminate = indeterminate;
		// }
	}

	public void setProgress(int value) {
		if (mHasStarted) {
			mProgress.setProgress(value);
			onProgressChanged();
		} else {
			mProgressVal = value;
		}
	}

	@Override
	public void setMessage(CharSequence message) {
		// TODO Auto-generated method stub
		// super.setMessage(message);
		if (mProgressMessage != null) {
			mProgressMessage.setText(message);
		} else {
			mMessage = message;
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mHasStarted = true;
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mHasStarted = false;
	}
}