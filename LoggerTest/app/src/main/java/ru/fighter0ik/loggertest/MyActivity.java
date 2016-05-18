package ru.fighter0ik.loggertest;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyActivity extends AppCompatActivity
{
    private static final String sLevelKey = "level";

    //

    private Spinner mSpinner;
    private EditText editText;
    private Button button;

    private Logger mLogger;

    //

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_my );

        mSpinner = (Spinner) findViewById( R.id.activity_my_spinner );
        editText = (EditText) findViewById( R.id.activity_my_editText );
        button = (Button) findViewById( R.id.activity_my_button );

        mSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected( AdapterView<?> parent, View view, int position, long id )
            {
                Level level = levelFromSpinner();
                levelToPreferences( level );
                mLogger.setLevel( level );
            }

            @Override
            public void onNothingSelected( AdapterView<?> parent )
            {}
        } );

        button.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                Level level = levelFromSpinner();
                String msg = editText.getText().toString();
                if ( !msg.isEmpty() ) mLogger.log( level, msg );
                editText.setText( "" );
            }
        } );

        Level level = levelFromPreferences();
        levelToSpinner( level );

        mLogger = Logger.getLogger( "LoggerTest" );
        mLogger.setUseParentHandlers( false );
        mLogger.setLevel( level );

        Handler[] handlers = mLogger.getHandlers();
        for ( Handler handler : handlers )
        {
            mLogger.removeHandler( handler );
        }
        addConsoleHandler();
        addFileHandler();
    }

    @Override
    protected void onDestroy()
    {
        Handler[] handlers = mLogger.getHandlers();
        for ( Handler handler : handlers )
        {
            mLogger.removeHandler( handler );
            handler.flush();
            handler.close();
        }

        super.onDestroy();
    }

    private void addConsoleHandler()
    {
        try
        {
            Handler handler = new MyConsoleHandler();
            handler.setFormatter( new MyFormatter() );
            handler.setEncoding( "UTF-8" );
            handler.setLevel( Level.ALL );
            mLogger.addHandler( handler );
        }
        catch ( UnsupportedEncodingException ex )
        {
            throw new RuntimeException( ex );
        }
    }

    private void addFileHandler()
    {
        File dir = getExternalFilesDir( null );
        File file = new File( dir, "log.txt" );

        try
        {
            Handler handler = new FileHandler( file.getAbsolutePath(), 128*1024*1024, 8, true );
            handler.setFormatter( new MyFormatter() );
            handler.setEncoding( "UTF-8" );
            handler.setLevel( Level.ALL );
            mLogger.addHandler( handler );
        }
        catch ( UnsupportedEncodingException ex )
        {
            throw new RuntimeException( ex );
        }
        catch ( IOException ex )
        {
            Log.e( getClass().getName(), "addFileHandler(). got io exception", ex );
        }
    }

    private void levelToSpinner( Level level )
    {
        String levelString = levelToString( level );

        SpinnerAdapter adapter = mSpinner.getAdapter();
        int count = adapter.getCount();

        for ( int i=0; i<count; i++ )
        {
            String s = adapter.getItem( i ).toString();
            if ( s.equals( levelString ) )
            {
                mSpinner.setSelection( i );
                break;
            }
        }
    }

    private Level levelFromSpinner()
    {
        return levelFromString( (String) mSpinner.getSelectedItem() );
    }

    private void levelToPreferences( Level level )
    {
        if ( level==null ) level = Level.OFF;
        SharedPreferences preferences = getPreferences( MODE_PRIVATE );
        preferences.edit().putInt( sLevelKey, level.intValue() ).apply();
    }

    private Level levelFromPreferences()
    {
        SharedPreferences preferences = getPreferences( MODE_PRIVATE );
        int levelInt = preferences.getInt( sLevelKey, Level.OFF.intValue() );
        return Level.parse( Integer.toString( levelInt ) );
    }

    private String levelToString( Level level )
    {
        if ( Level.SEVERE==level ) return "SEVERE";
        if ( Level.WARNING==level ) return "WARNING";
        if ( Level.INFO==level ) return "INFO";
        if ( Level.CONFIG==level ) return "CONFIG";
        if ( Level.FINE==level ) return "FINE";
        if ( Level.FINER==level ) return "FINER";
        if ( Level.FINEST==level ) return "FINEST";
        if ( Level.ALL==level ) return "ALL";
        return "OFF";
    }

    private Level levelFromString( String s )
    {
        if ( "SEVERE".equals( s ) ) return Level.SEVERE;
        if ( "WARNING".equals( s ) ) return Level.WARNING;
        if ( "INFO".equals( s ) ) return Level.INFO;
        if ( "CONFIG".equals( s ) ) return Level.CONFIG;
        if ( "FINE".equals( s ) ) return Level.FINE;
        if ( "FINER".equals( s ) ) return Level.FINER;
        if ( "FINEST".equals( s ) ) return Level.FINEST;
        if ( "ALL".equals( s ) ) return Level.ALL;
        return Level.OFF;
    }
}
