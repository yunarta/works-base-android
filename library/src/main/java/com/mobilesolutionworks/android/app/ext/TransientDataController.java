package com.mobilesolutionworks.android.app.ext;

import com.mobilesolutionworks.android.app.WorksController;

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
