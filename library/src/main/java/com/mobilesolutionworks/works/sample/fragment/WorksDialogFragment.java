package com.mobilesolutionworks.works.sample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.mobilesolutionworks.works.core.WorksSupportControllerManager;
import com.mobilesolutionworks.works.core.WorksController;
import com.mobilesolutionworks.works.core.WorksFragmentBase;
import com.mobilesolutionworks.works.sample.activity.WorksCompatActivity;

/**
 * Created by yunarta on 19/11/15.
 */
public class WorksDialogFragment extends DialogFragment implements WorksFragmentBase {

    private WorksSupportControllerManager mControllerManager;

    private int mTargetControllerId;

    private int mTargetControllerRequestCode;

    public void setTargetController(int id) {
        setTargetController(id, 0);
    }

    public void setTargetController(int id, int requestCode) {
        mTargetControllerId = id;
        mTargetControllerRequestCode = requestCode;
    }

    @Override
    public int getTargetControllerId() {
        return mTargetControllerId;
    }

    @Override
    public int getTargetControllerRequestCode() {
        return mTargetControllerRequestCode;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WorksSupportControllerManager.InternalLoader loader = (WorksSupportControllerManager.InternalLoader) getLoaderManager().initLoader(0, null, new WorksSupportControllerManager.LoaderCallbacks(getActivity()));
        mControllerManager = loader.getController();

        if (savedInstanceState != null) {
            mTargetControllerId = savedInstanceState.getInt("targetControllerId");
            mTargetControllerRequestCode = savedInstanceState.getInt("targetControllerRequestCode");
        }
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
//        dismissAllowingStateLoss();
    }

    @Override
    public void onResume() {
        super.onResume();
        mControllerManager.dispatchResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("targetController", mTargetControllerId);
        outState.putInt("targetControllerRequestCode", mTargetControllerRequestCode);

        mControllerManager.dispatchOnSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends WorksCompatActivity> T getBaseActivity() {
        FragmentActivity activity = getActivity();
        if (activity instanceof WorksCompatActivity) {
            return (T) activity;
        }

        return null;
    }

    @Override
    public void postControllerResult(int id, int requestCode, int resultCode, Object data) {
        WorksController controller = mControllerManager.getController(id);
        if (controller != null) {
            controller.onControllerResult(requestCode, resultCode, data);
        }
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
