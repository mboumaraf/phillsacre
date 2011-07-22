package uk.org.fordhamchurch.uploader.encoder;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.org.fordhamchurch.uploader.utils.CancelException;
import uk.org.fordhamchurch.uploader.utils.CommandBuilder;
import uk.org.fordhamchurch.uploader.utils.UploadRunnerWorker.ProgressListener;


/**
 * Use MP3Gain (http://mp3gain.sourceforge.net) to normalise the MP3.
 * 
 * @author psacre
 */
public class MP3Normaliser
{
    private static final Log    _log            = LogFactory.getLog( MP3Normaliser.class );

    private static final String CMD_MP3GAIN     = "mp3gain.exe";
    private static final String ARG_TRACK_GAIN  = "/r";
    private static final String ARG_IGNORE_WARN = "/c";

    private File                _mp3File;
    private ProgressListener    _listener;


    public MP3Normaliser( File mp3File, ProgressListener listener )
    {
        _mp3File = mp3File;
        _listener = listener;
    }

    public void normalise()
    {
        CommandBuilder builder = new CommandBuilder( CMD_MP3GAIN );

        builder.addArgument( ARG_TRACK_GAIN );
        builder.addArgument( ARG_IGNORE_WARN );
        builder.addArgument( _mp3File.getAbsolutePath() );

        ProcessBuilder pb = builder.getProcess();
        pb.redirectErrorStream( true );

        Pattern pattern = Pattern.compile( "^ +(\\d{1,2})%" );

        try
        {
            Process p = pb.start();

            BufferedReader reader = new BufferedReader( new InputStreamReader( p.getInputStream() ) );

            String line;
            while (null != (line = reader.readLine()))
            {
                Matcher m = pattern.matcher( line );
                if (m.find())
                {
                    try
                    {
                        int percentage = Integer.parseInt( m.group( 1 ) );
                        _listener.updateProgress( percentage );
                    }
                    catch ( CancelException e )
                    {
                        p.destroy();
                        return;
                    }
                }
            }

            p.waitFor();
        }
        catch ( InterruptedException e )
        {
            throw new CancelException();
        }
        catch ( Exception e )
        {
            _log.error( "Could not normalise MP3", e );
            throw new RuntimeException( "Could not normalise MP3", e );
        }
    }
}
