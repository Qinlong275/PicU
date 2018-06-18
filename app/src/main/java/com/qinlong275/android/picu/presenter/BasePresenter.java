package com.qinlong275.android.picu.presenter;

import android.content.Context;

import com.qinlong275.android.picu.ui.BaseView;

public class BasePresenter <M,V extends BaseView> {

    protected M mModel;

    protected V mView;

    public BasePresenter(M m,V v){

        this.mModel=m;
        this.mView = v;
    }

}
