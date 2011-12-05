/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.components;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import uk.me.phillsacre.lyricdisplay.App;
import uk.me.phillsacre.lyricdisplay.main.entities.Song;
import uk.me.phillsacre.lyricdisplay.main.events.SongSelectedEvent;
import uk.me.phillsacre.lyricdisplay.main.utils.Utils;
import uk.me.phillsacre.lyricdisplay.ui.frames.EditSongDialog;

/**
 * 
 * @author Phill
 * @since 1 Dec 2011
 */
public class SetListPanel extends JPanel
{
    private static final long  serialVersionUID = -8512902569247645211L;

    private AvailableSongsList _availableSongsList;

    public SetListPanel()
    {
	setLayout(new GridLayout(2, 1));

	SetList currentSongsList = new SetList();
	JScrollPane sp1 = new JScrollPane(currentSongsList);
	sp1.setBorder(BorderFactory.createTitledBorder("Current Songs"));
	add(sp1);

	_availableSongsList = new AvailableSongsList();

	JPanel container = new JPanel();
	container.setLayout(new BorderLayout());
	JToolBar toolBar = new JToolBar();
	toolBar.setFloatable(false);
	toolBar.add(new AddSongAction());
	toolBar.add(new EditSongAction());

	container.add(toolBar, BorderLayout.NORTH);

	JScrollPane sp2 = new JScrollPane(_availableSongsList);
	sp2.setBorder(BorderFactory.createTitledBorder("Available Songs"));
	container.add(sp2, BorderLayout.CENTER);

	add(container);
    }

    private class EditSongAction extends AbstractAction implements
	    EventSubscriber<SongSelectedEvent>
    {
	private static final long serialVersionUID = 7557391969106704814L;

	private Song              _song;

	public EditSongAction()
	{
	    super("Edit Song");

	    setEnabled(false);

	    putValue(SHORT_DESCRIPTION, "Edit the currently selected song");
	    putValue(SMALL_ICON,
		    new ImageIcon(Utils.getImageURL("images/edit.png")));

	    EventBus.subscribe(SongSelectedEvent.class, this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
	    EditSongDialog dialog = new EditSongDialog(App.getMainFrame(),
		    _song);
	    dialog.setVisible(true);
	}

	@Override
	public void onEvent(SongSelectedEvent event)
	{
	    setEnabled((_song = event.getSong()) != null);
	}
    }

    private class AddSongAction extends AbstractAction implements
	    EventSubscriber<SongSelectedEvent>
    {
	private static final long serialVersionUID = -4917399532600884261L;

	public AddSongAction()
	{
	    super("Add Song");

	    setEnabled(false);

	    putValue(SHORT_DESCRIPTION,
		    "Add the currently selected song to the set list");
	    putValue(SMALL_ICON,
		    new ImageIcon(Utils.getImageURL("images/add.png")));

	    EventBus.subscribe(SongSelectedEvent.class, this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
	    Song song = (Song) _availableSongsList.getSelectedValue();
	    if (null != song)
	    {
		_availableSongsList.addSong(song);
	    }
	}

	@Override
	public void onEvent(SongSelectedEvent event)
	{
	    setEnabled(event.getSong() != null);
	}
    }
}
