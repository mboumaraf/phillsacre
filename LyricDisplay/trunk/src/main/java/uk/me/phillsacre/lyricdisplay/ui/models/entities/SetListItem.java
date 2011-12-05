/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.models.entities;

import uk.me.phillsacre.lyricdisplay.main.entities.Song;
import uk.me.phillsacre.lyricdisplay.ui.presentation.backgrounds.Background;

/**
 * A Set List can theoretically contain more than just songs - so abstract
 * 
 * @author Phill
 * @since 5 Dec 2011
 */
public class SetListItem
{
    private Song       _song;
    private Background _background;

    public SetListItem(Song song)
    {
	_song = song;
    }

    public Song getSong()
    {
	return _song;
    }

    public void setBackground(Background background)
    {
	_background = background;
    }

    public Background getBackground()
    {
	return _background;
    }
}
