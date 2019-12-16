package com.aice.easyscrollerchartview;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aice.easyscrollerchartview.view.MyScrollerChartView;
import com.wxy.easyscrollerchartview.EasyScrollerChartView;
import com.wxy.easyscrollerchartview.model.ScrollerPointModel;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.ielse.view.SwitchView;
import top.defaults.colorpicker.ColorPickerPopup;

public class ScrollActivity extends AppCompatActivity {

    @BindView(R.id.esc_view)
    MyScrollerChartView escView;
    @BindView(R.id.switch_horizontal_line)
    SwitchView switchHorizontalLine;
    @BindView(R.id.tv_horizontal_line)
    TextView tvHorizontalLine;
    @BindView(R.id.switch_vertical_line)
    SwitchView switchVerticalLine;
    @BindView(R.id.tv_vertical_line)
    TextView tvVerticalLine;
    @BindView(R.id.seekbar_horizontal)
    AppCompatSeekBar seekbarHorizontal;
    @BindView(R.id.tv_horizontal_size)
    TextView tvHorizontalSize;
    @BindView(R.id.seekbar_vertical)
    AppCompatSeekBar seekbarVertical;
    @BindView(R.id.tv_vertical_size)
    TextView tvVerticalSize;
    @BindView(R.id.seekbar_scrollSideDamping)
    AppCompatSeekBar seekbarScrollSideDamping;
    @BindView(R.id.tv_scrollSideDamping)
    TextView tvScrollSideDamping;
    @BindView(R.id.seekbar_horizontalRatio)
    AppCompatSeekBar seekbarHorizontalRatio;
    @BindView(R.id.tv_horizontalRatio)
    TextView tvHorizontalRatio;
    @BindView(R.id.seekbar_horizontalAverageWeight)
    AppCompatSeekBar seekbarHorizontalAverageWeight;
    @BindView(R.id.tv_horizontalAverageWeight)
    TextView tvHorizontalAverageWeight;
    @BindView(R.id.btn_horizontal_text_color)
    Button btnHorizontalTextColor;
    @BindView(R.id.btn_vertical_text_color)
    Button btnVerticalTextColor;
    @BindView(R.id.btn_horizontal_line_color)
    Button btnHorizontalLineColor;
    @BindView(R.id.btn_vertical_line_color)
    Button btnVerticalLineColor;
    private List<ScrollerPointModel> myScrollerPointModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        ButterKnife.bind(this);
        initScroll(savedInstanceState);
        initSeekBar();

    }

    private void initSeekBar() {

        seekbarHorizontal.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int size = (int) ((float) progress / 100 * 100);
                tvHorizontalSize.setText("HorizontalTextSize："+size);
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
        seekbarVertical.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int size = (int) ((float) progress / 100 * 100);
                tvVerticalSize.setText("VertialTextSize："+size);
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
                tvScrollSideDamping.setText("滑动到边界阻尼系数："+((float) progress / 100));
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
                tvHorizontalRatio.setText("横坐标区间占可画区域比例："+(float) progress / 100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbarHorizontalAverageWeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                escView.setHorizontalMinAndAverageWeight(0,((float) progress / 10));
                escView.reSetCoordinates();
                tvHorizontalAverageWeight.setText("每个横坐标区间画多少个点："+((float) progress / 10));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

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
                ScrollerPointModel myScrollerPointModel = new ScrollerPointModel(i, ((int) (Math.random() * 5 + 1)) * 5000);
                myScrollerPointModelList.add(myScrollerPointModel);
            }
        }
        escView.setScrollerPointModelList(myScrollerPointModelList);
        //纵坐标最大和最小值
        escView.setVerticalMinAndMax(5000, 30000);
        //横坐标的起始值和每一个刻度区间画几个点
        escView.setHorizontalMinAndAverageWeight(0, 1);
        //横坐标的刻度值文字和每一个刻度区间占画区域内的多少，比如这里是5分之1
        escView.setHorizontalCoordinatesListScroll(horizontalCoordinatesList_Scoll, 0.2f);
        //滑动到左右边界的阻尼系数
        escView.setScrollSideDamping(0.5f);
        escView.setOnClickListener(new EasyScrollerChartView.onClickListener() {
            @Override
            public void onClick(float x, float y) {
                Toast.makeText(ScrollActivity.this, x+"="+y, Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("myScrollerPointModelList", (ArrayList<? extends Parcelable>) myScrollerPointModelList);
        super.onSaveInstanceState(outState);
    }

    @OnClick({ R.id.switch_horizontal_line, R.id.switch_vertical_line, R.id.btn_horizontal_text_color, R.id.btn_vertical_text_color, R.id.btn_horizontal_line_color, R.id.btn_vertical_line_color})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.switch_horizontal_line:
                if (switchHorizontalLine.isOpened()){
                    tvHorizontalLine.setText("是否画横坐标轴：是");
                    escView.setDrawHorizontalLine(true);
                }else {
                    tvHorizontalLine.setText("是否画横坐标轴：否");
                    escView.setDrawHorizontalLine(false);
                }
                break;
            case R.id.switch_vertical_line:
                if (switchVerticalLine.isOpened()){
                    escView.setDrawVerticalLine(true);
                    tvVerticalLine.setText("是否画纵坐标轴：是");
                }else {
                    escView.setDrawVerticalLine(false);
                    tvVerticalLine.setText("是否画纵坐标轴：否");
                }
                break;
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
}
