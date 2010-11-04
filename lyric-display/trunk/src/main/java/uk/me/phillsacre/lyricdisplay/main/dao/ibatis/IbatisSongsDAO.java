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
 * @since 31 Oct 2010
 */
@Named("songsDAO")
public class IbatisSongsDAO extends GenericIbatisDAO implements SongsDAO
{

    @Override
    public List<Song> getSongs()
    {
	return getSqlMapClientTemplate().queryForList("Song.selectAll");
    }

    @Override
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
