/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.components;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import uk.me.phillsacre.lyricdisplay.App;
import uk.me.phillsacre.lyricdisplay.main.entities.Song;
import uk.me.phillsacre.lyricdisplay.main.events.ChangeSetListSelectionEvent;
import uk.me.phillsacre.lyricdisplay.main.events.SetListControlEvent;
import uk.me.phillsacre.lyricdisplay.main.events.SetListItemUpdatedEvent;
import uk.me.phillsacre.lyricdisplay.main.events.utils.Target;
import uk.me.phillsacre.lyricdisplay.main.utils.Utils;
import uk.me.phillsacre.lyricdisplay.ui.frames.ChooseBackgroundDialog;
import uk.me.phillsacre.lyricdisplay.ui.models.SetListModel;
import uk.me.phillsacre.lyricdisplay.ui.models.entities.SetListItem;
import uk.me.phillsacre.lyricdisplay.ui.presentation.backgrounds.Background;

/**
 * 
 * @author Phill
 * @since 1 Dec 2011
 */
public class SetList extends JList<SetListItem>
{
    private static final long serialVersionUID = -5668154135438746367L;

    private JPopupMenu        _menu;
    private SetListModel      _model;

    public SetList()
    {
	setModel(_model = App.getApplicationContext().getBean(
	        SetListModel.class));
	setCellRenderer(new SetListRenderer());
	setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	_menu = createPopupMenu();

	EventBus.subscribeStrongly(SetListControlEvent.class,
	        new EventSubscriber<SetListControlEvent>() {
		    @Override
		    public void onEvent(SetListControlEvent event)
		    {
		        switch (event.getType()) {
			    case FORWARD:
				nextSetListItem();
				break;

			    case BACKWARD:
				previousSetListItem();
				break;
			}
		    }
	        });

	EventBus.subscribeStrongly(ChangeSetListSelectionEvent.class,
	        new EventSubscriber<ChangeSetListSelectionEvent>() {
		    @Override
		    public void onEvent(ChangeSetListSelectionEvent event)
		    {
		        if (event.getTarget() == Target.LIVE)
		        {
			    // When an item is moved to live, select the next
			    // item in the list
			    int index = getSelectedIndex();
			    if (index >= 0 && index < _model.getSize() - 1)
			    {
			        setSelectedIndex(index + 1);
			        EventBus.publish(new ChangeSetListSelectionEvent(
			                Target.PREVIEW, getSelectedValue()));
			    }

		        }
		    }
	        });

	addMouseListener(new MouseAdapter() {
	    @Override
	    public void mousePressed(MouseEvent e)
	    {
		showPopup(e);
	    }

	    @Override
	    public void mouseReleased(MouseEvent e)
	    {
		showPopup(e);
	    }

	    private void showPopup(MouseEvent e)
	    {
		if (e.isPopupTrigger())
		{
		    int index = locationToIndex(new Point(e.getX(), e.getY()));
		    if (index != -1)
		    {
			if (!isSelectedIndex(index))
			{
			    setSelectedIndex(index);
			}
		    }

		    _menu.show(e.getComponent(), e.getX(), e.getY());
		}
	    }

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

    private void nextSetListItem()
    {
	final int index = getSelectedIndex();
	final SetListItem item = getSelectedValue();

	if (index < 0)
	{
	    return;
	}

	if (index < _model.getSize() - 1)
	{
	    EventBus.publish(new ChangeSetListSelectionEvent(Target.LIVE, item));
	}
    }

    private void previousSetListItem()
    {
	final int index = getSelectedIndex();

	if (index > 1)
	{
	    setSelectedIndex(index - 2);
	    EventBus.publish(new ChangeSetListSelectionEvent(Target.LIVE,
		    getSelectedValue()));
	}
    }

    private JPopupMenu createPopupMenu()
    {
	JPopupMenu menu = new JPopupMenu();
	menu.add(new SetBackgroundAction());
	menu.add(new RemoveAction());

	return menu;
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

	    label.setIcon(new ImageIcon(Utils.getImageURL("images/song.png")));

	    return label;
	}
    }

    private class SetBackgroundAction extends AbstractAction
    {
	private static final long serialVersionUID = -3665750359053549784L;

	public SetBackgroundAction()
	{
	    super("Set Background...");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
	    SetListItem item = getSelectedValue();

	    ChooseBackgroundDialog dialog = new ChooseBackgroundDialog(
		    item.getBackground());
	    dialog.setVisible(true);

	    Background background = dialog.getSelectedBackground();

	    if (null != background)
	    {
		item.setBackground(background);

		EventBus.publish(new SetListItemUpdatedEvent(item));
	    }
	}
    }

    private class RemoveAction extends AbstractAction
    {
	private static final long serialVersionUID = 1442191833302033416L;

	public RemoveAction()
	{
	    super("Remove");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
	    SetListItem item = getSelectedValue();

	    _model.remove(item);
	}
    }
}
