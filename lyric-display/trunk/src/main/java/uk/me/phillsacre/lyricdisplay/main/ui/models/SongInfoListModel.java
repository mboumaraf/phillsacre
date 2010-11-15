/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.ui.models;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.swing.AbstractListModel;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import uk.me.phillsacre.lyricdisplay.main.entities.Song;
import uk.me.phillsacre.lyricdisplay.main.events.SelectSongEvent;
import uk.me.phillsacre.lyricdisplay.presenter.ui.slide.Slide;
import uk.me.phillsacre.lyricdisplay.presenter.ui.slide.SongSlide;


/**
 * 
 * @author Phill
 * @since 6 Nov 2010
 */
@Named( "songInfoListModel" )
public class SongInfoListModel extends AbstractListModel
{
    private static final long serialVersionUID = -7475366613550379719L;

    private Song              _song;
    private List<Slide>       _verses;


    @Override
    public Object getElementAt( int index )
    {
        return _verses.get( index );
    }

    @Override
    public int getSize()
    {
        return _verses == null ? 0 : _verses.size();
    }

    public void updateSong( Song song )
    {
        _song = song;

        SongSlide first = new SongSlide( _song, 0 );

        _verses = new ArrayList<Slide>();
        _verses.add( first );

        for ( int i = 1; i < first.getVerses().size(); i++ )
        {
            _verses.add( new SongSlide( _song, i ) );
        }

        fireContentsChanged( this, 0, _verses.size() );
    }
}
