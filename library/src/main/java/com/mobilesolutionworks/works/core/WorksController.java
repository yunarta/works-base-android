package com.mobilesolutionworks.works.core;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by yunarta on 16/11/15.
 */
public class WorksController {

    private int mId;

    private Object mData;

    public final void setId(int id) {
        mId = id;
    }

    public final int getId() {
        return mId;
    }

    public void onCreate(Bundle args, @Nullable Object data) {

    }

    public void onPaused() {

    }

    public void onResume() {

    }

    public void onDestroy() {

    }

    public void onSaveInstanceState(Bundle outState) {

    }

    public void onViewStateRestored(Bundle state) {

    }

    public void onConfigurationChanged(Configuration config) {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void onControllerResult(int requestCode, int resultCode, Object data) {

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

    }
}
