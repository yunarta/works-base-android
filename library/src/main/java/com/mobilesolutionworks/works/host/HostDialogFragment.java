package com.mobilesolutionworks.works.host;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.mobilesolutionworks.works.core.Host;
import com.mobilesolutionworks.works.core.Manager;

public class HostDialogFragment extends DialogFragment implements Host {

    private Manager mControllerManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Manager.InternalLoader loader = (Manager.InternalLoader) getLoaderManager().initLoader(0, null, new Manager.LoaderCallbacks(getActivity()));
        mControllerManager = loader.getController();
    }

    @Override
    public Manager getControllerManager() {
        return mControllerManager;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mControllerManager.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mControllerManager.dispatchPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mControllerManager.dispatchResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mControllerManager.dispatchOnSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public FragmentManager getHostFragmentManager() {
        return getChildFragmentManager();
    }

    @Override
    public void finish() {
        dismissAllowingStateLoss();
    }
}
