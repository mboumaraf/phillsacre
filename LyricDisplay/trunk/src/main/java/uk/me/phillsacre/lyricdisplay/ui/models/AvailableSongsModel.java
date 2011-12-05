/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.AbstractListModel;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import uk.me.phillsacre.lyricdisplay.main.dao.SongsDAO;
import uk.me.phillsacre.lyricdisplay.main.entities.Song;
import uk.me.phillsacre.lyricdisplay.main.events.SongUpdatedEvent;

/**
 * 
 * @author Phill
 * @since 1 Dec 2011
 */
@Named("availableSongsModel")
public class AvailableSongsModel extends AbstractListModel<Song> implements
        EventSubscriber<SongUpdatedEvent>
{
    private static final long serialVersionUID = 1542241519062982333L;

    private List<Song>        _songs;
    private SongsDAO          _songsDAO;

    public AvailableSongsModel()
    {
	_songs = new ArrayList<Song>();
    }

    @PostConstruct
    public void init()
    {
	refresh();
    }

    public void search(String text)
    {
	if (StringUtils.isBlank(text))
	{
	    refresh();
	}
	else
	{
	    // TODO Implement search
	}
    }

    public int getSize()
    {
	return _songs.size();
    }

    public Song getElementAt(int index)
    {
	return _songs.get(index);
    }

    @Inject
    public void setSongsDAO(SongsDAO songsDAO)
    {
	_songsDAO = songsDAO;
    }

    @Override
    public void onEvent(SongUpdatedEvent event)
    {
	Song song = event.getSong();

	if (event.isAdded())
	{
	    _songs.add(song);
	    sortList();

	    int idx = _songs.indexOf(song);

	    fireIntervalAdded(this, idx, idx + 1);
	}
	else
	{
	    int idx = _songs.indexOf(song);

	    _songs.set(idx, song);

	    fireContentsChanged(this, idx, idx + 1);
	}
    }

    private void refresh()
    {
	List<Song> songs = _songsDAO.getSongs();

	_songs.addAll(songs);
	sortList();

	fireContentsChanged(this, 0, _songs.size());

	EventBus.subscribe(SongUpdatedEvent.class, this);
    }

    private void sortList()
    {
	Collections.sort(_songs, new Comparator<Song>() {
	    @Override
	    public int compare(Song o1, Song o2)
	    {
		return o1.getTitle().compareTo(o2.getTitle());
	    }
	});
    }
}
