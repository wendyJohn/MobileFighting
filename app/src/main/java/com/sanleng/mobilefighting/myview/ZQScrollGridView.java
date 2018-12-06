package com.sanleng.mobilefighting.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class ZQScrollGridView extends GridView {
	  
	public ZQScrollGridView(Context context){
        this(context, null);  
   } 
	
	public ZQScrollGridView(Context context, AttributeSet attrs){
         super(context, attrs);  
    }  
 
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){  
         int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
         super.onMeasure(widthMeasureSpec, mExpandSpec);  
    } 
}
