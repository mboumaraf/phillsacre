/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.components;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import org.bushe.swing.event.EventBus;

import uk.me.phillsacre.lyricdisplay.App;
import uk.me.phillsacre.lyricdisplay.main.entities.Song;
import uk.me.phillsacre.lyricdisplay.main.events.ChangeSetListSelectionEvent;
import uk.me.phillsacre.lyricdisplay.main.events.utils.Target;
import uk.me.phillsacre.lyricdisplay.ui.models.SetListModel;
import uk.me.phillsacre.lyricdisplay.ui.models.entities.SetListItem;

/**
 * 
 * @author Phill
 * @since 1 Dec 2011
 */
public class SetList extends JList<SetListItem>
{
    private static final long serialVersionUID = -5668154135438746367L;

    public SetList()
    {
	setModel(App.getApplicationContext().getBean(SetListModel.class));
	setCellRenderer(new SetListRenderer());

	addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e)
	    {
		SetListItem item = (SetListItem) getSelectedValue();
		if (null != item)
		{
		    EventBus.publish(new ChangeSetListSelectionEvent(
			    Target.PREVIEW, item));
		}
	    }
	});
    }

    private class SetListRenderer extends DefaultListCellRenderer
    {
	private static final long serialVersionUID = 931386781311593431L;

	@Override
	public Component getListCellRendererComponent(JList<?> list,
	        Object value, int index, boolean isSelected,
	        boolean cellHasFocus)
	{
	    SetListItem item = (SetListItem) value;
	    Song song = item.getSong();

	    JLabel label = (JLabel) super.getListCellRendererComponent(list,
		    song.getTitle(), index, isSelected, cellHasFocus);

	    return label;
	}

    }
}
