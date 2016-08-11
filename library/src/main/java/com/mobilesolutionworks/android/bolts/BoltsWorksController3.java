package com.mobilesolutionworks.android.bolts;

import android.os.Handler;

import com.mobilesolutionworks.android.app.WorksController;
import com.mobilesolutionworks.android.app.WorksControllerManager;

import bolts.Continuation;
import bolts.Task;
import bolts.TaskCompletionSource;

/**
 * Created by yunarta on 28/6/16.
 */
public class BoltsWorksController3<Host> extends WorksController {

    boolean mIsPaused = true;

    TaskCompletionSource<Void> mDiplayTCS;

    protected Host mHost;

    protected Handler mHandler;

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

    public static abstract class ControllerCallbacks<Controller extends BoltsWorksController3<Host>, Host> implements WorksControllerManager.ControllerCallbacks<Controller> {

        private Host mHost;

        public ControllerCallbacks(Host host) {
            mHost = host;
        }

        @Override
        public void onCreated(int id, Controller controller) {
            controller.setHost(mHost);
        }

        @Override
        public void onReset(Controller loader) {

        }
    }
}
