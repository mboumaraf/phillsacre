/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.components;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import uk.me.phillsacre.lyricdisplay.main.entities.Song;

/**
 * 
 * @author Phill
 * @since 1 Dec 2011
 */
public class SongsCellRenderer extends DefaultListCellRenderer
{
    private static final long serialVersionUID = -6867216107217499592L;

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value,
	    int index, boolean isSelected, boolean cellHasFocus)
    {
	Song song = (Song) value;

	JLabel label = (JLabel) super.getListCellRendererComponent(list,
	        song.getTitle(), index, isSelected, cellHasFocus);

	return label;
    }

}
