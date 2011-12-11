/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.presentation.display;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import uk.me.phillsacre.lyricdisplay.App;
import uk.me.phillsacre.lyricdisplay.main.events.DefaultBackgroundSelectedEvent;
import uk.me.phillsacre.lyricdisplay.main.events.SetListItemUpdatedEvent;
import uk.me.phillsacre.lyricdisplay.main.utils.BackgroundUtils;
import uk.me.phillsacre.lyricdisplay.ui.models.entities.SetListItem;
import uk.me.phillsacre.lyricdisplay.ui.presentation.DisplayPanel;
import uk.me.phillsacre.lyricdisplay.ui.presentation.backgrounds.Background;
import uk.me.phillsacre.lyricdisplay.ui.presentation.text.DefaultTextRenderer;
import uk.me.phillsacre.lyricdisplay.ui.presentation.text.TextRenderer;

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

	String[] text = _text.split("\n");
	float size = _textRenderer.getSize(g2d, getSlideBounds(width, height),
	        text);
	_textRenderer
	        .renderText(g2d, text, size, getSlideBounds(width, height));
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
