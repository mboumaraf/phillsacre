/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.presenter.events;

import uk.me.phillsacre.lyricdisplay.presenter.ui.slide.Slide;

/**
 * 
 * @author Phill
 * @since 8 Nov 2010
 */
public class PreviewSlideEvent
{
    private final Slide _slide;

    public PreviewSlideEvent(Slide slide)
    {
	_slide = slide;
    }

    public Slide getSlide()
    {
	return _slide;
    }
}
