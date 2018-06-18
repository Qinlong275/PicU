package com.qinlong275.android.picu.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.qinlong275.android.picu.presenter.contract.PicUContract;
import com.qinlong275.android.picu.ui.bean.FragmentInfo;
import com.qinlong275.android.picu.ui.fragment.PicUPublishedFragment;
import com.qinlong275.android.picu.ui.fragment.PicUReceivedFragment;

import java.util.ArrayList;
import java.util.List;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {




    private List<FragmentInfo> mFragments = new ArrayList<>(2);


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);

        initFragments();
    }



    private void initFragments(){

        mFragments.add(new FragmentInfo("我收到的PicU",PicUReceivedFragment.class));
        mFragments.add(new FragmentInfo ("我制作的PicU", PicUPublishedFragment.class));
    }


    @Override
    public Fragment getItem(int position) {


        try {
            return (Fragment) mFragments.get(position).getFragment().newInstance();

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return  null;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mFragments.get(position).getTitle();
    }
}
