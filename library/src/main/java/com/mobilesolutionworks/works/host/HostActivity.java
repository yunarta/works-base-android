package com.mobilesolutionworks.works.host;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.mobilesolutionworks.works.core.Host;
import com.mobilesolutionworks.works.core.WorksController;
import com.mobilesolutionworks.works.core.WorksSupportControllerManager;

/**
 * Created by lucas34990 on 10/2/17.
 */

public class HostActivity extends AppCompatActivity implements Host {

    private WorksSupportControllerManager mController;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data); // To make the visibility public
    }

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

}
