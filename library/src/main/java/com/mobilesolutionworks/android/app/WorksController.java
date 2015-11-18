package com.mobilesolutionworks.android.app;

import android.app.Activity;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.PrintWriter;

/**
 * Created by yunarta on 16/11/15.
 */
public class WorksController
{
    private Activity mActivity;

    public void updateActivity(Activity activity)
    {
        mActivity = activity;
    }

    public void onCreate()
    {
        Log.d("/!C", "\t" + this + " onCreate() called with: " + "");
    }

    public void onStart()
    {
        Log.d("/!C", "\t" + this + " onStart() called with: " + "");
    }

    public void onPaused()
    {
        Log.d("/!C", "\t" + this + " onPaused() called with: " + "");
    }

    public void onResume()
    {
        Log.d("/!C", "\t" + this + " onResume() called with: " + "");
    }

    public void onStop()
    {
        Log.d("/!C", "\t" + this + " onStop() called with: " + "");
    }

    public void onDestroy()
    {
        Log.d("/!C", "\t" + this + " onDestroy() called with: " + "");
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args)
    {

    }
}
