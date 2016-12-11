package com.mobilesolutionworks.android.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;

/**
 * Created by yunarta on 19/11/15.
 */
public class WorksDialogFragment extends DialogFragment implements WorksFragmentBase {

    private WorksControllerManager mController;

    private transient Object mTransientData;

    private int mTargetControllerId;

    private int mTargetControllerRequestCode;

    public void setTargetController(int id) {
        setTargetController(id, 0);
    }

    public void setTargetController(int id, int requestCode) {
        mTargetControllerId = id;
        mTargetControllerRequestCode = requestCode;
    }

    public int getTargetControllerId() {
        return mTargetControllerId;
    }

    public int getTargetControllerRequestCode() {
        return mTargetControllerRequestCode;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WorksControllerManager.Loader loader = (WorksControllerManager.Loader) getLoaderManager().initLoader(0, null, new WorksControllerManager.LoaderCallbacks(getActivity(), mTransientData));
        mController = loader.getController();

        if (savedInstanceState != null) {
            mTargetControllerId = savedInstanceState.getInt("targetControllerId");
            mTargetControllerRequestCode = savedInstanceState.getInt("targetControllerRequestCode");
        }
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
        outState.putInt("targetController", mTargetControllerId);
        outState.putInt("targetControllerRequestCode", mTargetControllerRequestCode);

        mController.dispatchOnSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    public void postControllerResult(int id, int requestCode, int resultCode, Object data) {
        WorksController controller = mController.getController(id);
        if (controller != null) {
            controller.onControllerResult(requestCode, resultCode, data);
        }
    }
}
