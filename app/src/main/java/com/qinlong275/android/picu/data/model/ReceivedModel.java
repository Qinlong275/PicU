package com.qinlong275.android.picu.data.model;

import com.qinlong275.android.picu.bean.BaseBean;
import io.reactivex.Observable;

public class ReceivedModel extends BaseModel{

    public Observable<BaseBean<String>> index(){

        return  mApiService.index();
    }

    //收到的PicU界面的请求操作

}
