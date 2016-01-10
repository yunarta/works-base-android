package com.mobilesolutionworks.android.works;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobilesolutionworks.android.app.WorksControllerManager;
import com.mobilesolutionworks.android.app.WorksFragment;
import com.mobilesolutionworks.android.bolts.BoltsWorksController;

/**
 * A placeholder fragment containing a simple view.
 */
public class BWCHostFragment extends WorksFragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);

        WorksControllerManager manager = getControllerManager();
        manager.initController(0, null, new BoltsWorksController.ControllerCallbacks());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}
