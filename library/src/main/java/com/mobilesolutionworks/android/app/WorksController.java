package com.mobilesolutionworks.android.app;

import java.util.logging.Logger;

/**
 * Created by yunarta on 16/11/15.
 */
public class WorksController
{
    protected static final boolean DEBUG = true;

    protected static final Logger logger = Logger.getLogger(WorksController.class.getName());

    private final String mHash = Integer.toString(System.identityHashCode(this), Character.MAX_RADIX);

//    private Activity mActivity;
//
//    public void updateActivity(Activity activity)
//    {
//        mActivity = activity;
//    }
//
//    public Activity getActivity()
//    {
//        return mActivity;
//    }

    public void onCreate()
    {
        if (DEBUG) logger.fine("  wc[" + mHash + "] onCreate");
    }

    public void onStart()
    {
        if (DEBUG) logger.fine("  wc[" + mHash + "] onStart");
    }

    public void onPaused()
    {
        if (DEBUG) logger.fine("  wc[" + mHash + "] onPaused");
    }

    public void onResume()
    {
        if (DEBUG) logger.fine("  wc[" + mHash + "] onResume");
    }

    public void onStop()
    {
        if (DEBUG) logger.fine("  wc[" + mHash + "] onStop");
    }

    public void onDestroy()
    {
        if (DEBUG) logger.fine("  wc[" + mHash + "] onDestroy");
    }




//    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args)
//    {
//
//    }
}
