/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.controller;

import javax.inject.Named;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import uk.me.phillsacre.lyricdisplay.main.events.GoLiveEvent;
import uk.me.phillsacre.lyricdisplay.presenter.api.Presentation;

/**
 * 
 * @author Phill
 * @since 23 Nov 2010
 */
@Named("livePanelController")
public class LivePanelController
{
    private Presentation _presentation;

    public LivePanelController()
    {
	EventBus.subscribeStrongly(GoLiveEvent.class,
	        new EventSubscriber<GoLiveEvent>() {
		    @Override
		    public void onEvent(GoLiveEvent event)
		    {

		    }
	        });
    }

    public void setPresentation(Presentation presentation)
    {
	_presentation = presentation;
    }
}
