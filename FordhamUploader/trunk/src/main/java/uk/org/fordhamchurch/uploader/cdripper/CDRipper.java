package uk.org.fordhamchurch.uploader.cdripper;

import java.io.File;

import uk.org.fordhamchurch.uploader.gui.models.CDDriveModel.Drive;
import uk.org.fordhamchurch.uploader.utils.Utils;
import uk.org.fordhamchurch.uploader.utils.UploadRunnerWorker.ProgressListener;


public abstract class CDRipper
{
    private Drive            _drive;
    private ProgressListener _listener;


    /**
     * Retrieve the CD Ripper appropriate for the current platform.
     * 
     * @param drive
     * @return
     */
    public static CDRipper getRipperForPlatform( Drive drive, ProgressListener listener )
    {
        CDRipper ripper = null;

        if (Utils.isOSWindows())
        {
            ripper = new Cdda2WavCDRipper();
            ripper._drive = drive;
            ripper._listener = listener;
        }

        return ripper;
    }

    protected CDRipper()
    {
    }

    protected void setProgress( int percentage )
    {
        _listener.updateProgress( percentage );
    }

    /**
     * Retrieve the current Drive object.
     * 
     * @return
     */
    protected Drive getDrive()
    {
        return _drive;
    }

    /**
     * Rip all tracks on the CD and return a single File object.
     * 
     * @return
     * @throws Exception
     */
    public abstract File rip() throws Exception;
}
