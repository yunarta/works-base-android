package com.mobilesolutionworks.android.app;

import android.os.Bundle;
import android.util.SparseArray;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.logging.Level;

/**
 * Created by yunarta on 16/11/15.
 */
public class WorksControllerManagerImpl extends WorksControllerManager
{
    final class ControllerInfo // implements WorksController.OnLoadCompleteListener<Object>, WorksController.OnLoadCanceledListener<Object>
    {
        final int    mId;
        final Bundle mArgs;
        WorksControllerManager.LoaderCallbacks<WorksController> mCallbacks;
        WorksController                                         mLoader;

//        boolean mHaveData;
//        boolean mDeliveredData;

        @SuppressWarnings("hiding")
        boolean mInfoStarted;
        @SuppressWarnings("hiding")
        boolean mInfoRetaining;
        @SuppressWarnings("hiding")
        boolean mInfoRetainingStarted;
        boolean mInfoReportNextStart;
        boolean mInfoDestroyed;

//        boolean mListenerRegistered;

        // ControllerInfo mPendingLoader;

        public ControllerInfo(int id, Bundle args, WorksControllerManager.LoaderCallbacks<WorksController> callbacks)
        {
            mId = id;
            mArgs = args;
            mCallbacks = callbacks;
        }

        void start()
        {
            if (mInfoRetaining && mInfoRetainingStarted)
            {
                // Our owner is started, but we were being retained from a
                // previous instance in the started state...  so there is really
                // nothing to do here, since the loaders are still started.
                mInfoStarted = true;
                return;
            }

            if (mInfoStarted)
            {
                // If loader already started, don't restart.
                return;
            }

            mInfoStarted = true;

            if (DEBUG) LOGGER.fine("  Starting: " + this);
            if (mLoader == null && mCallbacks != null)
            {
                mLoader = mCallbacks.onCreateLoader(mId, mArgs);
                mLoader.onCreate();
            }

            if (mLoader != null && mCallbacks != null)
            {
                mCallbacks.onLoadFinished(mId, mLoader);
            }

            if (mLoader != null/* && !mInfoReportNextStart*/)
            {
//                if (mLoader.getClass().isMemberClass() && !Modifier.isStatic(mLoader.getClass().getModifiers()))
//                {
//                    throw new IllegalArgumentException("Object returned from onCreateLoader must not be a non-static inner member class: " + mLoader);
//                }

//                if (!mListenerRegistered)
//                {
////                    mLoader.registerListener(mId, this);
////                    mLoader.registerOnLoadCanceledListener(this);
//                    mListenerRegistered = true;
//                }
//                mLoader.startLoading();
                mLoader.onStart();
            }
        }

        void retain()
        {
            if (DEBUG) LOGGER.fine("  Retaining: " + this);

            if (mLoader != null)
            {
                mLoader.onPaused();
            }

            mInfoRetaining = true;
            mInfoRetainingStarted = mInfoStarted;
            mInfoStarted = false;
            mCallbacks = null;
        }

        void finishRetain()
        {
            if (mInfoRetaining)
            {
                if (DEBUG) LOGGER.fine("  Finished Retaining: " + this);
                mInfoRetaining = false;
                if (mInfoStarted != mInfoRetainingStarted)
                {
                    if (!mInfoStarted)
                    {
                        // This loader was retained in a started state, but
                        // at the end of retaining everything our owner is
                        // no longer started...  so make it stop.
                        stop();
                    }
                }
            }

            if (mInfoStarted && /*mHaveData &&*/ !mInfoReportNextStart)
            {
                // This loader has retained its data, either completely across
                // a configuration change or just whatever the last data set
                // was after being restarted from a stop, and now at the point of
                // finishing the retain we find we remain started, have
                // our data, and the owner has a new callback...  so
                // let's deliver the data now.
                callOnLoadFinished(mLoader);
            }
        }

        void reportStart()
        {
            if (mInfoStarted)
            {
                if (mInfoReportNextStart)
                {
                    mInfoReportNextStart = false;
//                    if (mHaveData)
//                    {
//                        callOnLoadFinished(mLoader, mData);
//                    }
                }
            }
        }

        public void resume()
        {
            if (mLoader != null)
            {
                mLoader.onResume();
            }
        }

        void stop()
        {
            if (DEBUG) LOGGER.fine("  Stopping: " + this);
            mInfoStarted = false;
            if (!mInfoRetaining)
            {
                if (mLoader != null/* && mListenerRegistered*/)
                {
                    // Let the loader know we're done with it
//                    mListenerRegistered = false;
//                    mLoader.unregisterListener(this);
//                    mLoader.unregisterOnLoadCanceledListener(this);
//                    mLoader.stopLoading();
                    mLoader.onStop();
                }
            }
        }

//        void cancel()
//        {
//            if (DEBUG) LOGGER.fine("  Canceling: " + this);
//            if (mStarted && mLoader != null && mListenerRegistered)
//            {
//                if (!mLoader.cancelLoad())
//                {
//                    onLoadCanceled(mLoader);
//                }
//            }
//        }

        void destroy()
        {
            if (DEBUG) LOGGER.fine("  Destroying: " + this);
            mInfoDestroyed = true;
//            boolean needReset = mDeliveredData;
//            mDeliveredData = false;
            if (mCallbacks != null && mLoader != null/* && mHaveData && needReset*/)
            {
                if (DEBUG) LOGGER.fine("  Reseting: " + this);
                String lastBecause = null;
                if (mHost != null)
                {
//                    todo
//                    lastBecause = mHost.mFragmentManager.mNoTransactionsBecause;
//                    mHost.mFragmentManager.mNoTransactionsBecause = "onLoaderReset";
                }
                try
                {
                    mCallbacks.onLoaderReset(mLoader);
                }
                finally
                {
                    if (mHost != null)
                    {
//                        todo
//                        mHost.mFragmentManager.mNoTransactionsBecause = lastBecause;
                    }
                }
            }
            mCallbacks = null;
//            mData = null;
//            mHaveData = false;
            if (mLoader != null)
            {
//                if (mListenerRegistered)
//                {
//                    mListenerRegistered = false;
////                    mLoader.unregisterListener(this);
////                    mLoader.unregisterOnLoadCanceledListener(this);
//                }
//                mLoader.reset();
                mLoader.onDestroy();
            }

//            if (mPendingLoader != null)
//            {
//                mPendingLoader.destroy();
//            }
        }

//        public void onLoadCanceled(WorksController<Object> loader)
//        {
//            if (DEBUG) LOGGER.fine("onLoadCanceled: " + this);
//
//            if (mDestroyed)
//            {
//                if (DEBUG) LOGGER.fine("  Ignoring load canceled -- destroyed");
//                return;
//            }
//
//            if (mLoaders.get(mId) != this)
//            {
//                // This cancellation message is not coming from the current active loader.
//                // We don't care about it.
//                if (DEBUG) LOGGER.fine("  Ignoring load canceled -- not active");
//                return;
//            }
//
//            ControllerInfo pending = mPendingLoader;
//            if (pending != null)
//            {
//                // There is a new request pending and we were just
//                // waiting for the old one to cancel or complete before starting
//                // it.  So now it is time, switch over to the new loader.
//                if (DEBUG) LOGGER.fine("  Switching to pending loader: " + pending);
//                mPendingLoader = null;
//                mLoaders.put(mId, null);
//                destroy();
//                installLoader(pending);
//            }
//        }

//        public void onLoadComplete(WorksController<Object> loader, Object data)
//        {
//            if (DEBUG) LOGGER.fine("onLoadComplete: " + this);
//
//            if (mDestroyed)
//            {
//                if (DEBUG) LOGGER.fine("  Ignoring load complete -- destroyed");
//                return;
//            }
//
//            if (mLoaders.get(mId) != this)
//            {
//                // This data is not coming from the current active loader.
//                // We don't care about it.
//                if (DEBUG) LOGGER.fine("  Ignoring load complete -- not active");
//                return;
//            }
//
//            ControllerInfo pending = mPendingLoader;
//            if (pending != null)
//            {
//                // There is a new request pending and we were just
//                // waiting for the old one to complete before starting
//                // it.  So now it is time, switch over to the new loader.
//                if (DEBUG) LOGGER.fine("  Switching to pending loader: " + pending);
//                mPendingLoader = null;
//                mLoaders.put(mId, null);
//                destroy();
//                installLoader(pending);
//                return;
//            }
//
//            // Notify of the new data so the app can switch out the old data before
//            // we try to destroy it.
//            if (mData != data || !mHaveData)
//            {
//                mData = data;
//                mHaveData = true;
//                if (mStarted)
//                {
//                    callOnLoadFinished(loader, data);
//                }
//            }
//
//            //if (DEBUG) LOGGER.fine( "  onLoadFinished returned: " + this);
//
//            // We have now given the application the new loader with its
//            // loaded data, so it should have stopped using the previous
//            // loader.  If there is a previous loader on the inactive list,
//            // clean it up.
//            ControllerInfo info = mInactiveLoaders.get(mId);
//            if (info != null && info != this)
//            {
//                info.mDeliveredData = false;
//                info.destroy();
//                mInactiveLoaders.remove(mId);
//            }
//
//            if (mHost != null && !hasRunningLoaders())
//            {
////                todo
////                mHost.mFragmentManager.startPendingDeferredFragments();
//            }
//        }

        void callOnLoadFinished(WorksController loader)
        {
            if (mCallbacks != null)
            {
                String lastBecause = null;
                if (mHost != null)
                {
//                    todo
//                    lastBecause = mHost.mFragmentManager.mNoTransactionsBecause;
//                    mHost.mFragmentManager.mNoTransactionsBecause = "onLoadFinished";
                }
                try
                {
                    if (DEBUG)
                    {
                        LOGGER.fine("  onLoadFinished in " + loader);
                    }
                    mCallbacks.onLoadFinished(mId, loader);
                }
                finally
                {
                    if (mHost != null)
                    {
//                        todo
//                        mHost.mFragmentManager.mNoTransactionsBecause = lastBecause;
                    }
                }
//                mDeliveredData = true;
                if (loader != null)
                {
                    loader.onStart();
                }
            }
        }

        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder(64);
            sb.append("ControllerInfo{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(" #");
            sb.append(mId);
            sb.append(" : ");
//            DebugUtils.buildShortClassTag(mLoader, sb);
            sb.append("}}");
            return sb.toString();
        }

        public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args)
        {
            writer.print(prefix);
            writer.print("mId=");
            writer.print(mId);
            writer.print(" mArgs=");
            writer.println(mArgs);
            writer.print(prefix);
            writer.print("mCallbacks=");
            writer.println(mCallbacks);
            writer.print(prefix);

            writer.print("mLoader=");
            writer.println(mLoader);

//            if (mLoader != null)
//            {
//                mLoader.dump(prefix + "  ", fd, writer, args);
//            }

//            if (mHaveData || mDeliveredData)
//            {
//                writer.print(prefix);
//                writer.print("mHaveData=");
//                writer.print(mHaveData);
//                writer.print("  mDeliveredData=");
//                writer.println(mDeliveredData);
//                writer.print(prefix);
//                writer.print("mData=");
//                writer.println(mData);
//            }

            writer.print(prefix);
            writer.print("mStarted=");
            writer.print(mInfoStarted);
            writer.print(" mReportNextStart=");
            writer.print(mInfoReportNextStart);
            writer.print(" mDestroyed=");
            writer.println(mInfoDestroyed);
            writer.print(prefix);
            writer.print("mRetaining=");
            writer.print(mInfoRetaining);
            writer.print(" mRetainingStarted=");
            writer.print(mInfoRetainingStarted);
//            writer.print(" mListenerRegistered=");
//            writer.println(mListenerRegistered);

//            if (mPendingLoader != null)
//            {
//                writer.print(prefix);
//                writer.println("Pending Loader ");
//                writer.print(mPendingLoader);
//                writer.println(":");
//                mPendingLoader.dump(prefix + "  ", fd, writer, args);
//            }
        }
    }

    // These are the currently active loaders.  A loader is here
    // from the time its load is started until it has been explicitly
    // stopped or restarted by the application.
    final SparseArray<ControllerInfo> mLoaders = new SparseArray<ControllerInfo>();

    // These are previously run loaders.  This list is maintained internally
    // to avoid destroying a loader while an application is still using it.
    // It allows an application to restart a loader, but continue using its
    // previously run loader until the new loader's data is available.
    final SparseArray<ControllerInfo> mInactiveLoaders = new SparseArray<ControllerInfo>();

    boolean mRetainingStarted;

    boolean mCreatingLoader;

    public WorksControllerManagerImpl(String who, ControllerHostCallback host, boolean started)
    {
        super(host, who);
    }

    private ControllerInfo createLoader(int id, Bundle args, WorksControllerManager.LoaderCallbacks<WorksController> callback)
    {
        ControllerInfo  info   = new ControllerInfo(id, args, callback);
        WorksController loader = callback.onCreateLoader(id, args);
        info.mLoader = loader;
        loader.onCreate();

        return info;
    }

    private ControllerInfo createAndInstallLoader(int id, Bundle args,
                                                  WorksControllerManager.LoaderCallbacks<WorksController> callback)
    {
        try
        {
            mCreatingLoader = true;

            ControllerInfo info = createLoader(id, args, callback);
            installLoader(info);
            return info;
        }
        finally
        {
            mCreatingLoader = false;
        }
    }

    void installLoader(ControllerInfo info)
    {
        mLoaders.put(info.mId, info);
        if (mStarted)
        {
            // The activity will start all existing loaders in it's onStart(),
            // so only start them here if we're past that point of the activitiy's
            // life cycle
            info.start();
        }
    }

    public <D extends WorksController> D initLoader(int id, Bundle args, WorksControllerManager.LoaderCallbacks<WorksController> callback)
    {
        if (mCreatingLoader)
        {
            throw new IllegalStateException("Called while creating a loader");
        }

        ControllerInfo info = mLoaders.get(id);

        if (DEBUG) LOGGER.fine("initLoader in " + this + ": args=" + args);

        if (info == null)
        {
            // Loader doesn't already exist; create.
            info = createAndInstallLoader(id, args, callback);
            if (DEBUG) LOGGER.fine("  Created new loader " + info);
        }
        else
        {
            if (DEBUG) LOGGER.fine("  Re-using existing loader " + info);
            info.mCallbacks = callback;
        }

        if (/*info.mHaveData && */mStarted)
        {
            // If the loader has already generated its data, report it now.
            info.callOnLoadFinished(info.mLoader);
        }

        return (D) info.mLoader;
    }

//    public <D> WorksController<D> restartLoader(int id, Bundle args, WorksControllerManager.LoaderCallbacks<D> callback)
//    {
//        if (mCreatingLoader)
//        {
//            throw new IllegalStateException("Called while creating a loader");
//        }
//
//        ControllerInfo info = mLoaders.get(id);
//        if (info != null)
//        {
//            ControllerInfo inactive = mInactiveLoaders.get(id);
//            if (inactive != null)
//            {
//                if (info.mHaveData)
//                {
//                    // This loader now has data...  we are probably being
//                    // called from within onLoadComplete, where we haven't
//                    // yet destroyed the last inactive loader.  So just do
//                    // that now.
//                    if (DEBUG) LOGGER.fine("  Removing last inactive loader: " + info);
//                    inactive.mDeliveredData = false;
//                    inactive.destroy();
//                    info.mLoader.abandon();
//                    mInactiveLoaders.put(id, info);
//                }
//                else
//                {
//                    // We already have an inactive loader for this ID that we are
//                    // waiting for!  What to do, what to do...
//                    if (!info.mStarted)
//                    {
//                        // The current Loader has not been started...  we thus
//                        // have no reason to keep it around, so bam, slam,
//                        // thank-you-ma'am.
//                        if (DEBUG) LOGGER.fine("  Current loader is stopped; replacing");
//                        mLoaders.put(id, null);
//                        info.destroy();
//                    }
//                    else
//                    {
//                        // Now we have three active loaders... we'll queue
//                        // up this request to be processed once one of the other loaders
//                        // finishes or is canceled.
//                        if (DEBUG) LOGGER.fine("  Current loader is running; attempting to cancel");
//                        info.cancel();
//                        if (info.mPendingLoader != null)
//                        {
//                            if (DEBUG) LOGGER.fine("  Removing pending loader: " + info.mPendingLoader);
//                            info.mPendingLoader.destroy();
//                            info.mPendingLoader = null;
//                        }
//                        if (DEBUG) LOGGER.fine("  Enqueuing as new pending loader");
//                        info.mPendingLoader = createLoader(id, args, (WorksControllerManager.LoaderCallbacks<Object>) callback);
//                        return (WorksController<D>) info.mPendingLoader.mLoader;
//                    }
//                }
//            }
//            else
//            {
//                // Keep track of the previous instance of this loader so we can destroy
//                // it when the new one completes.
//                if (DEBUG) LOGGER.fine("  Making last loader inactive: " + info);
//                info.mLoader.abandon();
//                mInactiveLoaders.put(id, info);
//            }
//        }
//
//        info = createAndInstallLoader(id, args, (WorksControllerManager.LoaderCallbacks<Object>) callback);
//        return (WorksController<D>) info.mLoader;
//    }

    public void destroyLoader(int id)
    {
        if (mCreatingLoader)
        {
            throw new IllegalStateException("Called while creating a loader");
        }

        if (DEBUG) LOGGER.fine("destroyLoader in " + this + " of " + id);
        int idx = mLoaders.indexOfKey(id);
        if (idx >= 0)
        {
            ControllerInfo info = mLoaders.valueAt(idx);
            mLoaders.removeAt(idx);
            info.destroy();
        }
        idx = mInactiveLoaders.indexOfKey(id);
        if (idx >= 0)
        {
            ControllerInfo info = mInactiveLoaders.valueAt(idx);
            mInactiveLoaders.removeAt(idx);
            info.destroy();
        }
        if (mHost != null && !hasRunningLoaders())
        {
            // todo: mHost.mFragmentManager.startPendingDeferredFragments();
        }
    }

    public <D extends WorksController> D getLoader(int id)
    {
        if (mCreatingLoader)
        {
            throw new IllegalStateException("Called while creating a loader");
        }

        ControllerInfo controllerInfo = mLoaders.get(id);
        if (controllerInfo != null)
        {
//            if (controllerInfo.mPendingLoader != null)
//            {
//                return (WorksController<D>) controllerInfo.mPendingLoader.mLoader;
//            }

            return (D) controllerInfo.mLoader;
        }
        return null;
    }

    public boolean hasRunningLoaders()
    {
        boolean   loadersRunning = false;
        final int count          = mLoaders.size();
        for (int i = 0; i < count; i++)
        {
            final ControllerInfo li = mLoaders.valueAt(i);
            loadersRunning |= li.mInfoStarted/* && !li.mDeliveredData*/;
        }
        return loadersRunning;
    }

    @Override
    void doStart()
    {
        if (DEBUG) LOGGER.fine("Starting in " + this);
        if (mStarted)
        {
            RuntimeException e = new RuntimeException("here");
            e.fillInStackTrace();
            LOGGER.log(Level.WARNING, "Called doStart when already started: " + this, e);
            return;
        }

        mStarted = true;

        // Call out to sub classes so they can start their loaders
        // Let the existing loaders know that we want to be notified when a load is complete
        for (int i = mLoaders.size() - 1; i >= 0; i--)
        {
            mLoaders.valueAt(i).start();
        }
    }

    @Override
    void doResume()
    {
        for (int i = mLoaders.size() - 1; i >= 0; i--)
        {
            mLoaders.valueAt(i).resume();
        }
    }

    @Override
    void doStop()
    {
        if (DEBUG) LOGGER.fine("Stopping in " + this);
        if (!mStarted)
        {
            RuntimeException e = new RuntimeException("here");
            e.fillInStackTrace();
            LOGGER.log(Level.WARNING, "Called doStop when not started: " + this, e);
            return;
        }

        for (int i = mLoaders.size() - 1; i >= 0; i--)
        {
            mLoaders.valueAt(i).stop();
        }
        mStarted = false;

    }

    @Override
    void doRetain()
    {
        if (DEBUG) LOGGER.fine("Retaining in " + this);
        if (!mStarted)
        {
            RuntimeException e = new RuntimeException("here");
            e.fillInStackTrace();
            LOGGER.log(Level.WARNING, "Called doRetain when not started: " + this, e);
            return;
        }

        mRetaining = true;
        mStarted = false;
        for (int i = mLoaders.size() - 1; i >= 0; i--)
        {
            mLoaders.valueAt(i).retain();
        }

    }

    @Override
    void finishRetain()
    {
        if (mRetaining)
        {
            if (DEBUG) LOGGER.fine("Finished Retaining in " + this);

            mRetaining = false;
            for (int i = mLoaders.size() - 1; i >= 0; i--)
            {
                mLoaders.valueAt(i).finishRetain();
            }
        }
    }

    @Override
    void doReportNextStart()
    {
        for (int i = mLoaders.size() - 1; i >= 0; i--)
        {
            mLoaders.valueAt(i).mInfoReportNextStart = true;
        }
    }

    @Override
    void doReportStart()
    {
        for (int i = mLoaders.size() - 1; i >= 0; i--)
        {
            mLoaders.valueAt(i).reportStart();
        }
    }

    @Override
    public void doDestroy()
    {
        if (!mRetaining)
        {
            if (DEBUG) LOGGER.fine("Destroying Active in " + this);
            for (int i = mLoaders.size() - 1; i >= 0; i--)
            {
                mLoaders.valueAt(i).destroy();
            }
            mLoaders.clear();
        }

        if (DEBUG) LOGGER.fine("Destroying Inactive in " + this);
        for (int i = mInactiveLoaders.size() - 1; i >= 0; i--)
        {
            mInactiveLoaders.valueAt(i).destroy();
        }
        mInactiveLoaders.clear();
    }
}
