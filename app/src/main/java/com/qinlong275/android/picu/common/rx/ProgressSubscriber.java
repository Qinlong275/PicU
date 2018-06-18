package com.qinlong275.android.picu.common.rx;

import android.content.Context;

import com.qinlong275.android.picu.common.util.ProgressDialogHandler;
import com.qinlong275.android.picu.ui.BaseView;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class ProgressSubscriber<T>implements Observer<T> {

    //判断是否订阅
    private Disposable mDisposable;

    private BaseView mView;

    public ProgressSubscriber(BaseView view){
        mView=view;
    }

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
        if (isShowProgress()) {
            mView.showLoading();
        }
    }

    private boolean isShowProgress() {
        return true;
    }

    @Override
    public void onComplete() {
        mView.dismissLoading();
    }

    @Override
    public void onError(Throwable e) {
        mView.showError("请求错误，请稍后重试");
        e.printStackTrace();
        mDisposable.dispose();
    }

}
