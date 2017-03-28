package com.mobilesolutionworks.works.sample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;

import com.mobilesolutionworks.works.core.Host;
import com.mobilesolutionworks.works.core.WorksController;
import com.mobilesolutionworks.works.core.WorksSupportControllerManager;
import com.mobilesolutionworks.works.sample.WaitingForResult;

/**
 * Created by lucas34990 on 10/2/17.
 */

public class WorksCompatActivity extends AppCompatActivity implements Host {

    private WorksSupportControllerManager mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WorksSupportControllerManager.InternalLoader loader = (WorksSupportControllerManager.InternalLoader) getSupportLoaderManager().initLoader(0, null, new WorksSupportControllerManager.LoaderCallbacks(this));
        mController = loader.getController();
    }

    @Override
    public WorksSupportControllerManager getControllerManager() {
        return mController;
    }

    @Override
    public Bundle getArguments() {
        Bundle bundle = null;
        if(getIntent() != null) {
            bundle = getIntent().getExtras();
        }
        return bundle != null ? bundle : new Bundle();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mController.dispatchPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mController.dispatchResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mController.dispatchOnSaveInstanceState(outState);
    }

    @Deprecated
    public void onActivityResultCompat(int requestCode, int resultCode, Intent data) {
        onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public FragmentActivity getActivity() {
        return this;
    }

    @Nullable
    @Override
    public View getView() {
        Window window = getWindow();
        return window != null ? window.getDecorView() : null;
    }

    @Override
    public FragmentManager getHostFragmentManager() {
        return getSupportFragmentManager();
    }

    public void postControllerResult(int id, int requestCode, int resultCode, Object data) {
        WorksController controller = mController.getController(id);
        if (controller != null) {
            controller.onControllerResult(requestCode, resultCode, data);
        }
    }
}
