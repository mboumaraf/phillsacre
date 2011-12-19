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
public class VerseEvent
{
    private final String      _text;
    private final Target      _target;
    private final SetListItem _item;

    public VerseEvent(Target target, SetListItem item, String text)
    {
	_text = text;
	_target = target;
	_item = item;
    }

    public String getText()
    {
	return _text;
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
