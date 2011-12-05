/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.events;

import uk.me.phillsacre.lyricdisplay.main.entities.Song;

/**
 * 
 * @author Phill
 * @since 3 Dec 2011
 */
public class SongSelectedEvent
{
    private Song _song;
    
    public SongSelectedEvent(Song song)
    {
	_song = song;
    }
    
    public Song getSong()
    {
	return _song;
    }
}
