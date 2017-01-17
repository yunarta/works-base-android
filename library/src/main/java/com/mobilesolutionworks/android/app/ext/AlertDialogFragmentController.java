package com.mobilesolutionworks.android.app.ext;

import com.mobilesolutionworks.android.app.WorksDialogFragment;
import com.mobilesolutionworks.android.bolts.BoltsWorksController3;

import rx.functions.Action2;

/**
 * Created by yunarta on 7/12/16.
 */

public class AlertDialogFragmentController<Data> extends BoltsWorksController3<WorksDialogFragment> {

    private final Data mData;

    private final Action2<Integer, Data> mAction;

    public AlertDialogFragmentController(Data mData, Action2<Integer, Data> action) {
        this.mData = mData;
        this.mAction = action;
    }

    public void postResult(int choiceRes) {
        // todo https://mint.splunk.com/dashboard/project/99f96009/errors/8347258028, mAction was  null when user clicked the dialog button?
        mAction.call(choiceRes, mData);
    }

}
