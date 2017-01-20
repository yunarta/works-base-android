package com.mobilesolutionworks.android.exe;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

/**
 * Created by yunarta on 19/11/15.
 */
public class WorksExecutor {

    public static final Executor UIExecutor = command -> new Handler(Looper.getMainLooper()).post(command);

}
