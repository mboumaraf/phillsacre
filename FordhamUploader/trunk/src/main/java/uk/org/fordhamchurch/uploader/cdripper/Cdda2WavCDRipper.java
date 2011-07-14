package uk.org.fordhamchurch.uploader.cdripper;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.org.fordhamchurch.uploader.utils.CommandBuilder;


/**
 * Implementation of CDRipper using the CDDA2WAV tool.
 * 
 * @author psacre
 */
public class Cdda2WavCDRipper extends CDRipper
{
    private static final Log    _log              = LogFactory.getLog( Cdda2WavCDRipper.class );

    private static final String FILENAME          = "cd-ripped.wav";

    // CDDA2WAV command and arguments
    private static final String CDDA2WAV_CMD      = "cdda2wav";
    private static final String ARG_NO_INFO_FILES = "-H";
    private static final String ARG_INFO_ONLY     = "-J";
    private static final String ARG_TRACKS        = "-t %d+%d";
    private static final String ARG_GUI           = "-g";
    private static final String ARG_MONO          = "-m";


    @Override
    public File rip() throws Exception
    {
        int trackCount = getTrackCount();

        _log.debug( "Number of tracks: " + trackCount );

        return doRip( trackCount );
    }

    private File doRip( int trackCount )
    {
        _log.debug( "Attempting to rip CD..." );

        try
        {
            CommandBuilder builder = new CommandBuilder( CDDA2WAV_CMD );
            builder.addArgument( ARG_NO_INFO_FILES );
            builder.addArgument( ARG_GUI );
            builder.addArgument( ARG_MONO );

            if (trackCount > 1)
            {
                builder.addArgument( String.format( ARG_TRACKS, 1, trackCount ) );
            }

            builder.addArgument( FILENAME );

            ProcessBuilder pb = builder.getProcess();
            pb.redirectErrorStream( true );
            Process p = pb.start();

            BufferedReader reader = new BufferedReader( new InputStreamReader( p.getInputStream() ) );

            int filesDone = 0;
            int totalPercentage = 0;

            String line;
            while ((line = reader.readLine()) != null)
            {
                if (line.contains( "recorded successfully" ))
                {
                    _log.debug( line );

                    filesDone++ ;
                    totalPercentage += (100 / trackCount);
                }
                else if (line.endsWith( "%" ))
                {
                    int percentage =
                                     Integer.parseInt( line.substring( line.lastIndexOf( ' ' ) + 1, line.indexOf( '%' ) ) );

                    setProgress( totalPercentage + (percentage / trackCount) );
                }
            }

            reader.close();

            p.waitFor();
            p = null;
        }
        catch (Exception e)
        {
            _log.error( "Could not rip CD", e );
            throw new RuntimeException( "Could not rip CD", e );
        }

        return new File( new File( System.getProperty( "java.io.tmpdir" ) ), FILENAME );
    }

    /**
     * Retrieve the number of tracks on the disc
     * 
     * @return
     */
    private int getTrackCount()
    {
        CommandBuilder builder = new CommandBuilder( CDDA2WAV_CMD );

        builder.addArgument( ARG_NO_INFO_FILES );
        builder.addArgument( ARG_INFO_ONLY );
        builder.addArgument( ARG_GUI );

        try
        {
            ProcessBuilder pb = builder.getProcess();
            pb.redirectErrorStream( true );

            Process p = pb.start();

            BufferedReader reader = new BufferedReader( new InputStreamReader( p.getInputStream() ) );

            int trackCount = -1;

            String line;
            while (null != (line = reader.readLine()))
            {
                if (line.startsWith( "Tracks:" ))
                {
                    trackCount = Integer.parseInt( line.substring( 7, line.indexOf( ' ' ) ) );
                }
            }

            p.waitFor();
            p = null;

            return trackCount;
        }
        catch (Exception e)
        {
            _log.error( "Could not get track count", e );
            throw new RuntimeException( "Could not get track count", e );
        }
    }
}
