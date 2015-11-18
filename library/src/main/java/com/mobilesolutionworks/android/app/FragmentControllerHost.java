package com.mobilesolutionworks.android.app;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by yunarta on 17/11/15.
 */
public class FragmentControllerHost
{
    private ControllerHostCallback mHost;

    WorksControllerManager mLoaderManager;

    boolean mLoadersStarted;

    boolean mCheckedForLoaderManager;

    private String mWho;

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
            throw new IllegalStateException("Fragment " + this + " not attached to Activity");
        }
        mCheckedForLoaderManager = true;
        mLoaderManager = mHost.getControllerManager(mWho, mLoadersStarted, true);
        return mLoaderManager;
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

//                if (/*retain || */mHost.getRetainLoaders())
//                {
//                    mLoaderManager.doRetain();
//                }
//                else
//                {
//                    mLoaderManager.doStop();
//                }
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
}
