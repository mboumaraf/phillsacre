/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.controllers;

import org.bushe.swing.event.EventBus;

import uk.me.phillsacre.lyricdisplay.App;
import uk.me.phillsacre.lyricdisplay.main.dao.SongsDAO;
import uk.me.phillsacre.lyricdisplay.main.entities.Song;
import uk.me.phillsacre.lyricdisplay.main.events.SongUpdatedEvent;

import com.jgoodies.binding.PresentationModel;

/**
 * 
 * @author Phill
 * @since 3 Dec 2011
 */
public class EditSongController extends PresentationModel<Song>
{
    private static final long serialVersionUID = 3652364232277425522L;

    public interface EditSongUI
    {
	void close();
    }

    private EditSongUI _ui;

    public EditSongController(EditSongUI ui, Song song)
    {
	super(new Song(song));

	_ui = ui;
    }

    public void saveSong()
    {
	Song song = getBean();

	boolean added = (song.getId() == null);

	SongsDAO songsDAO = App.getApplicationContext().getBean(SongsDAO.class);
	songsDAO.saveOrUpdate(song);

	EventBus.publish(new SongUpdatedEvent(song, added));

	_ui.close();
    }

    public void cancel()
    {
	_ui.close();
    }
}
