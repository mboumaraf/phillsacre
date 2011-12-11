/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.events;

import uk.me.phillsacre.lyricdisplay.ui.models.entities.SetListItem;

/**
 * 
 * @author Phill
 * @since 11 Dec 2011
 */
public class SetListItemUpdatedEvent
{
    private final SetListItem _item;

    public SetListItemUpdatedEvent(SetListItem item)
    {
	_item = item;
    }

    public SetListItem getItem()
    {
	return _item;
    }
}
