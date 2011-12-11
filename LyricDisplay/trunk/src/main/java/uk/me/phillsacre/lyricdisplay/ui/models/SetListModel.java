/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.models;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.swing.AbstractListModel;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import uk.me.phillsacre.lyricdisplay.main.entities.Song;
import uk.me.phillsacre.lyricdisplay.main.events.ModifySetListEvent;
import uk.me.phillsacre.lyricdisplay.main.events.ModifySetListEvent.Type;
import uk.me.phillsacre.lyricdisplay.ui.models.entities.SetListItem;

/**
 * 
 * @author Phill
 * @since 1 Dec 2011
 */
@Named("setListModel")
public class SetListModel extends AbstractListModel<SetListItem> implements
        EventSubscriber<ModifySetListEvent>
{
    private static final long serialVersionUID = -6283716466358816195L;

    private List<SetListItem> _items;

    public SetListModel()
    {
	_items = new ArrayList<SetListItem>();

	EventBus.subscribe(ModifySetListEvent.class, this);
    }

    @Override
    public int getSize()
    {
	return _items.size();
    }

    @Override
    public SetListItem getElementAt(int index)
    {
	return _items.get(index);
    }

    @Override
    public void onEvent(ModifySetListEvent event)
    {
	Song song = event.getSong();

	if (event.getType() == Type.ADD)
	{
	    SetListItem item = new SetListItem(song);

	    _items.add(item);
	    fireIntervalAdded(this, _items.size() - 1, _items.size());
	}
	else
	{
	    int index = _items.indexOf(song);
	    _items.remove(song);

	    fireIntervalRemoved(this, index, index);
	}
    }

}
