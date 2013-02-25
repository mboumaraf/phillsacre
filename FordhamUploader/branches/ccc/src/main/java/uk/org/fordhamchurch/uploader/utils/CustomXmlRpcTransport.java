/**
 * 
 */
package uk.org.fordhamchurch.uploader.utils;

import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcSunHttpTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;


/**
 * 
 * @author Phill
 * @since 13 Jul 2011
 */
public class CustomXmlRpcTransport extends XmlRpcSunHttpTransport
{
    private static final Logger LOG = LoggerFactory.getLogger( CustomXmlRpcTransport.class );


    /**
     * @param pClient
     */
    public CustomXmlRpcTransport( XmlRpcClient pClient )
    {
        super( pClient );
    }

    @Override
    protected InputStream getInputStream() throws XmlRpcException
    {
        try
        {
            InputStream input = super.getInputStream();

            return new LoggingInputStream( input );
        }
        catch ( XmlRpcException e )
        {
            HttpURLConnection conn = (HttpURLConnection) getURLConnection();
            InputStream errorStream = conn.getErrorStream();

            if (null == errorStream)
            {
                LOG.error( "ErrorStream is null!" );
            }
            else
            {
                ByteArrayOutputStream output = new ByteArrayOutputStream( 1024 * 30 );
                byte[] buffer = new byte[ 1024 ];
                int read;

                try
                {
                    while ( (read = errorStream.read( buffer )) != -1)
                    {
                        output.write( buffer, 0, read );
                    }
                }
                catch ( IOException ioe )
                {
                    throw new RuntimeException( "Could not read error stream", ioe );
                }

                logXml( "Error response", output.toString() );
            }

            throw e;
        }
    }

    @Override
    protected void writeRequest( final ReqWriter pWriter ) throws IOException, XmlRpcException, SAXException
    {
        ReqWriter loggingWriter = new ReqWriter()
        {
            public void write( OutputStream pStream ) throws XmlRpcException, IOException, SAXException
            {
                final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                FilterOutputStream output = new FilterOutputStream( pStream )
                {
                    public void write( int b ) throws IOException
                    {
                        super.write( b );

                        buffer.write( b );
                    }

                    public void write( byte[] b, int off, int len ) throws IOException
                    {
                        super.write( b, off, len );

                        buffer.write( b, off, len );
                    }
                };
                pWriter.write( output );
                logXml( "Sent Data", buffer.toString() );
            }
        };
        super.writeRequest( loggingWriter );
    }


    private static class LoggingInputStream extends FilterInputStream
    {
        private ByteArrayOutputStream _buffer = new ByteArrayOutputStream();


        public LoggingInputStream( InputStream inputStream )
        {
            super( inputStream );
        }

        @Override
        public int read() throws IOException
        {
            int b = super.read();

            _buffer.write( b );

            return b;
        }

        @Override
        public int read( byte[] b, int off, int len ) throws IOException
        {
            int read = super.read( b, off, len );

            if (read == -1)
            {
                logXml( "Received Data", _buffer.toString() );
            }
            else
            {
                _buffer.write( b, off, read );
            }

            return read;
        }
    }


    public static void logXml( String description, String xml )
    {
        xml = xml.replaceAll( "([ ]{2,}|\\r|\\n|\\t)", "" ); // remove unnecessary whitespace

        LOG.debug( description + " => " + xml );
    }
}
