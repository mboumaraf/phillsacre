/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractListModel;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.me.phillsacre.lyricdisplay.main.entities.Song;
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
    private static final long   serialVersionUID = 2969085267477573382L;

    private static final Logger LOG              = LoggerFactory
	                                                 .getLogger(VersesListModel.class);

    private final Target        _target;
    private List<String>        _verses;
    private SetListItem         _item;

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

	// Ignore if the 'new' item is the same as the current one.
	final SetListItem item = event.getItem();
	if (item.equals(_item))
	{
	    return;
	}

	int size = _verses.size();
	_verses.clear();
	fireContentsChanged(this, 0, size);

	_item = item;

	_verses.addAll(parseVerses(_item.getSong()));
	fireContentsChanged(this, 0, _verses.size());
    }

    public SetListItem getItem()
    {
	return _item;
    }

    private List<String> parseVerses(Song song)
    {
	String[] versesArr = song.getText().split("\n\n");
	final String songOrder = song.getSongOrder();

	ArrayList<String> verses = new ArrayList<String>(
	        Arrays.asList(versesArr));

	if (StringUtils.isNotBlank(songOrder))
	{
	    HashMap<String, String> map = new HashMap<String, String>();

	    for (String verse : verses)
	    {
		verse = verse.trim();

		// Use the first line as the identifier.
		String id = verse.substring(0, verse.indexOf('\n')).trim()
		        .toUpperCase();

		map.put(id, verse);
		if (id.startsWith("VERSE "))
		{
		    map.put(id.replace("VERSE ", "V"), verse);
		}
		else if (id.equals("CHORUS"))
		{
		    map.put("C", verse);
		}
	    }

	    verses.clear();

	    String[] components = songOrder.split(" ");
	    for (String component : components)
	    {
		verses.add(map.get(component));
	    }
	}

	LOG.debug("Parsed {} verses for song: {}", verses.size(),
	        song.getTitle());

	return verses;
    }
}
