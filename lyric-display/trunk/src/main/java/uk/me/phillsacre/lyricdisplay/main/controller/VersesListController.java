/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.controller;

import javax.inject.Named;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import uk.me.phillsacre.lyricdisplay.main.entities.Song;
import uk.me.phillsacre.lyricdisplay.main.events.SelectSongEvent;
import uk.me.phillsacre.lyricdisplay.presenter.events.PreviewSlideEvent;
import uk.me.phillsacre.lyricdisplay.presenter.ui.slide.SongSlide;
import uk.me.phillsacre.lyricdisplay.presenter.ui.slide.TextSlide;


/**
 * 
 * @author Phill
 * @since 8 Nov 2010
 */
@Named( "versesListController" )
public class VersesListController
{
    private Song _song;


    public VersesListController()
    {
        EventBus.subscribeStrongly( SelectSongEvent.class, new EventSubscriber<SelectSongEvent>()
        {
            @Override
            public void onEvent( SelectSongEvent event )
            {
                _song = event.getSong();
            }
        } );
    }

    public void handleSelection( int index )
    {
        TextSlide slide = new SongSlide( _song, index );
        EventBus.publish( new PreviewSlideEvent( slide ) );
    }
}
