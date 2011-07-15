package uk.org.fordhamchurch.uploader.utils;

import java.util.ResourceBundle;


public class PropertyUtils
{
    private static ResourceBundle _resourceBundle = ResourceBundle.getBundle( "uploader" );


    private PropertyUtils()
    {
        // Prevent instantiation
    }

    /**
     * Returns the property associated with the key. Note that a system property can override the specified property,
     * i.e. if there is a system property with the same name it will be used in preference to the resource bundle
     * property.
     * 
     * @param key
     * @return
     */
    public static String getProperty( String key )
    {
        if (System.getProperties().containsKey( key ))
        {
            return System.getProperty( key );
        }
        return _resourceBundle.getString( key );
    }
}
