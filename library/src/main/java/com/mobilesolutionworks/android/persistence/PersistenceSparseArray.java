package com.mobilesolutionworks.android.persistence;

import android.os.Bundle;
import android.util.SparseArray;

import com.mobilesolutionworks.android.app.WorksController;
import com.mobilesolutionworks.android.app.WorksControllerManager;

import java.lang.ref.WeakReference;

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

    public interface OnSparseArrayCreated {

        void onCreate(int id, PersistenceSparseArray controller);
    }

    public static class ManagerCallback implements WorksControllerManager.ControllerCallbacks<PersistenceSparseArray> {

        WeakReference<OnSparseArrayCreated> mCallback;

        public ManagerCallback(OnSparseArrayCreated callback) {
            mCallback = new WeakReference<>(callback);
        }

        @Override
        public PersistenceSparseArray onCreateController(int id, Bundle bundle) {
            return new PersistenceSparseArray();
        }

        @Override
        public void onLoadFinished(int id, Bundle bundle, PersistenceSparseArray controller) {
            if (mCallback != null) {
                OnSparseArrayCreated callback = mCallback != null ? mCallback.get() : null;
                if (callback != null) {
                    callback.onCreate(id, controller);
                }
            }
        }

        @Override
        public void onLoaderReset(PersistenceSparseArray loader) {

        }


    }
}