/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.events;

import uk.me.phillsacre.lyricdisplay.main.events.utils.Target;

/**
 * 
 * @author Phill
 * @since 1 Dec 2011
 */
public class VerseEvent
{
    private String _text;
    private Target _target;

    public VerseEvent(Target target, String text)
    {
	_text = text;
	_target = target;
    }

    public String getText()
    {
	return _text;
    }

    public Target getTarget()
    {
	return _target;
    }
}
