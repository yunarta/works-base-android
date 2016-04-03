package com.mobilesolutionworks.android.app;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.mobilesolutionworks.android.app.v4.SimpleArrayMap;

import java.util.logging.Logger;

/**
 * Created by yunarta on 16/11/15.
 */
public class ActivityControllerHost // <Host>
{
    public static final Logger LOGGER = Logger.getLogger(ActivityControllerHost.class.getName());

    private static final int MSG_REALLY_STOPPED = 1;

    ControllerHostCallback/*<Host>*/ mHost;

    private boolean mReallyStopped;

    private boolean mStopped;

    private boolean mRetaining;

    final Handler mHandler = new HandlerImpl();

    private class HandlerImpl extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case MSG_REALLY_STOPPED:
                    if (mStopped)
                    {
//                        doReallyStop(false);
                    }
                    break;

//                case MSG_RESUME_PENDING:
//                    onResumeFragments();
//                    mFragments.execPendingActions();
//                    break;

                default:
                    super.handleMessage(msg);
            }
        }
    }

    public ActivityControllerHost(Activity activity)
    {
        mHost = new ControllerHostCallback(activity, activity, mHandler);
    }

    /**
     * Returns a {@link WorksControllerManagerImpl}.
     */
    public WorksControllerManager getControllerManager()
    {
        return mHost.getControllerManager();
    }

    public void dispatchPostCreate()
    {
        mHost.reportControllerPostCreate();
    }

    public void dispatchStart()
    {
        mStopped = false;
        mReallyStopped = false;
        mHandler.removeMessages(MSG_REALLY_STOPPED);

        mHost.doControllerStart();
        mHost.reportControllerStart();
    }

    public void dispatchStop()
    {
        mStopped = true;
        mHandler.sendEmptyMessage(MSG_REALLY_STOPPED);
    }

    public void dispatchDestroy()
    {
        doReallyStop(false);
        mHost.doLoaderDestroy();

//        if (!mRetaining)
//        {
//            mHost.doAllLoaderDestroy();
//        }
    }

    void doReallyStop(boolean retaining)
    {
        if (!mReallyStopped)
        {
            mReallyStopped = true;
            mRetaining = retaining;
            mHandler.removeMessages(MSG_REALLY_STOPPED);
            onReallyStop();
        }
    }

    private void onReallyStop()
    {
        mHost.doLoaderStop(mRetaining);
    }

    public SimpleArrayMap<String, WorksControllerManager> retainLoaderNonConfig()
    {
        if (mStopped)
        {
            doReallyStop(true);
        }

        return mHost.retainLoaderNonConfig();
    }

    public void restoreLoaderNonConfig(SimpleArrayMap<String, WorksControllerManager> loaderManagers)
    {
        mHost.restoreLoaderNonConfig(loaderManagers);
    }
}
