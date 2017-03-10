package com.mobilesolutionworks.works.core;

import android.support.v4.app.Fragment;

import com.mobilesolutionworks.works.sample.activity.WorksCompatActivity;

/**
 * Created by yunarta on 11/12/16.
 */

public interface WorksFragmentBase extends Host {

    void postControllerResult(int id, int requestCode, int resultCode, Object data);

    int getTargetRequestCode();

    Fragment getParentFragment();

    int getTargetControllerId();

    int getTargetControllerRequestCode();

}