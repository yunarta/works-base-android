package com.mobilesolutionworks.android.bolts;

import android.os.Bundle;
import android.os.Handler;
import bolts.Continuation;
import bolts.Task;
import bolts.TaskCompletionSource;
import com.mobilesolutionworks.android.app.WorksController;
import com.mobilesolutionworks.android.app.WorksControllerManager;

/**
 * Created by yunarta on 19/11/15.
 */
public class BoltsWorksController2<Host> extends WorksController {

    boolean mIsPaused;

    TaskCompletionSource<Void> mDiplayTCS;

    protected Host mHost;

    protected Handler mHandler;

    public BoltsWorksController2() {
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
        this.mHost = host;
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

    public static class ControllerCallbacks implements WorksControllerManager.ControllerCallbacks<BoltsWorksController2> {
        BoltsWorksController2 mController;

        @Override
        public BoltsWorksController2 onCreateController(int id, Bundle args) {
            mController = new BoltsWorksController2();
            return mController;
        }

        @Override
        public void onCreated(int id, BoltsWorksController2 loader) {

        }

        @Override
        public void onReset(BoltsWorksController2 loader) {

        }
    }


}
