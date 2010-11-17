/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.ui.models;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.swing.AbstractListModel;

import uk.me.phillsacre.lyricdisplay.main.entities.Song;
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

        SongSlide first = new SongSlide( _song );

        _verses = new ArrayList<Slide>();
        _verses.add( first );

        for ( int i = 1; i < first.getPageCount(); i++ )
        {
            _verses.add( new SongSlide( _song ) );
        }

        fireContentsChanged( this, 0, _verses.size() );
    }
}
