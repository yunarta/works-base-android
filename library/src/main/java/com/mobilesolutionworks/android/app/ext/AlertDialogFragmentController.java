package com.mobilesolutionworks.android.app.ext;

import com.mobilesolutionworks.android.app.BiFunction;
import com.mobilesolutionworks.android.app.WorksDialogFragment;
import com.mobilesolutionworks.android.bolts.BoltsWorksController3;


/**
 * Created by yunarta on 7/12/16.
 */

public class AlertDialogFragmentController<Data> extends BoltsWorksController3<WorksDialogFragment> {

    private final Data mData;

    private final BiFunction<Integer, Data> mAction;

    public AlertDialogFragmentController(Data mData, BiFunction<Integer, Data> action) {
        this.mData = mData;
        this.mAction = action;
    }

    public void postResult(int choiceRes) {
        mAction.accept(choiceRes, mData);
    }

}
