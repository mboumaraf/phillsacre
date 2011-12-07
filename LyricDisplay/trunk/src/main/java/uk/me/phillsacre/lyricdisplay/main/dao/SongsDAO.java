/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.dao;

import java.util.List;

import uk.me.phillsacre.lyricdisplay.main.entities.Song;

/**
 * 
 * @author Phill
 * @since 1 Dec 2011
 */
public interface SongsDAO
{
    List<Song> getSongs();

    List<Song> searchSongs(String search);

    void saveOrUpdate(Song song);
}
