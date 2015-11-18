package com.mobilesolutionworks.android.works;

import android.app.Application;

import com.mobilesolutionworks.android.util.LogUtil;

/**
 * Created by yunarta on 18/11/15.
 */
public class MainApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        LogUtil.configure(this);
    }
}
