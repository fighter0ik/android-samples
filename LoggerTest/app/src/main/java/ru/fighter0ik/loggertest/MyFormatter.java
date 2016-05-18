package ru.fighter0ik.loggertest;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Created by DS on 18.05.2016.
 */
public class MyFormatter extends Formatter
{
    private static final SimpleDateFormat sFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.SSS", Locale.US );
    private static final String sSeparator = " --- ";

    //

    @Override
    public String format( LogRecord r )
    {
        StringBuilder builder = new StringBuilder();
        builder.append( r.getLevel() ).append( sSeparator );
        builder.append( sFormat.format( new Date( r.getMillis() ) ) ).append( sSeparator );
        builder.append( "THREAD " ).append( r.getThreadID() ).append( sSeparator );
        builder.append( r.getSourceClassName() ).append( sSeparator );
        builder.append( r.getSourceMethodName() ).append( sSeparator );
        builder.append( r.getMessage() ).append( "\n" );
        return builder.toString();
    }
}
