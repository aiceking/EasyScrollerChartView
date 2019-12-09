package com.aice.easyscrollerchartview;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.aice.easyscrollerchartview.model.MyScrollerPointModel;
import com.aice.easyscrollerchartview.view.MyScrollerChartView;
import com.wxy.easyscrollerchartview.EasyScrollerChartView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.esc_view)
    MyScrollerChartView escView;
    private List<MyScrollerPointModel> myScrollerPointModelList;
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
        List<String> horizontalCoordinatesList_Scoll = new ArrayList<>();
        for (int i = 0; i < 366; i++) {
            if (i < 365) {
                horizontalCoordinatesList_Scoll.add("第\n " + (i + 1) + "\n天");
            }
        }
        if (savedInstanceState!=null) {
            Log.v("heihei=","onCreate");

            myScrollerPointModelList = (ArrayList) savedInstanceState.getParcelableArrayList("myScrollerPointModelList");
        }
        if (myScrollerPointModelList==null){
        myScrollerPointModelList=new ArrayList<>();
        for (int i=0;i<366;i++) {
            if (i < 365) {
                MyScrollerPointModel myScrollerPointModel = new MyScrollerPointModel(i, ((int) (Math.random() * 5 + 1)) * 5000, "test" + (i + 1));
                myScrollerPointModelList.add(myScrollerPointModel);
            }
        }
        }
        escView.setScrollerPointModelList(myScrollerPointModelList);
        escView.setVerticalMinAndMax(5000,30000);
        escView.setHorizontalMinAndAverageWeight(0,1);
        escView.setHorizontalCoordinatesListScroll(horizontalCoordinatesList_Scoll,0.2f);
//        escView.setHorizontalCoordinatesListNoScroll(horizontalCoordinatesList_Scoll);
        escView.setScrollSideDamping(0.5f);
        escView.setOnClickListener(new EasyScrollerChartView.onClickListener() {
            @Override
            public void onClick(float x, float y) {
                Log.v("x==",x+"");
                Log.v("y==",y+"");

            }
        });
//        List<String> horizontalCoordinatesList_noScoll=new ArrayList<>();
//        horizontalCoordinatesList_noScoll.add("1月");
//        horizontalCoordinatesList_noScoll.add("2月");
//        horizontalCoordinatesList_noScoll.add("3月");
//        horizontalCoordinatesList_noScoll.add("4月");
//        horizontalCoordinatesList_noScoll.add("5月");
//        escView.setHorizontalCoordinatesListNoScroll(horizontalCoordinatesList_noScoll);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.v("heihei=","onSaveInstanceState");
        outState.putParcelableArrayList("myScrollerPointModelList", (ArrayList<? extends Parcelable>) myScrollerPointModelList);
        super.onSaveInstanceState(outState);
    }

}
