package com.mobilesolutionworks.android.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mobilesolutionworks.android.app.v4.SimpleArrayMap;

/**
 * Created by yunarta on 19/11/15.
 */
public class WorksActivity extends AppCompatActivity
{
    private static final boolean DEBUG = WorksBaseConfig.DEBUG;

    ActivityControllerHost mHost;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        if (DEBUG) Log.d("/!", "ON CREATE\n===\n");
        mHost = new ActivityControllerHost(this);
        NonConfiguration nc = (NonConfiguration) getLastCustomNonConfigurationInstance();
        if (nc != null)
        {
            mHost.restoreLoaderNonConfig(nc.allControllerManagers);
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        if (DEBUG) Log.d("/!", "ON POST CREATE\n===\n");
        mHost.dispatchPostCreate();
        super.onPostCreate(savedInstanceState);
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
        if (DEBUG) Log.d("/!", "ON DESTROY\n===\n");
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
