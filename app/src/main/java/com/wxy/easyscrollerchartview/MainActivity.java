package com.wxy.easyscrollerchartview;


import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wxy.easyscrollerchartviewlibrary.EasyScrollerChartView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.esc_view)
    EasyScrollerChartView escView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        List<String> verticalCoordinatesList=new ArrayList<>();
        verticalCoordinatesList.add("5000");
        verticalCoordinatesList.add("10000");
        verticalCoordinatesList.add("15000");
        verticalCoordinatesList.add("20000");
        verticalCoordinatesList.add("25000");
        verticalCoordinatesList.add("30000");
        escView.setVerticalCoordinatesList(verticalCoordinatesList);
        List<MyScrollerPointModel> myScrollerPointModelList=new ArrayList<>();
        for (int i=0;i<13;i++){
            MyScrollerPointModel myScrollerPointModel=new MyScrollerPointModel(i,((int) (Math.random() * 5 + 1))*5000);
            myScrollerPointModelList.add(myScrollerPointModel);
        }
        escView.setScrollerPointModelList(myScrollerPointModelList);
        escView.setVerticalMinAndMax(5000,30000);
        escView.setHorizontalMinAndAverageWeight(0,1);
        List<String> horizontalCoordinatesList_Scoll=new ArrayList<>();
        horizontalCoordinatesList_Scoll.add("1月");
        horizontalCoordinatesList_Scoll.add("2月");
        horizontalCoordinatesList_Scoll.add("3月");
        horizontalCoordinatesList_Scoll.add("4月");
        horizontalCoordinatesList_Scoll.add("5月");
        horizontalCoordinatesList_Scoll.add("6月");
        horizontalCoordinatesList_Scoll.add("7月");
        horizontalCoordinatesList_Scoll.add("8月");
        horizontalCoordinatesList_Scoll.add("9月");
        horizontalCoordinatesList_Scoll.add("10月");
        horizontalCoordinatesList_Scoll.add("11月");
        horizontalCoordinatesList_Scoll.add("12月");
        escView.setHorizontalCoordinatesListScroll(horizontalCoordinatesList_Scoll,0.2f);
//        List<String> horizontalCoordinatesList_noScoll=new ArrayList<>();
//        horizontalCoordinatesList_noScoll.add("1月");
//        horizontalCoordinatesList_noScoll.add("2月");
//        horizontalCoordinatesList_noScoll.add("3月");
//        horizontalCoordinatesList_noScoll.add("4月");
//        horizontalCoordinatesList_noScoll.add("5月");
//        escView.setHorizontalCoordinatesListNoScroll(horizontalCoordinatesList_noScoll);
    }
}
