package com.qinlong275.android.picu.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qinlong275.android.picu.R;
import com.qinlong275.android.picu.bean.PicUItem;
import com.qinlong275.android.picu.data.model.ReceivedModel;
import com.qinlong275.android.picu.presenter.ReceivedPresenter;
import com.qinlong275.android.picu.presenter.contract.PicUContract;
import com.qinlong275.android.picu.ui.adapter.PicUlistAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PicUReceivedFragment extends ProgressFragment<ReceivedPresenter> implements PicUContract.ReceivedView {

    @BindView(R.id.picu_list)
    RecyclerView mPicuList;
    private PicUlistAdapter mPicUlistAdapter;
    private List<PicUItem> mPicUItems=new ArrayList<>();

    @Override
    void onEmptyViewClick() {
        mPresenter.getReceivedPicu();
    }

    @Override
    public void init() {

        initRecyclerView();

        mPresenter = new ReceivedPresenter(new ReceivedModel(), this);
        mPresenter.getReceivedPicu();
    }

    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mPicuList.setLayoutManager(layoutManager);
        mPicUlistAdapter=new PicUlistAdapter(1,getActivity(),mPicUItems);
        mPicuList.setItemAnimator(new DefaultItemAnimator());
        mPicuList.setAdapter(mPicUlistAdapter);
    }

    @Override
    public int setLayout() {

        //收到界面的LayoutId
        return R.layout.showpicu_list;
    }

    @Override
    public void showResult() {
        //具体的获取数据后的显示界面
        initPiuList(mPicUItems);
//        mPicUlistAdapter.setDate(null);
        mPicUlistAdapter.notifyDataSetChanged();
    }

    private void initPiuList( List<PicUItem> picUItems){
        picUItems.add(new PicUItem("q","","a","","a","","","2",""));
        picUItems.add(new PicUItem("q","","a","","0","","","2","true"));
        picUItems.add(new PicUItem("","","a","","0","","","3",""));
        picUItems.add(new PicUItem("","","a","","a","","","","true"));
        picUItems.add(new PicUItem("q","","a","","a","","","3",""));
    }

}
