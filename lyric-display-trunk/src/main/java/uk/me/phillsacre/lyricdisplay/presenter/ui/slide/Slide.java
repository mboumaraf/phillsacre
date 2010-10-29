/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.presenter.ui.slide;

/**
 * 
 * @author Phill
 * @since 27 Oct 2010
 */
public interface Slide
{
    enum Type
    {
	TEXT
    }

    Type getType();

    String getText();
}
