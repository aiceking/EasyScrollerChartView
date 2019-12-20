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
    * [继承EasyScrollerChartView](#新建自己的View类继承EasyScrollerChartView实现drawContent方法如有需要还可以按实际需求重写父类的其他方法)
    * [代码中修改Data和View属性](#代码中修改Data和View属性以可滑动的折线图为例Demo中ScrollActivity和SwipRefreshActivity)
* [反馈与建议](#反馈与建议)
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
### 新建自己的View类，继承EasyScrollerChartView，实现drawContent方法，如有需要，还可以按实际需求重写父类的其他方法
#### drawContent方法
|参数  | 作用  |
| :--------| :--: |
|canvas  | 绘制所需的canvas  |
|scrollerPointModelList  | 你传入的所有点  |
|drawPiontModelList  | scrollerPointModelList经过处理后的点， drawPiontModelList中只包含在当前滑动距离后，可绘制区域内需要绘制的点（即使有1万个点，也只画当前该显示的那几个点），且已换算至View坐标系，直接拿来绘制即可|
|realHorizontalAverageWidth  | 每个点横坐标直接的距离（方便用来画柱图之类的）  |
|verticalRegionLength  | 纵坐标最大值和最小值区间转换成当前View坐标系的距离  |
|Rect  | 可绘制的区域，横坐标之上，纵坐标之又，需要结合canvas.clipRect(rect)来灵活使用使用  |
#### 可重载的其他方法
|方法  | 作用  |
| :--------| :--: |
|drawHorizontalLine  | 画横坐标轴  |
|drawVerticalLine  | 画纵坐标轴  |
|drawVerticalLineCoordinates  | 画横坐标刻度值  |
|drawHorizontalLineCoordinates  | 画纵坐标刻度值  |
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
### 代码中修改Data和View属性，以可滑动的折线图为例，Demo中ScrollActivity和SwipRefreshActivity
#### 设置View所需数据和基本配置
```java
private void initScroll(Bundle savedInstanceState) {
 List<String> verticalCoordinatesList = new ArrayList<>();
        verticalCoordinatesList.add("5000");
        verticalCoordinatesList.add("10000");
        verticalCoordinatesList.add("15000");
        verticalCoordinatesList.add("20000");
        verticalCoordinatesList.add("25000");
        verticalCoordinatesList.add("30000");
        //纵坐标刻度值文字
        escView.setVerticalCoordinatesList(verticalCoordinatesList);
        List<String> horizontalCoordinatesList_Scoll = new ArrayList<>();
        for (int i = 0; i < 101; i++) {
            horizontalCoordinatesList_Scoll.add(i + "");
        }
        if (savedInstanceState != null) {
            myScrollerPointModelList = (ArrayList) savedInstanceState.getParcelableArrayList("myScrollerPointModelList");
        }
        //所有的点
        if (myScrollerPointModelList == null) {
            myScrollerPointModelList = new ArrayList<>();
            for (int i = 0; i < 101; i++) {
                ScrollerPointModel myScrollerPointModel = new ScrollerPointModel( ((int) (Math.random() * 5 + 1)) * 5000);
                myScrollerPointModelList.add(myScrollerPointModel);
            }
        }
        escView.setScrollerPointModelList(myScrollerPointModelList);
        //纵坐标最大和最小值
        escView.setVerticalMinAndMax(5000, 30000);
        //横坐标的起始值和每一个刻度区间画几个点
        escView.setHorizontalMinAndAverageWeight(0, 1);
        //横坐标的刻度值文字和每一个刻度区间占可画区域内宽度的多少，比如这里是5分之1
        escView.setHorizontalCoordinatesListScroll(horizontalCoordinatesList_Scoll, 0.2f);
        //滑动到左右边界的阻尼系数
        escView.setScrollSideDamping(0.5f);
        escView.setOnClickListener(new EasyScrollerChartView.onClickListener() {
            @Override
            public void onClick(float x, float y) {
                Toast.makeText(SwipRefreshActivity.this, x + "=" + y, Toast.LENGTH_SHORT).show();
            }
        });
	//设置滑动期间不允许SwipeRefreshLayout拦截事件
        escView.setOnPromiseParentTouchListener(new EasyScrollerChartView.onPromiseParentTouchListener() {
            @Override
            public void onPromiseTouch(boolean promise) {
                    swipe.setEnabled(promise);
            }
        });
	}
``` 
#### 设置下来刷新期间，不允许view消费事件
```java
private void initSwipRefresh() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                escView.setEnableTouch(false);
                //模拟耗时
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            myScrollerPointModelList.clear();
                            for (int i = 0; i < 101; i++) {
                                ScrollerPointModel myScrollerPointModel = new ScrollerPointModel( ((int) (Math.random() * 5 + 1)) * 5000);
                                myScrollerPointModelList.add(myScrollerPointModel);
                            }
                        escView.notifyDataChanged();
                        swipe.setRefreshing(false);
                        escView.setEnableTouch(true);
                    }
                },2000);
            }
        });
    }
```
#### 其他属性的修改如下
```java
private void initSeekBar() {
        //修改横坐标刻度值文字的大小
        seekbarHorizontal.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int size = (int) ((float) progress / 100 * 100);
                tvHorizontalSize.setText("HorizontalTextSize：" + size);
                escView.getHorizontalTextPaint().setTextSize(size);
                //重置坐标系
                escView.reSetCoordinates();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
	//修改纵坐标刻度值文字的大小
        seekbarVertical.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int size = (int) ((float) progress / 100 * 100);
                tvVerticalSize.setText("VertialTextSize：" + size);
                escView.getVerticalTextPaint().setTextSize(size);
                //重置坐标系
                escView.reSetCoordinates();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbarScrollSideDamping.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                escView.setScrollSideDamping((float) progress / 100);
                tvScrollSideDamping.setText("滑动到边界阻尼系数：" + ((float) progress / 100));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbarHorizontalRatio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                escView.setHorizontalRatio((float) progress / 100);
                escView.reSetCoordinates();
                tvHorizontalRatio.setText("横坐标区间占可画区域比例：" + (float) progress / 100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
	//设置每个横坐标区间画多少个点
        seekbarHorizontalAverageWeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                escView.setHorizontalMinAndAverageWeight(escView.getHorizontalMin(), ((float) progress / 10));
                escView.reSetCoordinates();
                tvHorizontalAverageWeight.setText("每个横坐标区间画多少个点：" + ((float) progress / 10));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
	//设置横坐标从第几个横坐标点画起
        seekbarHorizontalStart.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                escView.setHorizontalMinAndAverageWeight((int)((float) progress / 10),escView.getHorizontalAverageWeight());
                escView.reSetCoordinates();
                tvHorizontalStart.setText("横坐标从第几个画起：" + (int)((float) progress / 10));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
```

```java
 @OnClick({R.id.switch_horizontal_line, R.id.switch_vertical_line, R.id.btn_horizontal_text_color, R.id.btn_vertical_text_color, R.id.btn_horizontal_line_color, R.id.btn_vertical_line_color})
    public void onViewClicked(View view) {
        switch (view.getId()) {
	    //是否画横坐标轴
            case R.id.switch_horizontal_line:
                if (switchHorizontalLine.isOpened()) {
                    tvHorizontalLine.setText("是否画横坐标轴：是");
                    escView.setDrawHorizontalLine(true);
                } else {
                    tvHorizontalLine.setText("是否画横坐标轴：否");
                    escView.setDrawHorizontalLine(false);
                }
                break;
		//是否画纵坐标轴
            case R.id.switch_vertical_line:
                if (switchVerticalLine.isOpened()) {
                    escView.setDrawVerticalLine(true);
                    tvVerticalLine.setText("是否画纵坐标轴：是");
                } else {
                    escView.setDrawVerticalLine(false);
                    tvVerticalLine.setText("是否画纵坐标轴：否");
                }
                break;
		//修改横坐标刻度值文字颜色
            case R.id.btn_horizontal_text_color:
                new ColorPickerPopup.Builder(this)
                        .initialColor(Color.RED) // Set initial color
                        .enableBrightness(true) // Enable brightness slider or not
                        .enableAlpha(true) // Enable alpha slider or not
                        .okTitle("确定")
                        .cancelTitle("取消")
                        .showIndicator(true)
                        .showValue(true)
                        .build()
                        .show(btnHorizontalTextColor, new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void onColorPicked(int color) {
                                escView.getHorizontalTextPaint().setColor(color);
                                escView.notifySettingChanged();
                            }
                        });
                break;
		//修改纵坐标刻度值文字颜色
            case R.id.btn_vertical_text_color:
                new ColorPickerPopup.Builder(this)
                        .initialColor(Color.RED) // Set initial color
                        .enableBrightness(true) // Enable brightness slider or not
                        .enableAlpha(true) // Enable alpha slider or not
                        .okTitle("确定")
                        .cancelTitle("取消")
                        .showIndicator(true)
                        .showValue(true)
                        .build()
                        .show(btnVerticalTextColor, new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void onColorPicked(int color) {
                                escView.getVerticalTextPaint().setColor(color);
                                escView.notifySettingChanged();
                            }
                        });
                break;
		//修改横坐标轴颜色
            case R.id.btn_horizontal_line_color:
                new ColorPickerPopup.Builder(this)
                        .initialColor(Color.RED) // Set initial color
                        .enableBrightness(true) // Enable brightness slider or not
                        .enableAlpha(true) // Enable alpha slider or not
                        .okTitle("确定")
                        .cancelTitle("取消")
                        .showIndicator(true)
                        .showValue(true)
                        .build()
                        .show(btnHorizontalLineColor, new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void onColorPicked(int color) {
                                escView.getHorizontalLinePaint().setColor(color);
                                escView.notifySettingChanged();
                            }
                        });
                break;
		//修改纵坐标轴颜色
            case R.id.btn_vertical_line_color:
                new ColorPickerPopup.Builder(this)
                        .initialColor(Color.RED) // Set initial color
                        .enableBrightness(true) // Enable brightness slider or not
                        .enableAlpha(true) // Enable alpha slider or not
                        .okTitle("确定")
                        .cancelTitle("取消")
                        .showIndicator(true)
                        .showValue(true)
                        .build()
                        .show(btnVerticalLineColor, new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void onColorPicked(int color) {
                                escView.getVerticalLinePaint().setColor(color);
                                escView.notifySettingChanged();
                            }
                        });
                break;
        }
    }
```
# 反馈与建议
- 邮箱：<871601607@qq.com>

# License
```
Copyright (c) [2018] [static]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
---------
