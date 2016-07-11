package com.mobilesolutionworks.android.bolts;

import android.os.Bundle;

import com.mobilesolutionworks.android.app.WorksController;
import com.mobilesolutionworks.android.app.WorksControllerManager;

import bolts.Continuation;
import bolts.Task;
import bolts.TaskCompletionSource;

/**
 * Created by yunarta on 28/6/16.
 */
public class BoltsWorksController3<Host> extends WorksController {

    private boolean mIsPaused;

    private TaskCompletionSource<Void> mDiplayTCS;

    private Host mHost;

    public BoltsWorksController3() {
        mDiplayTCS = new TaskCompletionSource<>();
    }

    public <T, R> Task<R> addTask(Task<T> task, Continuation<T, R> continuation) {
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
