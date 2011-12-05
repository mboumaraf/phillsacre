/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.events;

/**
 * 
 * @author Phill
 * @since 4 Dec 2011
 */
public class DisplayLiveEvent
{
    private boolean _live;

    public DisplayLiveEvent(boolean live)
    {
	_live = live;
    }

    public boolean isLive()
    {
	return _live;
    }
}
