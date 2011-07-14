package uk.org.fordhamchurch.uploader.utils;

import java.io.File;
import java.util.List;

import javax.swing.SwingWorker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.org.fordhamchurch.uploader.cdripper.CDRipper;
import uk.org.fordhamchurch.uploader.dao.FordhamDAO;
import uk.org.fordhamchurch.uploader.encoder.MP3Encoder;
import uk.org.fordhamchurch.uploader.encoder.MP3Normaliser;
import uk.org.fordhamchurch.uploader.entities.Upload;
import uk.org.fordhamchurch.uploader.gui.models.CDDriveModel.Drive;
import uk.org.fordhamchurch.uploader.gui.models.ProgressPresentationModel;

import com.sshtools.j2ssh.FileTransferProgress;
import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.transport.IgnoreHostKeyVerification;


/**
 * SwingWorker implementation to convert the CD to MP3 and upload.
 * 
 * @author psacre
 */
public class UploadRunnerWorker extends SwingWorker<Void, String>
{
    private static final Log          _log = LogFactory.getLog( UploadRunnerWorker.class );

    private ProgressPresentationModel _pModel;
    private Upload                    _upload;
    private String                    _targetFilename;


    public UploadRunnerWorker( ProgressPresentationModel pModel, Upload upload )
    {
        _pModel = pModel;
        _upload = upload;

        _targetFilename = Utils.getSanitisedName( _upload.getTitle() ) + ".mp3";
    }

    @Override
    protected Void doInBackground() throws Exception
    {
        final File originalFile = _upload.getFile();

        try
        {
            File mp3 = null;

            if (null == originalFile)
            {
                File wav = ripCD();

                mp3 = convertToMP3( wav );

                if ( !wav.delete())
                {
                    _log.warn( "Could not delete WAV file" );
                }
            }
            else
            {
                // If the file is an MP3 already, it will be re-coded in
                // the required bitrate
                mp3 = convertToMP3( originalFile );
            }

            normalise( mp3 );

            uploadToWebsite( mp3 );

            if ( !mp3.delete())
            {
                _log.warn( "Could not delete MP3 file" );
            }

            addToDatabase();
        }
        catch ( Exception e )
        {
            _log.error( "Could not upload", e );
            throw new RuntimeException( "Could not upload", e );
        }

        return null;
    }

    @Override
    protected void done()
    {
        _pModel.setStatus( "Complete" );

        setProgress( 100 );
        _pModel.setComplete( true );
    }

    @Override
    protected void process( List<String> statuses )
    {
        for ( String status : statuses )
        {
            _pModel.setStatus( status );
        }
    }

    private File ripCD()
    {
        publish( "Ripping CD..." );

        Drive drive = _upload.getCdLocation();

        _log.debug( "Drive: " + drive );

        CDRipper ripper = CDRipper.getRipperForPlatform( drive, new DefaultProgressListener() );

        try
        {
            File rawFile = ripper.rip();

            _log.debug( "Rip complete, file size is: " + rawFile.length() + " bytes" );
            publish( "CD Rip complete" );
            setProgress( 100 );

            return rawFile;
        }
        catch ( Exception e )
        {
            _log.error( "Could not rip CD", e );
            throw new RuntimeException( "Could not rip CD", e );
        }
    }

    private File convertToMP3( File wavFile )
    {
        publish( "Converting to MP3..." );
        setProgress( 0 );

        MP3Encoder encoder = new MP3Encoder( wavFile, _upload.getTitle(), new DefaultProgressListener() );
        File mp3 = encoder.encode();

        _log.debug( "Encode complete, encoded file size is " + mp3.length() + " bytes" );

        publish( "MP3 Encode complete" );
        setProgress( 100 );

        return mp3;
    }

    private void normalise( File mp3 )
    {
        publish( "Normalising MP3..." );
        setProgress( 0 );

        MP3Normaliser normaliser = new MP3Normaliser( mp3, new DefaultProgressListener() );
        normaliser.normalise();

        publish( "Normalise complete" );
        setProgress( 100 );
    }

    private void uploadToWebsite( File mp3File )
    {
        publish( "Uploading to website..." );
        setProgress( 0 );

        String hostname = PropertyUtils.getProperty( "ftp.hostname" );
        String username = PropertyUtils.getProperty( "ftp.username" );
        String password = PropertyUtils.getProperty( "ftp.password" );

        try
        {
            String date = _upload.getDate();
            String[] parts = date.split( "-" );
            String folders = parts[ 2 ] + "/" + parts[ 1 ] + "/" + parts[ 0 ];

            SshClient client = new SshClient();
            client.connect( hostname, new IgnoreHostKeyVerification() );

            PasswordAuthenticationClient auth = new PasswordAuthenticationClient();
            auth.setUsername( username );
            auth.setPassword( password );

            int result = client.authenticate( auth );

            if (result != AuthenticationProtocolState.COMPLETE)
            {
                _log.error( "Could not login" );
                throw new RuntimeException( "could not login to FTP" );
            }

            SftpClient sftp = client.openSftpClient();
            sftp.cd( "public_html/audio" );

            _log.info( "Creating folder structure: " + folders );

            sftp.mkdirs( folders );

            sftp.cd( folders );

            _log.info( String.format( "Uploading [%s] to [%s]", mp3File.getName(), sftp.pwd() + "/" + folders + "/"
                                                                                   + _targetFilename ) );

            sftp.put( mp3File.getAbsolutePath(), _targetFilename, new DefaultProgressListener() );

            sftp.quit();

            client.disconnect();

            publish( "Upload complete" );
        }
        catch ( Exception e )
        {
            _log.error( "Could not upload to website", e );
            throw new RuntimeException( "Could not upload to website", e );
        }
    }

    private void addToDatabase()
    {
        FordhamDAO.getInstance().addSermon(
                _upload.getSpeaker().getId(), _upload.getBook().getId(), _upload.getChapter(), _upload.getDate(),
                _upload.getTitle(), _targetFilename );
    }


    private class DefaultProgressListener implements ProgressListener, FileTransferProgress
    {
        private long _bytesTotal;


        public void updateProgress( int percentage )
        {
            setProgress( percentage );
        }

        public void completed()
        {
            setProgress( 100 );
        }

        public boolean isCancelled()
        {
            return false;
        }

        public void progressed( long bytesSoFar )
        {
            setProgress( (int) ( (bytesSoFar * 100) / _bytesTotal) );
        }

        /**
         * @param remoteFile
         */
        public void started( long bytesTotal, String remoteFile )
        {
            _bytesTotal = bytesTotal;
        }

    }


    public static interface ProgressListener
    {
        public void updateProgress( int percentage );
    }
}
