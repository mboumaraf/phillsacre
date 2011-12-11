/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.presentation.display;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import uk.me.phillsacre.lyricdisplay.App;
import uk.me.phillsacre.lyricdisplay.main.entities.Song;
import uk.me.phillsacre.lyricdisplay.main.events.DefaultBackgroundSelectedEvent;
import uk.me.phillsacre.lyricdisplay.main.events.SetListItemUpdatedEvent;
import uk.me.phillsacre.lyricdisplay.main.utils.BackgroundUtils;
import uk.me.phillsacre.lyricdisplay.ui.models.entities.SetListItem;
import uk.me.phillsacre.lyricdisplay.ui.presentation.DisplayPanel;
import uk.me.phillsacre.lyricdisplay.ui.presentation.backgrounds.Background;
import uk.me.phillsacre.lyricdisplay.ui.presentation.text.DefaultTextRenderer;
import uk.me.phillsacre.lyricdisplay.ui.presentation.text.TextRenderer;
import uk.me.phillsacre.lyricdisplay.ui.presentation.text.TextRenderer.FontStyle;

/**
 * 
 * @author Phill
 * @since 4 Dec 2011
 */
public class SongPanel implements DisplayPanel
{
    private BackgroundUtils       _backgroundUtils;

    private SetListItem           _item;
    private Background            _background;
    private TextRenderer          _textRenderer;
    private DefaultTextRenderer   _footerRenderer;
    private int                   _availableWidth;
    private int                   _availableHeight;
    private String                _text;
    private List<RepaintListener> _repaintListeners;

    public SongPanel(SetListItem item, String text)
    {
	_backgroundUtils = App.getApplicationContext().getBean(
	        BackgroundUtils.class);
	_repaintListeners = new ArrayList<RepaintListener>();

	_item = item;
	_background = _item.getBackground() == null ? _backgroundUtils
	        .getDefaultBackground() : item.getBackground();

	_textRenderer = App.getApplicationContext().getBean(
	        DefaultTextRenderer.class);
	_footerRenderer = new DefaultTextRenderer();
	_footerRenderer.setUseDefaultRowHeight(false);
	_footerRenderer.setFontStyle(new FontStyle(Color.white, true, false));

	text = text.replaceAll("\\b(Verse|Chorus|Bridge) ?[\\d]*\\b", "");

	_text = text;

	EventBus.subscribe(SetListItemUpdatedEvent.class,
	        new EventSubscriber<SetListItemUpdatedEvent>() {
		    @Override
		    public void onEvent(SetListItemUpdatedEvent event)
		    {
		        if (_item.equals(event.getItem()))
		        {
			    updateItem(event.getItem());
		        }
		    }
	        });

	if (null == _item.getBackground())
	{
	    EventBus.subscribe(DefaultBackgroundSelectedEvent.class,
		    new EventSubscriber<DefaultBackgroundSelectedEvent>() {
		        @Override
		        public void onEvent(DefaultBackgroundSelectedEvent event)
		        {
			    _background = event.getBackground();

			    doRepaint();
		        }
		    });
	}
    }

    @Override
    public void render(Graphics2D g2d, int width, int height)
    {
	int marginW = (int) Math.round((float) width * 0.1);
	int marginH = (int) Math.round((float) height * 0.1);

	_availableWidth = width - (marginW * 2);
	_availableHeight = height - (marginH * 2);

	drawBackground(g2d, width, height);

	final Rectangle slideBounds = getSlideBounds(width, height);
	final Rectangle footerBounds = getFooterBounds(width, height, marginH);

	String[] text = _text.split("\n");
	float size = _textRenderer.getSize(g2d, slideBounds, text);
	_textRenderer.renderText(g2d, text, size, slideBounds);

	String footer = getFooterString(_item.getSong());
	if (footer.length() > 0)
	{
	    float footerSize = _footerRenderer.getSize(g2d, footerBounds,
		    new String[] { footer });

	    _footerRenderer.renderText(g2d, new String[] { footer },
		    footerSize, footerBounds);
	}
    }

    private String getFooterString(Song song)
    {
	StringBuilder footer = new StringBuilder();

	if (StringUtils.isNotBlank(song.getAuthor()))
	{
	    footer.append(song.getAuthor());
	}
	if (null != song.getYear() && StringUtils.isBlank(song.getCopyright()))
	{
	    if (footer.length() > 0)
	    {
		footer.append("; ");
	    }

	    footer.append(song.getYear());
	}
	if (StringUtils.isNotBlank(song.getCopyright()))
	{
	    if (footer.length() > 0)
	    {
		footer.append("; ");
	    }

	    footer.append("Copyright © ").append(song.getCopyright());
	}

	return footer.toString();
    }

    private void updateItem(SetListItem item)
    {
	_item = item;
	_background = item.getBackground() == null ? _backgroundUtils
	        .getDefaultBackground() : item.getBackground();

	doRepaint();
    }

    private void doRepaint()
    {
	for (RepaintListener listener : _repaintListeners)
	{
	    listener.repaint();
	}
    }

    private Rectangle getFooterBounds(int width, int height, int marginH)
    {
	int left = (width - _availableWidth) / 2;

	Rectangle bounds = new Rectangle(left, _availableHeight
	        + ((height - _availableHeight) / 2), _availableWidth, marginH / 2);

	return bounds;
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

    @Override
    public void addRepaintListener(RepaintListener repaintListener)
    {
	_repaintListeners.add(repaintListener);
    }
}
