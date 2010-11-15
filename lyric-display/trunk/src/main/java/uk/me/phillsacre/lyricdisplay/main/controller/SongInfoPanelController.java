/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.controller;

import javax.inject.Named;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import uk.me.phillsacre.lyricdisplay.presenter.api.Presentation;
import uk.me.phillsacre.lyricdisplay.presenter.events.PreviewSlideEvent;


/**
 * 
 * @author Phill
 * @since 8 Nov 2010
 */
@Named( "songInfoPanelController" )
public class SongInfoPanelController
{
    private Presentation _presentation;


    public SongInfoPanelController()
    {
        EventBus.subscribeStrongly( PreviewSlideEvent.class, new EventSubscriber<PreviewSlideEvent>()
        {
            @Override
            public void onEvent( PreviewSlideEvent event )
            {
                _presentation.setSlide( event.getSlide(), event.getPageNo() );
            }
        } );
    }

    public void setPresentation( Presentation presentation )
    {
        _presentation = presentation;
    }
}
