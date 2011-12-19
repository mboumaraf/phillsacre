/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.events;

/**
 * 
 * @author Phill
 * @since 19 Dec 2011
 */
public class SetListControlEvent
{
    public enum Type
    {
	FORWARD, BACKWARD
    };

    private final Type _type;

    public SetListControlEvent(Type type)
    {
	_type = type;
    }

    public Type getType()
    {
	return _type;
    }
}
