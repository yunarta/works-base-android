package com.mobilesolutionworks.android.app;

import android.support.v4.app.Fragment;

/**
 * Created by yunarta on 17/11/15.
 */
public class FragmentControllerHost {

    private static final boolean DEBUG = WorksBaseConfig.DEBUG;

    WorksControllerManager mLoaderManager;

    ControllerHostCallback mHost;

    String mWho;

    boolean mCheckedForLoaderManager;

    boolean mLoaderStarted;

    boolean mRetaining;

    boolean mRetainerAttached;

    int mHostState;

    int mCheckingState;

//    WeakReference<FragmentHostCallback> mFragmentHostCallback;

//    private Continuation<Boolean, Object> mContinuation;

    public FragmentControllerHost(String who, ActivityControllerHost host) {
        mWho = who;
        mHost = host.mHost;

//        mHost.addFragmentControllerHost(this);
//        mContinuation = new Continuation<Boolean, Object>() {
//            @Override
//            public Object then(Task<Boolean> task) throws Exception {
////                        if (mFragmentHostCallback != null && mFragmentHostCallback.get() != null) {
////                            loaderManager.setFragmentHostCallback(mFragmentHostCallback.get());
////                        }
//
//                boolean retain = task.getResult();
//
//                final WorksControllerManager loaderManager = mHost.getControllerManager(mWho, mHostState, false);
//                if (retain) // || loaderManager.isFragmentRetaining())
//                {
//                    loaderManager.doRetain();
//                } else {
////                            loaderManager.doRelease();
//                    loaderManager.doStop();
////                            loaderManager.doDestroy(false);
//                }
//
//                return null;
//            }
//        };

    }

    private void addToRetainerChain() {
//        if (!mRetainerAttached) {
//            // we haven't been added to retainer chain, then we check whether we have a controller manager
//            final WorksControllerManager loaderManager = mHost.getControllerManager(mWho, mHostState, false);
//            if (loaderManager != null) {
//                mRetainerAttached = true;
//                mHost.getRetainLoadersTask().continueWith(mContinuation);
//            }
//        }
    }

    /**
     * Return the LoaderManager for this fragment, creating it if needed.
     */
    public WorksControllerManager getControllerManager() {
        if (mLoaderManager != null) {
            return mLoaderManager;
        }

        if (mHost == null) {
            throw new IllegalStateException("Fragment " + FragmentControllerHost.this + " not attached to Activity");
        }

        try {
            mCheckedForLoaderManager = true;

            mLoaderManager = mHost.getControllerManager(mWho, mHostState, true);

            updateStates(mCheckingState, mHostState, mLoaderManager);
            mCheckingState = mHostState;

            return mLoaderManager;
        } finally {
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
    public void dispatchRetainInstance(boolean retain) {
//        if (!mCheckedForLoaderManager)
//        {
//            mCheckedForLoaderManager = true;
//            mLoaderManager = mHost.getControllerManager(mWho, mHostState, false);
//        }
//
//        if (mLoaderManager != null)
//        {
//            mLoaderManager.mFragmentRetaining = retain;
//            addToRetainerChain();
//        }
    }

    public void dispatchCreate() {
        mHostState = HostState.CREATED;
        mCheckingState = HostState.CREATED;

//        // this is post checking for non-retained fragment in back stack where onCreate will be called and we check
//        // whether there's a controller manager is created in onCreate() of Fragment
//        mHost.getPostCreateTask().continueWith(new Continuation<Boolean, Object>()
//        {
//            @Override
//            public Object then(Task<Boolean> task) throws Exception
//            {
//                addToRetainerChain();
//                return null;
//            }
//        });
    }

    public void dispatchStart() {
        mHostState = HostState.START;
        if (!mLoaderStarted) {
            mLoaderStarted = true;

            if (!mCheckedForLoaderManager) {
                mCheckedForLoaderManager = true;
                mLoaderManager = mHost.getControllerManager(mWho, mHostState, false);
            }

            if (mLoaderManager != null) {
                updateStates(mCheckingState, mHostState, mLoaderManager);
                mCheckingState = mHostState;
            }
        }
    }

    public void dispatchResume() {
        mHostState = HostState.RESUME;

        if (!mCheckedForLoaderManager) {
            mCheckedForLoaderManager = true;
            mLoaderManager = mHost.getControllerManager(mWho, mHostState, false);
        }

        if (mLoaderManager != null) {
            updateStates(mCheckingState, mHostState, mLoaderManager);
            mCheckingState = mHostState;
        }
    }

    public void dispatchPause() {
        mHostState = HostState.PAUSED;

        if (!mCheckedForLoaderManager) {
            mCheckedForLoaderManager = true;
            mLoaderManager = mHost.getControllerManager(mWho, mHostState, false);
        }

        if (mLoaderManager != null) {
            updateStates(mCheckingState, mHostState, mLoaderManager);
            mCheckingState = mHostState;
        }
    }

    public void dispatchStop() {
//        if (mLoaderStarted) {
//            mLoaderStarted = false;
//            if (!mCheckedForLoaderManager) {
//                mCheckedForLoaderManager = true;
//                mLoaderManager = mHost.getControllerManager(mWho, mHostState, false);
//            }
//
//            if (mLoaderManager != null) {
//                if (retaining) {
//                    mLoaderManager.doRetain();
//                } else {
//                    mLoaderManager.doDestroy(false);
//                }
//            }
//        }


//        mHostState = HostState.STOP;
//
//        if (!mCheckedForLoaderManager)
//        {
//            mCheckedForLoaderManager = true;
//            mLoaderManager = mHost.getControllerManager(mWho, mHostState, false);
//        }
//
//        if (mLoaderManager != null)
//        {
//            updateStates(mCheckingState, mHostState, mLoaderManager);
//            mCheckingState = mHostState;
//
//            addToRetainerChain();
//        }
    }

    public void dispatchDestroyView() {
//        if (!mCheckedForLoaderManager)
//        {
//            mCheckedForLoaderManager = true;
//            mLoaderManager = mHost.getControllerManager(mWho, mHostState, false);
//        }
//
//        if (mLoaderManager != null)
//        {
//            mLoaderManager.doReportNextStart();
//        }
    }

    public void dispatchDestroy() {
        mHostState = HostState.DESTROYED;

        if (!mCheckedForLoaderManager) {
            mCheckedForLoaderManager = true;
            mLoaderManager = mHost.getControllerManager(mWho, mHostState, false);
        }

        if (mLoaderManager != null) {
            mLoaderManager.doDestroy(false);
//            if (!mLoaderManager.isRetaining())
//            {
//                updateStates(mCheckingState, mHostState, mLoaderManager);
//                mCheckingState = mHostState;
//            }
        }
    }

    private void updateStates(int lastState, int newState, WorksControllerManager manager) {
//        for (int state = lastState + 1; state <= newState; state++) {
//            updateState(state, manager);
//        }
        if (lastState != newState)
            updateState(newState, manager);
    }

    private void updateState(int state, WorksControllerManager manager) {
        switch (state) {
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
                manager.doDestroy(false);
                break;
        }
    }

    @Override
    public String toString() {
        return "FragmentControllerHost[mWho='" + mWho + '\'' + ']';
    }

//    public void setFragmentHostCallback(FragmentHostCallback fragmentHostCallback) {
//        mFragmentHostCallback = new WeakReference<>(fragmentHostCallback);
//    }

    public void dispatchReallyStop(boolean retain) {
        final WorksControllerManager loaderManager = mHost.getControllerManager(mWho, mHostState, false);
        if (loaderManager != null) {
            if (retain) {
                loaderManager.doRetain();
            } else {
                loaderManager.doStop();
            }
        }
    }

    /**
     * @see @{@link android.support.v4.app.FragmentManagerImpl#moveToState(Fragment, int, int, int, boolean)}
     * @see @{@link android.support.v4.app.Fragment#performReallyStop()}
     */
    public void v2performReallyStop() {
        final WorksControllerManager loaderManager = mHost.getControllerManager(mWho, mHostState, false);
        if (loaderManager != null) {
            if (mHost.getRetainLoaders()) {
                loaderManager.doRetain();
            } else {
                loaderManager.doStop();
            }
        }
    }
}
