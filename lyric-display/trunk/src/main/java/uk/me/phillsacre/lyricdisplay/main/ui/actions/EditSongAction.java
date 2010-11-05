/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import uk.me.phillsacre.lyricdisplay.main.entities.Song;
import uk.me.phillsacre.lyricdisplay.main.events.SelectSongEvent;
import uk.me.phillsacre.lyricdisplay.main.ui.frame.EditSongDialog;

/**
 * 
 * @author Phill
 * @since 31 Oct 2010
 */
public class EditSongAction extends AbstractAction implements
        EventSubscriber<SelectSongEvent>
{
    private Song _song;

    public EditSongAction()
    {
	super("Edit");

	setEnabled(false);

	EventBus.subscribe(SelectSongEvent.class, this);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
	EditSongDialog dialog = new EditSongDialog(_song);
	dialog.setVisible(true);
    }

    @Override
    public void onEvent(SelectSongEvent event)
    {
	_song = event.getSong();
	setEnabled(_song != null);
    }

}
