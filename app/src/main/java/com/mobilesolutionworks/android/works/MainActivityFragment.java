package com.mobilesolutionworks.android.works;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobilesolutionworks.android.app.WorksController;
import com.mobilesolutionworks.android.app.WorksControllerManager;
import com.mobilesolutionworks.android.app.WorksFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends WorksFragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        WorksControllerManager manager = getControllerManager();
        manager.initLoader(0, null, new WorksControllerManager.LoaderCallbacks<WorksController>()
        {
            @Override
            public WorksController onCreateLoader(int id, Bundle args)
            {
                return new WorksController();
            }

            @Override
            public void onLoadFinished(int id, WorksController loader)
            {

            }

            @Override
            public void onLoaderReset(WorksController loader)
            {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}
