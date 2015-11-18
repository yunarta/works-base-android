package com.mobilesolutionworks.android.works;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.mobilesolutionworks.android.app.FragmentControllerHost;
import com.mobilesolutionworks.android.app.WorksControllerManager;

/**
 * Created by yunarta on 17/11/15.
 */
public class BaseFragment extends Fragment
{
    private static int sInstanceCount;

    String mInstanceName;

    FragmentControllerHost mHost;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        Log.d("/!", this + " onCreate() called with: " + "savedInstanceState = [" + savedInstanceState + "]");

        if (savedInstanceState != null)
        {
            mInstanceName = savedInstanceState.getString(":fragment:instance");
        }
        else
        {
            mInstanceName = "fragment:" + (++sInstanceCount);
        }

        BaseActivity activity = (BaseActivity) getActivity();
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
//        Log.d("/!", this + " onStart() called with: " + "");
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
        Log.d("/!", this + " onStop() called with: " + "");
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
        Log.d("/!", this + " onDestroy() called with: " + "");

        mHost.dispatchDestroy();
        super.onDestroy();
    }

    public WorksControllerManager getControllerManager()
    {
        return mHost.getControllerManager();
    }
}
