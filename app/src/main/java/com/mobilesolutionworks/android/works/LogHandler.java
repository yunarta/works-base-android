package com.mobilesolutionworks.android.works;

import android.text.TextUtils;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * This class need to be sub-classed for Android Log in the main application.
 * <p>
 * Created by yunarta on 17/12/15.
 */
public abstract class LogHandler extends Handler
{
    protected String mPrefix;

    protected boolean mShorten;

    protected boolean mPrintThread;

    protected boolean mHasPrefix;

    public void setup(String prefix, boolean shorten, boolean thread)
    {
        mPrefix = prefix;
        mHasPrefix = !TextUtils.isEmpty(mPrefix);

        mShorten = shorten;
        mPrintThread = thread;
    }

    @Override
    public void publish(LogRecord record)
    {
        StringBuilder sb = new StringBuilder();
        if (mHasPrefix)
        {
            sb.append(mPrefix);
        }

        sb.append(" - ");
        if (mPrintThread)
        {
            sb.append(Thread.currentThread().toString());
        }

        String name = record.getLoggerName();
        if (mShorten)
        {
            name = name.substring(name.lastIndexOf(".") + 1);
        }

        sb.append(record.getMessage());

        String message = sb.toString();
        record.setMessage(message);
    }
}
