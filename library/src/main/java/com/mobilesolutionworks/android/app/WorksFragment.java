package com.mobilesolutionworks.android.app;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;

import java.util.logging.Logger;

/**
 * Created by yunarta on 19/11/15.
 */
public class WorksFragment extends Fragment {

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

        mHandler = new Handler();

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

    public void onSaveInstanceState(Bundle bundle) {
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
