package com.mobilesolutionworks.android.app;

import android.app.Activity;

import java.util.logging.Logger;

/**
 * Created by yunarta on 16/11/15.
 */
public class WorksController
{
    protected static final boolean DEBUG = true;

    protected Logger logger = Logger.getLogger(getClass().getName());

    private final String mHash = Integer.toString(System.identityHashCode(this), Character.MAX_RADIX);

    private Activity mActivity;

    public void updateActivity(Activity activity)
    {
        mActivity = activity;
    }

    public Activity getActivity()
    {
        return mActivity;
    }

    public void onCreate()
    {
        if (DEBUG) logger.fine("  [" + mHash + "] onCreate");
    }

    public void onStart()
    {
        if (DEBUG) logger.fine("  [" + mHash + "] onStart");
    }

    public void onPaused()
    {
        if (DEBUG) logger.fine("  [" + mHash + "] onPaused");
    }

    public void onResume()
    {
        if (DEBUG) logger.fine("  [" + mHash + "] onResume");
    }

    public void onStop()
    {
        if (DEBUG) logger.fine("  [" + mHash + "] onStop");
    }

    public void onDestroy()
    {
        if (DEBUG) logger.fine("  [" + mHash + "] onDestroy");
    }



//    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args)
//    {
//
//    }
}
