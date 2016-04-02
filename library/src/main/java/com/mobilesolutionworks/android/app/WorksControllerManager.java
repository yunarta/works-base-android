package com.mobilesolutionworks.android.app;

import android.os.Bundle;

import java.util.logging.Logger;

/**
 * Created by yunarta on 18/11/15.
 */
public abstract class WorksControllerManager
{
//    private WeakReference<FragmentHostCallback> mFragmentHostCallback;
//
//    public void setFragmentHostCallback(FragmentHostCallback fragmentHostCallback)
//    {
//        mFragmentHostCallback = new WeakReference<>(fragmentHostCallback);
//    }
//
//    public boolean isFragmentRetaining()
//    {
//        // if fragment is retaining, then controller manager is retaining
//        boolean mFragmentRetaining = false;
//        if (mFragmentHostCallback != null && mFragmentHostCallback.get() != null)
//        {
//            mFragmentRetaining = mFragmentHostCallback.get().isRetaining();
//        }
//
//        return mFragmentRetaining;
//    }

    /**
     * Callback interface for a client to interact with the manager.
     */
    public interface ControllerCallbacks<D extends WorksController>
    {

        D onCreateController(int id, Bundle args);

        void onCreated(int id, D controller);

        void onReset(D loader);

    }

    protected static final Logger LOGGER = Logger.getLogger(WorksControllerManager.class.getName());

    protected static final boolean DEBUG = true;

    final String mWho;

    protected ControllerHostCallback mHost;

    boolean mStarted;

    boolean mRetaining;

    public boolean isRetaining()
    {
//        return true; // mRetaining || isFragmentRetaining();
        return mRetaining; // || isFragmentRetaining();
    }

    boolean mFragmentRetaining;

    public WorksControllerManager(ControllerHostCallback host, String who)
    {
        mHost = host;
        mWho = who;
    }

    public String who()
    {
        return mWho;
    }

    public abstract <D extends WorksController> D initController(int id, Bundle args, ControllerCallbacks<D> callback);

    public abstract void destroyController(int id);

    void updateHostController(ControllerHostCallback host)
    {
        mHost = host;
    }

    abstract void updateState(int state);

    abstract void doStart();

    abstract void doResume();

    abstract void doPause();

    abstract void doStop();

    abstract void doRelease();

    abstract void doRetain();

    abstract void finishRetain();

    abstract void doReportNextStart();

    abstract void doReportStart();

    abstract void doDestroy(boolean release);
}
