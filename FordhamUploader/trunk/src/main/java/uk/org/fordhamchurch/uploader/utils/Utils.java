package uk.org.fordhamchurch.uploader.utils;

import java.io.Closeable;


public class Utils
{
    private Utils()
    {
        // Prevent instantiation
    }

    /**
     * Returns true if the underlying OS is Windows.
     * 
     * @return
     */
    public static boolean isOSWindows()
    {
        return System.getProperty( "os.name" ).toLowerCase().contains( "win" );
    }

    /**
     * Closes an input stream. Discards any exceptions that occur.
     * 
     * @param input
     */
    public static void close( Closeable closeable )
    {
        if (null != closeable)
        {
            try
            {
                closeable.close();
            }
            catch ( Exception e )
            {
                // Do nothing
            }
        }
    }

    /**
     * Converts a sermon title into a sanitised filename. For example "Andy Saville - Daniel 6" would get converted into
     * "andy-saville-daniel-6".
     * 
     * @param title The title of the sermon
     * @return The sanitised filename
     */
    public static String getSanitisedName( String title )
    {
        title = title.toLowerCase();

        title = title.replaceAll( "[^a-z0-9\\-]+", "-" );
        if (title.endsWith( "-" ))
        {
            title = title.substring( 0, title.length() - 1 );
        }
        title = title.replaceAll( "\\-{2,}", "-" );

        return title;
    }
}
