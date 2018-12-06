package com.sanleng.mobilefighting.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.sanleng.mobilefighting.R;

import java.util.List;

/**
 */
public class CustomCurveChart extends View {

    // 鍧愭爣鍗曚綅
    private String[] xLabel;
    private String[] yLabel;
    // 鏇茬嚎鏁版嵁
    private List<int[]> dataList;
    private List<Integer> colorList;
    private boolean showValue;
    // 榛樿杈硅窛
    private int margin = 20;
    // 璺濈宸﹁竟鍋忕Щ閲�
    private int marginX = 30;
    // 鍘熺偣鍧愭爣
    private int xPoint;
    private int yPoint;
    // X,Y杞寸殑鍗曚綅闀垮害
    private int xScale;
    private int yScale;
    // 鐢荤瑪
    private Paint paintAxes;
    private Paint paintCoordinate;
    private Paint paintTable;
    private Paint paintCurve;
    private Paint paintRectF;
    private Paint paintValue;

    public CustomCurveChart(Context context, String[] xLabel, String[] yLabel,
                            List<int[]> dataList, List<Integer> colorList, boolean showValue) {
        super(context);
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.dataList = dataList;
        this.colorList = colorList;
        this.showValue = showValue;
    }

    public CustomCurveChart(Context context) {
        super(context);
    }

    /**
     * 鍒濆鍖栨暟鎹�煎拰鐢荤瑪
     */
    public void init() {
        xPoint = margin + marginX;
        yPoint = this.getHeight() - margin;
        xScale = (this.getWidth() - 2 * margin - marginX) / (xLabel.length - 1);
        yScale = (this.getHeight() - 2 * margin) / (yLabel.length - 1);

        paintAxes = new Paint();
        paintAxes.setStyle(Paint.Style.STROKE);
        paintAxes.setAntiAlias(true);
        paintAxes.setDither(true);
        paintAxes.setColor(ContextCompat.getColor(getContext(), R.color.color14));
        paintAxes.setStrokeWidth(8);

        paintCoordinate = new Paint();
        paintCoordinate.setStyle(Paint.Style.STROKE);
        paintCoordinate.setDither(true);
        paintCoordinate.setAntiAlias(true);
        paintCoordinate.setColor(ContextCompat.getColor(getContext(), R.color.color14));
        paintCoordinate.setTextSize(18);

        paintTable = new Paint();
        paintTable.setStyle(Paint.Style.STROKE);
        paintTable.setAntiAlias(true);
        paintTable.setDither(true);
        paintTable.setColor(ContextCompat.getColor(getContext(), R.color.color4));
        paintTable.setStrokeWidth(8);

        paintCurve = new Paint();
        paintCurve.setStyle(Paint.Style.STROKE);
        paintCurve.setDither(true);
        paintCurve.setAntiAlias(true);
        paintCurve.setStrokeWidth(8);
        PathEffect pathEffect = new CornerPathEffect(25);
        paintCurve.setPathEffect(pathEffect);

        paintRectF = new Paint();
        paintRectF.setStyle(Paint.Style.FILL);
        paintRectF.setDither(true);
        paintRectF.setAntiAlias(true);
        paintRectF.setStrokeWidth(8);

        paintValue = new Paint();
        paintValue.setStyle(Paint.Style.STROKE);
        paintValue.setAntiAlias(true);
        paintValue.setDither(true);
        paintValue.setColor(ContextCompat.getColor(getContext(), R.color.color1));
        paintValue.setTextAlign(Paint.Align.CENTER);
        paintValue.setTextSize(18);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(ContextCompat.getColor(getContext(), R.color.color1));
        init();
        drawTable(canvas, paintTable);
        drawAxesLine(canvas, paintAxes);
        drawCoordinate(canvas, paintCoordinate);
        for (int i = 0; i < dataList.size(); i++) {
            drawCurve(canvas, paintCurve, dataList.get(i), colorList.get(i));
            if (showValue) {
                drawValue(canvas, paintRectF, dataList.get(i), colorList.get(i));
            }
        }
    }

    /**
     * 缁樺埗鍧愭爣杞�
     */
    private void drawAxesLine(Canvas canvas, Paint paint) {
        // X
        canvas.drawLine(xPoint, yPoint, this.getWidth() - margin / 6, yPoint, paint);
        canvas.drawLine(this.getWidth() - margin / 6, yPoint, this.getWidth() - margin / 2, yPoint - margin / 3, paint);
        canvas.drawLine(this.getWidth() - margin / 6, yPoint, this.getWidth() - margin / 2, yPoint + margin / 3, paint);

        // Y
        canvas.drawLine(xPoint, yPoint, xPoint, margin / 6, paint);
        canvas.drawLine(xPoint, margin / 6, xPoint - margin / 3, margin / 2, paint);
        canvas.drawLine(xPoint, margin / 6, xPoint + margin / 3, margin / 2, paint);
    }

    /**
     * 缁樺埗琛ㄦ牸
     */
    private void drawTable(Canvas canvas, Paint paint) {
        Path path = new Path();
        // 妯悜绾�
        for (int i = 1; (yPoint - i * yScale) >= margin; i++) {
            int startX = xPoint;
            int startY = yPoint - i * yScale;
            int stopX = xPoint + (xLabel.length - 1) * xScale;
            path.moveTo(startX, startY);
            path.lineTo(stopX, startY);
            canvas.drawPath(path, paint);
        }

        // 绾靛悜绾�
        for (int i = 1; i * xScale <= (this.getWidth() - margin); i++) {
            int startX = xPoint + i * xScale;
            int startY = yPoint;
            int stopY = yPoint - (yLabel.length - 1) * yScale;
            path.moveTo(startX, startY);
            path.lineTo(startX, stopY);
            canvas.drawPath(path, paint);
        }
    }

    /**
     * 缁樺埗鍒诲害
     */
    private void drawCoordinate(Canvas canvas, Paint paint) {
        // X杞村潗鏍�
        for (int i = 0; i <= (xLabel.length - 1); i++) {
            paint.setTextAlign(Paint.Align.CENTER);
            int startX = xPoint + i * xScale;
            canvas.drawText(xLabel[i], startX, this.getHeight() - margin / 6, paint);
        }

        // Y杞村潗鏍�
        for (int i = 0; i <= (yLabel.length - 1); i++) {
            paint.setTextAlign(Paint.Align.LEFT);
            int startY = yPoint - i * yScale;
            int offsetX;
            switch (yLabel[i].length()) {
                case 1:
                    offsetX = 28;
                    break;

                case 2:
                    offsetX = 20;
                    break;

                case 3:
                    offsetX = 12;
                    break;

                case 4:
                    offsetX = 5;
                    break;

                default:
                    offsetX = 0;
                    break;
            }
            int offsetY;
            if (i == 0) {
                offsetY = 0;
            } else {
                offsetY = margin / 5;
            }
            // x榛樿鏄瓧绗︿覆鐨勫乏杈瑰湪灞忓箷鐨勪綅缃紝y榛樿鏄瓧绗︿覆鏄瓧绗︿覆鐨刡aseline鍦ㄥ睆骞曚笂鐨勪綅缃�
            canvas.drawText(yLabel[i], margin / 4 + offsetX, startY + offsetY, paint);
        }
    }

    /**
     * 缁樺埗鏇茬嚎
     */
    private void drawCurve(Canvas canvas, Paint paint, int[] data, int color) {
        paint.setColor(ContextCompat.getColor(getContext(), color));
        Path path = new Path();
        for (int i = 0; i <= (xLabel.length - 1); i++) {
            if (i == 0) {
                path.moveTo(xPoint, toY(data[0]));
            } else {
                path.lineTo(xPoint + i * xScale, toY(data[i]));
            }

            if (i == xLabel.length - 1) {
                path.lineTo(xPoint + i * xScale, toY(data[i]));
            }
        }
        canvas.drawPath(path, paint);
    }

    /**
     * 缁樺埗鏁板��
     */
    private void drawValue(Canvas canvas, Paint paint, int data[], int color) {
        paint.setColor(ContextCompat.getColor(getContext(), color));
        for (int i = 1; i <= (xLabel.length - 1); i++) {
            RectF rect;
            if (toY(data[i - 1]) < toY(data[i])) {
                rect = new RectF(xPoint + i * xScale - 20, toY(data[i]) - 15,
                        xPoint + i * xScale + 20, toY(data[i]) + 5);
                canvas.drawRoundRect(rect, 5, 5, paint);
                canvas.drawText(data[i] + "w", xPoint + i * xScale, toY(data[i]), paintValue);
            } else if (toY(data[i - 1]) > toY(data[i])) {
                rect = new RectF(xPoint + i * xScale - 20, toY(data[i]) - 5,
                        xPoint + i * xScale + 20, toY(data[i]) + 15);
                canvas.drawRoundRect(rect, 5, 5, paint);
                canvas.drawText(data[i] + "w", xPoint + i * xScale, toY(data[i]) + 10, paintValue);
            } else {
                rect = new RectF(xPoint + i * xScale - 20, toY(data[i]) - 10,
                        xPoint + i * xScale + 20, toY(data[i]) + 10);
                canvas.drawRoundRect(rect, 5, 5, paint);
                canvas.drawText(data[i] + "w", xPoint + i * xScale, toY(data[i]) + 5, paintValue);
            }
        }
    }

    /**
     * 鏁版嵁鎸夋瘮渚嬭浆鍧愭爣
     */
    private float toY(int num) {
        float y;
        try {
            float a = (float) num / 1.0f;
            y = yPoint - a * yScale;
        } catch (Exception e) {
            return 0;
        }
        return y;
    }

}
