package com.mobilesolutionworks.android.works;

import android.os.Bundle;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.app.AppCompatActivity;

import com.mobilesolutionworks.android.app.ActivityControllerHost;
import com.mobilesolutionworks.android.app.WorksControllerManager;

/**
 * Created by yunarta on 17/11/15.
 */
public class BaseActivity extends AppCompatActivity
{
    ActivityControllerHost mHost;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        mHost = new ActivityControllerHost(this);
        NonConfiguration nc = (NonConfiguration) getLastCustomNonConfigurationInstance();
        if (nc != null)
        {
            mHost.restoreLoaderNonConfig(nc.allControllerManagers);
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart()
    {
        mHost.dispatchStart();
        super.onStart();
    }

    @Override
    protected void onStop()
    {
        mHost.dispatchStop();
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        mHost.dispatchDestroy();
        super.onDestroy();
    }

    public Object getLastPersistedObject()
    {
        NonConfiguration nc = (NonConfiguration) super.getLastCustomNonConfigurationInstance();
        return nc != null ? nc.persisted : null;
    }

    public Object onRetainPersistedObject()
    {
        return null;
    }

    @Override
    public final Object onRetainCustomNonConfigurationInstance()
    {
        SimpleArrayMap<String, WorksControllerManager> allControllerManagers = mHost.retainLoaderNonConfig();

        NonConfiguration nc = new NonConfiguration();
        nc.persisted = onRetainPersistedObject();
        nc.allControllerManagers = allControllerManagers;

        return nc;
    }

    @Override
    @Deprecated
    public Object getLastCustomNonConfigurationInstance()
    {
        return super.getLastCustomNonConfigurationInstance();
    }

    class NonConfiguration
    {
        public Object persisted;

        public SimpleArrayMap<String, WorksControllerManager> allControllerManagers;
    }

    ActivityControllerHost getControllerHost()
    {
        return mHost;
    }

    public WorksControllerManager getControllerManager()
    {
        return mHost.getControllerManager();
    }
}
