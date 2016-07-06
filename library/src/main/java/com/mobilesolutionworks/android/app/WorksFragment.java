package com.mobilesolutionworks.android.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;

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
    public void onPause() {
        super.onPause();
        mController.dispatchPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mController.dispatchResume();
    }
}
