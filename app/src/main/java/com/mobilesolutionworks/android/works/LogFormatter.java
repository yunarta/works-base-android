package com.mobilesolutionworks.android.works;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Created by yunarta on 25/11/15.
 */
class LogFormatter extends Formatter {
    private final String mPrefix;

    private final boolean mShortenName;

    private final boolean mThread;

    private final Date dat = new Date();

    private String format = "%1$tb %1$td, %1$tY %1$tl:%1$tM:%1$tS %1$Tp %2$s\t%4$s: %5$s%6$s%n";

    public LogFormatter(String prefix, boolean shortenName, boolean printThread) {
        mPrefix = prefix;
        mShortenName = shortenName;
        mThread = printThread;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public synchronized String format(LogRecord record) {
        dat.setTime(record.getMillis());
//        if (record.getSourceClassName() != null)
//        {
//            source = record.getSourceClassName();
//            if (record.getSourceMethodName() != null)
//            {
//                source += " " + record.getSourceMethodName();
//            }
//        }
//        else
//        {
//            source = record.getLoggerName();
//        }

        String message = formatMessage(record);

        StringBuilder sb = new StringBuilder();
        if (mThread) {
            sb.append(Thread.currentThread().toString());
            sb.append(" - ");
        }

        sb.append(message);

        message = sb.toString();

        String source = record.getLoggerName();
        if (mShortenName) {
            source = source.substring(source.lastIndexOf(".") + 1);
        }

        String throwable = "";
        if (record.getThrown() != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            pw.println();
            record.getThrown().printStackTrace(pw);
            pw.close();
            throwable = sw.toString();
        }

        return String.format(Locale.ENGLISH, format, dat, source, record.getLoggerName(), record.getLevel().getName(), message, throwable);
    }
}
