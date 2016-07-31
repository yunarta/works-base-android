package com.mobilesolutionworks.android.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.util.SparseArray;

/**
 * Created by yunarta on 18/11/15.
 */
public class WorksControllerManager {

    public void dispatchPause() {
        int size = mControllers.size();
        for (int i = 0; i < size; i++) {
            mControllers.valueAt(i).controller.onPaused();
        }
    }

    public void dispatchResume() {
        int size = mControllers.size();
        for (int i = 0; i < size; i++) {
            mControllers.valueAt(i).controller.onResume();
        }
    }

    private void dispatchDestroy() {
        int size = mControllers.size();
        for (int i = 0; i < size; i++) {
            mControllers.valueAt(i).controller.onDestroy();
        }
        mControllers.clear();
    }

    public void onRestoreInstanceState(Bundle state) {
        int size = mControllers.size();
        for (int i = 0; i < size; i++) {
            mControllers.valueAt(i).controller.onViewStateRestored(state);
        }
    }

    private class ControllerInfo<D extends WorksController> {

        D controller;

        ControllerCallbacks<D> callback;

        public ControllerInfo(ControllerCallbacks<D> callback) {
            this.callback = callback;
        }
    }

    public interface ControllerCallbacks<D extends WorksController> {

        D onCreateController(int id, Bundle bundle);

        void onLoadFinished(int id, Bundle bundle, D controller);

        void onLoaderReset(D loader);
    }

    SparseArray<ControllerInfo> mControllers;

    public WorksControllerManager() {
        mControllers = new SparseArray<>();
    }

    public <D extends WorksController> D initController(int id, Bundle bundle, ControllerCallbacks<D> callback) {
        ControllerInfo info = mControllers.get(id);
        if (info == null) {
            info = new ControllerInfo(callback);

            D newController = callback.onCreateController(id, bundle);
            info.controller = newController;

            callback.onLoadFinished(id, bundle, newController);
            newController.onCreate(bundle);

            mControllers.put(id, info);
        } else {
            info.callback = callback;
            info.callback.onLoadFinished(id, bundle, info.controller);
        }

        return (D) info.controller;
    }

    public static class Loader extends android.support.v4.content.Loader<WorksControllerManager> {

        private WorksControllerManager mData;

        public Loader(Context context) {
            super(context);
            mData = new WorksControllerManager();
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();

            deliverResult(mData);
        }

        public WorksControllerManager getController() {
            return mData;
        }
    }

    public static class LoaderCallbacks implements LoaderManager.LoaderCallbacks<WorksControllerManager> {

        private Context mContext;

        public LoaderCallbacks(Context context) {
            mContext = context.getApplicationContext();
        }

        @Override
        public android.support.v4.content.Loader<WorksControllerManager> onCreateLoader(int id, Bundle args) {
            return new Loader(mContext);
        }

        @Override
        public void onLoadFinished(android.support.v4.content.Loader<WorksControllerManager> loader, WorksControllerManager data) {

        }

        @Override
        public void onLoaderReset(android.support.v4.content.Loader<WorksControllerManager> loader) {
            Loader l = (Loader) loader;
            l.getController().dispatchDestroy();
        }
    }
}
