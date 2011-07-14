package uk.org.fordhamchurch.uploader.utils;

import java.util.ResourceBundle;


public class PropertyUtils
{
    private static ResourceBundle _resourceBundle = ResourceBundle.getBundle( "uploader" );


    private PropertyUtils()
    {
        // Prevent instantiation
    }
    
    public static String getProperty(String key)
    {
        return _resourceBundle.getString( key );
    }
}
