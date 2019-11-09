package com.wxy.easyscrollerchartview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.wxy.easyscrollerchartviewlibrary.EasyScrollerChartView;

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
    public void drawContent(Canvas canvas, Point originalPoint, float horizontalAverageWidth, float verticalRegionLength, Rect rect) {
        Path path=new Path();
        canvas.save();
        canvas.clipRect(rect);
        for (int i=0;i<scrollerPointModelList.size();i++ ){
            float x=((scrollerPointModelList.get(i).getX()-horizontalMin)/horizontalAverageWeight*horizontalAverageWidth)+originalPoint.x;
            float y=originalPoint.y-((scrollerPointModelList.get(i).getY()-verticalMin)/(verticalMax-verticalMin)* verticalRegionLength);
            pointTextPaint.setColor(Color.BLUE);
            pointTextPaint.setStrokeWidth(5);
            pointTextPaint.setStyle(Paint.Style.STROKE);
            if (i==0){
                path.moveTo(x,y);
            }else {
                path.lineTo(x,y);
            }
            canvas.drawPath(path,pointTextPaint);
        }
        canvas.restore();
        canvas.save();
        canvas.clipRect(new Rect(rect.left-10,rect.top,rect.right,rect.bottom));
        for (int i=0;i<scrollerPointModelList.size();i++ ){
            float x=((scrollerPointModelList.get(i).getX()-horizontalMin)/horizontalAverageWeight*horizontalAverageWidth)+originalPoint.x;
            float y=originalPoint.y-((scrollerPointModelList.get(i).getY()-verticalMin)/(verticalMax-verticalMin)* verticalRegionLength);
            pointTextPaint.setColor(Color.RED);
            pointTextPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(x,
                    y,10,pointTextPaint);
        }
        canvas.restore();
    }
}
