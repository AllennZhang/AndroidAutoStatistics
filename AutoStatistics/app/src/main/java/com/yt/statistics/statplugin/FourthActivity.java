package com.yt.statistics.statplugin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;


import com.hipac.codeless.core.TraceService;
import com.yt.statistics.statplugin.base.BaseActivity;
import com.yt.statistics.statplugin.fragment.TestFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by youri on 2018/3/12.
 */

public class FourthActivity extends BaseActivity {
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

//    @BindView(R.id.btn_commit)
//    Button btnCommit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);
        ButterKnife.bind(this);

        Log.e("Test", "Activity onCreate " + TraceService.getCurrentPage());

        TestFragment fragment = new TestFragment();
        TestFragment fragment1 = new TestFragment();
        TestFragment fragment2 = new TestFragment();
        TestFragment fragment3 = new TestFragment();
        TestFragment fragment4 = new TestFragment();
        TestFragment fragment5 = new TestFragment();
        List<Fragment> list = new ArrayList<>();
        list.add(fragment);
        list.add(fragment1);
        list.add(fragment2);
        list.add(fragment3);
        list.add(fragment4);
        list.add(fragment5);
        PageAdapter adapter = new PageAdapter(getSupportFragmentManager(),list,new String[]{"苹果","橘子","香蕉","菠萝","Tab5","Tab6"});
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }

//    @OnClick(R.id.btn_commit)
//    public void onViewClicked() {
//        Toast.makeText(this,"登录成功",Toast.LENGTH_SHORT).show();
//        Log.e("Test", "onClick "+HipacStatService.getCurrentPage());
//        finish();
//    }
}
