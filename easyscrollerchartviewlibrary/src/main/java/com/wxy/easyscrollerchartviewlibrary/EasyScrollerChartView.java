package com.wxy.easyscrollerchartviewlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.wxy.easyscrollerchartviewlibrary.model.ScrollerPointModel;

import java.util.List;


public abstract class EasyScrollerChartView extends View {
    protected final String TAG = getClass().getName();
    //设置默认的宽和高,比例为4:3
    protected static final int DEFUALT_VIEW_WIDTH=1200;
    protected static final int DEFUALT_VIEW_HEIGHT=900;
    protected Paint horizontalLinePaint;
    protected Paint verticalLinePaint;
    protected TextPaint horizontalTextPaint ;
    protected TextPaint verticalTextPaint;
    protected TextPaint pointTextPaint;
    protected float verticalMin= 0;//纵坐标最小值
    protected float verticalMax= 0;//纵坐标最大值
    protected float horizontalMin= 0;//横坐标最小值
    protected float horizontalAverageWeight= 0;//横坐标每个平均区间代表多少值
    protected float horizontalRatio=0.2f;
    protected List<String> horizontalCoordinatesList;
    protected List<String> verticalCoordinatesList;
    protected List<? extends ScrollerPointModel> scrollerPointModelList;
    protected boolean isScoll;
    protected int mLastX = 0,mLastY=0;
    protected boolean isFling;
    protected int downX=0,downY=0;
    protected VelocityTracker mVelocityTracker;
    protected Scroller scroller;
    protected float horizontalAverageWidth;
    protected float verticalRegionLength=0;//纵坐标有限区间长度
    protected float verticalTextoffsetX;
    protected Point originalPoint;
    protected float saveInstanceStateScrollX;
    protected float scrollSideDamping=0.5f;
    private onClickListener onClickListener;
    private int mActivePointerId;
    private  final int INVALID_POINTER = -1;
    private int minX,maxX,minX_horizontalCoordinates,maxX_horizontalCoordinates;
    private int  mLastX_dispatch,mLastY_dispatch ;

    public EasyScrollerChartView(Context context) {
        super(context);
    }

    public EasyScrollerChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //横坐标轴的画笔
        horizontalLinePaint=new Paint();
        horizontalLinePaint.setAntiAlias(true);
        horizontalLinePaint.setColor(Color.BLACK);
        horizontalLinePaint.setStrokeWidth(2);

        //纵坐标轴的画笔
        verticalLinePaint=new Paint();
        verticalLinePaint.setAntiAlias(true);
        verticalLinePaint.setColor(Color.BLACK);
        verticalLinePaint.setStrokeWidth(2);

        //横坐标的刻度值画笔
        horizontalTextPaint= new TextPaint();
        horizontalTextPaint.setAntiAlias(true);
        horizontalTextPaint.setTextSize(40);
        horizontalTextPaint.setColor(Color.BLACK);

        //纵坐标的刻度值画笔
        verticalTextPaint= new TextPaint();
        horizontalTextPaint.setAntiAlias(true);
        verticalTextPaint.setTextAlign(Paint.Align.RIGHT);
        verticalTextPaint.setTextSize(40);
        verticalTextPaint.setColor(Color.BLACK);

        //每个点的值画笔
        pointTextPaint= new TextPaint();
        verticalLinePaint.setAntiAlias(true);
        pointTextPaint.setTextAlign(Paint.Align.CENTER);
        pointTextPaint.setTextSize(40);
        pointTextPaint.setColor(Color.BLACK);

        scroller=new Scroller(context);
    }

    public EasyScrollerChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putFloat("getScrollX", (float) getScrollX()/(getWidth()-getPaddingRight()-originalPoint.x));
        Log.v("onSaveInstanceState=",(float) getScrollX()/(float)getWidth()+"");
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            saveInstanceStateScrollX=bundle.getFloat("getScrollX");
            state = bundle.getParcelable("superState");
            super.onRestoreInstanceState(state);
        }

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
                        height=Math.min(width*3/4,getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
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
                        width=Math.min(height*4/3,getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec));

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
        if (verticalCoordinatesList==null){
            Log.v(TAG,"请初始化纵坐标刻度值，setVerticalCoordinatesList()");
            return;
        }
        if (verticalCoordinatesList.size()==0){
            Log.v(TAG,"VerticalCoordinatesList的size不能为0");
            return;
        }
        if (horizontalCoordinatesList==null){
            Log.v(TAG,"请初始化纵坐标刻度值，setHorizontalCoordinatesList");
            return;
        }
        if (horizontalCoordinatesList.size()==0){
            Log.v(TAG,"horizontalCoordinatesList的size不能为0");
            return;
        }
        if (scrollerPointModelList==null){
            Log.v(TAG,"请初始化所有坐标值，setScrollerPointModelList");
            return;
        }
        if (scrollerPointModelList.size()==0){
            Log.v(TAG,"scrollerPointModelList的size不能为0");
            return;
        }
        if (verticalMin==verticalMax){
            Log.v(TAG,"纵坐标区间最大最小值不能相等");
            return;
        }
        if (verticalMin>=verticalMax){
            Log.v(TAG,"纵坐标区间最大值必须大于最小值");
            return;
        }
        /**先计算原点的位置*/
        if (originalPoint==null){
        originalPoint=calculateOriginalPoint();
        horizontalAverageWidth=(getWidth()-getPaddingRight()-originalPoint.x)*horizontalRatio;
        verticalRegionLength=originalPoint.y-getPaddingTop()-((originalPoint.y-getPaddingTop())/verticalCoordinatesList.size());
        /**计算可滑动的数据边界，即只画可显示范围内左右各多一个点的内容，防止数据量过大导致卡顿*/
        }
        calculateSide(horizontalAverageWidth);
        /**画横坐标的线,如果可以滑动，默认横坐标的线要画满整个view的宽度，因为数据多，能表示出滑动还有数据，如果不可以滑动，则默认宽度不占满，并且留出一个横坐标平均区间的50%，看起来美观*/
        drawHorizontalLine(canvas);
        /**画纵坐标的线*/
        drawVerticalLine(canvas);
       /**画纵坐标的刻度值*/
        drawVerticalLineCoordinates(canvas,originalPoint);
        /**画横坐标的刻度值*/
        drawHorizontalLineCoordinates(canvas,originalPoint);
        /**画所有的点*/
        drawContent(canvas,originalPoint,scrollerPointModelList,minX,maxX,horizontalAverageWidth,horizontalAverageWeight,horizontalMin,verticalRegionLength,verticalMin,verticalMax,new Rect(originalPoint.x+getScrollX(),getPaddingTop(),getWidth()-getPaddingRight()+getScrollX(),getHeight()-getPaddingBottom()));
        if (saveInstanceStateScrollX!=0){
            scrollTo((int)(saveInstanceStateScrollX*(getWidth()-getPaddingRight()-originalPoint.x)),0);
            saveInstanceStateScrollX=0;
        }
    }
    //当点比较多的时候计算一下要画的点的边界值
    public void calculateSide(float horizontalAverageWidth) {
        //一开始已经花了一个点
        minX=(int) Math.ceil(getScrollX()/horizontalAverageWidth*horizontalAverageWeight)-1;
        minX_horizontalCoordinates=(int) Math.ceil(getScrollX()/horizontalAverageWidth)-1;
        //一开始没滑动前，画了1/getHorizontalRatio()+1个点，然后用getScrollX()/horizontalAverageWidth向上取整得到滑动过的区域有几个点，然后再画的时候补充进来
        maxX=(int) Math.ceil((1/getHorizontalRatio())*horizontalAverageWeight+1)+(int) Math.ceil(getScrollX()/horizontalAverageWidth*horizontalAverageWeight);
        maxX_horizontalCoordinates=(int) Math.ceil((1/getHorizontalRatio()+1))+(int) Math.ceil(getScrollX()/horizontalAverageWidth);
        //数据修正
        minX=minX>=0?minX:0;
        maxX=maxX<=scrollerPointModelList.size()?maxX:scrollerPointModelList.size();

        minX_horizontalCoordinates=minX_horizontalCoordinates>=0?minX_horizontalCoordinates:0;
        maxX_horizontalCoordinates=maxX_horizontalCoordinates<=horizontalCoordinatesList.size()?maxX_horizontalCoordinates:horizontalCoordinatesList.size();
    }
    //转化成相对当前坐标系的坐标
    public float calculateX(float x) {
        float calculateX=((x-horizontalMin)/horizontalAverageWeight*horizontalAverageWidth)+originalPoint.x;
        return calculateX;

    }
    //转化成相对当前坐标系的坐标
    public float calculateY(float y) {
        float calculateY=originalPoint.y-((y-verticalMin)/(verticalMax-verticalMin)* verticalRegionLength);
        return calculateY;

    }
    /**画横坐标的线*/
    protected void drawHorizontalLine(Canvas canvas) {
        if (isScoll){
            canvas.drawLine((float) originalPoint.x+getScrollX(),(float) originalPoint.y,(float) getWidth()-getPaddingRight()+getScrollX(),(float) originalPoint.y,horizontalLinePaint);
        }else {
            canvas.drawLine((float) originalPoint.x,(float) originalPoint.y,(float) (getWidth()-getPaddingRight()-((getWidth()-getPaddingRight()-originalPoint.x)/(horizontalCoordinatesList.size()+1)/4)),(float) originalPoint.y,horizontalLinePaint);
        }
    }

    /**画纵坐标的线*/
    protected void drawVerticalLine(Canvas canvas) {

        canvas.drawLine((float) originalPoint.x+getScrollX(),(float) originalPoint.y,(float) originalPoint.x+getScrollX(),(float) getPaddingTop()+((originalPoint.y-getPaddingTop())/verticalCoordinatesList.size())-verticalTextPaint.getTextSize()/2,verticalLinePaint);

    }

    public abstract void drawContent(Canvas canvas, Point originalPoint, List<? extends ScrollerPointModel> scrollerPointModelList,
                                     int minX, int maxX,
                                     float horizontalAverageWidth, float horizontalAverageWeight, float horizontalMin,
                                     float verticalRegionLength,float verticalMin,float verticalMax, Rect rect);


    /**画纵坐标的刻度值*/
    protected void drawVerticalLineCoordinates(Canvas canvas, Point point) {
                for (int i=0;i<verticalCoordinatesList.size();i++){
                    canvas.drawText(verticalCoordinatesList.get(i),point.x+getScrollX()-((int) verticalTextoffsetX),
                            point.y-(point.y-getPaddingTop())/verticalCoordinatesList.size()*i-getTextOffset(verticalTextPaint,verticalCoordinatesList.get(i)),
                            verticalTextPaint);
        }
    }


    /**画横坐标的刻度值*/

    protected void drawHorizontalLineCoordinates(Canvas canvas,Point point) {
        /** 默认横坐标文字两边需要留白，避免相邻的横坐标值挨在一起，所以在现有的宽度上减少一点*/
        if (!isScoll){
            /** 如果不可以滑动，横坐标平均区间由horizontalCoordinatesList.size()来决定*/
            horizontalRatio=1f/(float) (horizontalCoordinatesList.size()+1);
        }
         float HorizontalAverageTextwidth=(getWidth()-getPaddingRight()-point.x)*horizontalRatio*4/5;
        canvas.save();
        canvas.clipRect(new Rect(point.x+getScrollX(),getPaddingTop(),getWidth()-getPaddingRight()+getScrollX(),getHeight()-getPaddingBottom()));
        Rect horizontalRectOneText=getTextRect(horizontalTextPaint,horizontalCoordinatesList.get(0));
        for (int i=minX_horizontalCoordinates;i<maxX_horizontalCoordinates;i++){
                    StaticLayout sl = new StaticLayout(horizontalCoordinatesList.get(i),horizontalTextPaint,(int)HorizontalAverageTextwidth, Layout.Alignment.ALIGN_NORMAL,1.0f,0.0f,true);
                                        canvas.save();
                    canvas.translate((float) point.x+(getWidth()-getPaddingRight()-point.x)*horizontalRatio*(i+1)-(sl.getLineWidth(0)/2),(float) point.y+(horizontalRectOneText.bottom-horizontalRectOneText.top));
                    sl.draw(canvas);
                    canvas.translate(0,0);
                    canvas.restore();
        }
        canvas.restore();
    }

    /**先计算原点的位置*/
    private Point calculateOriginalPoint() {
        Point point=new Point();
        /**计算出原点需要向右偏移多少*/
        //拿到纵坐标长度最长的字符串
        String verticalMaxLengthString="";
        for (int i=0;i<verticalCoordinatesList.size();i++){
            if (verticalCoordinatesList.get(i).length()>verticalMaxLengthString.length()){
                verticalMaxLengthString=verticalCoordinatesList.get(i);
            }
        }
        // 默认纵坐标文字两边需要留白，所以增加一点,默认增加文字长度的一半
        Rect rect=getTextRect(verticalTextPaint,verticalMaxLengthString);
        verticalTextoffsetX=(rect.bottom-rect.top)*2;
        point.x=(int) ((rect.right-rect.left)+(rect.bottom-rect.top)*4+getPaddingLeft());
        /**计算出原点需要向上偏移多少*/
        if (!isScoll){
            /** 如果不可以滑动，横坐标平均区间由horizontalCoordinatesList.size()来决定*/
            horizontalRatio=1f/(float) (horizontalCoordinatesList.size()+1);
        }
        // 默认横坐标文字两边需要留白，避免相邻的横坐标值挨在一起，所以在现有的宽度上减少一点
        float HorizontalAverageTextwidth=(getWidth()-getPaddingRight()-point.x)*horizontalRatio*4/5;
        //拿到横坐标在换行后高度最大的高度
        point.y=0;
        for (int i=0;i<horizontalCoordinatesList.size();i++){
            StaticLayout sl = new StaticLayout(horizontalCoordinatesList.get(i),horizontalTextPaint,(int)HorizontalAverageTextwidth, Layout.Alignment.ALIGN_NORMAL,1.0f,0.0f,true);
            if (sl.getHeight()>point.y){
                point.y=sl.getHeight();
            }
        }
        //默认横坐标文字的上下需要留白，所以这个高度要加大,上下留出一个字符大小的距离
        Rect horizontalRectOneText=getTextRect(horizontalTextPaint,horizontalCoordinatesList.get(0));
        point.y=point.y+(horizontalRectOneText.bottom-horizontalRectOneText.top)*2;
        //矫正至Canvas所在的坐标
        point.y=getHeight()-getPaddingBottom()-point.y;

        //这里要修正纵坐标刻度值的textSize，因为防止数据过多之后产生上下重叠现象
        int verticalAverage=(point.y-getPaddingTop())/verticalCoordinatesList.size();
        if (verticalTextPaint.getTextSize()>=verticalAverage){
            verticalTextPaint.setTextSize(verticalAverage*3/4);
        }

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

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int x=(int)event.getX();
        int y=(int)event.getY();
        switch (event.getAction()){
             case MotionEvent.ACTION_DOWN:
                 getParent().requestDisallowInterceptTouchEvent(true);
                 break;
            case  MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX_dispatch;
                int deltaY = y - mLastY_dispatch;
                if(Math.abs(deltaX)<Math.abs(deltaY)){
                    //父控件拦截
                    Log.v("haha=","上下");
                    getParent().requestDisallowInterceptTouchEvent(false);
                }else{
                    Log.v("haha=","左右");
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                 break;
            default:
                break;
        }
        mLastX_dispatch = x;
        mLastY_dispatch = y;
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (isScoll){
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        }
        switch (event.getAction()& MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId=event.getPointerId(0);
                downX=(int) event.getX();
                downY=(int) event.getY();

                if (!scroller.isFinished()) {
                    isFling=false;
                    scroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //如果有新的手指按下，就直接把它当作当前活跃的指针
                final int index = event.getActionIndex();
                mActivePointerId = event.getPointerId(index);
                //并且刷新上一次记录的旧坐标值
                downX=(int) event.getX(index);
                downY=(int) event.getY(index);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(event);
                break;
            case MotionEvent.ACTION_MOVE:
                if (isScoll) {
                    int activePointerIndex = event.findPointerIndex(mActivePointerId);
                    if (activePointerIndex == INVALID_POINTER) {
                        break;
                    }
                    int dx = (int) event.getX(activePointerIndex) - mLastX;
                    int scrollX = (int) event.getX(activePointerIndex) - downX;
                    int scrollY = (int) event.getY(activePointerIndex) - downY;
                    if (getScrollX() < 0 || getScrollX() >= (scrollerPointModelList.size() * horizontalAverageWidth - ((getWidth() - getPaddingRight() - originalPoint.x)))) {
                        dx =(int) (dx * scrollSideDamping);
                    }
                    scrollBy(-dx, 0);
                    invalidate();
//                    if (Math.abs(scrollX) < Math.abs(scrollY)) {
//                        if (getScrollX() < 0) {
//                            scroller.startScroll(getScrollX(), 0, -getScrollX(), 0, 800);
//                            invalidate();
//                        } else if (getScrollX() >= (scrollerPointModelList.size() * horizontalAverageWidth - ((getWidth() - getPaddingRight() - originalPoint.x)))) {
//                            scroller.startScroll(getScrollX(), 0, (int) (scrollerPointModelList.size() * horizontalAverageWidth - (getWidth() - getPaddingRight() - originalPoint.x) - getScrollX()), 0, 800);
//                            invalidate();
//                        }
//                        getParent().requestDisallowInterceptTouchEvent(false);
//                        return  false;
//                    } else {
//                        getParent().requestDisallowInterceptTouchEvent(true);
//                        if (getScrollX() < 0 || getScrollX() >= (scrollerPointModelList.size() * horizontalAverageWidth - ((getWidth() - getPaddingRight() - originalPoint.x)))) {
//                            dx =(int) (dx * scrollSideDamping);
//                        }
//                        scrollBy(-dx, 0);
//                        invalidate();
//                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (getScrollX() < 0) {
                    scroller.startScroll(getScrollX(), 0, -getScrollX(), 0, 800);
                    invalidate();
                } else if (getScrollX() >= (scrollerPointModelList.size() * horizontalAverageWidth - ((getWidth() - getPaddingRight() - originalPoint.x)))) {
                    scroller.startScroll(getScrollX(), 0, (int) (scrollerPointModelList.size() * horizontalAverageWidth - (getWidth() - getPaddingRight() - originalPoint.x) - getScrollX()), 0, 800);
                    invalidate();
                }
            case MotionEvent.ACTION_UP:
                mActivePointerId = INVALID_POINTER;
                int clickX = downX - (int) event.getX();
                int clickY = downY - (int) event.getY();
                if (Math.abs(clickX)<= ViewConfiguration.get(getContext()).getScaledTouchSlop()&&
                        Math.abs(clickY)<= ViewConfiguration.get(getContext()).getScaledTouchSlop()){
                    if (onClickListener!=null){
                        onClickListener.onClick(((event.getX()+getScrollX())-originalPoint.x)/horizontalAverageWidth*horizontalAverageWeight+horizontalMin,
                                (originalPoint.y- event.getY())/verticalRegionLength*(verticalMax-verticalMin)+verticalMin);
                    }
                }else {
                if (isScoll){
                if (getScrollX()<0){
                    scroller.startScroll(getScrollX(),0,-getScrollX(),0,800);
                    invalidate();
                }else if (getScrollX()>=(scrollerPointModelList.size()*horizontalAverageWidth-((getWidth()-getPaddingRight()-originalPoint.x)))){
                    scroller.startScroll(getScrollX(),0,(int) (scrollerPointModelList.size()*horizontalAverageWidth-(getWidth()-getPaddingRight()-originalPoint.x)-getScrollX()),0,800);
                    invalidate();
               }else{
                    final int pointerId = event.getPointerId(0);
                    mVelocityTracker.computeCurrentVelocity(1000, ViewConfiguration.getMaximumFlingVelocity());
                    final int velocityX = (int)mVelocityTracker.getXVelocity(pointerId);
                    isFling=true;
                    scroller.fling(getScrollX(),0,-velocityX,0,-(getWidth()-getPaddingRight()-originalPoint.x),(int) (scrollerPointModelList.size()*horizontalAverageWidth)+(getWidth()-getPaddingRight()-originalPoint.x),0,0);
                    invalidate();
                    if (mVelocityTracker != null) {
                        mVelocityTracker.clear();
                        mVelocityTracker = null;
                    }
                }
                }
                }
                break;
        }
        if (mActivePointerId != INVALID_POINTER) {
            mLastX = (int) event.getX(event.findPointerIndex(mActivePointerId));
            mLastY=(int) event.getY(event.findPointerIndex(mActivePointerId));
        }else {
            mLastX = (int) event.getX();
        }

        return true;
    }
    private void onSecondaryPointerUp(MotionEvent ev) {
         int pointerIndex = ev.getActionIndex();
         int pointerId = ev.getPointerId(pointerIndex);
        //如果抬起的那根手指，刚好是当前活跃的手指，那么
        if (pointerId == mActivePointerId) {
            //另选一根手指，并把它标记为活跃（皇帝驾崩，太子登基）
             int newPointerIndex =  pointerIndex == 0 ? 1 : 0;
            mActivePointerId = ev.getPointerId(newPointerIndex);
            //把上一次记录的坐标，更新为新手指的当前坐标
            downX = (int) ev.getX(newPointerIndex);
            downY =(int)  ev.getY(newPointerIndex);
            if (mVelocityTracker != null) {
                mVelocityTracker.clear();
            }
        }
    }
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()){
            if (isFling){
                if (scroller.isFinished()){
                    if (getScrollX()<=0){
                        isFling=false;
                        scroller.startScroll(getScrollX(),0,-getScrollX(),0,800);
                        invalidate();
                    }
                    else if (getScrollX()>=(scrollerPointModelList.size()*horizontalAverageWidth-((getWidth()-getPaddingRight()-originalPoint.x)))){
                        isFling=false;
                        scroller.startScroll(getScrollX(),0,(int) ( (scrollerPointModelList.size()*horizontalAverageWidth-((getWidth()-getPaddingRight()-originalPoint.x)))-getScrollX()),0,800);
                        invalidate();
                    }
                }else {
                    if  (getScrollX()<=-(getWidth()-getPaddingRight()-originalPoint.x)/2){
                        scroller.abortAnimation();
                        isFling=false;
                        scroller.startScroll(getScrollX(),0,-getScrollX(),0,800);
                        invalidate();

                    }else if (getScrollX()>= (scrollerPointModelList.size()*horizontalAverageWidth-((getWidth()-getPaddingRight()-originalPoint.x))+((getWidth()-getPaddingRight()-originalPoint.x))/2)){
                        scroller.abortAnimation();
                        isFling=false;
                        scroller.startScroll(getScrollX(),0,(int) ( (scrollerPointModelList.size()*horizontalAverageWidth-((getWidth()-getPaddingRight()-originalPoint.x)))-getScrollX()),0,800);
                        invalidate();
                    }else {
                        scrollTo(scroller.getCurrX(), scroller.getCurrY());
                        invalidate();
                    }
                }
            }else {
                scrollTo(scroller.getCurrX(), scroller.getCurrY());
                invalidate();
            }

        }
    }
    public Paint getHorizontalLinePaint() {
        return horizontalLinePaint;
    }
    public Paint getVerticalLinePaint() {
        return verticalLinePaint;
    }
    public TextPaint getHorizontalTextPaint() {
        return horizontalTextPaint;
    }
    public TextPaint getVerticalTextPaint() {
        return verticalTextPaint;
    }

    public TextPaint getPointTextPaint() {
        return pointTextPaint;
    }
    public boolean isScoll() {
        return isScoll;
    }
    public void setScoll(boolean scoll) {
        isScoll = scoll;
    }
    //设置纵坐标最小值和最大值区间
    public void setVerticalMinAndMax(float verticalMin,float verticalMax) {
        this.verticalMin=verticalMin;
        this.verticalMax=verticalMax;
        invalidate();
    }
    //设置横坐标最小值以及每个平均区间所占值
    public void setHorizontalMinAndAverageWeight(float horizontalMin,float horizontalAverageWeight) {
        this.horizontalMin=horizontalMin;
        this.horizontalAverageWeight=horizontalAverageWeight;
        invalidate();
    }
    public float getHorizontalRatio() {
        return horizontalRatio;
    }

    public void setHorizontalRatio(float horizontalRatio) {
        this.horizontalRatio = horizontalRatio;
    }
    public void setHorizontalCoordinatesListScroll(List<String> horizontalCoordinatesList,float horizontalRatio) {
        this.horizontalCoordinatesList = horizontalCoordinatesList;
        this.isScoll=true;
        this.horizontalRatio=horizontalRatio;
        invalidate();
    }
    public void setHorizontalCoordinatesListNoScroll(List<String> horizontalCoordinatesList) {
        this.horizontalCoordinatesList = horizontalCoordinatesList;
        this.isScoll=false;
        invalidate();
    }
    public void setVerticalCoordinatesList(List<String> verticalCoordinatesList) {
        this.verticalCoordinatesList = verticalCoordinatesList;
        invalidate();
    }
    public List<? extends ScrollerPointModel> getScrollerPointModelList() {
        return scrollerPointModelList;
    }

    public void setScrollerPointModelList(List<? extends ScrollerPointModel> scrollerPointModelList) {
        this.scrollerPointModelList = scrollerPointModelList;
        invalidate();
    }

    public EasyScrollerChartView.onClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(EasyScrollerChartView.onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    public float getScrollSideDamping() {
        return scrollSideDamping;
    }

    public void setScrollSideDamping(float scrollSideDamping) {
        this.scrollSideDamping = scrollSideDamping;
    }
    public void reSet() {
       originalPoint=null;
    }
   public interface onClickListener{
       void onClick(float x,float y);
   }
}
