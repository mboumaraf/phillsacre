/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.ui.components;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import uk.me.phillsacre.lyricdisplay.main.entities.Song;
import uk.me.phillsacre.lyricdisplay.main.events.SelectSongEvent;
import uk.me.phillsacre.lyricdisplay.main.ui.frame.EditSongDialog;
import uk.me.phillsacre.lyricdisplay.main.ui.models.SongListModel;

/**
 * 
 * @author Phill
 * @since 31 Oct 2010
 */
public class SongList extends JList
{
    public SongList()
    {
	setModel(new SongListModel());
	setCellRenderer(new SongListCellRenderer());
	setFont(getFont().deriveFont(Font.PLAIN));
	addListSelectionListener(new ListSelectionListener() {
	    @Override
	    public void valueChanged(ListSelectionEvent e)
	    {
		Song song = (Song) getSelectedValue();

		EventBus.publish(new SelectSongEvent(song));
	    }
	});

	addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e)
	    {
		if (e.getClickCount() == 2)
		{
		    Song song = (Song) getSelectedValue();
		    if (null != song)
		    {
			EditSongDialog dialog = new EditSongDialog(song);
			dialog.setVisible(true);
		    }
		}
	    }
	});
    }

    private static class SongListCellRenderer extends DefaultListCellRenderer
    {
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
	        int index, boolean isSelected, boolean cellHasFocus)
	{
	    JLabel label = (JLabel) super.getListCellRendererComponent(list,
		    value, index, isSelected, cellHasFocus);

	    Song song = (Song) value;

	    StringBuilder text = new StringBuilder();
	    text.append("<html>");
	    text.append("<b>" + song.getTitle() + "</b>");

	    String summary = getSummary(song);

	    if (null != summary)
	    {
		text.append("<br/><font color='#777'><small><i>" + summary
		        + "</i></small></font>");
	    }

	    label.setToolTipText(song.getTitle());

	    text.append("</html>");

	    label.setText(text.toString());
	    return label;
	}

	private String getSummary(Song song)
	{
	    String text = song.getText();

	    if (StringUtils.isBlank(text))
	    {
		return null;
	    }

	    if (text.indexOf('\n') > -1)
	    {
		String firstLine = text.substring(0, text.indexOf('\n'));
		return firstLine;
	    }

	    return text;
	}

    }
}
