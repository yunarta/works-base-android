package com.mobilesolutionworks.android.app;

import android.util.Log;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by yunarta on 17/11/15.
 */
public class FragmentControllerHost
{
    private static final boolean DEBUG = WorksBaseConfig.DEBUG;

    private ControllerHostCallback mHost;

    WorksControllerManager mLoaderManager;

    boolean mLoadersStarted;

    boolean mCheckedForLoaderManager;

    private String  mWho;

    private boolean mRetainerAttached;

    public FragmentControllerHost(String who, ActivityControllerHost host)
    {
        mWho = who;
        mHost = host.mHost;
    }

    /**
     * Return the LoaderManager for this fragment, creating it if needed.
     */
    public WorksControllerManager getControllerManager()
    {
        if (mLoaderManager != null)
        {
            return mLoaderManager;
        }

        if (mHost == null)
        {
            throw new IllegalStateException("Fragment " + FragmentControllerHost.this + " not attached to Activity");
        }
        mCheckedForLoaderManager = true;
        mLoaderManager = mHost.getControllerManager(mWho, mLoadersStarted, true);
        return mLoaderManager;
    }

    public void dispatchCreate()
    {
        mHost.getPostCreateTask().continueWith(new Continuation<Boolean, Object>()
        {
            @Override
            public Object then(Task<Boolean> task) throws Exception
            {
                if (DEBUG) Log.d("/!", FragmentControllerHost.this + " DISPATCH ON POST CREATE\n===\n");
                if (!mCheckedForLoaderManager)
                {
                    mCheckedForLoaderManager = true;
                    mLoaderManager = mHost.getControllerManager(mWho, mLoadersStarted, false);
                }

                if (mLoaderManager != null)
                {
                    if (DEBUG) Log.d("/!", FragmentControllerHost.this + " DISPATCH ON mLoaderManager\n===\n");
                    mRetainerAttached = true;
                    mHost.getRetainLoadersTask().continueWith(new Continuation<Boolean, Object>()
                    {
                        @Override
                        public Object then(Task<Boolean> task) throws Exception
                        {
                            boolean retain = task.getResult();

                            if (DEBUG) Log.d("/!", FragmentControllerHost.this + " RETAIN SET=" + retain + "\n===\n");
                            if (retain)
                            {
                                mLoaderManager.doRetain();
                            }
                            else
                            {
                                mLoaderManager.doStop();
                            }

                            return null;
                        }
                    });
                }

                return null;
            }
        });
    }

    public void dispatchStart()
    {
        if (!mLoadersStarted)
        {
            mLoadersStarted = true;
            if (!mCheckedForLoaderManager)
            {
                mCheckedForLoaderManager = true;
                mLoaderManager = mHost.getControllerManager(mWho, mLoadersStarted, false);
            }

            if (mLoaderManager != null)
            {
                mLoaderManager.doStart();
            }
        }

        performStart();
    }

    public void dispatchResume()
    {
        if (mLoaderManager != null)
        {
            mLoaderManager.doResume();
        }
    }


    void performStart()
    {
        if (mLoaderManager != null)
        {
            mLoaderManager.doReportStart();
        }
    }


    public void dispatchStop(boolean retain)
    {
        if (mRetainerAttached) return;

        if (mLoadersStarted)
        {
            mLoadersStarted = false;
            if (!mCheckedForLoaderManager)
            {
                mCheckedForLoaderManager = true;
                mLoaderManager = mHost.getControllerManager(mWho, mLoadersStarted, false);
            }

            if (mLoaderManager != null)
            {
                mHost.getRetainLoadersTask().continueWith(new Continuation<Boolean, Object>()
                {
                    @Override
                    public Object then(Task<Boolean> task) throws Exception
                    {
                        boolean retain = task.getResult();
                        if (retain)
                        {
                            mLoaderManager.doRetain();
                        }
                        else
                        {
                            mLoaderManager.doStop();
                        }

                        return null;
                    }
                });
            }
        }
    }

    public void dispatchDestroyView()
    {
        if (mLoaderManager != null)
        {
            mLoaderManager.doReportNextStart();
        }
    }

    public void dispatchDestroy()
    {
        if (!mCheckedForLoaderManager)
        {
            mCheckedForLoaderManager = true;
            mLoaderManager = mHost.getControllerManager(mWho, mLoadersStarted, false);
        }

        if (mLoaderManager != null)
        {
            mLoaderManager.doDestroy();
        }
    }

    @Override
    public String toString()
    {
        return "FragmentControllerHost[" +
                "mWho='" + mWho + '\'' +
                ']';
    }
}
