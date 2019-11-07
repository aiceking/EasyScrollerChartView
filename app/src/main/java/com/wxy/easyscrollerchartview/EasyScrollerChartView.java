package com.wxy.easyscrollerchartview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;

import java.util.List;


public class EasyScrollerChartView extends View {
    private final String TAG = getClass().getName();
    //设置默认的宽和高,比例为4:3
    private static final int DEFUALT_VIEW_WIDTH=400;
    private static final int DEFUALT_VIEW_HEIGHT=300;
    private Paint mPaint;
    private TextPaint horizontalTextPaint ;
    private TextPaint verticalTextPaint;
    private TextPaint pointTextPaint;
    private int mLastX = 0;
    private int mLastY = 0;
    private int downX=0,downY=0;
    private VelocityTracker mVelocityTracker;
    private Scroller scroller;

    public void setHorizontalCoordinatesList(List<String> horizontalCoordinatesList) {
        this.horizontalCoordinatesList = horizontalCoordinatesList;
        invalidate();
    }

    public void setVerticalCoordinatesList(List<String> verticalCoordinatesList) {
        this.verticalCoordinatesList = verticalCoordinatesList;
        invalidate();
    }

    private List<String> horizontalCoordinatesList;
    private List<String> verticalCoordinatesList;
    private float horizontalRatio=0.2f;
    private long verticalMax= (long) 0.2;
    public long getVerticalMax() {
        return verticalMax;
    }

    public void setVerticalMax(long verticalMax) {
        this.verticalMax = verticalMax;
    }

    public long getVerticalMin() {
        return verticalMin;
    }

    public void setVerticalMin(long verticalMin) {
        this.verticalMin = verticalMin;
    }

    private long verticalMin= (long) 0.2;
    public float getVerticalRatio() {
        return verticalRatio;
    }

    public void setVerticalRatio(float verticalRatio) {
        this.verticalRatio = verticalRatio;
    }

    private float verticalRatio=0.2f;
    public float getHorizontalRatio() {
        return horizontalRatio;
    }

    public void setHorizontalRatio(float horizontalRatio) {
        this.horizontalRatio = horizontalRatio;
    }

    public EasyScrollerChartView(Context context) {
        super(context);
    }

    public EasyScrollerChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLUE);
        //横坐标的刻度值画笔
        horizontalTextPaint= new TextPaint();
        horizontalTextPaint.setTextSize(40);
        horizontalTextPaint.setColor(Color.BLACK);
        //纵坐标的刻度值画笔
        verticalTextPaint= new TextPaint();
        verticalTextPaint.setTextAlign(Paint.Align.RIGHT);
        verticalTextPaint.setTextSize(40);
        verticalTextPaint.setColor(Color.BLACK);
        //每个点的值画笔
        pointTextPaint= new TextPaint();
        pointTextPaint.setTextAlign(Paint.Align.CENTER);
        pointTextPaint.setTextSize(40);
        pointTextPaint.setColor(Color.BLACK);


        scroller=new Scroller(context);
    }

    public EasyScrollerChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width=0,height=0;
        int width_specMode= MeasureSpec.getMode(widthMeasureSpec);
        int height_specMode= MeasureSpec.getMode(heightMeasureSpec);
        switch (width_specMode){
            //宽度精确值
            case MeasureSpec.EXACTLY:
                switch (height_specMode){
                    //高度精确值
                    case MeasureSpec.EXACTLY:
                        width= MeasureSpec.getSize(widthMeasureSpec);
                        height= MeasureSpec.getSize(heightMeasureSpec);
                        break;
                    case MeasureSpec.AT_MOST:
                    case MeasureSpec.UNSPECIFIED:
                        width= MeasureSpec.getSize(widthMeasureSpec);
                        height=width*3/4;
                        break;
                }
                break;
            //宽度wrap_content
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                switch (height_specMode){
                    //高度精确值
                    case MeasureSpec.EXACTLY:
                        height= MeasureSpec.getSize(heightMeasureSpec);
                        width=height*4/3;
                        break;
                    case MeasureSpec.AT_MOST:
                    case MeasureSpec.UNSPECIFIED:
                        height=DEFUALT_VIEW_HEIGHT;
                        width=DEFUALT_VIEW_WIDTH;
                        break;
                }
                break;
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**先计算原点的位置*/
        Point point=calculateOriginalPoint();
        /**画横坐标的线*/
        canvas.drawLine((float) point.x,(float) point.y,(float) (getWidth()-getPaddingRight()),(float) point.y,mPaint);
        /**画纵坐标的线*/
        canvas.drawLine((float) point.x,(float) point.y,(float) point.x,(float) getPaddingTop(),mPaint);
        /**画横坐标的刻度值*/
        /** 默认横坐标文字两边需要留白，避免相邻的横坐标值挨在一起，所以在现有的宽度上减少一点*/
        float HorizontalAveragewidth=(getWidth()-getPaddingLeft()-getPaddingRight()-point.x)*horizontalRatio*4/5;
        if (horizontalCoordinatesList!=null){
            if (horizontalCoordinatesList.size()>0){
            for (int i=0;i<horizontalCoordinatesList.size();i++){
                StaticLayout sl = new StaticLayout(horizontalCoordinatesList.get(i),horizontalTextPaint,(int)HorizontalAveragewidth, Layout.Alignment.ALIGN_NORMAL,1.0f,0.0f,true);
                canvas.save();
                canvas.translate((float) point.x+(getWidth()-getPaddingLeft()-getPaddingRight())*horizontalRatio*(i+1)-(sl.getLineWidth(0)/2),(float) point.y+(getHeight()-getPaddingBottom()-point.y)/4);
                sl.draw(canvas);
                canvas.translate(0,0);
                canvas.restore();
                canvas.drawCircle((float) point.x+(getWidth()-getPaddingLeft()-getPaddingRight())*horizontalRatio*(i+1),(float) point.y,20,horizontalTextPaint);

            }
            }
        }
        /**画纵坐标的刻度值*/
        if (verticalCoordinatesList!=null){
            if (verticalCoordinatesList.size()>0){
            for (int i=0;i<verticalCoordinatesList.size();i++){
                canvas.drawText(verticalCoordinatesList.get(i),point.x-((point.x-getPaddingLeft())/4),
                        point.y-(point.y-getPaddingTop())/verticalCoordinatesList.size()*i-getTextOffset(verticalTextPaint,verticalCoordinatesList.get(i)),
                        verticalTextPaint);
            }
            }
        }
    }
    /**先计算原点的位置*/
    private Point calculateOriginalPoint() {
        Point point=new Point();
        /**计算出原点需要向上偏移多少*/
        // 默认横坐标文字两边需要留白，避免相邻的横坐标值挨在一起，所以在现有的宽度上减少一点
        float HorizontalAveragewidth=(getWidth()-getPaddingLeft()-getPaddingRight()-point.x)*horizontalRatio*4/5;
        //拿到横坐标在换行后高度最大的高度
        point.y=0;
        if (horizontalCoordinatesList!=null){
            if (horizontalCoordinatesList.size()>0){
                for (int i=0;i<horizontalCoordinatesList.size();i++){
                    StaticLayout sl = new StaticLayout(horizontalCoordinatesList.get(i),horizontalTextPaint,(int)HorizontalAveragewidth, Layout.Alignment.ALIGN_NORMAL,1.0f,0.0f,true);
                    if (sl.getHeight()>point.y){
                        point.y=sl.getHeight();
                    }
                }
            }
        }
        //默认横坐标文字的上下需要留白，所以这个高度要放大
        point.y=point.y*2;
        //矫正至Canvas所在的坐标
        point.y=getHeight()-getPaddingBottom()-point.y;

        //这里要修正计算出原点需要向右偏移多少，因为防止数据过多之后产生上下重叠现象
        if (horizontalCoordinatesList!=null){
            if (horizontalCoordinatesList.size()>0){
        int verticalAverage=(point.y-getPaddingTop())/verticalCoordinatesList.size();
        if (verticalTextPaint.getTextSize()>=verticalAverage){
            verticalTextPaint.setTextSize(verticalAverage*3/4);
        }
            }
        }
        /**计算出原点需要向右偏移多少*/
        //拿到纵坐标长度最长的字符串
        String verticalMaxLengthString="";
        if (verticalCoordinatesList!=null){
            if (verticalCoordinatesList.size()>0){
                for (int i=0;i<verticalCoordinatesList.size();i++){
                    if (verticalCoordinatesList.get(i).length()>verticalMaxLengthString.length()){
                        verticalMaxLengthString=verticalCoordinatesList.get(i);
                    }
                }
            }
        }
        // 默认纵坐标文字两边需要留白，所以增加一点
        Rect rect=getTextRect(verticalTextPaint,verticalMaxLengthString);
        point.x=(int) ((rect.right-rect.left)*2f+getPaddingLeft());
        return point;
    }
    public Rect getTextRect(Paint paint, String text){
        Rect bounds=new Rect();
        paint.getTextBounds(text,0,text.length(),bounds);
        return bounds;
    }
    public float getTextOffset(Paint paint, String text){
        Rect bounds=new Rect();
        paint.getTextBounds(text,0,text.length(),bounds);
        float offset=(bounds.top+bounds.bottom)/2;
        return offset;
    }

}
