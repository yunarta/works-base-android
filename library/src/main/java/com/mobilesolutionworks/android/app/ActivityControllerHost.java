package com.mobilesolutionworks.android.app;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mobilesolutionworks.android.app.v4.SimpleArrayMap;

/**
 * Created by yunarta on 16/11/15.
 */
public class ActivityControllerHost // <Host>
{
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
                        doReallyStop(false);
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

    public void dispatchStart()
    {
        Log.d("/!", "    Activity.dispatchStart");
        mStopped = false;
        mReallyStopped = false;

        mHost.doControllerStart();
        mHost.reportControllerStart();
    }

    public void dispatchStop()
    {
        Log.d("/!", "    Activity.dispatchStop");
        mStopped = true;
        mHandler.sendEmptyMessage(MSG_REALLY_STOPPED);
    }

    public void dispatchDestroy()
    {
        Log.d("/!", "    Activity.dispatchDestroy");
        doReallyStop(false);

        mHost.doLoaderDestroy();
    }

    void doReallyStop(boolean retaining)
    {
        Log.d("/!", "    Activity.doReallyStop retaining = " + retaining + " reallyStopped = " + mReallyStopped);
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
        Log.d("/!", "    Activity.onReallyStop");
        mHost.doControllerStop(mRetaining);
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
