package com.mobilesolutionworks.android.bolts;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

import bolts.Task;
import bolts.TaskCompletionSource;

/**
 * Created by yunarta on 24/1/16.
 */
@SuppressWarnings("All")
public class BoltsWaitForMultipleObjects {

    private final int mCount;

    private SparseArray<TaskCompletionSource<Void>> mTasks;

    public BoltsWaitForMultipleObjects(int... objects) {
        mCount = objects.length;

        mTasks = new SparseArray<>();
        for (int object : objects) {
            mTasks.append(object, new TaskCompletionSource<Void>());
        }
    }

    public Task completion() {
        int size = mTasks.size();

        List<Task<?>> tasks = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            tasks.add(mTasks.valueAt(i).getTask());
        }

        return Task.whenAll(tasks);
    }

    public void completed(int key) {
        mTasks.get(key).trySetResult(null);
    }
}
