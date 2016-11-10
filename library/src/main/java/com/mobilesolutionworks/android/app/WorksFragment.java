package com.mobilesolutionworks.android.app;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

/**
 * Created by yunarta on 19/11/15.
 */
public class WorksFragment extends Fragment {

    private WorksControllerManager mController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WorksControllerManager.Loader loader = (WorksControllerManager.Loader) getLoaderManager().initLoader(0, null, new WorksControllerManager.LoaderCallbacks(getActivity()));
        mController = loader.getController();
    }

    public WorksControllerManager getControllerManager() {
        return mController;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mController.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mController.dispatchPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mController.dispatchResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mController.dispatchOnSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mController.onConfigurationChanged(newConfig);
    }
}
