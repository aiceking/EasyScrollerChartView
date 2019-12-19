package com.aice.easyscrollerchartview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_scroll)
    Button btnScroll;
    @BindView(R.id.btn_noscroll)
    Button btnNoscroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_scroll, R.id.btn_noscroll,R.id.btn_refresh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_scroll:
                startActivity(new Intent(this,ScrollActivity.class));
                break;
            case R.id.btn_noscroll:
                startActivity(new Intent(this,NoScrollActivity.class));

                break;
            case R.id.btn_refresh:
                startActivity(new Intent(this, SwipRefreshActivity.class));

                break;
        }
    }
}
