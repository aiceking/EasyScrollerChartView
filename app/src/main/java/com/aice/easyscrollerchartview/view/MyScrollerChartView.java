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

import com.wxy.easyscrollerchartview.EasyScrollerChartView;
import com.wxy.easyscrollerchartview.model.DrawPiontModel;
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
                            List<DrawPiontModel> drawPiontModelList,
                            float realHorizontalAverageWidth,
                            float verticalRegionLength,Rect rect) {
        Path path=new Path();
        canvas.save();
        canvas.clipRect(rect);
        for (int i=0;i<drawPiontModelList.size();i++ ){
            pointTextPaint.setColor(Color.BLUE);
            pointTextPaint.setStrokeWidth(5);
            pointTextPaint.setStyle(Paint.Style.STROKE);
            if (i==0){
                path.moveTo(drawPiontModelList.get(i).getX(),drawPiontModelList.get(i).getY());
            }else {
                path.lineTo(drawPiontModelList.get(i).getX(),drawPiontModelList.get(i).getY());
            }
            canvas.drawPath(path,pointTextPaint);
        }
        canvas.restore();
        canvas.save();
        canvas.clipRect(new Rect(rect.left-10,rect.top,rect.right,rect.bottom));
        for (DrawPiontModel drawPiontModel:drawPiontModelList){
            pointTextPaint.setColor(Color.RED);
            pointTextPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(drawPiontModel.getX(),
                    drawPiontModel.getY(),10,pointTextPaint);
        }
        canvas.restore();
    }

}
