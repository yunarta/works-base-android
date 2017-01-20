package com.mobilesolutionworks.android.bolts;

import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;

import com.mobilesolutionworks.android.app.WorksController;
import com.mobilesolutionworks.android.app.WorksControllerManager;
import com.mobilesolutionworks.android.exe.WorksExecutor;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by yunarta on 28/6/16.
 */
public class BoltsWorksController3<Host> extends WorksController {

    private final Observable observable = new Observable();

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
            WorksExecutor.UIExecutor.execute(runnable);
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

    public Host getHost() {
        return mHost;
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsPaused = false;
        observable.notifyObservers();
    }

    protected void onHostUpdated() {

    }

    @Override
    public void onPaused() {
        super.onPaused();
        mIsPaused = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        observable.deleteObservers();
    }

    public static abstract class ControllerCallbacks<Controller extends BoltsWorksController3<Host>, Host> implements WorksControllerManager.ControllerCallbacks<Controller> {

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
