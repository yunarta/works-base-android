package com.mobilesolutionworks.works.sample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.mobilesolutionworks.works.core.Host;
import com.mobilesolutionworks.works.core.WorksSupportControllerManager;

/**
 * Created by yunarta on 19/11/15.
 */
public class WorksDialogFragment extends DialogFragment implements Host {

    private WorksSupportControllerManager mControllerManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WorksSupportControllerManager.InternalLoader loader = (WorksSupportControllerManager.InternalLoader) getLoaderManager().initLoader(0, null, new WorksSupportControllerManager.LoaderCallbacks(getActivity()));
        mControllerManager = loader.getController();
    }

    @Override
    public WorksSupportControllerManager getControllerManager() {
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
