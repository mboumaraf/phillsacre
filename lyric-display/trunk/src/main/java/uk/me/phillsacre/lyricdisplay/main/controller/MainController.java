/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.controller;

import javax.inject.Inject;
import javax.inject.Named;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import uk.me.phillsacre.lyricdisplay.main.dao.SongsDAO;
import uk.me.phillsacre.lyricdisplay.main.entities.Song;
import uk.me.phillsacre.lyricdisplay.main.events.SaveSongEvent;

/**
 * 
 * @author Phill
 * @since 5 Nov 2010
 */
@Named("mainController")
public class MainController
{
    private SongsDAO _songsDAO;

    @Inject
    public void setSongsDAO(SongsDAO songsDAO)
    {
	_songsDAO = songsDAO;
    }

    public MainController()
    {
	EventBus.subscribeStrongly(SaveSongEvent.class,
	        new EventSubscriber<SaveSongEvent>() {
		    @Override
		    public void onEvent(SaveSongEvent event)
		    {
		        saveSong(event.getSong());
		    }
	        });

    }

    public void saveSong(Song song)
    {
	_songsDAO.saveOrUpdate(song);
    }
}
