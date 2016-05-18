package ru.fighter0ik.loggertest;

import android.util.Log;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Created by DS on 18.05.2016.
 */
public class MyConsoleHandler extends Handler
{
    @Override
    public void close()
    {}

    @Override
    public void flush()
    {}

    @Override
    public void publish( LogRecord record )
    {
        if ( record==null ) return;
        if ( !super.isLoggable( record ) ) return;

        String msg = getFormatter().format( record );
        String tag = record.getLoggerName();
        Level level = record.getLevel();

        if ( level==Level.FINE || level==Level.FINER || level==Level.FINEST ) Log.println( Log.DEBUG, tag, msg );
        else if ( level==Level.CONFIG || level==Level.INFO ) Log.println( Log.INFO, tag, msg );
        else if ( level==Level.WARNING ) Log.println( Log.WARN, tag, msg );
        else if ( level==Level.SEVERE ) Log.println( Log.ERROR, tag, msg );
        else Log.println( Log.VERBOSE, tag, msg );
    }
}
