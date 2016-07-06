package com.mobilesolutionworks.android.exe;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

/**
 * Created by yunarta on 19/11/15.
 */
public class WorksExecutor {
    static class MainExecutor implements Executor {
        MainExecutor() {
            mHandler = new Handler(Looper.getMainLooper());
        }

        private Handler mHandler;


        @Override
        public void execute(Runnable runnable) {
            mHandler.post(runnable);
        }
    }

    public static final Executor MAIN = new MainExecutor();
}
