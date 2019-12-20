# EasyScrollerChartView
[![](https://jitpack.io/v/NoEndToLF/EasyScrollerChartView.svg)](https://jitpack.io/#NoEndToLF/EasyScrollerChartView)
**EasyScrollerChartView**：这是一个自定义绘制内容，可滑动、惯性滑动、边界回弹、可点击的坐标系图，使用简单，具备一个自定义View应有的基本素质（适用于横坐标区间平均，只关注纵坐标的场景）
* **原理** ：先通过横坐标刻度值和纵坐标刻度值确定原点的位置，保证刻度值的绘制，再次基础上确定可绘制区域Rect，滑动使用Scroller。
* **功能** ：
   * 1、通过继承EasyScrollerChartView实现drawContent方法，使用参数绘制自己所需的内容（柱、线、点随你画，参数齐全），其他功能皆由父类实现，如需修改可重载父类方法。
   * 2、可随手势拖动，越过左右边界自回弹，且在边界处滑动阻尼。
   * 3、提供点击事件，且将点击坐标处理换算（横坐标代表点击位置相对于所画内容的横坐标下标，纵坐标为相对于当前自定义坐标系的值），实际使用需自己再次处理。
   * 4、横纵坐标刻度值可以是任意文本，绘制内容的坐标（横坐标按顺序绘制，纵坐标按设置的verticalMin和verticalMax决定）
   * 5、提供了刷新入口(Setting改变刷新)
   * 6、提供了重置坐标系入口(某些设置的改变需要重置坐标系，比如横纵坐标刻度值的TextSize)
* **基本素质** ：
   * 1、旋转屏幕状态不丢失，转之前啥样，回来还是啥样（处理好onSaveInstanceState和onRestoreInstanceState）
   * 2、暴露事件冲突接口，允许外界操作父控件的事件及该view自己的事件（因为这只是个View，没办法直接处理所有的滑动冲突场景）
   * 3、永远只画显示范围内左右+1个坐标点，内存抖动要小，防止内存溢出。
-------------------
# 示例（只是画了一个简单的折线图）
## Demo演示了普通用法和涉及到SwipeRefreshLayout等类似的滑动冲突的用法（细节请看代码）。
* **1、普通使用** ：只有move的点左右滑动距离大于上下滑动距离才可以响应拖拽事件，否则通知父控件拦截。
* **2、下拉刷新及其他滑动冲突** ：
   * 1、拖拽事件：move的点左右滑动距离大于上下滑动距离，通过onPromiseParentTouchListener方法中使用SwipeRefreshLayout.setEnabled(promise)通知外界设置SwipeRefreshLayout不可以滑动。反之，通知父控件拦截。
   * 2、在SwipeRefreshLayout的OnRefreshListener中设置EasyScrollerChartView的setEnableTouch(false)方法通知刷新期间，EasyScrollerChartView不响应任何事件。


| 屏幕旋转      |可配置属性  |
| :--------:| :--------:| 
|![](https://github.com/NoEndToLF/EasyScrollerChartView/blob/master/DemoImage/demo1.gif)| ![](https://github.com/NoEndToLF/EasyScrollerChartView/blob/master/DemoImage/demo2.gif)| 
 <br />
 
下拉刷新及其他滑动冲突  |
 :--------:|
 ![](https://github.com/NoEndToLF/EasyScrollerChartView/blob/master/DemoImage/demo3.gif)| 
 
 # 开始使用  
* [基本API](#基本API)
* [使用](#使用)
    * [引入](#引入)
    * [继承EasyScrollerChartView](#布局XML中添加与系统View使用方式一样宽高如果只确定其一另一个根据parent的宽高和map的比例取最小值确定最终map的宽度和高度由padding决定)
    * [代码中修改Data和View属性](#代码中通过ChinaMapView的getChinaMapModel方法拿到ChinaMapModel通过修改ChinaMapModel的属性来刷新ChinaMapView的显示效果其他的缩放倍数和接口通过ChinaMapView直接设置Demo中的SwipRefreshAppbarActivity和NormalActivity中有详细使用代码)

# 基本API

### Data实例类ScrollerPointModel
|属性  | 类型  |作用  |
| :--------| :--------|:--: |
| y| float|点的纵坐标值|


### EasyScrollerChartView
#### 可滑动的EasyScrollerChartView，关注以下属性
|方法  |参数或返回值  | 作用  |
| :--------| :--------| :--: |
|setEnableTouch  |boolean  | 设置是否可以消费事件（默认为true）  |
|setVerticalCoordinatesList  |List<String>  | 设置纵坐标的刻度值文字  |
|setVerticalMinAndMax  |float verticalMin,float verticalMax   | 设置纵坐标的区间（最大最小值）  |
|setHorizontalMinAndAverageWeight  |float horizontalMin,float horizontalAverageWeight   | 设置横坐标从第几个坐标开始画，设置每个横坐标区间画多少个点）  |
|setHorizontalCoordinatesListScroll  |List<String> horizontalCoordinatesList,float horizontalRatio  | 设置横坐标的刻度值文字，设置每个横坐标区间占坐标系内可画区域宽度的比例  |  
|setScrollerPointModelList  |List<ScrollerPointModel>  | 设置可滑动的需要绘制的坐标点  |
|setScrollSideDamping  |float scrollSideDamping  | 设置滑动到左右边界的阻尼系数  |  
|setOnClickListener  |EasyScrollerChartView.onClickListener  | 设置点击事件，返回点击位置的横纵坐标  |  
|setOnPromiseParentTouchListener  |ChinaMapView.onPromiseParentTouchListener  | 通知外界是否允许EasyScrollerChartView之上的view拦截事件 |
|setDrawHorizontalLine  |boolean  | 设置是否画横坐标轴  |  
|setDrawVerticalLine  |boolean  | 设置是否画纵坐标轴  |  
|getHorizontalTextPaint  |返回Paint  | 横坐标刻度值文字的画笔 |  
|getVerticalTextPaint  |返回Paint  | 纵坐标刻度值文字的画笔 |  
|getHorizontalLinePaint  |返回Paint  | 横坐标轴的画笔 |  
|getVerticalLinePaint  |返回Paint  | 纵坐标轴的画笔 | 
|notifySettingChanged  |void  | 刷新View（颜色类修改，未改变坐标系原点位置） |  
|notifyDataChanged  |void  | 刷新View（Data改变，或者其他引起坐标系原点位置变化的，需要重置坐标系） |  

#### 不可滑动的EasyScrollerChartView，关注以下属性

|方法  |参数或返回值  | 作用  |
| :--------| :--------| :--: |
|setEnableTouch  |boolean  | 设置是否可以消费事件（默认为true）  |
|setVerticalCoordinatesList  |List<String>  | 设置纵坐标的刻度值文字  |
|setVerticalMinAndMax  |float verticalMin,float verticalMax   | 设置纵坐标的区间（最大最小值）  |
|setHorizontalMinAndAverageWeight  |float horizontalMin,float horizontalAverageWeight   | 设置横坐标从第几个坐标开始画，设置每个横坐标区间画多少个点）  |
|setHorizontalCoordinatesListNoScroll  |List<String> horizontalCoordinatesList  | 设置横坐标的刻度值文字，设置每个横坐标区间占坐标系内可画区域宽度的比例  |  
|setScrollerPointModelList  |List<ScrollerPointModel>  | 设置需要绘制的坐标点  | 
|setOnClickListener  |EasyScrollerChartView.onClickListener  | 设置点击事件，返回点击位置的横纵坐标  |  
|setDrawHorizontalLine  |boolean  | 设置是否画横坐标轴  |  
|setDrawVerticalLine  |boolean  | 设置是否画纵坐标轴  |  
|getHorizontalTextPaint  |返回Paint  | 横坐标刻度值文字的画笔 |  
|getVerticalTextPaint  |返回Paint  | 纵坐标刻度值文字的画笔 |  
|getHorizontalLinePaint  |返回Paint  | 横坐标轴的画笔 |  
|getVerticalLinePaint  |返回Paint  | 纵坐标轴的画笔 | 
|notifySettingChanged  |void  | 刷新View（颜色类修改，未改变坐标系原点位置） |  
|notifyDataChanged  |void  | 刷新View（Data改变，或者其他引起坐标系原点位置变化的，需要重置坐标系） |  

# 使用
### 引入
Step 1. Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.NoEndToLF:EasyScrollerChartView:1.0.2'
	}
### 新建自己的View类，继承EasyScrollerChartView，实现drawContent方法

|参数  | 作用  |
| :--------| :--: |
|canvas  | 绘制所需的canvas  |
|scrollerPointModelList  | 你传入的所有点  |
|drawPiontModelList  | scrollerPointModelList经过处理后的点， drawPiontModelList中只包含在当前滑动距离后，可绘制区域内需要绘制的点（即使有1万个点，也只画当前该显示的那几个点），且已换算至View坐标系，直接拿来绘制即可|
|realHorizontalAverageWidth  | 每个点横坐标直接的距离（方便用来画柱图之类的）  |
|verticalRegionLength  | 纵坐标最大值和最小值区间转换成当前View坐标系的距离  |
|Rect  | 可绘制的区域，横坐标之上，纵坐标之又，需要结合canvas.clipRect(rect)来灵活使用使用  |
### Demo中只是画了简单的折线图，的例子如下
```java
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
```
### xml中添加
```xml
<com.aice.easyscrollerchartview.view.MyScrollerChartView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:id="@+id/esc_view">
            </com.aice.easyscrollerchartview.view.MyScrollerChartView>
``` 
### 代码中修改Data和View属性
