/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.controller;

import javax.inject.Inject;
import javax.inject.Named;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import uk.me.phillsacre.lyricdisplay.main.entities.Song;
import uk.me.phillsacre.lyricdisplay.main.events.SaveSongEvent;
import uk.me.phillsacre.lyricdisplay.main.events.SelectSongEvent;
import uk.me.phillsacre.lyricdisplay.main.ui.models.SongInfoListModel;
import uk.me.phillsacre.lyricdisplay.presenter.events.PreviewSlideEvent;
import uk.me.phillsacre.lyricdisplay.presenter.ui.slide.Slide;


/**
 * 
 * @author Phill
 * @since 8 Nov 2010
 */
@Named( "versesListController" )
public class VersesListController
{
    private Song              _song;
    private SongInfoListModel _listModel;


    @Inject
    public void setListModel( SongInfoListModel listModel )
    {
        _listModel = listModel;
    }

    public SongInfoListModel getListModel()
    {
        return _listModel;
    }

    public VersesListController()
    {
        EventBus.subscribeStrongly( SelectSongEvent.class, new EventSubscriber<SelectSongEvent>()
        {
            @Override
            public void onEvent( SelectSongEvent event )
            {
                _song = event.getSong();

                _listModel.updateSong( _song );

                if (_listModel.getSize() > 0)
                {
                    EventBus.publish( new PreviewSlideEvent( (Slide) _listModel.getElementAt( 0 ) ) );
                }
            }
        } );

        EventBus.subscribeStrongly( SaveSongEvent.class, new EventSubscriber<SaveSongEvent>()
        {
            @Override
            public void onEvent( SaveSongEvent event )
            {
                if (event.getSong() == _song)
                {
                    _listModel.updateSong( _song );

                    if (_listModel.getSize() > 0)
                    {
                        EventBus.publish( new PreviewSlideEvent( (Slide) _listModel.getElementAt( 0 ) ) );
                    }
                }
            }
        } );
    }

    public void handleSelection( int index )
    {
        EventBus.publish( new PreviewSlideEvent( (Slide) _listModel.getElementAt( index ) ) );
    }
}
