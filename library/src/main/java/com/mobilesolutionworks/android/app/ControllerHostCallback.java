package com.mobilesolutionworks.android.app;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.mobilesolutionworks.android.app.v4.SimpleArrayMap;

/**
 * Created by yunarta on 16/11/15.
 */
public class ControllerHostCallback // <Host>
{
    final Activity mActivity;

    final Context mContext;

    final Handler mHandler;

    SimpleArrayMap<String, WorksControllerManager> mAllLoaderManagers;

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

    ControllerHostCallback(Activity activity, Context context, Handler handler)
    {
        mActivity = activity;
        mContext = context;
        mHandler = handler;
    }

//    public abstract Host onGetHost();

    WorksControllerManager getControllerManager()
    {
        if (mLoaderManager != null)
        {
            return mLoaderManager;
        }

        mCheckedForLoaderManager = true;
        mLoaderManager = getControllerManager("(root)", mLoadersStarted, true /*create*/);
        return mLoaderManager;
    }

    WorksControllerManager getControllerManager(String who, boolean started, boolean create)
    {
        if (mAllLoaderManagers == null)
        {
            mAllLoaderManagers = new SimpleArrayMap<>();
        }

        WorksControllerManager lm = mAllLoaderManagers.get(who);
        if (lm == null)
        {
            if (create)
            {
                lm = new WorksControllerManagerImpl(who, this, started);
                mAllLoaderManagers.put(who, lm);
            }
        }
        else
        {
            lm.updateHostController(this);
        }
        return lm;
    }

    void inactivateFragment(String who)
    {
        //Log.v(TAG, "invalidateSupportFragment: who=" + who);
        if (mAllLoaderManagers != null)
        {
            WorksControllerManager lm = (WorksControllerManager) mAllLoaderManagers.get(who);
            if (lm != null && !lm.mRetaining)
            {
                lm.doDestroy();
                mAllLoaderManagers.remove(who);
            }
        }
    }

    /// this is called in fragment
    boolean getRetainLoaders()
    {
        return mRetainLoaders;
    }

    void doControllerStart()
    {
        if (mLoadersStarted)
        {
            return;
        }
        mLoadersStarted = true;

        if (mLoaderManager != null)
        {
            mLoaderManager.doStart();
        }
        else if (!mCheckedForLoaderManager)
        {
            mLoaderManager = getControllerManager("(root)", mLoadersStarted, false);
            // the returned loader manager may be a new one, so we have to start it
            if ((mLoaderManager != null) && (!mLoaderManager.mStarted))
            {
                mLoaderManager.doStart();
            }
        }
        mCheckedForLoaderManager = true;
    }

    void reportControllerStart()
    {
        if (mAllLoaderManagers != null)
        {
            final int N = mAllLoaderManagers.size();
            WorksControllerManager loaders[] = new WorksControllerManager[N];
            for (int i = N - 1; i >= 0; i--)
            {
                loaders[i] = mAllLoaderManagers.valueAt(i);
            }
            for (int i = 0; i < N; i++)
            {
                WorksControllerManager lm = loaders[i];
                lm.finishRetain();
                lm.doReportStart();
            }
        }
    }

    // retain -- whether to stop the loader or retain it
    void doControllerStop(boolean retain)
    {
        mRetainLoaders = retain;

        if (mLoaderManager == null)
        {
            return;
        }

        if (!mLoadersStarted)
        {
            return;
        }
        mLoadersStarted = false;

        if (retain)
        {
            mLoaderManager.doRetain();
        }
        else
        {
            mLoaderManager.doStop();
        }
    }

    @Deprecated
    void doLoaderRetain()
    {
        if (mLoaderManager == null)
        {
            return;
        }
        mLoaderManager.doRetain();
    }

    void doLoaderDestroy()
    {
        if (mLoaderManager == null)
        {
            return;
        }
        mLoaderManager.doDestroy();
    }

    // onRetainNonConfigurationInstance
    SimpleArrayMap<String, WorksControllerManager> retainLoaderNonConfig()
    {
        boolean retainLoaders = false;
        if (mAllLoaderManagers != null)
        {
            // prune out any loader managers that were already stopped and so
            // have nothing useful to retain.
            final int N = mAllLoaderManagers.size();
            WorksControllerManager loaders[] = new WorksControllerManager[N];
            for (int i = N - 1; i >= 0; i--)
            {
                loaders[i] = mAllLoaderManagers.valueAt(i);
            }
            for (int i = 0; i < N; i++)
            {
                WorksControllerManager lm = loaders[i];
                if (lm.mRetaining)
                {
                    retainLoaders = true;
                }
                else
                {
                    lm.doDestroy();
                    mAllLoaderManagers.remove(lm.mWho);
                }
            }
        }

        if (retainLoaders)
        {
            return mAllLoaderManagers;
        }
        return null;
    }

    // onCreate
    void restoreLoaderNonConfig(SimpleArrayMap<String, WorksControllerManager> loaderManagers)
    {
        mAllLoaderManagers = loaderManagers;
    }
}