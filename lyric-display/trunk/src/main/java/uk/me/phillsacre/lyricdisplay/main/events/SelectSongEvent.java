/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.events;

import uk.me.phillsacre.lyricdisplay.main.entities.Song;

/**
 * 
 * @author Phill
 * @since 5 Nov 2010
 */
public class SelectSongEvent
{
    private final Song _song;

    public SelectSongEvent(Song song)
    {
	_song = song;
    }

    public Song getSong()
    {
	return _song;
    }
}
