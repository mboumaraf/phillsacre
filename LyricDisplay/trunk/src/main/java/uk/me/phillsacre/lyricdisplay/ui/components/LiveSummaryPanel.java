/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.components;

import java.awt.GraphicsDevice;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.ComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.me.phillsacre.lyricdisplay.App;
import uk.me.phillsacre.lyricdisplay.main.events.BlankScreenEvent;
import uk.me.phillsacre.lyricdisplay.main.events.DisplayLiveEvent;
import uk.me.phillsacre.lyricdisplay.main.utils.Utils;
import uk.me.phillsacre.lyricdisplay.ui.controllers.LiveSummaryPanelController;
import uk.me.phillsacre.lyricdisplay.ui.presentation.PresentationFrame;

/**
 * 
 * @author Phill
 * @since 3 Dec 2011
 */
public class LiveSummaryPanel extends SummaryPanel implements
        EventSubscriber<DisplayLiveEvent>
{
    private static final long         serialVersionUID = -3909295393240844993L;
    private static final Logger       LOG              = LoggerFactory
	                                                       .getLogger(LiveSummaryPanel.class);

    private JComboBox<GraphicsDevice> _displayComboBox;

    public LiveSummaryPanel()
    {
	super(App.getApplicationContext().getBean(
	        LiveSummaryPanelController.class), "Live");
    }

    @Override
    public JToolBar createToolBar()
    {
	JToolBar toolBar = new JToolBar();

	toolBar.add(Box.createHorizontalGlue());

	_displayComboBox = new ScreenDeviceComboBox();
	toolBar.add(_displayComboBox);

	JToggleButton toggleButton = new JToggleButton(new ToggleLiveAction(
	        _displayComboBox.getModel()));
	toolBar.add(toggleButton);

	JToggleButton toggleBlankScreen = new JToggleButton(
	        new ToggleBlankScreen());
	toolBar.add(toggleBlankScreen);

	EventBus.subscribe(DisplayLiveEvent.class, this);

	return toolBar;
    }

    @Override
    public void onEvent(DisplayLiveEvent event)
    {
	_displayComboBox.setEnabled(!event.isLive());
    }

    private static class ToggleBlankScreen extends AbstractAction implements
	    EventSubscriber<DisplayLiveEvent>
    {
	private static final long serialVersionUID = 4422308683439374921L;

	private boolean           _blank;

	public ToggleBlankScreen()
	{
	    super("Blank");

	    setEnabled(false);

	    putValue(SELECTED_KEY, false);
	    putValue(SHORT_DESCRIPTION, "Display a blank screen");
	    putValue(SMALL_ICON,
		    new ImageIcon(Utils.getImageURL("images/blackscreen.png")));

	    EventBus.subscribe(DisplayLiveEvent.class, this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
	    if (_blank)
	    {
		_blank = false;
		putValue(SELECTED_KEY, false);
	    }
	    else
	    {
		_blank = true;
		putValue(SELECTED_KEY, true);
	    }

	    EventBus.publish(new BlankScreenEvent(_blank));
	}

	@Override
	public void onEvent(DisplayLiveEvent event)
	{
	    setEnabled(event.isLive());
	}
    }

    private static class ToggleLiveAction extends AbstractAction implements
	    EventSubscriber<DisplayLiveEvent>
    {
	private static final long             serialVersionUID = -6421217351666380012L;

	private boolean                       _live            = false;
	private PresentationFrame             _frame;
	private GraphicsDevice                _device;
	private boolean                       _fullScreen;
	private ComboBoxModel<GraphicsDevice> _screenDeviceModel;

	public ToggleLiveAction(ComboBoxModel<GraphicsDevice> screenDeviceModel)
	{
	    super("Live");

	    _screenDeviceModel = screenDeviceModel;
	    _frame = new PresentationFrame();

	    putValue(SELECTED_KEY, false);
	    putValue(SHORT_DESCRIPTION,
		    "Toggle whether the live screen is displayed");
	    putValue(SMALL_ICON,
		    new ImageIcon(Utils.getImageURL("images/showlive.png")));

	    EventBus.subscribe(DisplayLiveEvent.class, this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
	    if (_live)
	    {
		EventBus.publish(new DisplayLiveEvent(false));
	    }
	    else
	    {
		EventBus.publish(new DisplayLiveEvent(true));
	    }
	}

	private void hidePresentation()
	{
	    LOG.debug("Hiding presentation");

	    if (!_fullScreen)
	    {
		_frame.setVisible(false);
	    }
	    else
	    {
		if (_device.isFullScreenSupported())
		{
		    _device.setFullScreenWindow(null);
		    _frame.setVisible(false);
		}
	    }

	    putValue(SELECTED_KEY, false);
	    _live = false;
	}

	private void displayPresentation()
	{
	    _device = (GraphicsDevice) _screenDeviceModel.getSelectedItem();
	    _fullScreen = _screenDeviceModel.getSize() > 1;

	    if (!_fullScreen)
	    {
		LOG.debug("Displaying presentation as Frame");

		if (_frame.isUndecorated())
		{
		    _frame.setUndecorated(false);
		}

		_frame.setVisible(true);
		_frame.setExtendedState(_frame.getExtendedState()
		        | JFrame.MAXIMIZED_BOTH);
	    }
	    else
	    {
		if (_device.isFullScreenSupported())
		{
		    LOG.debug("Displaying full-screen presentation");

		    _device.setFullScreenWindow(_frame);
		}
		else
		{
		    throw new RuntimeException("Full screen not supported");
		}
	    }

	    putValue(SELECTED_KEY, true);
	    _live = true;
	}

	@Override
	public void onEvent(DisplayLiveEvent event)
	{
	    if (event.isLive())
	    {
		displayPresentation();
	    }
	    else
	    {
		hidePresentation();
	    }
	}
    }
}
