package com.mobilesolutionworks.android.app;

import android.util.Log;

import java.lang.ref.WeakReference;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by yunarta on 17/11/15.
 */
public class FragmentControllerHost
{
    private static final boolean DEBUG = WorksBaseConfig.DEBUG;

    WorksControllerManager mLoaderManager;

    ControllerHostCallback mHost;

    String mWho;

    boolean mCheckedForLoaderManager;

    boolean mRetainerAttached;

    int mHostState;
    int mCheckingState;

    WeakReference<FragmentHostCallback> mFragmentHostCallback;

    public FragmentControllerHost(String who, ActivityControllerHost host)
    {
        mWho = who;
        mHost = host.mHost;
    }

    private void addToRetainerChain()
    {
        if (!mRetainerAttached)
        {
            // we haven't been added to retainer chain, then we check whether we have a controller manager
            final WorksControllerManager loaderManager = mHost.getControllerManager(mWho, mHostState, false);
            if (loaderManager != null)
            {
                mRetainerAttached = true;
                mHost.getRetainLoadersTask().continueWith(new Continuation<Boolean, Object>()
                {
                    @Override
                    public Object then(Task<Boolean> task) throws Exception
                    {
                        if (mFragmentHostCallback != null && mFragmentHostCallback.get() != null)
                        {
                            loaderManager.setFragmentHostCallback(mFragmentHostCallback.get());
                        }

                        boolean retain = task.getResult();
                        if (retain || loaderManager.isFragmentRetaining())
                        {
                            loaderManager.doRetain();
                        }
                        else
                        {
                            loaderManager.doDestroy();
                        }

                        return null;
                    }
                }).continueWith(new Continuation<Object, Object>()
                {
                    @Override
                    public Object then(Task<Object> task) throws Exception
                    {
                        if (task.isFaulted())
                        {
                            Log.d("/!@#", "error", task.getError());
                        }
                        return null;
                    }
                });
            }
        }
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

        try
        {
            mCheckedForLoaderManager = true;

            mLoaderManager = mHost.getControllerManager(mWho, mHostState, true);

            updateStates(mCheckingState, mHostState, mLoaderManager);
            mCheckingState = mHostState;

            return mLoaderManager;
        }
        finally
        {
            // controller manager created, therefore we will put the controller on retain loader chain
            addToRetainerChain();
        }
    }

    /**
     * android.app.Fragment#setRetainInstance(boolean) may be called at any time,
     * then if a loader created we put into retainer chain if retain is true
     *
     * @param retain android.app.Fragment#setRetainInstance(boolean) retain value.
     * @see android.app.Fragment#setRetainInstance(boolean)
     */
    public void dispatchRetainInstance(boolean retain)
    {
        if (!mCheckedForLoaderManager)
        {
            mCheckedForLoaderManager = true;
            mLoaderManager = mHost.getControllerManager(mWho, mHostState, false);
        }

        if (mLoaderManager != null)
        {
            mLoaderManager.mFragmentRetaining = retain;
            addToRetainerChain();
        }
    }

    public void dispatchCreate()
    {
        mHostState = HostState.CREATED;
        mCheckingState = HostState.CREATED;

        // this is post checking for non-retained fragment in back stack where onCreate will be called and we check
        // whether there's a controller manager is created in onCreate() of Fragment
        mHost.getPostCreateTask().continueWith(new Continuation<Boolean, Object>()
        {
            @Override
            public Object then(Task<Boolean> task) throws Exception
            {
                addToRetainerChain();
                return null;
            }
        });
    }

    public void dispatchStart()
    {
        mHostState = HostState.START;

        if (!mCheckedForLoaderManager)
        {
            mCheckedForLoaderManager = true;
            mLoaderManager = mHost.getControllerManager(mWho, mHostState, false);
        }

        if (mLoaderManager != null)
        {
            updateStates(mCheckingState, mHostState, mLoaderManager);
            mCheckingState = mHostState;
        }
    }

    public void dispatchResume()
    {
        mHostState = HostState.RESUME;

        if (!mCheckedForLoaderManager)
        {
            mCheckedForLoaderManager = true;
            mLoaderManager = mHost.getControllerManager(mWho, mHostState, false);
        }

        if (mLoaderManager != null)
        {
            updateStates(mCheckingState, mHostState, mLoaderManager);
            mCheckingState = mHostState;
        }
    }

    public void dispatchPause()
    {
        mHostState = HostState.PAUSED;

        if (!mCheckedForLoaderManager)
        {
            mCheckedForLoaderManager = true;
            mLoaderManager = mHost.getControllerManager(mWho, mHostState, false);
        }

        if (mLoaderManager != null)
        {
            updateStates(mCheckingState, mHostState, mLoaderManager);
            mCheckingState = mHostState;
        }
    }

    public void dispatchStop()
    {
        mHostState = HostState.STOP;

        if (!mCheckedForLoaderManager)
        {
            mCheckedForLoaderManager = true;
            mLoaderManager = mHost.getControllerManager(mWho, mHostState, false);
        }

        if (mLoaderManager != null)
        {
            updateStates(mCheckingState, mHostState, mLoaderManager);
            mCheckingState = mHostState;

            addToRetainerChain();
        }
    }

    public void dispatchDestroyView()
    {
        if (!mCheckedForLoaderManager)
        {
            mCheckedForLoaderManager = true;
            mLoaderManager = mHost.getControllerManager(mWho, mHostState, false);
        }

        if (mLoaderManager != null)
        {
            mLoaderManager.doReportNextStart();
        }
    }

    public void dispatchDestroy()
    {
        mHostState = HostState.DESTROYED;

        if (!mCheckedForLoaderManager)
        {
            mCheckedForLoaderManager = true;
            mLoaderManager = mHost.getControllerManager(mWho, mHostState, false);
        }

        if (mLoaderManager != null)
        {
            if (!mLoaderManager.isRetaining())
            {
                updateStates(mCheckingState, mHostState, mLoaderManager);
                mCheckingState = mHostState;
            }
        }
    }

    private void updateStates(int lastState, int newState, WorksControllerManager manager)
    {
        for (int state = lastState + 1; state <= newState; state++)
        {
            updateState(state, manager);
        }
    }

    private void updateState(int state, WorksControllerManager manager)
    {
        switch (state)
        {
            default:
            case HostState.CREATED:
                break;

            case HostState.START:
                manager.doStart();
//                mLoaderManager.doReportStart();
                break;

            case HostState.RESUME:
                manager.doResume();
                break;

            case HostState.PAUSED:
                manager.doPause();
                break;

            case HostState.STOP:
                manager.doStop();
                break;

            case HostState.DESTROYED:
                manager.doDestroy();
                break;
        }
    }

    @Override
    public String toString()
    {
        return "FragmentControllerHost[mWho='" + mWho + '\'' + ']';
    }

    public void setFragmentHostCallback(FragmentHostCallback fragmentHostCallback)
    {
        mFragmentHostCallback = new WeakReference<>(fragmentHostCallback);
    }
}
