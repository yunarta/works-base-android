package com.mobilesolutionworks.android.app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.View;

import java.util.logging.Logger;

/**
 * Created by yunarta on 19/11/15.
 */
public class WorksDialogFragment extends DialogFragment {

    private static final boolean DEBUG = WorksBaseConfig.DEBUG;

    protected final Logger LOGGER = Logger.getLogger(getClass().getName());

    private static int sInstanceCount;

    String mInstanceName;

    FragmentControllerHost mHost;

    Handler mHandler;

    FragmentHostCallback mFragmentHostCallback;

    public Handler getHandler() {
        return mHandler;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mInstanceName = savedInstanceState.getString(":fragment:instance");
        } else {
            mInstanceName = "fragment:" + (++sInstanceCount);
        }

        mHandler = new Handler(Looper.myLooper());

        WorksActivity activity = (WorksActivity) getActivity();

        mHost = new FragmentControllerHost(mInstanceName, activity.getControllerHost());
        mFragmentHostCallback = new FragmentHostCallback() {
            @Override
            public boolean isRetaining() {
                return getRetainInstance();
            }
        };

//        mHost.setFragmentHostCallback(mFragmentHostCallback);
        mHost.dispatchCreate();
    }

    @Override
    public void setRetainInstance(boolean retain) {
        mHost.dispatchRetainInstance(retain);
        super.setRetainInstance(retain);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        getControllerManager().dispatchSaveInstanceState(bundle);
        bundle.putString(":fragment:instance", mInstanceName);
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onStart() {
        mHost.dispatchStart();
        super.onStart();
    }

    @Override
    public void onResume() {
        mHost.dispatchResume();
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getControllerManager().onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onPause() {
        mHost.dispatchPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        mHost.dispatchStop();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        mHost.dispatchDestroyView();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mHost.dispatchDestroy();
        super.onDestroy();
    }

    public WorksControllerManager getControllerManager() {
        return mHost.getControllerManager();
    }

}
