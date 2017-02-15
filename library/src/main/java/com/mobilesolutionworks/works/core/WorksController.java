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

    protected void onCreate(Bundle args, @Nullable Object data) {

    }

    protected void onPaused() {

    }

    protected void onResume() {

    }

    protected void onDestroy() {

    }

    protected void onSaveInstanceState(Bundle outState) {

    }

    protected void onViewStateRestored(Bundle state) {

    }

    protected void onConfigurationChanged(Configuration config) {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void onControllerResult(int requestCode, int resultCode, Object data) {

    }

    protected void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

    }
}
