package com.sanleng.mobilefighting.myview;

import com.sanleng.mobilefighting.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("DrawAllocation")
public class RadarView extends View {
	private int w, h;
	// 宽高
	private Paint mCirclePaint;
	// 圆的画笔
	Bitmap mBitmap;
	// 显示在中心的图片
	private int image_half_width;
	// 图片的半径
	private Paint mGradientCirclePaint;
	// 渐变圆的笔
	Handler mHandler = new Handler();
	Matrix matrix;
	// 矩阵
	private float degrees = 0;
	private long delayMillis = 20;
	// 子线程实现旋转效果
	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// 旋转
			matrix.postRotate(++degrees, w / 2, h / 2);
			RadarView.this.invalidate();
			mHandler.postDelayed(runnable, delayMillis);
		}
	};

	public RadarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// setBackgroundResource(resid);
		// 获取宽高
		w = context.getResources().getDisplayMetrics().widthPixels;
		h = context.getResources().getDisplayMetrics().heightPixels;
		matrix = new Matrix();
		// 初始化画笔
		mCirclePaint = new Paint();
		mCirclePaint.setColor(Color.WHITE);
		mCirclePaint.setStrokeWidth(3);
		// 画的宽度
		mCirclePaint.setStyle(Paint.Style.STROKE);
		mCirclePaint.setAntiAlias(true);
		// 获取头像图片
		mBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.scanning)).getBitmap();
		image_half_width = ((w / 3) * 1);
		// 图片设置成最小圆的一半,这是图片的半径 // 图片缩放比例
		float sx = (float) 2 * image_half_width / mBitmap.getWidth();
		// 图片显示的宽度/原图宽度
		float sy = (float) 2 * image_half_width / mBitmap.getHeight();
		// 缩小图片
		Matrix matrix = new Matrix();
		matrix.setScale(sx, sy);
		mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, false);
		// 着色器
		Shader mShader = new SweepGradient(w / 2, h / 2, Color.TRANSPARENT, Color.parseColor("#AAAAAAAA"));
		mGradientCirclePaint = new Paint();
		mGradientCirclePaint.setColor(Color.WHITE); //
		mGradientCirclePaint.setStrokeWidth(3);
		mGradientCirclePaint.setStyle(Paint.Style.FILL);
		// 实心
		mGradientCirclePaint.setAntiAlias(true);
		mGradientCirclePaint.setShader(mShader);
		mHandler.post(runnable);
	}

	// 重写绘制方法 @Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 画4个不能半径的圆
		canvas.drawCircle(w / 2, h / 2, w / 6, mCirclePaint);
		canvas.drawCircle(w / 2, h / 2, w / 3, mCirclePaint);
		canvas.drawCircle(w / 2, h / 2, 3 * w / 10, mCirclePaint);
		canvas.drawCircle(w / 2, h / 2, 4 * h / 16, mCirclePaint);
		// 显示的图片，绘制的左上角坐标（中心点的坐标减去图片的半径），画笔
		canvas.drawBitmap(mBitmap, w / 2 - image_half_width, h / 2 - image_half_width, null);
		// 这里不需要笔，就是一个图 // 画渐变 的圆
		canvas.concat(matrix);
		canvas.drawCircle(w / 2, h / 2, 3 * h / 12, mGradientCirclePaint);
		matrix.reset();
	}

	// 实现点击波浪效果 @Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}
}
