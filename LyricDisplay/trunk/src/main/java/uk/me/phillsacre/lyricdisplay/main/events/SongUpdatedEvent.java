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
public class SongUpdatedEvent
{
    private Song    _song;
    private boolean _added;

    public SongUpdatedEvent(Song song, boolean added)
    {
	_song = song;
	_added = added;
    }

    public Song getSong()
    {
	return _song;
    }

    public boolean isAdded()
    {
	return _added;
    }
}
