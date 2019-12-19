# EasyScrollerChartView
[![](https://jitpack.io/v/NoEndToLF/EasyScrollerChartView.svg)](https://jitpack.io/#NoEndToLF/EasyScrollerChartView)
**EasyScrollerChartView**：这是一个自定义绘制内容，可滑动、惯性滑动、边界回弹、可点击的坐标系图，使用简单，具备一个自定义View应有的基本素质
* **原理** ：先通过横坐标刻度值和纵坐标刻度值确定原点的位置，保证刻度值的绘制，再次基础上确定可绘制区域Rect，滑动使用Scroller。
* **功能** ：
   * 1、通过继承实现drawContent方法，使用参数绘制自己所需内容的自定义，其他功能皆由父类实现，如需修改可重载父类方法。
   * 2、可随手势拖动，越过左右边界自回弹，且在边界处滑动阻尼。
   * 3、提供点击事件，且将点击坐标处理换算（横坐标代表点击位置相对于所画内容的横坐标下标，纵坐标为相对于当前自定义坐标系的值），实际使用需自己再次处理。
   * 4、提供了刷新入口(Setting改变刷新)
   * 5、提供了重置坐标系入口(某些设置的改变需要重置坐标系，比如横纵坐标刻度值的TextSize)
* **基本素质** ：
   * 1、旋转屏幕状态不丢失，转之前啥样，回来还是啥样（处理好onSaveInstanceState和onRestoreInstanceState）
   * 2、暴露事件冲突接口，允许外界操作父控件的事件及该view自己的事件（因为这只是个View，没办法直接处理所有的滑动冲突场景）
   * 3、内存抖动要小，防止内存溢出。
-------------------
# 示例
## Demo演示了普通用法和涉及到SwipeRefreshLayout等类似的滑动冲突的用法（细节请看代码）。
* **1、普通使用** ：只有move的点左右滑动距离大于上下滑动距离才可以响应拖拽事件，否则通知父控件拦截。
* **2、下拉刷新及其他滑动冲突** ：
   * 1、拖拽事件：move的点左右滑动距离大于上下滑动距离，通过onPromiseParentTouchListener方法中使用SwipeRefreshLayout.setEnabled(promise)通知外界设置SwipeRefreshLayout不可以滑动。反之，通知父控件拦截。
   * 2、在SwipeRefreshLayout的OnRefreshListener中设置EasyScrollerChartView的setEnableTouch(false)方法通知刷新期间，EasyScrollerChartView不响应任何事件。


| 屏幕旋转      |可配置属性  |
| :--------:| :--------:| 
|![](https://github.com/NoEndToLF/EasyScrollerChartView/blob/master/DemoImage/demo1.gif)| ![](https://github.com/NoEndToLF/EasyScrollerChartView/blob/master/DemoImage/demo2.gif)| 
 <br />
 
|下拉刷新及其他滑动冲突  |

| :--------:|

| ![](https://github.com/NoEndToLF/EasyScrollerChartView/blob/master/DemoImage/demo3.gif)| 
