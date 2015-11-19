package com.mobilesolutionworks.android.util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Created by yunarta on 13/11/15.
 */
public class LogUtil
{
    public static final Logger NULL_LOGGER;

    public static final List<Logger> mCreatedLoggers;

    public static boolean enableLogging;

    static
    {
        NULL_LOGGER = Logger.getLogger("<null>");
        NULL_LOGGER.setLevel(Level.OFF);

        enableLogging = true; // BuildConfig.ENABLE_LOGGING;
        mCreatedLoggers = new ArrayList<>();
    }

    public static Logger getLogger(Class cl)
    {
        return enableLogging ? Logger.getLogger(cl.getName()) : NULL_LOGGER;
    }

    public static Logger getLogger(String name)
    {
        return enableLogging ? Logger.getLogger(name) : NULL_LOGGER;
    }

    public static final Handler handler        = new MyHandler();
    public static final Handler shortenHandler = new MyShortHandler();

    public static void configure(Context context, int id)
    {
        doConfigureDebug(context);
        doConfigure(context, id);
    }

    public static void configure(Context context)
    {
        doConfigureDebug(context);
        doConfigure(context, context.getResources().getIdentifier("loggable", "xml", context.getPackageName()));
    }

    private static void doConfigureDebug(Context context)
    {
        try
        {
            Class<?> cl = Class.forName(context.getPackageName() + ".BuildConfig");
            Field logging = cl.getField("ENABLE_LOGGING");
            enableLogging = logging.getBoolean(null);
        }
        catch (Exception e)
        {
            // e.printStackTrace();
        }
    }

    private static void doConfigure(Context context, int identifier)
    {
        Resources resources = context.getResources();

        if (identifier != 0)
        {
            XmlResourceParser xml = resources.getXml(identifier);
            boolean parsing = true;
            try
            {
                while (parsing)
                {
                    int token = xml.next();
                    switch (token)
                    {
                        case XmlResourceParser.END_DOCUMENT:
                        {
                            parsing = false;
                            break;
                        }

                        case XmlPullParser.START_TAG:
                        {
                            String name = xml.getName();
                            if ("package".equals(name))
                            {
                                try
                                {
                                    String packageName = xml.getAttributeValue(null, "name");

                                    String shorten = xml.getAttributeValue(null, "shortName");
                                    Logger logger = Logger.getLogger(packageName);

                                    mCreatedLoggers.add(logger);

                                    Handler[] handlers = logger.getHandlers();
//                                    for (Handler h : handlers)
//                                    {
//                                        logger.removeHandler(h);
//                                    }

                                    logger.removeHandler(handler);
                                    logger.removeHandler(shortenHandler);

                                    if (Boolean.parseBoolean(shorten))
                                    {
                                        logger.addHandler(shortenHandler);
                                    }
                                    else
                                    {
                                        logger.addHandler(handler);
                                    }

                                    Level level = Level.parse(xml.getAttributeValue(null, "level").toUpperCase());
                                    logger.setLevel(Level.ALL);
                                }
                                catch (Exception e)
                                {
                                    // e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
//                e.printStackTrace();
            }
        }
    }

    private static class MyHandler extends Handler
    {
        @Override
        public void close()
        {

        }

        @Override
        public boolean isLoggable(LogRecord record)
        {
            return true;
        }

        @Override
        public void flush()
        {

        }

        @Override
        public void publish(LogRecord record)
        {
            if (!enableLogging) return;

            Level level = record.getLevel();
            if (level == Level.OFF)
            {

            }
            else
            {
                String loggerName = record.getLoggerName();
//                loggerName = loggerName.substring(loggerName.lastIndexOf(".") + 1);
                loggerName = "!" + loggerName;

                String message = Thread.currentThread() + " " + record.getMessage();
                if (level == Level.SEVERE)
                {
                    Log.e(loggerName, message, record.getThrown());
                }
                else if (level == Level.WARNING)
                {
                    Log.w(loggerName, message, record.getThrown());
                }
                else if (level == Level.INFO)
                {
                    Log.i(loggerName, message, record.getThrown());
                }
                else if (level == Level.CONFIG)
                {
                    Log.v(loggerName, message, record.getThrown());
                }
                else
                {
                    Log.d(loggerName, message, record.getThrown());
                }
            }
        }
    }

    private static class MyShortHandler extends Handler
    {
        @Override
        public void close()
        {

        }

        @Override
        public boolean isLoggable(LogRecord record)
        {
            return true;
        }

        @Override
        public void flush()
        {

        }

        @Override
        public void publish(LogRecord record)
        {
//            if (!enableLogging) return;

            Level level = record.getLevel();
            if (level == Level.OFF)
            {

            }
            else
            {
                String loggerName = record.getLoggerName();

                loggerName = loggerName.substring(loggerName.lastIndexOf(".") + 1);
                loggerName = "!" + loggerName;

                String message;

//                message = Thread.currentThread() + " " + record.getMessage();
                message = record.getMessage();
                if (level == Level.SEVERE)
                {
                    Log.e(loggerName, message, record.getThrown());
                }
                else if (level == Level.WARNING)
                {
                    Log.w(loggerName, message, record.getThrown());
                }
                else if (level == Level.INFO)
                {
                    Log.i(loggerName, message, record.getThrown());
                }
                else if (level == Level.CONFIG)
                {
                    Log.v(loggerName, message, record.getThrown());
                }
                else
                {
                    Log.d(loggerName, message, record.getThrown());
                }
            }
        }
    }
}
