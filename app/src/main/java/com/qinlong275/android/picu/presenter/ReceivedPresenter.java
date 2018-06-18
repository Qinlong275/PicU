package com.qinlong275.android.picu.presenter;

import com.qinlong275.android.picu.common.rx.ProgressSubscriber;
import com.qinlong275.android.picu.common.rx.RxHttpResponseCompat;
import com.qinlong275.android.picu.data.model.ReceivedModel;
import com.qinlong275.android.picu.presenter.contract.PicUContract;
import com.qinlong275.android.picu.ui.BaseView;

import java.util.List;

public class ReceivedPresenter extends BasePresenter<ReceivedModel,PicUContract.ReceivedView>{


    public ReceivedPresenter(ReceivedModel receivedModel, PicUContract.ReceivedView receivedView) {
        super(receivedModel, receivedView);
    }

    public void getReceivedPicu(){

//        mModel.index().compose(RxHttpResponseCompat.<String>compatResult())
//                .subscribe(new ProgressSubscriber<String>(mView) {
//                    @Override
//                    public void onNext(String categories) {
//                        mView.showResult();
//                    }
//                });
        mView.showResult();

        //先判断本地是否有缓存可用
        //如果没有缓存，mModel请求数据，完成后回调mView的UI相关接口
    }
}
