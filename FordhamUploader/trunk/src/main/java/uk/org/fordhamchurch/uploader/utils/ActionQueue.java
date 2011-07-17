package uk.org.fordhamchurch.uploader.utils;

import java.util.ArrayList;
import java.util.List;

import uk.org.fordhamchurch.uploader.utils.UploadRunnerWorker.ProgressListener;

/**
 * Action queue designed so that each action can set its own progress to be
 * between 0 and 100, and this will set the total progress accordingly.
 * 
 * @author Phill Sacre
 * @since 14 Jul 2011
 */
public class ActionQueue implements ProgressListener
{
    private List<Action>     _actions        = new ArrayList<Action>();
    private int              _executingIndex = 0;
    private ProgressListener _progressListener;
    private boolean          _cancelled      = false;

    public ActionQueue(ProgressListener progressListener)
    {
	_progressListener = progressListener;
    }

    public void addAction(Action action)
    {
	_actions.add(action);
    }

    public void execute()
    {
	for (_executingIndex = 0; _executingIndex < _actions.size(); _executingIndex++)
	{
	    if (_cancelled)
	    {
		break;
	    }

	    Action action = _actions.get(_executingIndex);
	    action.execute(this);
	}
    }

    public void updateProgress(int percentage)
    {
	int range = 100 / _actions.size(); // e.g. 4 actions will equal range of
	                                   // 25
	float delta = (float) percentage / 100.0f; // The percentage as a float,
	                                           // e.g. 0.5
	int converted = Math.round(range * delta); // The converted range, e.g.
	                                           // 12

	int base = _executingIndex * range; // The base value of the total
	                                    // percentage, i.e. 25 * 3 = 75

	try
	{
	    _progressListener.updateProgress(base + converted);
	}
	catch (CancelException e)
	{
	    // prevent excuting any more tasks
	    _cancelled = true;
	    throw e;
	}
    }

    public static interface Action
    {
	void execute(ProgressListener progressListener);
    }
}
