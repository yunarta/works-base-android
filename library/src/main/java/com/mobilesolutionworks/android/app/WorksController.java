package com.mobilesolutionworks.android.app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.logging.Logger;

/**
 * Created by yunarta on 16/11/15.
 */
public class WorksController {
    protected static final boolean DEBUG = true;

    private static final Logger WC_LOGGER = Logger.getLogger(WorksController.class.getName());

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

    public void onCreate(Bundle savedInstanceState) {
        if (DEBUG) WC_LOGGER.fine("  wc[" + mHash + "] onCreate");
    }

    public void onStart() {
        if (DEBUG) WC_LOGGER.fine("  wc[" + mHash + "] onStart");
    }

    public void onPaused() {
        if (DEBUG) WC_LOGGER.fine("  wc[" + mHash + "] onPaused");
    }

    public void onResume() {
        if (DEBUG) WC_LOGGER.fine("  wc[" + mHash + "] onResume");
    }

    public void onStop() {
        if (DEBUG) WC_LOGGER.fine("  wc[" + mHash + "] onStop");
    }

    public void onDestroy() {
        if (DEBUG) WC_LOGGER.fine("  wc[" + mHash + "] onDestroy");
    }

    public Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.myLooper(), new HandlerImpl(this));
        }

        return mHandler;
    }

    public boolean handleMessage(Message message) {
        return true;
    }

    public void onViewStateRestored(Bundle bundle) {

    }

    public void onSaveInstanceState(Bundle bundle) {

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
