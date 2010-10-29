/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.presenter.events;

/**
 * 
 * @author Phill
 * @since 28 Oct 2010
 */
public class BlackoutEvent
{
    private final boolean _blackout;

    public BlackoutEvent(boolean blackout)
    {
	_blackout = blackout;
    }

    public boolean isBlackout()
    {
	return _blackout;
    }
}
