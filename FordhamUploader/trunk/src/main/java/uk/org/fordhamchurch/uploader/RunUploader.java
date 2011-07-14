package uk.org.fordhamchurch.uploader;
import java.lang.Thread.UncaughtExceptionHandler;

import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.org.fordhamchurch.uploader.gui.frames.MainFrame;



public class RunUploader
{
    private static final Logger LOG = LoggerFactory.getLogger( RunUploader.class );


    /**
     * @param args
     */
    public static void main( String[] args ) throws Exception
    {
        UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );

        Thread.setDefaultUncaughtExceptionHandler( new UncaughtExceptionHandler()
        {
            public void uncaughtException( Thread t, Throwable e )
            {
                LOG.error( "Caught error in thread: " + t.getName(), e );
            }
        } );

        MainFrame.getInstance().setVisible( true );
    }

}
