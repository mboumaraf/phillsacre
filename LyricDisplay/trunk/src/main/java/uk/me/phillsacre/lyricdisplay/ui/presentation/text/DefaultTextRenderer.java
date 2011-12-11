/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.presentation.text;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;

import javax.inject.Named;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author Phill
 * @since 28 Nov 2011
 */
@Named("defaultTextRenderer")
public class DefaultTextRenderer implements TextRenderer
{
    private FontStyle _fontStyle;
    private boolean   _useDefaultRowHeight;

    public DefaultTextRenderer()
    {
	this(new FontStyle(Color.white, true, true));
    }

    public DefaultTextRenderer(FontStyle fontStyle)
    {
	_fontStyle = fontStyle;
	_useDefaultRowHeight = true;
    }

    public void setUseDefaultRowHeight(boolean useDefaultRowHeight)
    {
	_useDefaultRowHeight = useDefaultRowHeight;
    }

    public void renderText(Graphics2D g2d, String[] lines, float pointSize,
	    Rectangle bounds)
    {
	Graphics2D g = (Graphics2D) g2d.create();

	g.setColor(_fontStyle.getColor());
	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	        RenderingHints.VALUE_ANTIALIAS_ON);
	g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
	        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	g.setRenderingHint(RenderingHints.KEY_RENDERING,
	        RenderingHints.VALUE_RENDER_QUALITY);

	FontRenderContext frc = g.getFontRenderContext();
	Font f = getFont();
	f = f.deriveFont(pointSize);

	final int left = (int) bounds.getX();
	int top = (int) bounds.getY();
	final FontMetrics fm = g2d.getFontMetrics(f);

	if (_fontStyle.isCentered())
	{
	    top += (bounds.height - (fm.getHeight() * lines.length)) / 2;
	}

	for (int i = 0; i < lines.length; i++)
	{
	    if (StringUtils.isBlank(lines[i]))
	    {
		continue;
	    }
	    TextLayout layout = new TextLayout(lines[i], f, frc);
	    top += layout.getAscent();

	    int drawLeft = left;

	    if (_fontStyle.isCentered())
	    {
		int width = fm.stringWidth(lines[i]);
		drawLeft = left + (bounds.width - width) / 2;
	    }

	    if (_fontStyle.isOutlined())
	    {
		AffineTransform transform = AffineTransform
		        .getTranslateInstance(drawLeft, top);
		Shape shape = layout.getOutline(transform);

		g.setColor(_fontStyle.getOutlineColor());
		g.setStroke(new BasicStroke(2.0f));
		g.draw(shape);
		g.setColor(_fontStyle.getColor());
		g.fill(shape);
	    }
	    else
	    {
		layout.draw(g, drawLeft, top);
	    }

	    top += layout.getDescent();
	    top += layout.getLeading();
	}

	g.dispose();
    }

    public void setFontStyle(FontStyle fontStyle)
    {
	_fontStyle = fontStyle;
    }

    /**
     * Calculate the maximum point size that text can be in order to fit on the
     * screen.
     * 
     * @param f
     *            The font to use
     * @param lines
     *            The lines of text to display on screen
     * @return
     */
    public float getSize(Graphics2D g2d, Rectangle bounds, String[] lines)
    {
	Font f = getFont();

	// TODO: This call always takes about 1 second the first time it is run.
	// Run this on intialisation instead.
	FontMetrics fm = g2d.getFontMetrics(f);

	final int fontSize = f.getSize();

	int maxWidth = 0;
	for (String line : lines)
	{
	    maxWidth = Math.max(maxWidth, fm.stringWidth(line));
	}

	int maxHeight = lines.length * fm.getHeight();

	float scaleFactorW = (float) bounds.getWidth() / (float) maxWidth;
	float scaleFactorH = (float) bounds.getHeight() / (float) maxHeight;

	float scaleFactor = Math.min(scaleFactorW, scaleFactorH);

	if (_useDefaultRowHeight)
	{
	    float defaultRowHeight = (float) bounds.getHeight() / 10.f;
	    float defaultScaleFactor = defaultRowHeight / fm.getHeight();
	    
	    return Math.min(fontSize * scaleFactor, fontSize
		    * defaultScaleFactor);
	}

	return fontSize * scaleFactor;
    }

    protected Font getFont()
    {
	return new Font("Helvetica", Font.PLAIN, 36);
    }

}
