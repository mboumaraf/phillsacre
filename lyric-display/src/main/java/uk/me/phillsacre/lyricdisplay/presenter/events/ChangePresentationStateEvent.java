/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.presenter.events;

/**
 * 
 * @author Phill
 * @since 27 Oct 2010
 */
public class ChangePresentationStateEvent
{
    public enum State
    {
	ENABLED, DISABLED
    }

    private final State _state;

    public ChangePresentationStateEvent(State state)
    {
	_state = state;
    }

    public State getState()
    {
	return _state;
    }
}
