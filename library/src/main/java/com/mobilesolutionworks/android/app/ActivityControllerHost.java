package com.mobilesolutionworks.android.app;

import android.app.Activity;
import android.support.v4.util.SimpleArrayMap;

import java.util.logging.Logger;

/**
 * Host class of work base to be installed in Activity.
 * <p>
 * Created by yunarta on 16/11/15.
 */
public class ActivityControllerHost // <Host>
{
    private static final Logger LOGGER = Logger.getLogger(ActivityControllerHost.class.getName());

    ControllerHostCallback/*<Host>*/ mHost;

    private boolean mReallyStopped;

    private boolean mStopped;

    private boolean mRetaining;

    private final Activity mActivity;

    public ActivityControllerHost(Activity activity) {
        mActivity = activity;
        mHost = new ControllerHostCallback(activity);
    }

    public WorksControllerManager getControllerManager() {
        return mHost.getControllerManager();
    }

    public void dispatchStart() {
        mStopped = false;
        mReallyStopped = false;

        mHost.doControllerStart();
        mHost.reportControllerStart();
    }

    public void dispatchStop() {
        mStopped = true;

    }

    public void dispatchDestroy() {
        doReallyStop(false);

        // // KB#0001 - stopping by pressing home button, actually breaks retain
        // mHost.doLoaderDestroy();
        // if (!mRetaining) {
        //     mHost.doAllLoaderDestroy();
        // }

        if (!mActivity.isChangingConfigurations()) {
            mHost.doLoaderDestroy();
        }
    }

    void doReallyStop(boolean retaining) {
        if (!mReallyStopped) {
            mReallyStopped = true;
            mRetaining = retaining;

            onReallyStop();
        } else if (retaining) {
            mHost.doControllerStart();
            mHost.doLoaderStop(true);
        }
    }

    private void onReallyStop() {
        mHost.doLoaderStop(mRetaining);
    }

    public SimpleArrayMap<String, WorksControllerManager> retainLoaderNonConfig() {
        if (mStopped) {
            doReallyStop(true);
        }

        return mHost.retainLoaderNonConfig();
    }

    public void restoreLoaderNonConfig(SimpleArrayMap<String, WorksControllerManager> loaderManagers) {
        mHost.restoreLoaderNonConfig(loaderManagers);
    }
}
