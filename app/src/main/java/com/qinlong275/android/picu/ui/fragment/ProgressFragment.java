package com.qinlong275.android.picu.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.qinlong275.android.picu.R;
import com.qinlong275.android.picu.presenter.BasePresenter;
import com.qinlong275.android.picu.ui.BaseView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class ProgressFragment<T extends BasePresenter> extends Fragment  implements BaseView {

    private FrameLayout mRootView;

    private View mViewProgress;
    private FrameLayout mViewContent;       //放入真正正常显示的View
    private View mViewEmpty;
    private Unbinder mUnbinder;
    private TextView mTextError;

    T mPresenter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRootView = (FrameLayout) inflater.inflate(R.layout.fragment_progress,container,false);
        mViewProgress = mRootView.findViewById(R.id.view_progress);
        mViewContent = (FrameLayout) mRootView.findViewById(R.id.view_content);
        mViewEmpty = mRootView.findViewById(R.id.view_empty);
        mTextError = (TextView) mRootView.findViewById(R.id.text_tip);

        mViewEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEmptyViewClick();
            }
        });
        return mRootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRealContentView();

        init();
    }

    //子类重写，进行重获取数据
    abstract void onEmptyViewClick();

    private void setRealContentView() {

        View realContentView=  LayoutInflater.from(getActivity()).inflate(setLayout(),mViewContent,true);
        mUnbinder=  ButterKnife.bind(this, realContentView);
    }



    public void  showProgressView(){
        showView(R.id.view_progress);

    }

    public void showContentView(){

        showView(R.id.view_content);
    }

    public void showEmptyView(){
        showView(R.id.view_empty);
    }


    public void showEmptyView(String msg){
        showEmptyView();
        mTextError.setText(msg);
    }



    //根据当前请求数据的情况，只显示指定的View
    public void showView(int viewId){

        for(int i=0;i<mRootView.getChildCount();i++){

            if( mRootView.getChildAt(i).getId() == viewId){

                mRootView.getChildAt(i).setVisibility(View.VISIBLE);
            }
            else {
                mRootView.getChildAt(i).setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mUnbinder != Unbinder.EMPTY){
            mUnbinder.unbind();
        }
    }

    @Override
    public void showLoading() {
        showProgressView();
    }

    @Override
    public void showError(String msg) {

        showEmptyView(msg);
    }

    //presenter命令moduel请求数据成功时，回调
    @Override
    public void dismissLoading() {
        showContentView();
    }

    //进入页面的初始化操作，请求数据...
    public abstract void  init();

    //子类具体的页面Layoutid
    public abstract int setLayout();

}
