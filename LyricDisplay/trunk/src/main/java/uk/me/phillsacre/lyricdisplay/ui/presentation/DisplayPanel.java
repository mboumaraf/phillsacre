/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.presentation;

import java.awt.Graphics2D;

/**
 * 
 * @author Phill
 * @since 28 Nov 2011
 */
public interface DisplayPanel
{
    void render(Graphics2D g2d, int width, int height);
}
