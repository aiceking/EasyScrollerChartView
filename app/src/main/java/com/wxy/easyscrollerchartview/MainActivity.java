package com.wxy.easyscrollerchartview;


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
//        verticalCoordinatesList.add("35000");
//        verticalCoordinatesList.add("40000");
//        verticalCoordinatesList.add("45000");
//        verticalCoordinatesList.add("50000");
//        verticalCoordinatesList.add("55000");
//        verticalCoordinatesList.add("60000");
//        verticalCoordinatesList.add("65000");
//        verticalCoordinatesList.add("70000");
//        verticalCoordinatesList.add("75000");
//        verticalCoordinatesList.add("80000");
        List<String> horizontalCoordinatesList=new ArrayList<>();
        horizontalCoordinatesList.add("1月");
        horizontalCoordinatesList.add("2月");
        horizontalCoordinatesList.add("3月");
        horizontalCoordinatesList.add("4月");
        horizontalCoordinatesList.add("5月");
        horizontalCoordinatesList.add("6月");
        horizontalCoordinatesList.add("7月");
        horizontalCoordinatesList.add("8月");
        horizontalCoordinatesList.add("9月");
        horizontalCoordinatesList.add("10月");
        horizontalCoordinatesList.add("11月");
        horizontalCoordinatesList.add("12月");
        escView.setHorizontalCoordinatesList(horizontalCoordinatesList);
        escView.setVerticalCoordinatesList(verticalCoordinatesList);
    }
}
