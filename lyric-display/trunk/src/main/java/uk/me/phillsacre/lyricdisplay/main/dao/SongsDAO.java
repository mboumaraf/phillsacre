/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.dao;

import java.util.List;

import uk.me.phillsacre.lyricdisplay.main.entities.Song;

/**
 * 
 * @author Phill
 * @since 31 Oct 2010
 */
public interface SongsDAO
{
    List<Song> getSongs();

    void saveOrUpdate(Song song);
}
