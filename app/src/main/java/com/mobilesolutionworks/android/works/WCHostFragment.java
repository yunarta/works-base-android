package com.mobilesolutionworks.android.works;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobilesolutionworks.android.app.WorksController;
import com.mobilesolutionworks.android.app.WorksControllerManager;
import com.mobilesolutionworks.android.app.WorksFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class WCHostFragment extends WorksFragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        createController();

        getActivity().getLoaderManager().initLoader(1, null, new android.app.LoaderManager.LoaderCallbacks<String>() {
            @Override
            public android.content.Loader<String> onCreateLoader(int id, Bundle args) {
                LOGGER.finest("onCreateLoader2");
                return new android.content.AsyncTaskLoader<String>(getActivity()) {
                    @Override
                    protected void onStartLoading() {
                        super.onStartLoading();
                        forceLoad();
                    }

                    @Override
                    public String loadInBackground() {
                        return "ABC";
                    }
                };

            }

            @Override
            public void onLoadFinished(android.content.Loader<String> loader, String data) {

            }

            @Override
            public void onLoaderReset(android.content.Loader<String> loader) {

            }
        });
        getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<String>() {
            @Override
            public Loader<String> onCreateLoader(int id, Bundle args) {
                LOGGER.finest("onCreateLoader");
                return new AsyncTaskLoader<String>(getActivity()) {
                    @Override
                    protected void onStartLoading() {
                        super.onStartLoading();
                        forceLoad();
                    }

                    @Override
                    public String loadInBackground() {
                        return "ABC";
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<String> loader, String data) {
                LOGGER.finest("onLoadFinished = " + data);
            }

            @Override
            public void onLoaderReset(Loader<String> loader) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    private void createController()
    {
        getControllerManager().initController(0, null, new WorksControllerManager.ControllerCallbacks<WorksController>()
        {
            @Override
            public WorksController onCreateController(int id, Bundle args)
            {
                return new WorksController();
            }

            @Override
            public void onCreated(int id, WorksController loader)
            {

            }

            @Override
            public void onReset(WorksController loader)
            {

            }
        });
    }
}
