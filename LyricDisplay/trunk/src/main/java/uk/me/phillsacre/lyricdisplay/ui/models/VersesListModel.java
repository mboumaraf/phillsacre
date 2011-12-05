/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractListModel;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import uk.me.phillsacre.lyricdisplay.main.events.ChangeSetListSelectionEvent;
import uk.me.phillsacre.lyricdisplay.main.events.utils.Target;
import uk.me.phillsacre.lyricdisplay.ui.models.entities.SetListItem;

/**
 * 
 * @author Phill
 * @since 1 Dec 2011
 */
public class VersesListModel extends AbstractListModel<String> implements
        EventSubscriber<ChangeSetListSelectionEvent>
{
    private static final long serialVersionUID = 2969085267477573382L;

    private final Target      _target;
    private List<String>      _verses;
    private SetListItem       _item;

    public VersesListModel(Target target)
    {
	_target = target;
	_verses = new ArrayList<String>();

	EventBus.subscribe(ChangeSetListSelectionEvent.class, this);
    }

    @Override
    public int getSize()
    {
	return _verses.size();
    }

    @Override
    public String getElementAt(int index)
    {
	if (index >= _verses.size())
	{
	    return null;
	}

	return _verses.get(index);
    }

    @Override
    public void onEvent(ChangeSetListSelectionEvent event)
    {
	if (event.getTarget() != _target)
	{
	    return;
	}

	int size = _verses.size();
	_verses.clear();
	fireContentsChanged(this, 0, size);

	_item = event.getItem();

	_verses.addAll(parseVerses(_item.getSong().getText()));
	fireContentsChanged(this, 0, _verses.size());
    }

    public SetListItem getItem()
    {
	return _item;
    }

    private List<String> parseVerses(String text)
    {
	String[] verses = text.split("\n\n");

	return Arrays.asList(verses);
    }
}
