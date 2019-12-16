package com.aice.easyscrollerchartview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import com.wxy.easyscrollerchartview.EasyScrollerChartView;
import com.wxy.easyscrollerchartview.model.ScrollerPointModel;

import java.util.List;

public class MyScrollerChartView extends EasyScrollerChartView {
    public MyScrollerChartView(Context context) {
        super(context);
    }

    public MyScrollerChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollerChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public void drawContent(Canvas canvas, Point originalPoint, List<? extends ScrollerPointModel> scrollerPointModelList,
                            int minX, int maxX,
                            float realHorizontalAverageWidth,
                            float verticalRegionLength,Rect rect) {

        Path path=new Path();
        canvas.save();
        canvas.clipRect(rect);
        for (int i=minX;i<maxX;i++ ){
            float x=calculateX(scrollerPointModelList.get(i).getX());
            float y=calculateY(scrollerPointModelList.get(i).getY());
            pointTextPaint.setColor(Color.BLUE);
            pointTextPaint.setStrokeWidth(5);
            pointTextPaint.setStyle(Paint.Style.STROKE);
            if (i==minX){
                path.moveTo(x,y);
            }else {
                path.lineTo(x,y);
            }
            canvas.drawPath(path,pointTextPaint);
        }
        canvas.restore();
        canvas.save();
        canvas.clipRect(new Rect(rect.left-10,rect.top,rect.right,rect.bottom));
        for (int i=minX;i<maxX;i++ ){
            float x=calculateX(scrollerPointModelList.get(i).getX());
            float y=calculateY(scrollerPointModelList.get(i).getY());
            pointTextPaint.setColor(Color.RED);
            pointTextPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(x,
                    y,10,pointTextPaint);
        }
        canvas.restore();
    }

}
