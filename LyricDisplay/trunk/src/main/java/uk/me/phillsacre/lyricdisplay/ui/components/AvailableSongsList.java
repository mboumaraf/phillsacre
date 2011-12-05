/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.components;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.me.phillsacre.lyricdisplay.App;
import uk.me.phillsacre.lyricdisplay.main.entities.Song;
import uk.me.phillsacre.lyricdisplay.main.events.ModifySetListEvent;
import uk.me.phillsacre.lyricdisplay.main.events.ModifySetListEvent.Type;
import uk.me.phillsacre.lyricdisplay.main.events.SongSelectedEvent;
import uk.me.phillsacre.lyricdisplay.main.events.SongUpdatedEvent;
import uk.me.phillsacre.lyricdisplay.ui.models.AvailableSongsModel;

/**
 * 
 * @author Phill
 * @since 1 Dec 2011
 */
public class AvailableSongsList extends JList<Song> implements
        EventSubscriber<SongUpdatedEvent>
{
    private static final long   serialVersionUID = 755064509874109331L;

    private static final Logger LOG              = LoggerFactory
	                                                 .getLogger(AvailableSongsList.class);

    public AvailableSongsList()
    {
	ListModel<Song> availableSongsModel = App.getApplicationContext().getBean(
	        AvailableSongsModel.class);

	setModel(availableSongsModel);
	setCellRenderer(new SongsCellRenderer());

	addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e)
	    {
		if (e.getClickCount() == 2)
		{
		    Song song = (Song) getSelectedValue();

		    addSong(song);
		}
	    }
	});

	addListSelectionListener(new ListSelectionListener() {
	    @Override
	    public void valueChanged(ListSelectionEvent e)
	    {
		Song song = (Song) getSelectedValue();
		EventBus.publish(new SongSelectedEvent(song));
	    }
	});

	EventBus.subscribe(SongUpdatedEvent.class, this);
    }

    public void addSong(Song song)
    {
	LOG.debug("Publishing event: adding song [{}] to set list",
	        song.getTitle());

	ModifySetListEvent event = new ModifySetListEvent(Type.ADD, song);
	EventBus.publish(event);
    }

    @Override
    public void onEvent(SongUpdatedEvent event)
    {
	Song song = event.getSong();
	if (song.equals(getSelectedValue()))
	{
	    EventBus.publish(new SongSelectedEvent(song));
	}
    }
}
