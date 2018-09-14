package com.yt.statistics.statplugin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;


import com.hipac.codeless.core.TraceService;
import com.yt.statistics.statplugin.base.BaseActivity;
import com.yt.statistics.statplugin.view.ManagePopuWindow;

/**
 * Created by youri on 2018/2/27.
 */

public class ThirdActivity extends BaseActivity implements View.OnClickListener{

    private CheckBox checkBoxEN;
    private CheckBox cheboxCH;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        findViewById(R.id.btn_toMain).setOnClickListener(this);
        findViewById(R.id.btnShowPopu).setOnClickListener(this);
        checkBoxEN = findViewById(R.id.checkbox_EN);
        cheboxCH = findViewById(R.id.checkbox_CH);
        radioGroup = findViewById(R.id.rg_sexgroup);
        initListener();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("Test","onNewIntent() "+ TraceService.getCurrentPage());
    }

    private void initListener() {
        checkBoxEN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        cheboxCH.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_toMain:
                Intent intent = new Intent(ThirdActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
            case R.id.btnShowPopu:
                View parent = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
                ManagePopuWindow popuWindow = new ManagePopuWindow(this,true);
                popuWindow.showAtLocationWithBgAnimate((ViewGroup) findViewById(android.R.id.content), parent, Gravity.BOTTOM, 0, 0);
                break;
            default:
                break;
        }
    }

}
