package com.mobilesolutionworks.works.core;

import android.support.v4.app.Fragment;

import com.mobilesolutionworks.works.sample.activity.WorksActivity;

/**
 * Created by yunarta on 11/12/16.
 */

public interface WorksFragmentBase {

    void postControllerResult(int id, int requestCode, int resultCode, Object data);

    <T extends WorksActivity> T getBaseActivity();

    int getTargetRequestCode();

    Fragment getParentFragment();

    int getTargetControllerId();

    int getTargetControllerRequestCode();

}
