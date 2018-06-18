package com.qinlong275.android.picu.presenter.contract;

import com.qinlong275.android.picu.ui.BaseView;

public interface PicUContract {

    interface ReceivedView extends BaseView {

        //收到的PicU界面的具体显示UI操作
        void  showResult();

    }

    interface PublishedView extends BaseView{

        //显示发出的PicU历史
        void showHistory();
    }
}
