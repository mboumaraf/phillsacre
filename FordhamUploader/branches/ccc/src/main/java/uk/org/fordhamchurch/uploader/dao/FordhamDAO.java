package uk.org.fordhamchurch.uploader.dao;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcTransport;
import org.apache.xmlrpc.client.XmlRpcTransportFactory;

import uk.org.fordhamchurch.uploader.entities.Book;
import uk.org.fordhamchurch.uploader.entities.Speaker;
import uk.org.fordhamchurch.uploader.utils.CustomXmlRpcTransport;
import uk.org.fordhamchurch.uploader.utils.PropertyUtils;


/**
 * DAO to communicate with the Fordham XML-RPC server.
 * 
 * @author psacre
 */
public class FordhamDAO
{
    private static final Log        _log     = LogFactory.getLog( FordhamDAO.class );

    private static final FordhamDAO INSTANCE = new FordhamDAO();

    private String                  _serverUrl;
    private String                  _xmlRpcAuth;
    private XmlRpcClient            _client;


    public static FordhamDAO getInstance()
    {
        return INSTANCE;
    }

    private FordhamDAO()
    {
        _serverUrl = PropertyUtils.getProperty( "xmlrpc.url" );
        _xmlRpcAuth = PropertyUtils.getProperty( "xmlrpc.auth" );

        try
        {
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL( new URL( _serverUrl ) );
            _client = new XmlRpcClient();
            _client.setConfig( config );
            _client.setTransportFactory( new XmlRpcTransportFactory()
            {
                public XmlRpcTransport getTransport()
                {
                    return new CustomXmlRpcTransport( _client );
                }
            } );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( "Could not create XML-RPC Client", e );
        }
    }

    @SuppressWarnings( "unchecked" )
    public List<Speaker> getSpeakers()
    {
        List<Speaker> speakers = new ArrayList<Speaker>();
        Object[] results = (Object[]) sendRequest( "fordham.getSpeakers" );

        for ( Object obj : results )
        {
            Map<String, String> speaker = (Map<String, String>) obj;

            speakers.add( new Speaker( Long.valueOf( speaker.get( "id" ) ), speaker.get( "name" ) ) );
        }

        _log.debug( "Retrieved " + speakers.size() + " speakers" );

        return speakers;
    }

    @SuppressWarnings( "unchecked" )
    public List<Book> getBooks()
    {
        List<Book> books = new ArrayList<Book>();

        Object[] results = (Object[]) sendRequest( "fordham.getBibleBooks" );

        for ( Object obj : results )
        {
            Map<String, String> book = (Map<String, String>) obj;

            books.add( new Book( Long.valueOf( book.get( "id" ) ), book.get( "name" ) ) );
        }

        return books;
    }

    public void addSermon( Long speaker, Long book, String chapter, String date, String title, String filename )
    {
        // XML-RPC does not handle null values
        if (null == chapter)
        {
            chapter = "";
        }

        Object result =
                        sendRequest(
                                "fordham.addSermon", speaker.intValue(), book.intValue(), chapter, date, title,
                                filename, _xmlRpcAuth );

        _log.debug( "Result is: " + result );
    }

    private Object sendRequest( String name, Object... params )
    {
        _log.debug( String.format( "Sending request [%s] with %d params", name, params.length ) );

        try
        {
            Object results = _client.execute( name, params );

            return results;
        }
        catch ( Exception e )
        {
            throw new RuntimeException( "Could not complete request [" + name + "]", e );
        }
    }
}
