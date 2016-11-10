package com.mobilesolutionworks.android.bolts;

import android.os.Bundle;

import com.mobilesolutionworks.android.app.WorksController;
import com.mobilesolutionworks.android.app.WorksControllerManager;

import bolts.Continuation;
import bolts.Task;
import bolts.TaskCompletionSource;

/**
 * Created by yunarta on 19/11/15.
 */
public class BoltsWorksController2<Host> extends WorksController {

    boolean mIsPaused;

    TaskCompletionSource<Void> mDisplayTCS;

    protected Host mHost;

    public BoltsWorksController2() {
        mDisplayTCS = new TaskCompletionSource<>();
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
        return mDisplayTCS.getTask();
    }

    public void setHost(Host host) {
        this.mHost = host;
    }

    public Host getHost() {
        return mHost;
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsPaused = false;

        mDisplayTCS.trySetResult(null);
    }

    @Override
    public void onPaused() {
        super.onPaused();

        mIsPaused = true;

        mDisplayTCS = new TaskCompletionSource<>();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mDisplayTCS != null) {
            mDisplayTCS.trySetCancelled();
        }
    }

    public static class ControllerCallbacks implements WorksControllerManager.ControllerCallbacks<BoltsWorksController2> {
        BoltsWorksController2 mController;

        @Override
        public BoltsWorksController2 onCreateController(int id, Bundle bundle) {
            mController = new BoltsWorksController2();
            return mController;
        }

        @Override
        public void onLoadFinished(int id, Bundle bundle, BoltsWorksController2 controller) {

        }

        @Override
        public void onLoaderReset(BoltsWorksController2 loader) {

        }
    }


}
