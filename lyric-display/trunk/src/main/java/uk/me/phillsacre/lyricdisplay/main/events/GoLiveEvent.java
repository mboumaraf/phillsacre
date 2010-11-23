/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.events;

import uk.me.phillsacre.lyricdisplay.presenter.ui.slide.Slide;

/**
 * 
 * @author Phill
 * @since 23 Nov 2010
 */
public class GoLiveEvent
{
    private final Slide _slide;

    public GoLiveEvent(Slide slide)
    {
	_slide = slide;
    }

    public Slide getSlide()
    {
	return _slide;
    }
}
