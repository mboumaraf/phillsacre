/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.utils;

import java.net.URL;

/**
 * 
 * @author Phill
 * @since 3 Dec 2011
 */
public final class Utils
{
    public static final URL getImageURL(String loc)
    {
	return Utils.class.getClassLoader().getResource(loc);
    }
}
