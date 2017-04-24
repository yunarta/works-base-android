package com.mobilesolutionworks.works.core;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.SparseArray;

public class Manager {

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

    public void dispatchDestroy() {
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

    public <T extends Lifecycle> T getController(int id, Class<T> klass) {
        Lifecycle lifecycle = mControllers.get(id).controller;
        if (lifecycle != null && klass.isInstance(lifecycle)) {
            return (T) lifecycle;

        }

        return null;
    }

    private class ControllerInfo<D extends Lifecycle> {

        int id;

        D controller;

        ControllerCallbacks<D> callback;

        public ControllerInfo(ControllerCallbacks<D> callback) {
            this.callback = callback;
        }
    }

    public interface ControllerCallbacks<D extends Lifecycle> {

        D onCreateController(int id, Bundle bundle);

        void onLoadFinished(int id, Bundle bundle, D controller);

        void onLoaderReset(D loader);
    }

    SparseArray<ControllerInfo> mControllers;

    public Manager() {
        mControllers = new SparseArray<>();
    }


    public <D extends Lifecycle> D initController(int id, Bundle bundle, ControllerCallbacks<D> callback) {
        ControllerInfo info = mControllers.get(id);
        if (info == null) {
            info = new ControllerInfo(callback);

            D newController = callback.onCreateController(id, bundle);
            info.controller = newController;


            callback.onLoadFinished(id, bundle, newController);
            newController.onCreate(bundle, null);

            mControllers.put(id, info);
        } else {
            info.callback = callback;
            info.callback.onLoadFinished(id, bundle, info.controller);
        }

        return (D) info.controller;
    }

    public static class InternalLoader extends Loader<Manager> {

        private Manager mControllerManager;

        public InternalLoader(Context context) {
            super(context);

            mControllerManager = new Manager();
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();

            deliverResult(mControllerManager);
        }

        public Manager getController() {
            return mControllerManager;
        }
    }

    public static class LoaderCallbacks implements LoaderManager.LoaderCallbacks<Manager> {

        private Context mContext;

        public LoaderCallbacks(Context context) {
            mContext = context.getApplicationContext();
            mContext = context;
        }

        @Override
        public Loader<Manager> onCreateLoader(int id, Bundle args) {
            return new InternalLoader(mContext);
        }

        @Override
        public void onLoadFinished(Loader<Manager> loader, Manager data) {

        }

        @Override
        public void onLoaderReset(Loader<Manager> loader) {
            InternalLoader l = (InternalLoader) loader;
            l.getController().dispatchDestroy();
        }
    }
}
