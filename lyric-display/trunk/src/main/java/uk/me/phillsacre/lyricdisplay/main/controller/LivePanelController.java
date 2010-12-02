/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.controller;

import javax.inject.Named;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import uk.me.phillsacre.lyricdisplay.main.controller.VersesListController.VersesListUI;
import uk.me.phillsacre.lyricdisplay.main.events.GoLiveEvent;
import uk.me.phillsacre.lyricdisplay.main.ui.models.SongInfoListModel;
import uk.me.phillsacre.lyricdisplay.presenter.api.Presentation;
import uk.me.phillsacre.lyricdisplay.presenter.events.ChangeSlideEvent;
import uk.me.phillsacre.lyricdisplay.presenter.ui.slide.SongSlide;

/**
 * 
 * @author Phill
 * @since 23 Nov 2010
 */
@Named("livePanelController")
public class LivePanelController
{
    private Presentation      _presentation;
    private VersesListUI      _ui;
    private SongInfoListModel _listModel;

    public LivePanelController()
    {
	_listModel = new SongInfoListModel();

	EventBus.subscribeStrongly(GoLiveEvent.class,
	        new EventSubscriber<GoLiveEvent>() {
		    @Override
		    public void onEvent(GoLiveEvent event)
		    {
		        if (event.getSlide() instanceof SongSlide)
		        {
		            // TODO: This is a hack - fix it
		            SongSlide songSlide = (SongSlide) event.getSlide();
		            
		        }
		        EventBus.publish(new ChangeSlideEvent(event.getSlide(),
		                0));
		    }
	        });
    }

    public void setPresentation(Presentation presentation)
    {
	_presentation = presentation;
    }

    public void setUI(VersesListUI ui)
    {
	_ui = ui;

	_ui.addListSelectionListener(new ListSelectionListener() {
	    @Override
	    public void valueChanged(ListSelectionEvent e)
	    {
		handleSelection(e.getFirstIndex());
	    }
	});
	_ui.setListModel(_listModel);
    }

    private void handleSelection(int index)
    {

    }
}
