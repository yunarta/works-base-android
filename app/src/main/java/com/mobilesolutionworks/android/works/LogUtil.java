package com.mobilesolutionworks.android.works;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.text.TextUtils;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yunarta on 6/3/16.
 */
public class LogUtil {
    public static Class<? extends LogHandler> sDefaultHandler;

    public static Logger getLogger(Class cl) {
        return Logger.getLogger(cl.getName());
    }

    public static Logger getLogger(String name) {
        return Logger.getLogger(name);
    }

    public static void setDefaultLogger(Class<? extends LogHandler> handler) {
        sDefaultHandler = handler;
    }

    public static void configure(Context context, int id) {
        doConfigure(context, id);
    }

    public static void configure(Context context) {
        doConfigure(context, context.getResources().getIdentifier("logutil", "xml", context.getPackageName()));
    }

    private static void doConfigure(Context context, int identifier) {
        File dir = context.getFilesDir();

        File logDir = new File(dir, "logs");
        logDir.mkdirs();

        File[] files = logDir.listFiles();
        for (File file : files) {
            if (file.getName().endsWith(".lck")) {
                file.delete();
            }
        }

        Resources resources = context.getResources();
        LogConfig logConfig = null;

        if (identifier != 0) {
            XmlResourceParser xml = resources.getXml(identifier);
            boolean parsing = true;
            try {
                while (parsing) {
                    int token = xml.next();
                    switch (token) {
                        case XmlResourceParser.END_DOCUMENT: {
                            parsing = false;
                            break;
                        }

                        case XmlPullParser.START_TAG: {
                            String name = xml.getName();
                            if ("package".equals(name)) {
                                try {
                                    String packageName = xml.getAttributeValue(null, "name");

                                    boolean shorten = xml.getAttributeBooleanValue(null, "shortName", false);
                                    boolean printThread = xml.getAttributeBooleanValue(null, "printThread", false);
                                    String prefix = xml.getAttributeValue(null, "prefix");
                                    Level level = Level.parse(xml.getAttributeValue(null, "level").toUpperCase());

                                    logConfig = new LogConfig(packageName, prefix, shorten, printThread, level);

                                    Logger logger;
                                    Handler[] handlers;

                                    logger = Logger.getLogger(packageName);
                                    handlers = logger.getHandlers();
                                    for (Handler h : handlers) {
                                        if (sDefaultHandler != null && h.getClass().isAssignableFrom(sDefaultHandler)) {
                                            logger.removeHandler(h);
                                        }
                                    }

                                    LogHandler handler;

                                    if (sDefaultHandler != null) {
                                        handler = sDefaultHandler.newInstance();
                                        handler.setup(prefix, shorten, printThread);

                                        handler.setLevel(level);

                                        logger.addHandler(handler);
                                        logger.setLevel(level);
                                    }
                                } catch (Exception e) {
                                    // e.printStackTrace();
                                }
                            } else if ("fileLogger".equals(name)) {
                                if (logConfig != null) {
                                    Logger logger;
                                    Handler[] handlers;

                                    String file = xml.getAttributeValue(null, "file");
                                    String format = xml.getAttributeValue(null, "format");
                                    int limit = xml.getAttributeIntValue(null, "limit", 1024);
                                    int count = xml.getAttributeIntValue(null, "count", 5);

                                    String pattern = logDir.getAbsolutePath() + "/" + file;
                                    logger = Logger.getLogger(logConfig.name);
                                    handlers = logger.getHandlers();
                                    for (Handler h : handlers) {
                                        if (h instanceof FileHandler) {
                                            logger.removeHandler(h);
                                        }
                                    }

                                    FileHandler handler = new FileHandler(pattern, limit, count, true);
                                    handler.setLevel(logConfig.level);

                                    LogFormatter formatter = new LogFormatter(logConfig.prefix, logConfig.shortenName, logConfig.printThread);

                                    if (!TextUtils.isEmpty(format)) {
                                        formatter.setFormat(format);
                                    }
                                    handler.setFormatter(formatter);

                                    logger.addHandler(handler);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
    }

    private static class LogConfig {
        final String name;
        final String prefix;
        final boolean shortenName;
        final boolean printThread;
        final Level level;

        public LogConfig(String name, String prefix, boolean shortenName, boolean printThread, Level level) {
            this.name = name;
            this.prefix = prefix;
            this.shortenName = shortenName;
            this.printThread = printThread;
            this.level = level;
        }
    }

}
