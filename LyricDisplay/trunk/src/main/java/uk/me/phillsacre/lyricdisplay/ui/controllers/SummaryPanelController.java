/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.controllers;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import uk.me.phillsacre.lyricdisplay.main.events.VerseEvent;
import uk.me.phillsacre.lyricdisplay.main.events.utils.Target;
import uk.me.phillsacre.lyricdisplay.ui.models.VersesListModel;
import uk.me.phillsacre.lyricdisplay.ui.models.entities.SetListItem;

/**
 * 
 * @author Phill
 * @since 1 Dec 2011
 */
public abstract class SummaryPanelController implements
        EventSubscriber<VerseEvent>
{
    public interface SummaryPanelUI
    {
	String getSelectedVerse();

	void addListSelectionListener(ListSelectionListener l);

	void setDisplayVerse(SetListItem item, String verse);
    }

    private Target          _target;
    private VersesListModel _versesListModel;
    private SummaryPanelUI  _ui;

    public SummaryPanelController(Target target)
    {
	_target = target;
	_versesListModel = new VersesListModel(target);

	_versesListModel.addListDataListener(new ListDataListener() {
	    @Override
	    public void intervalAdded(ListDataEvent e)
	    {
	    }

	    @Override
	    public void intervalRemoved(ListDataEvent e)
	    {
	    }

	    @Override
	    public void contentsChanged(ListDataEvent e)
	    {
		String verse = _ui.getSelectedVerse();

		if (null != verse)
		{
		    EventBus.publish(new VerseEvent(_target, verse));
		}
	    }
	});

	EventBus.subscribe(VerseEvent.class, this);
    }

    public void init()
    {
	_ui.addListSelectionListener(new ListSelectionListener() {
	    @Override
	    public void valueChanged(ListSelectionEvent e)
	    {
		final String verse = _ui.getSelectedVerse();

		EventBus.publish(new VerseEvent(_target, verse));
	    }
	});
    }

    public final void setUI(SummaryPanelUI ui)
    {
	_ui = ui;
    }

    public ListModel<String> getVersesListModel()
    {
	return _versesListModel;
    }

    public void setDisplayVerse(String text)
    {
	_ui.setDisplayVerse(_versesListModel.getItem(), text);
    }

    @Override
    public void onEvent(VerseEvent event)
    {
	if (_target == event.getTarget())
	{
	    setDisplayVerse(event.getText());
	}
    }
}