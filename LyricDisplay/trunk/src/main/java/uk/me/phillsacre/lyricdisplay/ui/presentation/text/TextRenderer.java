/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.presentation.text;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * 
 * @author Phill
 * @since 28 Nov 2011
 */
public interface TextRenderer
{
    public static final class FontStyle
    {
	private Color   _color        = Color.white;
	private Color   _outlineColor = Color.black;
	private boolean _outlined;
	private boolean _centered;

	public FontStyle(Color color, boolean outlined, boolean centered)
	{
	    _color = color;
	    _outlined = outlined;
	    _centered = centered;
	}

	public Color getColor()
	{
	    return _color;
	}

	public Color getOutlineColor()
	{
	    return _outlineColor;
	}

	public boolean isOutlined()
	{
	    return _outlined;
	}

	public boolean isCentered()
	{
	    return _centered;
	}
    }

    float getSize(Graphics2D g2d, Rectangle bounds, String[] lines);

    void renderText(Graphics2D g2d, String[] lines, float pointSize,
	    Rectangle bounds);

    void setFontStyle(FontStyle fontStyle);
}
