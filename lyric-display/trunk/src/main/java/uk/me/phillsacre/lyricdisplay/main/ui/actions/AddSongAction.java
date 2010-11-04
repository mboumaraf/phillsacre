/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import uk.me.phillsacre.lyricdisplay.main.entities.Song;
import uk.me.phillsacre.lyricdisplay.main.ui.frame.EditSongDialog;

/**
 * 
 * @author Phill
 * @since 31 Oct 2010
 */
public class AddSongAction extends AbstractAction
{
    public AddSongAction()
    {
	super("Add");
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
	EditSongDialog frame = new EditSongDialog(new Song());
	frame.setVisible(true);
    }

}
