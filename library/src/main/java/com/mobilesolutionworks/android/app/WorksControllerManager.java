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

    abstract void doStart();

    abstract void doResume();

    abstract void doStop();

    abstract void doRetain();

    abstract void finishRetain();

    abstract void doReportNextStart();

    abstract void doReportStart();

    abstract void doDestroy();
}
