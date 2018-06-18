package com.qinlong275.android.picu.data.model;

import com.qinlong275.android.picu.common.SingleTons;
import com.qinlong275.android.picu.data.http.ApiService;

public class BaseModel {

    protected ApiService mApiService;

    public BaseModel(){
        mApiService= SingleTons.getApiService();
    }

}
