package com.mobilesolutionworks.android.bolts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.mobilesolutionworks.android.app.WorksController;
import com.mobilesolutionworks.android.app.WorksControllerManager;

import bolts.Continuation;
import bolts.Task;
import bolts.TaskCompletionSource;

/**
 * Created by yunarta on 28/6/16.
 */
public class BoltsWorksController3<Host> extends WorksController {

    private boolean mIsPaused = true;

    private TaskCompletionSource<Void> mDiplayTCS;

    private Host mHost;

    private Handler mHandler;

    public BoltsWorksController3() {
        mDiplayTCS = new TaskCompletionSource<>();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public Handler getHandler() {
        return mHandler;
    }

    public void runOnUIWhenIsReady(final Runnable runnable) {
        if (mIsPaused) {
            getDisplayTCS().continueWith(new Continuation<Void, Object>() {
                @Override
                public Object then(Task<Void> task) throws Exception {
                    runnable.run();
                    return null;
                }
            }, Task.UI_THREAD_EXECUTOR);
        } else {
            Task.UI_THREAD_EXECUTOR.execute(runnable);
        }
    }

    public <T, R> Task<R> addTask(final Task<T> task, Continuation<T, R> continuation) {
        if (getHost() instanceof Fragment) {
            Fragment fragment = (Fragment) getHost();
        }

        return task.continueWithTask(new Continuation<T, Task<T>>() {
            @Override
            public Task<T> then(final Task<T> finished) throws Exception {
                if (mIsPaused) {
                    return getDisplayTCS().continueWithTask(new Continuation<Void, Task<T>>() {
                        @Override
                        public Task<T> then(Task<Void> task) throws Exception {
                            if (task.getError() != null) {
                                Log.d("task", "task.getError()", task.getError());
                            }
                            return finished;
                        }
                    });
                } else {
                    return finished;
                }
            }
        }, Task.UI_THREAD_EXECUTOR).continueWith(continuation);
    }

    public <T> Task<T> createTask(Task<T> task) {
        return task.continueWithTask(new Continuation<T, Task<T>>() {
            @Override
            public Task<T> then(final Task<T> finished) throws Exception {
                if (mIsPaused) {
                    return getDisplayTCS().continueWithTask(new Continuation<Void, Task<T>>() {
                        @Override
                        public Task<T> then(Task<Void> task) throws Exception {
                            return finished;
                        }
                    });
                } else {
                    return finished;
                }
            }
        }, Task.UI_THREAD_EXECUTOR);
    }

    private Task<Void> getDisplayTCS() {
        return mDiplayTCS.getTask();
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

        mDiplayTCS.trySetResult(null);
    }

    protected void onHostUpdated() {

    }

    @Override
    public void onPaused() {
        super.onPaused();

        mIsPaused = true;
        mDiplayTCS = new TaskCompletionSource<>();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mDiplayTCS != null) {
            mDiplayTCS.trySetCancelled();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

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
