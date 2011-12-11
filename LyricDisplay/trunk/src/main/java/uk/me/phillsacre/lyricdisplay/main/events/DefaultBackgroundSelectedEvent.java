/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.events;

import uk.me.phillsacre.lyricdisplay.ui.presentation.backgrounds.Background;

/**
 * 
 * @author Phill
 * @since 11 Dec 2011
 */
public class DefaultBackgroundSelectedEvent
{
    private final Background _background;

    public DefaultBackgroundSelectedEvent(Background background)
    {
	_background = background;
    }

    public Background getBackground()
    {
	return _background;
    }
}
