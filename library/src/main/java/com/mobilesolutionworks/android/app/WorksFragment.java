package com.mobilesolutionworks.android.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

        Log.d("fragmentstate", this.getClass() + " onCreate");
    }

    public WorksControllerManager getControllerManager() {
        return mController;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mController.onRestoreInstanceState(savedInstanceState);

        Log.d("fragmentstate", this.getClass() + " onViewCreated");
    }

    @Override
    public void onPause() {
        super.onPause();
        mController.dispatchPause();

        Log.d("fragmentstate", this.getClass() + " onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        mController.dispatchResume();

        Log.d("fragmentstate", this.getClass() + " Resume");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mController.dispatchOnSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mController.dispatchOnActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mController.dispatchOnRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
