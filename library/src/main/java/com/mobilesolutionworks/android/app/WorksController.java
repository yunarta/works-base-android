package com.mobilesolutionworks.android.app;

import android.os.Handler;
import android.os.Message;

import java.util.logging.Logger;

/**
 * Created by yunarta on 16/11/15.
 */
public class WorksController {
    protected static final boolean DEBUG = true;

    protected static final Logger logger = Logger.getLogger(WorksController.class.getName());

    private final String mHash = Integer.toString(System.identityHashCode(this), Character.MAX_RADIX);

    protected Handler mHandler;

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

    public void onCreate() {
        if (DEBUG) logger.fine("  wc[" + mHash + "] onCreate");
    }

    public void onStart() {
        if (DEBUG) logger.fine("  wc[" + mHash + "] onStart");
    }

    public void onPaused() {
        if (DEBUG) logger.fine("  wc[" + mHash + "] onPaused");
    }

    public void onResume() {
        if (DEBUG) logger.fine("  wc[" + mHash + "] onResume");
    }

    public void onStop() {
        if (DEBUG) logger.fine("  wc[" + mHash + "] onStop");
    }

    public void onDestroy() {
        if (DEBUG) logger.fine("  wc[" + mHash + "] onDestroy");
    }

    public Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(new HandlerImpl(this));
        }

        return mHandler;
    }

    public boolean handleMessage(Message message) {
        return true;
    }

    public static class HandlerImpl implements Handler.Callback {

        private WorksController mController;

        public HandlerImpl(WorksController controller) {
            mController = controller;
        }

        @Override
        public boolean handleMessage(Message message) {
            return mController.handleMessage(message);
        }
    }


//    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args)
//    {
//
//    }
}
