/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.presenter.ui.slide;

import java.awt.Graphics2D;
import java.awt.Rectangle;

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

    void render(Graphics2D g2d, Rectangle bounds);
}
