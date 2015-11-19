package com.mobilesolutionworks.android.app;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.logging.Logger;

/**
 * Created by yunarta on 19/11/15.
 */
public class WorksFragment extends Fragment
{
    private static final boolean DEBUG  = WorksBaseConfig.DEBUG;

    protected final      Logger  LOGGER = Logger.getLogger(getClass().getName());

    private static int sInstanceCount;

    String mInstanceName;

    FragmentControllerHost mHost;

    Handler mHandler;

    public Handler getHandler()
    {
        return mHandler;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        if (DEBUG) Log.d("/!", this + " FRAGMENT ON CREATE\n===\n");

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
        mHandler = new Handler();

        mHost.dispatchCreate();
    }

    public void onSaveInstanceState(Bundle bundle)
    {
        bundle.putString(":fragment:instance", mInstanceName);
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onStart()
    {
        if (DEBUG) Log.d("/!", this + " FRAGMENT ON START\n===\n");
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
        if (DEBUG) Log.d("/!", this + " FRAGMENT ON DESTROY\n===\n");
        mHost.dispatchDestroy();
        super.onDestroy();
    }

    public WorksControllerManager getControllerManager()
    {
        return mHost.getControllerManager();
    }

}
