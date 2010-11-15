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
    private final int   _pageNo;


    public ChangeSlideEvent( Slide slide, int pageNo )
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
