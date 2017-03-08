package com.mobilesolutionworks.works.core;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.res.Configuration;
import android.os.Bundle;
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

    public void dispatchOnSaveInstanceState(Bundle state) {
        int size = mControllers.size();
        for (int i = 0; i < size; i++) {
            mControllers.valueAt(i).controller.onSaveInstanceState(state);
        }
    }

    public void onConfigurationChanged(Configuration config) {
        int size = mControllers.size();
        for (int i = 0; i < size; i++) {
            mControllers.valueAt(i).controller.onConfigurationChanged(config);
        }
    }

    public <T extends WorksController> T getController(int id, Class<T> klass) {
        WorksController worksController = mControllers.get(id).controller;
        if (worksController != null && klass.isInstance(worksController)) {
            return (T) worksController;

        }

        return null;
    }

    private class ControllerInfo<D extends WorksController> {

        int id;

        D controller;

        ControllerCallbacks<D> callback;

        public ControllerInfo(int id, ControllerCallbacks<D> callback) {
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

    public <D extends WorksController> D getController(int id) {
        ControllerInfo info = mControllers.get(id);
        if (info != null) {
            return (D) info.controller;
        }

        return null;
    }

    public <D extends WorksController> D initController(int id, Bundle bundle, ControllerCallbacks<D> callback) {
        ControllerInfo info = mControllers.get(id);
        if (info == null) {
            info = new ControllerInfo(id, callback);

            D newController = callback.onCreateController(id, bundle);
            info.controller = newController;
            info.controller.setId(id);


            callback.onLoadFinished(id, bundle, newController);
            newController.onCreate(bundle, null);

            mControllers.put(id, info);
        } else {
            info.callback = callback;
            info.callback.onLoadFinished(id, bundle, info.controller);
        }

        return (D) info.controller;
    }

    public static class InternalLoader extends Loader<WorksControllerManager> {

        private WorksControllerManager mControllerManager;

        public InternalLoader(Context context, Object data) {
            super(context);

            mControllerManager = new WorksControllerManager();
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();

            deliverResult(mControllerManager);
        }

        public WorksControllerManager getController() {
            return mControllerManager;
        }
    }

    public static class LoaderCallbacks implements LoaderManager.LoaderCallbacks<WorksControllerManager> {

        private Context mContext;

        private final Object mData;

        public LoaderCallbacks(Context context, Object transientData) {
            mContext = context.getApplicationContext();
            mContext = context;

            mData = transientData;
        }

        @Override
        public Loader<WorksControllerManager> onCreateLoader(int id, Bundle args) {
            return new InternalLoader(mContext, mData);
        }

        @Override
        public void onLoadFinished(android.content.Loader<WorksControllerManager> loader, WorksControllerManager data) {

        }

        @Override
        public void onLoaderReset(android.content.Loader<WorksControllerManager> loader) {
            InternalLoader l = (InternalLoader) loader;
            l.getController().dispatchDestroy();
        }
    }
}
