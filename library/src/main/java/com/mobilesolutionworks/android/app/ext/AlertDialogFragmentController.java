package com.mobilesolutionworks.android.app.ext;

import com.mobilesolutionworks.android.app.WorksDialogFragment;
import com.mobilesolutionworks.android.bolts.BoltsWorksController3;
import com.pacoworks.rxtuples.RxTuples;

import rx.Observable;
import rx.functions.Action2;
import rx.functions.Func2;

/**
 * Created by yunarta on 7/12/16.
 */

public class AlertDialogFragmentController<Data> extends BoltsWorksController3<WorksDialogFragment> {

    private final Data mData;

    private final Action2<Integer, Data> action;

    public AlertDialogFragmentController(Data mData, Action2<Integer, Data> action) {
        this.mData = mData;
        this.action = action;
    }

    public void postResult(int choiceRes) {
        action.call(choiceRes, mData);
    }

}
