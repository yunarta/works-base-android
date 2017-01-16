package com.mobilesolutionworks.android.app.ext;

import com.mobilesolutionworks.android.app.WorksDialogFragment;
import com.mobilesolutionworks.android.bolts.BoltsWorksController3;

import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiConsumer;


/**
 * Created by yunarta on 7/12/16.
 */

public class AlertDialogFragmentController<Data> extends BoltsWorksController3<WorksDialogFragment> {

    private final Data mData;

    private final BiConsumer<Integer, Data> action;

    public AlertDialogFragmentController(Data mData, BiConsumer<Integer, Data> action) {
        this.mData = mData;
        this.action = action;
    }

    public void postResult(int choiceRes) {
        try {
            action.accept(choiceRes, mData);
        } catch (Exception e) {
            throw Exceptions.propagate(e);
        }
    }

}
