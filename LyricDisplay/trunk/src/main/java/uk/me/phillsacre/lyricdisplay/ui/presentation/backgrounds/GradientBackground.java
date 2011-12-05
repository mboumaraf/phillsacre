/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.presentation.backgrounds;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;

/**
 * 
 * @author Phill
 * @since 28 Nov 2011
 */
public class GradientBackground implements Background
{
    private Color _startColor;
    private Color _endColor;

    public GradientBackground(Color startColor, Color endColor)
    {
	_startColor = startColor;
	_endColor = endColor;
    }

    public void renderBackground(Graphics2D g2d, int width, int height)
    {
	Graphics2D g = (Graphics2D) g2d.create();
	GradientPaint paint = new GradientPaint(0, 0, _startColor, 0, height,
	        _endColor, true);
	g.setPaint(paint);

	g.fillRect(0, 0, width, height);
	g.dispose();
    }
}
