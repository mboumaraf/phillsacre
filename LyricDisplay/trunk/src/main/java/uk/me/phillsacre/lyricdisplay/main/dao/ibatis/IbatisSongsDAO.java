/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.dao.ibatis;

import java.util.List;

import javax.inject.Named;

import uk.me.phillsacre.lyricdisplay.main.dao.SongsDAO;
import uk.me.phillsacre.lyricdisplay.main.entities.Song;

/**
 * 
 * @author Phill
 * @since 1 Dec 2011
 */
@Named("songsDAO")
public class IbatisSongsDAO extends GenericIbatisDAO implements SongsDAO
{
    public List<Song> getSongs()
    {
	return getSqlMapClientTemplate().queryForList("Song.selectAll");
    }

    public void saveOrUpdate(Song song)
    {
	if (null == song.getId())
	{
	    Integer id = (Integer) getSqlMapClientTemplate().insert(
		    "Song.insert", song);
	    song.setId(id);
	}
	else
	{
	    getSqlMapClientTemplate().update("Song.update", song);
	}
    }
}
