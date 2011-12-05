/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.presentation.display;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import uk.me.phillsacre.lyricdisplay.App;
import uk.me.phillsacre.lyricdisplay.ui.models.entities.SetListItem;
import uk.me.phillsacre.lyricdisplay.ui.presentation.DisplayPanel;
import uk.me.phillsacre.lyricdisplay.ui.presentation.backgrounds.Background;
import uk.me.phillsacre.lyricdisplay.ui.presentation.backgrounds.GradientBackground;
import uk.me.phillsacre.lyricdisplay.ui.presentation.text.DefaultTextRenderer;
import uk.me.phillsacre.lyricdisplay.ui.presentation.text.TextRenderer;

/**
 * 
 * @author Phill
 * @since 4 Dec 2011
 */
public class SongPanel implements DisplayPanel
{
    private SetListItem  _item;
    private Background   _background;
    private TextRenderer _textRenderer;
    private int          _availableWidth;
    private int          _availableHeight;
    private String       _text;

    public SongPanel(SetListItem item, String text)
    {
	_item = item;
	_background = _item.getBackground() == null ? new GradientBackground(
	        Color.blue, Color.black) : item.getBackground();

	_textRenderer = App.getApplicationContext().getBean(
	        DefaultTextRenderer.class);

	text = text.replaceAll("\\b(Verse|Chorus|Bridge) ?[\\d]*\\b", "");

	_text = text;
    }

    @Override
    public void render(Graphics2D g2d, int width, int height)
    {
	int marginW = (int) Math.round((float) width * 0.1);
	int marginH = (int) Math.round((float) height * 0.1);

	_availableWidth = width - (marginW * 2);
	_availableHeight = height - (marginH * 2);

	drawBackground(g2d, width, height);

	String[] text = _text.split("\n");
	float size = _textRenderer.getSize(g2d, getSlideBounds(width, height),
	        text);
	_textRenderer
	        .renderText(g2d, text, size, getSlideBounds(width, height));
    }

    private Rectangle getSlideBounds(int width, int height)
    {
	int left = (width - _availableWidth) / 2;
	int top = (height - _availableHeight) / 2;

	Rectangle bounds = new Rectangle(left, top, _availableWidth,
	        _availableHeight);

	return bounds;
    }

    private void drawBackground(Graphics2D g2d, int width, int height)
    {
	_background.renderBackground(g2d, width, height);
    }
}
