package com.mobilesolutionworks.works.util;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.mobilesolutionworks.works.core.WorksFragmentBase;
import com.mobilesolutionworks.works.sample.activity.WorksCompatActivity;
import com.mobilesolutionworks.works.sample.fragment.WorksCompatDialogFragment;

/**
 * Created by lucas34990 on 10/2/17.
 */

public class WorksFragmentUtils {

    private WorksFragmentUtils() {
        // No instance
    }

    public static void sendResultToParent(WorksFragmentBase fragment, int resultCode) {
        Fragment target = fragment.getParentFragment();
        sendResultToTarget(fragment, target, resultCode, new Intent());
    }

    public static void sendResultToParent(WorksFragmentBase fragment, int resultCode, Intent intent) {
        Fragment target = fragment.getParentFragment();
        sendResultToTarget(fragment, target, resultCode, intent);
    }

    public static void sendControllerResultToParent(WorksFragmentBase fragment, int resultCode) {
        sendControllerResultToParent(fragment, resultCode, null);
    }

    public static void sendControllerResultToParent(WorksFragmentBase fragment, int resultCode, Object data) {
        Fragment target = fragment.getParentFragment();
        sendControllerResultToTarget(fragment, target, resultCode, data);
    }

    private static void sendResultToTarget(WorksFragmentBase fragment, Fragment target, int resultCode, Intent intent) {
        if (target != null) {
            target.onActivityResult(fragment.getTargetRequestCode(), resultCode, intent);
            return;
        } else {
            FragmentActivity activity = fragment.getActivity();
            if(activity instanceof WorksCompatActivity) {
                WorksCompatActivity compatActivity = (WorksCompatActivity) activity;
                compatActivity.onActivityResultCompat(fragment.getTargetRequestCode(), resultCode, intent);
                return;
            }
        }
        Log.e("sendResultToTarget", "Cannot handle the result");
    }

    private static void sendControllerResultToTarget(WorksFragmentBase fragment, Fragment target, int resultCode, Object data) {
        if (target instanceof WorksFragmentBase) {
            ((WorksFragmentBase) target).postControllerResult(fragment.getTargetControllerId(), fragment.getTargetControllerRequestCode(), resultCode, data);
            return;
        } else {
            FragmentActivity activity = fragment.getActivity();
            if(activity instanceof WorksCompatActivity) {
                WorksCompatActivity compatActivity = (WorksCompatActivity) activity;
                compatActivity.postControllerResult(fragment.getTargetControllerId(), fragment.getTargetControllerRequestCode(), resultCode, data);
                return;
            }
        }
        Log.e("sendControllerResult", "Cannot handle the result");
    }

}
