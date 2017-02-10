package com.mobilesolutionworks.works.util;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.mobilesolutionworks.works.core.WorksFragmentBase;

/**
 * Created by lucas34990 on 10/2/17.
 */

public class WorksFragmentUtils {

    private WorksFragmentUtils() {
        // No instance
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
        } else {
            fragment.getBaseActivity().onActivityResultCompat(fragment.getTargetRequestCode(), resultCode, intent);
        }
    }

    private static void sendControllerResultToTarget(WorksFragmentBase fragment, Fragment target, int resultCode, Object data) {
        if (target != null && target instanceof WorksFragmentBase) {
            ((WorksFragmentBase) target).postControllerResult(fragment.getTargetControllerId(), fragment.getTargetControllerRequestCode(), resultCode, data);
        } else {
            fragment.getBaseActivity().postControllerResult(fragment.getTargetControllerId(), fragment.getTargetControllerRequestCode(), resultCode, data);
        }
    }

}
