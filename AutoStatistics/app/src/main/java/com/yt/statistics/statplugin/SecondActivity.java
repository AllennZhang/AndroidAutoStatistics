package com.yt.statistics.statplugin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hipac.codeless.util.TraceHolder;
import com.yt.statistics.statplugin.fragment.TestFragment;

/**
 * Created by youri on 2018/2/27.
 */

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        findViewById(R.id.btn_jump).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this,ThirdActivity.class));
            }
        });
        TraceHolder.setPageExtendFields(SecondActivity.class,"{\"data\":{\"page\":\"\",\"utp\":\"1\",\"viewPath\":\"LinearLayout/XRecyclerView[53]/RelativeLayout[0]/ContentFrameLayout[0]/HomeActivity\"},\"requestTime\":\"1522142129479\",\"type\":\"1001\"}");
        TestFragment fragment = new TestFragment();
        TestFragment fragment1 = new TestFragment();
        fragment.writeAttribute("test1");
        fragment1.writeAttribute("test2");
        FragmentManager fm = getSupportFragmentManager();
        if (!fragment.isAdded()){
            fm.beginTransaction().add(R.id.frag_container,fragment).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}
