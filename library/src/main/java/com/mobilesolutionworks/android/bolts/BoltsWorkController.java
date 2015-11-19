package com.mobilesolutionworks.android.bolts;

import android.os.Bundle;
import bolts.Continuation;
import bolts.Task;
import bolts.TaskCompletionSource;
import com.mobilesolutionworks.android.app.WorksController;
import com.mobilesolutionworks.android.app.WorksControllerManager;
import com.mobilesolutionworks.android.app.v4.SimpleArrayMap;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by yunarta on 19/11/15.
 */
public class BoltsWorkController extends WorksController
{
    SimpleArrayMap<String, ContinuationFactory> mFactories;

    Set<String> mRegisteredTask;

    boolean mIsPaused;

    TaskCompletionSource mDiplayTCS;

    public BoltsWorkController()
    {
        mFactories = new SimpleArrayMap<>();
        mRegisteredTask = new HashSet<>();
    }

    public void setContinuation(String requestCode, ContinuationFactory factory)
    {
        mFactories.put(requestCode, factory);
    }

    public boolean isTaskRegistered(String requestCode)
    {
        return mRegisteredTask.contains(requestCode);
    }

    public void addTask(final String requestCode, Task task)
    {
        addTask(requestCode, task, false);
    }

    public void addTask(final String requestCode, Task task, boolean register)
    {
        if (register) {
            mRegisteredTask.add(requestCode);
        }

        task.continueWithTask(new Continuation()
        {
            @Override
            public Object then(Task task) throws Exception
            {
                if (mIsPaused)
                {
                    return getDisplayTCS().continueWith(new Continuation()
                    {
                        @Override
                        public Object then(Task task) throws Exception
                        {
                            return task;
                        }
                    });
                }
                else
                {
                    return task;
                }
            }
        }).continueWith(new Continuation()
        {
            @Override
            public Object then(Task task) throws Exception
            {
                ContinuationFactory factory = mFactories.get(requestCode);
                if (factory != null)
                {
                    return factory.continueWith(task);
                }
                return null;
            }
        });
    }

    private Task getDisplayTCS()
    {
        return mDiplayTCS.getTask();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mIsPaused = false;

        mDiplayTCS.trySetResult(null);
    }

    @Override
    public void onPaused()
    {
        super.onPaused();

        mIsPaused = true;

        mDiplayTCS = new TaskCompletionSource();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if (mDiplayTCS != null)
        {
            mDiplayTCS.trySetCancelled();
        }
    }

    public interface ContinuationFactory<Result>
    {
        Task<Result> continueWith(Task<Result> task);
    }

    public static class ControllerCallbacks implements WorksControllerManager.ControllerCallbacks<BoltsWorkController>
    {
        BoltsWorkController mController;

        @Override
        public BoltsWorkController onCreateController(int id, Bundle args)
        {
            mController = new BoltsWorkController();
            return mController;
        }

        @Override
        public void onCreated(int id, BoltsWorkController loader)
        {

        }

        @Override
        public void onReset(BoltsWorkController loader)
        {

        }
    }
}
