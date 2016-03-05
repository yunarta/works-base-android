package com.mobilesolutionworks.android.persistence;

import android.os.Bundle;
import android.util.SparseArray;

import com.mobilesolutionworks.android.app.WorksController;
import com.mobilesolutionworks.android.app.WorksControllerManager;

/**
 * Created by yunarta on 6/3/16.
 */
public class PersistenceSparseArray extends WorksController {

    SparseArray<Object> mInstances;

    public PersistenceSparseArray() {
        mInstances = new SparseArray<>();
    }

    public <T> T get(int name) {
        return (T) mInstances.get(name);
    }

    public void set(int name, Object object) {
        mInstances.put(name, object);
    }

    public static abstract class ManagerCallback implements WorksControllerManager.ControllerCallbacks<PersistenceSparseArray> {

        @Override
        public PersistenceSparseArray onCreateController(int id, Bundle args) {
            return new PersistenceSparseArray();
        }

        @Override
        public void onReset(PersistenceSparseArray loader) {

        }
    }
}