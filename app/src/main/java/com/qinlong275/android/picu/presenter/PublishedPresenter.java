package com.qinlong275.android.picu.presenter;

import com.qinlong275.android.picu.common.rx.ProgressSubscriber;
import com.qinlong275.android.picu.common.rx.RxHttpResponseCompat;
import com.qinlong275.android.picu.data.model.PublishedModel;
import com.qinlong275.android.picu.presenter.BasePresenter;
import com.qinlong275.android.picu.presenter.contract.PicUContract;

public class PublishedPresenter extends BasePresenter<PublishedModel,PicUContract.PublishedView>{

    public PublishedPresenter(PublishedModel publishedModel, PicUContract.PublishedView publishedView) {
        super(publishedModel, publishedView);
    }

    public void getPublishedPicu(){

//        mModel.login().compose(RxHttpResponseCompat.<String>compatResult())
//                .subscribe(new ProgressSubscriber<String>(mView) {
//                    @Override
//                    public void onNext(String categories) {
//                        mView.showHistory();
//                    }
//                });

        mView.showHistory();
        //先判断本地是否有缓存可用
        //如果没有缓存，mModel请求数据，完成后回调mView的UI相关接口
    }

}
