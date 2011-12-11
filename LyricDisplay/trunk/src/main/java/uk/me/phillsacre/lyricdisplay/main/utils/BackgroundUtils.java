/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.utils;

import java.awt.Color;

import javax.inject.Named;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import uk.me.phillsacre.lyricdisplay.main.events.DefaultBackgroundSelectedEvent;
import uk.me.phillsacre.lyricdisplay.ui.presentation.backgrounds.Background;
import uk.me.phillsacre.lyricdisplay.ui.presentation.backgrounds.GradientBackground;

/**
 * 
 * @author Phill
 * @since 11 Dec 2011
 */
@Named("backgroundUtils")
public class BackgroundUtils implements
        EventSubscriber<DefaultBackgroundSelectedEvent>
{
    private Background _defaultBackground;

    public BackgroundUtils()
    {
	EventBus.subscribe(DefaultBackgroundSelectedEvent.class, this);

	_defaultBackground = new GradientBackground(Color.blue, Color.black);
    }

    @Override
    public void onEvent(DefaultBackgroundSelectedEvent event)
    {
	_defaultBackground = event.getBackground();
    }

    public Background getDefaultBackground()
    {
	return _defaultBackground;
    }

    public void setDefaultBackground(Background defaultBackground)
    {
	EventBus.publish(new DefaultBackgroundSelectedEvent(defaultBackground));
    }
}
