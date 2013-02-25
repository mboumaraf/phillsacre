package uk.org.fordhamchurch.uploader.encoder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.org.fordhamchurch.uploader.utils.CancelException;
import uk.org.fordhamchurch.uploader.utils.CommandBuilder;
import uk.org.fordhamchurch.uploader.utils.PropertyUtils;
import uk.org.fordhamchurch.uploader.utils.UploadRunnerWorker.ProgressListener;


/**
 * Class to convert a WAV file into an MP3. This uses the LAME encoder, requires lame executable to be available.
 * 
 * @author psacre
 */
public class MP3Encoder
{
    private static final Log    _log             = LogFactory.getLog( MP3Encoder.class );

    private static final int    DEFAULT_BITRATE  = 32;

    // LAME Commands
    private static final String CMD_LAME         = "lame";
    private static final String ARG_BITRATE      = "-b";
    private static final String ARG_HIGH_QUALITY = "-h";
    private static final String ARG_ID3_AUTHOR   = "--ta";
    private static final String ARG_ID3_TITLE    = "--tt";
    private static final String ARG_ID3_GENRE    = "--tg";

    private File                _wavFile;
    private ProgressListener    _listener;
    private String              _title;


    public MP3Encoder( File wavFile, String title, ProgressListener progressListener )
    {
        _wavFile = wavFile;
        _listener = progressListener;
        _title = title;
    }

    public File encode()
    {
        String author = PropertyUtils.getProperty( "mp3.author" );
        String genre = PropertyUtils.getProperty( "mp3.genre" );

        try
        {
            File output = File.createTempFile( "cd-ripped", ".mp3" );

            _log.debug( "MP3 will be written to: " + output.getAbsolutePath() );

            CommandBuilder builder = new CommandBuilder( CMD_LAME );
            builder.addArgument( ARG_HIGH_QUALITY );
            builder.addArgument( ARG_BITRATE, DEFAULT_BITRATE );
            builder.addArgument( ARG_ID3_AUTHOR, author );
            builder.addArgument( ARG_ID3_GENRE, genre );
            builder.addArgument( ARG_ID3_TITLE, _title );

            builder.addArgument( _wavFile.getAbsolutePath() );
            builder.addArgument( output.getAbsolutePath() );

            ProcessBuilder pb = builder.getProcess();
            pb.redirectErrorStream( true );
            Process p = pb.start();

            BufferedReader reader = new BufferedReader( new InputStreamReader( p.getInputStream() ) );

            Pattern pattern = Pattern.compile( "^.+\\((..)%\\).+$" );

            String line;
            while (null != (line = reader.readLine()))
            {
                Matcher m = pattern.matcher( line );
                if (m.matches())
                {
                    try
                    {
                        int percentage = Integer.parseInt( m.group( 1 ).trim() );
                        _listener.updateProgress( percentage );
                    }
                    catch ( CancelException e )
                    {
                        p.destroy();
                        throw e;
                    }
                }
            }

            reader.close();

            p.waitFor();
            p = null;

            _listener.updateProgress( 100 );

            return output;
        }
        catch ( InterruptedException e )
        {
            throw new CancelException();
        }
        catch ( IOException e )
        {
            _log.error( "Could not convert MP3", e );
            throw new RuntimeException( "Could not convert MP3", e );
        }
    }
}
