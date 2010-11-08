/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.presenter.controller;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.inject.Named;
import javax.swing.SwingUtilities;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import uk.me.phillsacre.lyricdisplay.main.ui.frame.MainFrame;
import uk.me.phillsacre.lyricdisplay.presenter.events.BlackoutEvent;
import uk.me.phillsacre.lyricdisplay.presenter.events.ChangePresentationStateEvent;
import uk.me.phillsacre.lyricdisplay.presenter.events.ChangeSlideEvent;
import uk.me.phillsacre.lyricdisplay.presenter.events.ChangePresentationStateEvent.State;
import uk.me.phillsacre.lyricdisplay.presenter.ui.PresentationFrame;
import uk.me.phillsacre.lyricdisplay.presenter.ui.PresentationPanel;

/**
 * 
 * @author Phill
 * @since 27 Oct 2010
 */
@Named("presentationController")
public class PresentationController
{
    private PresentationPanel _presentation;
    private PresentationFrame _frame;

    public PresentationController()
    {
	_presentation = new PresentationPanel();
	_frame = new PresentationFrame(_presentation);

	bind();
    }

    public void init()
    {
    }

    private void bind()
    {
	EventBus.subscribeStrongly(ChangePresentationStateEvent.class,
	        new EventSubscriber<ChangePresentationStateEvent>() {
		    @Override
		    public void onEvent(ChangePresentationStateEvent event)
		    {
		        State state = event.getState();
		        switch (state) {
			    case DISABLED:
			        hidePresentation();
			        break;
			    case ENABLED:
			        displayPresentation();
			        break;
		        }
		    }
	        });

	EventBus.subscribeStrongly(ChangeSlideEvent.class,
	        new EventSubscriber<ChangeSlideEvent>() {
		    @Override
		    public void onEvent(ChangeSlideEvent event)
		    {
		        _presentation.setSlide(event.getSlide());
		    }
	        });

	EventBus.subscribeStrongly(BlackoutEvent.class,
	        new EventSubscriber<BlackoutEvent>() {
		    @Override
		    public void onEvent(BlackoutEvent event)
		    {
		        setBlackout(event.isBlackout());
		    }
	        });
    }

    private void setBlackout(boolean blackout)
    {
	_presentation.setBlackout(blackout);
	// _presentation.render();
    }

    private void displayPresentation()
    {
	GraphicsDevice[] devices = GraphicsEnvironment
	        .getLocalGraphicsEnvironment().getScreenDevices();

	if (devices.length == 1)
	{
	    // TODO Testing code - remove
	    if (_frame.isUndecorated())
	    {
		_frame.setUndecorated(false);
	    }
	    _frame.setVisible(true);
	    _presentation.repaint();
	    // _presentation.setExtendedState(JFrame.MAXIMIZED_BOTH);

	}
	else
	{
	    GraphicsDevice gd = getGraphicsDevice(devices);
	    if (null == gd)
	    {
		System.out.println("NULL DEVICE");
	    }

	    if (gd.isFullScreenSupported())
	    {
		gd.setFullScreenWindow(_frame);
		SwingUtilities.invokeLater(new Runnable() {
		    public void run()
		    {
			_presentation.repaint();
		    }
		});
	    }
	    else
	    {
		System.out.println("FULL SCREEN NOT SUPPORTED");
	    }
	}
    }

    private void hidePresentation()
    {
	GraphicsDevice[] devices = GraphicsEnvironment
	        .getLocalGraphicsEnvironment().getScreenDevices();

	if (devices.length == 1)
	{
	    _frame.setVisible(false);
	}
	else
	{
	    GraphicsDevice gd = getGraphicsDevice(devices);
	    if (null == gd)
	    {
		System.out.println("NULL DEVICE");
	    }

	    if (gd.isFullScreenSupported())
	    {
		gd.setFullScreenWindow(null);
		_frame.setVisible(false);
	    }
	}
    }

    /**
     * Try to auto-select the graphics device based on the window that the main
     * frame is NOT displaying on
     * 
     * @param devices
     * @return
     */
    private GraphicsDevice getGraphicsDevice(GraphicsDevice[] devices)
    {
	MainFrame mainFrame = MainFrame.getInstance();
	Rectangle bounds = mainFrame.getBounds();

	int mainIdx = -1;

	for (int i = 0; i < devices.length; i++)
	{
	    GraphicsDevice device = devices[i];

	    GraphicsConfiguration config = device.getDefaultConfiguration();
	    if (config.getBounds().contains(bounds))
	    {
		mainIdx = i;
		break;
	    }
	}

	if (devices.length == 2 && mainIdx > -1)
	{
	    // Simple: just return the 'other' device
	    return devices[mainIdx == 0 ? 1 : 0];
	}

	return null;
    }
}
