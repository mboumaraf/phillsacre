/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.events;

/**
 * 
 * @author Phill
 * @since 4 Dec 2011
 */
public class BlankScreenEvent
{
    private boolean _blank;
    
    public BlankScreenEvent(boolean blank)
    {
	_blank = blank;
    }

    public boolean isBlank()
    {
        return _blank;
    }
}
