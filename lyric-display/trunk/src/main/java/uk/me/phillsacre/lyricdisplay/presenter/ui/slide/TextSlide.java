/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.presenter.ui.slide;


/**
 * 
 * @author Phill
 * @since 27 Oct 2010
 */
public class TextSlide implements Slide
{
    private final String _text;

    public TextSlide(String text)
    {
	_text = text;
    }

    @Override
    public String getText()
    {
	return _text;
    }

    @Override
    public Type getType()
    {
	return Type.TEXT;
    }
}
