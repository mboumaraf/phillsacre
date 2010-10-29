/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.presenter.events;

import uk.me.phillsacre.lyricdisplay.presenter.ui.slide.Slide;

/**
 * 
 * @author Phill
 * @since 27 Oct 2010
 */
public class ChangeSlideEvent
{
    private final Slide _slide;

    public ChangeSlideEvent(Slide slide)
    {
	_slide = slide;
    }

    public Slide getSlide()
    {
	return _slide;
    }
}
