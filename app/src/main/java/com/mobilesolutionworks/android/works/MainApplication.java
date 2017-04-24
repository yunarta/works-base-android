package com.mobilesolutionworks.android.works;

import android.app.Application;
import android.util.Log;

import java.util.Locale;
import java.util.logging.LogRecord;

public class MainApplication extends Application {
    static {
        Class<? extends LogHandler> handler = LogCatHandler.class;
        LogUtil.setDefaultLogger(handler);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        LogUtil.configure(this);
    }

    public static class LogCatHandler extends LogHandler {
        @Override
        public void publish(LogRecord record) {
            super.publish(record);

            Log.d(mPrefix, String.format(Locale.ENGLISH, "%s", record.getMessage()));
        }

        @Override
        public void close() {

        }

        @Override
        public void flush() {

        }
    }
}
