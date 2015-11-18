package com.mobilesolutionworks.android.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by yunarta on 19/11/15.
 */
public class WorksFragment extends Fragment
{
    private static int sInstanceCount;

    String mInstanceName;

    FragmentControllerHost mHost;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
        {
            mInstanceName = savedInstanceState.getString(":fragment:instance");
        }
        else
        {
            mInstanceName = "fragment:" + (++sInstanceCount);
        }

        WorksActivity activity = (WorksActivity) getActivity();
        mHost = new FragmentControllerHost(mInstanceName, activity.getControllerHost());
    }

    public void onSaveInstanceState(Bundle bundle)
    {
        bundle.putString(":fragment:instance", mInstanceName);
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onStart()
    {
        mHost.dispatchStart();
        super.onStart();
    }

    @Override
    public void onResume()
    {
        mHost.dispatchResume();
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onStop()
    {
        mHost.dispatchStop(getActivity() != null);
        super.onStop();
    }

    @Override
    public void onDestroyView()
    {
        mHost.dispatchDestroyView();
        super.onDestroyView();
    }

    @Override
    public void onDestroy()
    {
        mHost.dispatchDestroy();
        super.onDestroy();
    }

    public WorksControllerManager getControllerManager()
    {
        return mHost.getControllerManager();
    }

}
