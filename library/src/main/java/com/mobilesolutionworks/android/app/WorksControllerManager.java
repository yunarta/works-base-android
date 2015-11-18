package com.mobilesolutionworks.android.app;

import android.os.Bundle;

import java.util.logging.Logger;

/**
 * Created by yunarta on 18/11/15.
 */
public abstract class WorksControllerManager
{
    /**
     * Callback interface for a client to interact with the manager.
     */
    public interface LoaderCallbacks<D extends WorksController>
    {
        public D onCreateLoader(int id, Bundle args);

        public void onLoadFinished(int id, D loader);

        public void onLoaderReset(D loader);
    }

    protected static final Logger LOGGER = Logger.getLogger(WorksControllerManager.class.getName());

    protected static final boolean DEBUG = false;

    protected static final String TAG = "/!";

    final String mWho;

    protected ControllerHostCallback mHost;

    boolean mStarted;

    boolean mRetaining;

    public WorksControllerManager(ControllerHostCallback host, String who)
    {
        mHost = host;
        mWho = who;
    }

    public String who()
    {
        return mWho;
    }

    public abstract <D extends WorksController> D initLoader(int id, Bundle args, WorksControllerManager.LoaderCallbacks<WorksController> callback);

    public abstract void destroyLoader(int id);

    void updateHostController(ControllerHostCallback host)
    {
        mHost = host;
    }

    abstract void doStart();

    abstract void doResume();

    abstract void doStop();

    abstract void doRetain();

    abstract void finishRetain();

    abstract void doReportNextStart();

    abstract void doReportStart();

    abstract void doDestroy();
}
