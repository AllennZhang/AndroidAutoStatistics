package com.yt.statistics.statplugin;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yt.statistics.statplugin.fragment.TestFragment;

import java.util.List;

public class PageAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments ;
    private String[] titles;

    public PageAdapter(FragmentManager fm, List<Fragment> list,String[] titles) {
        super(fm);
        this.fragments = list;
        this.titles = titles;

    }

    @Override
    public Fragment getItem(int position) {
      Fragment fragment =   fragments == null ?  null : fragments.get(position);

      if (position == 0){
          ((TestFragment) fragment).writeAttribute("苹果 position:"+position);
      }else if (position == 1){
          ((TestFragment) fragment).writeAttribute("橘子 position:"+position);
      }else if (position ==2){
          ((TestFragment) fragment).writeAttribute("香蕉 position:"+position);
      }else if (position == 3){
          ((TestFragment) fragment).writeAttribute("菠萝 position:"+position);
      }else {
          ((TestFragment) fragment).writeAttribute("other position:"+position);
      }
        return fragment;
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 :fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
