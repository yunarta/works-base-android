package com.mobilesolutionworks.works.sample.controler;

import com.mobilesolutionworks.works.sample.BiFunction;
import com.mobilesolutionworks.works.sample.fragment.WorksDialogFragment;
import com.mobilesolutionworks.works.core.SimpleWorksController;


/**
 * Created by yunarta on 7/12/16.
 */

public class AlertDialogFragmentController<Data> extends SimpleWorksController<WorksDialogFragment> {

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
