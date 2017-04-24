package com.mobilesolutionworks.works.host;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.mobilesolutionworks.works.core.Host;
import com.mobilesolutionworks.works.core.WorksSupportControllerManager;

public class HostFragment extends Fragment implements Host {

    private WorksSupportControllerManager mController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WorksSupportControllerManager.InternalLoader loader = (WorksSupportControllerManager.InternalLoader) getLoaderManager().initLoader(0, null, new WorksSupportControllerManager.LoaderCallbacks(getActivity()));
        mController = loader.getController();
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

    @Override
    public WorksSupportControllerManager getControllerManager() {
        return mController;
    }

    @Override
    public FragmentManager getHostFragmentManager() {
        return getChildFragmentManager();
    }

    @Override
    public void finish() {
        // Nothing to do
    }

}