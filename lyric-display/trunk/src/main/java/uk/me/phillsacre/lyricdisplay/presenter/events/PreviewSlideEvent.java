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
    private final int   _pageNo;


    public PreviewSlideEvent( Slide slide, int pageNo )
    {
        _slide = slide;
        _pageNo = pageNo;
    }

    public int getPageNo()
    {
        return _pageNo;
    }

    public Slide getSlide()
    {
        return _slide;
    }
}
