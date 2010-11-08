/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.controller;

import javax.inject.Named;

import org.bushe.swing.event.EventBus;

import uk.me.phillsacre.lyricdisplay.presenter.events.PreviewSlideEvent;
import uk.me.phillsacre.lyricdisplay.presenter.ui.slide.TextSlide;

/**
 * 
 * @author Phill
 * @since 8 Nov 2010
 */
@Named("versesListController")
public class VersesListController
{
    public void handleSelection(Object selectedValue)
    {
	TextSlide slide = new TextSlide((String) selectedValue);
	EventBus.publish(new PreviewSlideEvent(slide));
    }
}
