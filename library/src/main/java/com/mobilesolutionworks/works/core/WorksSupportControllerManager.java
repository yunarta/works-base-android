package com.mobilesolutionworks.works.core;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.SparseArray;

/**
 * Created by yunarta on 18/11/15.
 */
public class WorksSupportControllerManager {

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

    public WorksSupportControllerManager() {
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

    public static class InternalLoader extends Loader<WorksSupportControllerManager> {

        private WorksSupportControllerManager mControllerManager;

        public InternalLoader(Context context) {
            super(context);

            mControllerManager = new WorksSupportControllerManager();
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();

            deliverResult(mControllerManager);
        }

        public WorksSupportControllerManager getController() {
            return mControllerManager;
        }
    }

    public static class LoaderCallbacks implements LoaderManager.LoaderCallbacks<WorksSupportControllerManager> {

        private Context mContext;

        public LoaderCallbacks(Context context) {
            mContext = context.getApplicationContext();
            mContext = context;
        }

        @Override
        public Loader<WorksSupportControllerManager> onCreateLoader(int id, Bundle args) {
            return new InternalLoader(mContext);
        }

        @Override
        public void onLoadFinished(Loader<WorksSupportControllerManager> loader, WorksSupportControllerManager data) {

        }

        @Override
        public void onLoaderReset(Loader<WorksSupportControllerManager> loader) {
            InternalLoader l = (InternalLoader) loader;
            l.getController().dispatchDestroy();
        }
    }
}