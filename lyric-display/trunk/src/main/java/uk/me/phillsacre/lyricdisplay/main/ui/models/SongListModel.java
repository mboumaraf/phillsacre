/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.ui.models;

import java.util.List;

import javax.swing.AbstractListModel;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import uk.me.phillsacre.lyricdisplay.LyricDisplay;
import uk.me.phillsacre.lyricdisplay.main.dao.SongsDAO;
import uk.me.phillsacre.lyricdisplay.main.entities.Song;
import uk.me.phillsacre.lyricdisplay.main.events.SaveSongEvent;


/**
 * 
 * @author Phill
 * @since 31 Oct 2010
 */
public class SongListModel extends AbstractListModel
{
    private static final long serialVersionUID = 4724812052110483910L;

    private SongsDAO          _songsDAO;
    private List<Song>        _songs;


    public SongListModel()
    {
        _songsDAO = LyricDisplay.getApplicationContext().getBean( SongsDAO.class );

        _songs = _songsDAO.getSongs();

        EventBus.subscribeStrongly( SaveSongEvent.class, new EventSubscriber<SaveSongEvent>()
        {

            @Override
            public void onEvent( SaveSongEvent event )
            {
                refreshSongs();
            }
        } );
    }

    @Override
    public Object getElementAt( int index )
    {
        return _songs.get( index );
    }

    @Override
    public int getSize()
    {
        return _songs.size();
    }

    private void refreshSongs()
    {
        _songs = _songsDAO.getSongs();

        fireContentsChanged( this, 0, getSize() );
    }
}
