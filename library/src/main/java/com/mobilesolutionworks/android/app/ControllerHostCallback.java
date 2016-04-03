package com.mobilesolutionworks.android.app;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.mobilesolutionworks.android.app.v4.SimpleArrayMap;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import bolts.Task;
import bolts.TaskCompletionSource;

/**
 * Created by yunarta on 16/11/15.
 */
public class ControllerHostCallback {

    private static final boolean DEBUG = WorksBaseConfig.DEBUG;

    final Activity mActivity;

    final Context mContext;

    final Handler mHandler;

    SimpleArrayMap<String, WorksControllerManager> mAllLoaderManagers;

    private boolean mRetainLoaders;

    private TaskCompletionSource<Boolean> mRetainLoadersTCS;

    private TaskCompletionSource<Boolean> mCheckLoaderTCS;

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

    ControllerHostCallback(Activity activity, Handler handler) {
        mActivity = activity;
        mContext = activity;
        mHandler = handler;

        mRetainLoadersTCS = new TaskCompletionSource<>();
        mCheckLoaderTCS = new TaskCompletionSource<>();
    }

//    public abstract Host onGetHost();

    WorksControllerManager getControllerManager() {
        if (mLoaderManager != null) {
            return mLoaderManager;
        }

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
            lm.updateState(state);
            lm.updateHostController(this);
        }
        return lm;
    }

    void inactivateFragment(String who) {
        //Log.v(TAG, "invalidateSupportFragment: who=" + who);
        if (mAllLoaderManagers != null) {
            WorksControllerManager lm = mAllLoaderManagers.get(who);
            if (lm != null && !lm.isRetaining()) {
                lm.doDestroy(false);
                mAllLoaderManagers.remove(who);
            }
        }
    }

    /// this is called in fragment
    boolean getRetainLoaders() {
        return mRetainLoaders;
    }

    @NonNull
    Task<Boolean> getRetainLoadersTask() {
        return mRetainLoadersTCS.getTask();
    }

    void reportControllerPostCreate() {
        mCheckLoaderTCS.trySetResult(true);
    }

    Task<Boolean> getPostCreateTask() {
        return mCheckLoaderTCS.getTask();
    }

    void doControllerStart() {
        if (!mLoadersStarted) {
            mLoadersStarted = true;

            if (mLoaderManager != null) {
                mLoaderManager.doStart();
            } else if (!mCheckedForLoaderManager) {
                mLoaderManager = getControllerManager("(root)", mHostState, false);

                // the returned loader manager may be a new one, so we have to create it
                if ((mLoaderManager != null) && (!mLoaderManager.mStarted)) {
                    mLoaderManager.doStart();
                }
            }
            mCheckedForLoaderManager = true;
        }
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
        mRetainLoadersTCS.trySetResult(mRetainLoaders);

        ArrayList<WeakReference<FragmentControllerHost>> missings = new ArrayList<>();
        for (WeakReference<FragmentControllerHost> host : mFragmentControllerHosts) {
            FragmentControllerHost controllerHost = host.get();
            if (controllerHost == null) {
                missings.add(host);
            } else {
                controllerHost.dispatchReallyStop(retain);
            }
        }

        mFragmentControllerHosts.removeAll(missings);

        if (mLoaderManager != null) {
            if (mLoadersStarted) {
                mLoadersStarted = false;

                if (retain) {
                    mLoaderManager.doRetain();
                } else {
                    mLoaderManager.doStop();
                }
            }
        }
    }

    @Deprecated
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
                if (lm.isRetaining()) {
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

    // onCreate
    void restoreLoaderNonConfig(SimpleArrayMap<String, WorksControllerManager> loaderManagers) {
        mAllLoaderManagers = loaderManagers;
    }

    public void doAllLoaderDestroy() {
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
                lm.doDestroy(true);
            }
        }
    }

    List<WeakReference<FragmentControllerHost>> mFragmentControllerHosts = new ArrayList<>();

    public void addFragmentControllerHost(FragmentControllerHost host) {
        mFragmentControllerHosts.add(new WeakReference<>(host));
    }
}