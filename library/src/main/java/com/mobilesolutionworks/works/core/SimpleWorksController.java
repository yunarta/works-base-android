package com.mobilesolutionworks.works.core;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;


import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executor;

/**
 * Created by yunarta on 28/6/16.
 */
public class SimpleWorksController<Host> extends WorksController {

    private static final Executor UIExecutor = command -> new Handler(Looper.getMainLooper()).post(command);

    private final PublicObservable observable = new PublicObservable();

    private boolean mIsPaused = true;

    private Host mHost;

    public void runOnUIWhenIsReady(final Runnable runnable) {
        if (mIsPaused) {
            observable.addObserver(new Observer() {
                @Override
                public void update(Observable o, Object arg) {
                    observable.deleteObserver(this);
                    executeInUIThread(runnable);
                }
            });
        } else {
            executeInUIThread(runnable);
        }
    }

    private void executeInUIThread(final Runnable runnable) {
        if(isInUIThread()) {
            runnable.run();
        } else {
            UIExecutor.execute(runnable);
        }
    }

    private boolean isInUIThread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? Looper.getMainLooper().isCurrentThread()
                : Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    public <T, R> Runnable addTask(Runnable runnable) {
        if (getHost() instanceof Fragment) {
            Fragment fragment = (Fragment) getHost();
        }
        return () -> runOnUIWhenIsReady(runnable);
    }

    public void setHost(Host host) {
        boolean sendUpdate = mHost != host;

        this.mHost = host;
        if (sendUpdate) {
            onHostUpdated();
        }
    }

    protected Host getHost() {
        return mHost;
    }

    @Override
    public void onResume() {
        mIsPaused = false;
        observable.setChanged();
        observable.notifyObservers();
    }

    protected void onHostUpdated() {

    }

    @Override
    public void onPaused() {
        mIsPaused = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        observable.deleteObservers();
    }

    private static final class PublicObservable extends Observable {

        @Override
        public void setChanged() {
            super.setChanged();
        }

    }

    public static abstract class ControllerCallbacks<Controller extends SimpleWorksController<Host>, Host> implements WorksSupportControllerManager.ControllerCallbacks<Controller> {

        private Host mHost;

        public ControllerCallbacks(Host host) {
            mHost = host;
        }

        @Override
        public void onLoadFinished(int id, Bundle bundle, Controller controller) {
            controller.setHost(mHost);
        }

        @Override
        public void onLoaderReset(Controller loader) {

        }
    }
}
