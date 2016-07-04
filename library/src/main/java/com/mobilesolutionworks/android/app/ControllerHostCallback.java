package com.mobilesolutionworks.android.app;

import android.app.Activity;
import android.content.Context;
import android.support.v4.util.SimpleArrayMap;

/**
 * Created by yunarta on 16/11/15.
 */
public class ControllerHostCallback {

    private final Activity mActivity;

    private final Context mContext;

    /**
     * Controller for individual fragments.
     */
    private SimpleArrayMap<String, WorksControllerManager> mAllLoaderManagers;

    /**
     * Whether or not fragment loader should retain their state.
     */
    private boolean mRetainLoaders;

    /**
     * The controller manager for the fragment host
     */
    private WorksControllerManager mLoaderManager;
    private boolean mCheckedForLoaderManager;

    /**
     * Whether or not the fragment host controller manager was started
     */
    private boolean mLoadersStarted;

    private int mHostState;

    ControllerHostCallback(Activity activity) {
        mActivity = activity;
        mContext = activity;
    }

//    public abstract Host onGetHost();

    WorksControllerManager getControllerManager() {
        if (mLoaderManager != null) {
            return mLoaderManager;
        }

        // WORKS: state not being used
        mCheckedForLoaderManager = true;
        mLoaderManager = getControllerManager("(root)", mHostState, true /*create*/);
        return mLoaderManager;
    }

    WorksControllerManager getControllerManager(String who, int state, boolean create) {
        if (mAllLoaderManagers == null) {
            mAllLoaderManagers = new SimpleArrayMap<>();
        }

        WorksControllerManager lm = mAllLoaderManagers.get(who);
        if (lm == null) {
            if (create) {
                lm = new WorksControllerManagerImpl(who, this, state);
                mAllLoaderManagers.put(who, lm);
            }
        } else {
            // WORKS: state not being used
            lm.updateState(state);
            lm.updateHostController(this);
        }

        return lm;
    }

    // this is called in fragment
    boolean getRetainLoaders() {
        return mRetainLoaders;
    }

    void doControllerStart() {
        if (mLoadersStarted) {
            return;
        }
        mLoadersStarted = true;

        if (mLoaderManager != null) {
            mLoaderManager.doStart();
        } else if (!mCheckedForLoaderManager) {
            mLoaderManager = getControllerManager("(root)", mHostState, false);
            // the returned controller manager may be a new one, so we have to create it
            if ((mLoaderManager != null) && (!mLoaderManager.mStarted)) {
                mLoaderManager.doStart();
            }
        }
        mCheckedForLoaderManager = true;
    }

    void reportControllerStart() {
        if (mAllLoaderManagers != null) {
            final int N = mAllLoaderManagers.size();
            WorksControllerManager loaders[] = new WorksControllerManager[N];

            for (int i = N - 1; i >= 0; i--) {
                loaders[i] = mAllLoaderManagers.valueAt(i);
            }

            for (int i = 0; i < N; i++) {
                WorksControllerManager lm = loaders[i];
                lm.finishRetain();
                lm.doReportStart();
            }
        }
    }

    // retain -- whether to stop the loader or retain it
    void doLoaderStop(boolean retain) {
        mRetainLoaders = retain;

        // ArrayList<WeakReference<FragmentControllerHost>> missings = new ArrayList<>();
        // for (WeakReference<FragmentControllerHost> host : mFragmentControllerHosts) {
        //     FragmentControllerHost controllerHost = host.get();
        //     if (controllerHost == null) {
        //         missings.add(host);
        //     } else {
        //         controllerHost.dispatchReallyStop(retain);
        //     }
        // }
        // mFragmentControllerHosts.removeAll(missings);

        if (mLoaderManager == null) {
            return;
        }

        if (!mLoadersStarted) {
            return;
        }
        mLoadersStarted = false;

        if (retain) {
            mLoaderManager.doRetain();
        } else {
            mLoaderManager.doStop();
        }
    }

    void doLoaderRetain() {
        if (mLoaderManager == null) {
            return;
        }
        mLoaderManager.doRetain();
    }

    void doLoaderDestroy() {
        if (mLoaderManager != null) {
            mLoaderManager.doDestroy(false);
        }
    }

    // onRetainNonConfigurationInstance
    SimpleArrayMap<String, WorksControllerManager> retainLoaderNonConfig() {
        boolean retainLoaders = false;
        if (mAllLoaderManagers != null) {
            // prune out any loader managers that were already stopped and so
            // have nothing useful to retain.
            final int N = mAllLoaderManagers.size();
            WorksControllerManager loaders[] = new WorksControllerManager[N];
            for (int i = N - 1; i >= 0; i--) {
                loaders[i] = mAllLoaderManagers.valueAt(i);
            }

            for (int i = 0; i < N; i++) {
                WorksControllerManager lm = loaders[i];
                if (lm.mRetaining) {
                    retainLoaders = true;
                } else {
                    lm.doDestroy(false);
                    mAllLoaderManagers.remove(lm.mWho);
                }
            }
        }

        if (retainLoaders) {
            return mAllLoaderManagers;
        }

        return null;
    }

    void restoreLoaderNonConfig(SimpleArrayMap<String, WorksControllerManager> loaderManagers) {
        mAllLoaderManagers = loaderManagers;
    }

    // public void doAllLoaderDestroy() {
    //     if (mAllLoaderManagers != null) {
    //         // prune out any loader managers that were already stopped and so
    //         // have nothing useful to retain.
    //         final int N = mAllLoaderManagers.size();
    //         WorksControllerManager loaders[] = new WorksControllerManager[N];
    //         for (int i = N - 1; i >= 0; i--) {
    //             loaders[i] = mAllLoaderManagers.valueAt(i);
    //         }
    //
    //         for (int i = 0; i < N; i++) {
    //             WorksControllerManager lm = loaders[i];
    //             lm.doDestroy(true);
    //         }
    //     }
    // }

    // List<WeakReference<FragmentControllerHost>> mFragmentControllerHosts = new ArrayList<>();

    // public void addFragmentControllerHost(FragmentControllerHost host) {
    //     mFragmentControllerHosts.add(new WeakReference<>(host));
    // }
}