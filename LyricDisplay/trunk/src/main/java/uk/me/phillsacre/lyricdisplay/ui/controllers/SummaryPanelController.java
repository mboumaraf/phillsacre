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

import uk.me.phillsacre.lyricdisplay.main.events.ControlEvent;
import uk.me.phillsacre.lyricdisplay.main.events.SetListControlEvent;
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
	int getSelectedIndex();

	void setSelectedIndex(int selectedIndex);

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
		    EventBus.publish(new VerseEvent(_target, _versesListModel
			    .getItem(), verse));
		}
	    }
	});

	EventBus.subscribe(VerseEvent.class, this);
	if (_target == Target.LIVE)
	{
	    EventBus.subscribeStrongly(ControlEvent.class,
		    new EventSubscriber<ControlEvent>() {
		        @Override
		        public void onEvent(ControlEvent event)
		        {
			    switch (event.getType()) {
				case FORWARD:
				    selectNextVerse();
				    break;

				case BACKWARD:
				    selectPreviousVerse();
				    break;

				default:
				    break;

			    }
			}
		    });
	}
    }

    public void init()
    {
	_ui.addListSelectionListener(new ListSelectionListener() {
	    @Override
	    public void valueChanged(ListSelectionEvent e)
	    {
		final String verse = _ui.getSelectedVerse();

		EventBus.publish(new VerseEvent(_target, _versesListModel
		        .getItem(), verse));
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

    private void selectPreviousVerse()
    {
	int selectedIndex = _ui.getSelectedIndex();

	if (selectedIndex > 0)
	{
	    selectedIndex -= 1;
	    _ui.setSelectedIndex(selectedIndex);
	}
	else
	{
	    EventBus.publish(new SetListControlEvent(
		    SetListControlEvent.Type.BACKWARD));
	}
    }

    private void selectNextVerse()
    {
	int selectedIndex = _ui.getSelectedIndex();

	if (selectedIndex < _versesListModel.getSize() - 1)
	{
	    selectedIndex += 1;
	    _ui.setSelectedIndex(selectedIndex);
	}
	else
	{
	    EventBus.publish(new SetListControlEvent(
		    SetListControlEvent.Type.FORWARD));
	}
    }
}
