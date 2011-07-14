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
        InputStream input = super.getInputStream();

        return new LoggingInputStream( input );
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
                    public void write( byte[] b, int off, int len ) throws IOException
                    {
                        super.write( b, off, len );

                        buffer.write( b, off, len );
                    }
                };
                pWriter.write( output );
                logXml( "Sent Data", buffer.toString( "UTF8" ) );
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
        public int read( byte[] b, int off, int len ) throws IOException
        {
            int read = super.read( b, off, len );

            if (read == -1)
            {
                logXml( "Received Data", _buffer.toString( "UTF8" ) );
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
        xml = xml.replace( "\n", "" );
        xml = xml.replace( "\r", "" );
        xml = xml.replaceAll( "[\\s]{2,}", "" ); // remove unnecessary spaces

        LOG.debug( description + " => " + xml );
    }
}
