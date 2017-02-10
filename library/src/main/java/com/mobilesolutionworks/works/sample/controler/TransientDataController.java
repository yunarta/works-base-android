package com.mobilesolutionworks.works.sample.controler;

import com.mobilesolutionworks.works.core.WorksController;

/**
 * Created by yunarta on 10/12/16.
 */

public class TransientDataController extends WorksController {

    private Object mData;

    public void setData(Object data) {
        mData = data;
    }

    public Object getData() {
        return mData;
    }
}
