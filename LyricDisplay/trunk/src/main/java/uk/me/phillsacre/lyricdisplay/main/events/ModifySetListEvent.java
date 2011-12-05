/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.events;

import uk.me.phillsacre.lyricdisplay.main.entities.Song;

/**
 * 
 * @author Phill
 * @since 1 Dec 2011
 */
public class ModifySetListEvent
{
    public enum Type
    {
	ADD, REMOVE
    }

    private Type _type;
    private Song _song;

    public ModifySetListEvent(Type type, Song song)
    {
	_type = type;
	_song = song;
    }

    public Type getType()
    {
	return _type;
    }

    public Song getSong()
    {
	return _song;
    }
}
