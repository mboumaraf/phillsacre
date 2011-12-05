/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.events;

import uk.me.phillsacre.lyricdisplay.main.events.utils.Target;
import uk.me.phillsacre.lyricdisplay.ui.models.entities.SetListItem;

/**
 * 
 * @author Phill
 * @since 1 Dec 2011
 */
public class ChangeSetListSelectionEvent
{
    private Target      _target;
    private SetListItem _item;

    public ChangeSetListSelectionEvent(Target target, SetListItem item)
    {
	_target = target;
	_item = item;
    }

    public Target getTarget()
    {
	return _target;
    }

    public SetListItem getItem()
    {
	return _item;
    }
}
