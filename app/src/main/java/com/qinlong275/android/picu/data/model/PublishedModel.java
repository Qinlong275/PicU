package com.qinlong275.android.picu.data.model;
import com.qinlong275.android.picu.bean.BaseBean;

import io.reactivex.Observable;

public class PublishedModel extends BaseModel {

    public Observable<BaseBean<String>> login(){

        return  mApiService.login("");
    }

    //请求我制作过的PicU信息

}
