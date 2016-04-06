package com.mobilesolutionworks.android.bolts;

import android.os.Bundle;

import com.mobilesolutionworks.android.app.WorksController;
import com.mobilesolutionworks.android.app.WorksControllerManager;
import com.mobilesolutionworks.android.app.v4.SimpleArrayMap;

import java.util.HashSet;
import java.util.Set;

import bolts.Continuation;
import bolts.Task;
import bolts.TaskCompletionSource;

/**
 * Created by yunarta on 19/11/15.
 */
public class BoltsWorksController extends WorksController {

    SimpleArrayMap<String, ContinuationFactory> mFactories;

    Set<String> mRegisteredTask;

    boolean mIsPaused;

    TaskCompletionSource mDiplayTCS;

    public BoltsWorksController() {
        mFactories = new SimpleArrayMap<>();
        mRegisteredTask = new HashSet<>();

        mDiplayTCS = new TaskCompletionSource();
    }

    public void setContinuation(String requestCode, ContinuationFactory factory) {
        mFactories.put(requestCode, factory);
    }

    public boolean isTaskRegistered(String requestCode) {
        return mRegisteredTask.contains(requestCode);
    }

    public void addTask(final String requestCode, Task task) {
        addTask(requestCode, task, false);
    }

    public void addTask(final String requestCode, Task task, boolean register) {
        if (register) {
            mRegisteredTask.add(requestCode);
        }

        task.continueWithTask(new Continuation() {
            @Override
            public Object then(final Task task) throws Exception {
                if (mIsPaused) {
                    return getDisplayTCS().continueWith(new Continuation() {
                        @Override
                        public Object then(Task ignored) throws Exception {
                            return task;
                        }
                    });
                } else {
                    return task;
                }
            }
        }, Task.UI_THREAD_EXECUTOR).continueWith(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                ContinuationFactory factory = mFactories.get(requestCode);
                if (factory != null) {
                    return factory.continueWith(task);
                }
                return null;
            }
        });
    }

    private Task getDisplayTCS() {
        return mDiplayTCS.getTask();
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

        mDiplayTCS = new TaskCompletionSource();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mDiplayTCS != null) {
            mDiplayTCS.trySetCancelled();
        }
    }

    public interface ContinuationFactory<Result, Response> {
        Task<Response> continueWith(Task<Result> task);
    }

    public static class ControllerCallbacks implements WorksControllerManager.ControllerCallbacks<BoltsWorksController> {
        BoltsWorksController mController;

        @Override
        public BoltsWorksController onCreateController(int id, Bundle args) {
            mController = new BoltsWorksController();
            return mController;
        }

        @Override
        public void onCreated(int id, BoltsWorksController loader) {

        }

        @Override
        public void onReset(BoltsWorksController loader) {

        }
    }
}
