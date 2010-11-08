/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.presenter.ui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.bushe.swing.event.EventBus;

import uk.me.phillsacre.lyricdisplay.presenter.events.ChangePresentationStateEvent;
import uk.me.phillsacre.lyricdisplay.presenter.events.ChangePresentationStateEvent.State;

/**
 * 
 * @author Phill
 * @since 25 Oct 2010
 */
public class PresentationFrame extends JFrame
{
    private static final long serialVersionUID = -7637114333874163665L;

    public PresentationFrame(PresentationPanel panel)
    {
	// setAlwaysOnTop(true);
	setSize(800, 600);
	setUndecorated(true);
	setIgnoreRepaint(true);

	setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	addWindowListener(new WindowAdapter() {
	    @Override
	    public void windowClosing(WindowEvent e)
	    {
		ChangePresentationStateEvent event = new ChangePresentationStateEvent(
		        State.DISABLED);
		EventBus.publish(event);
	    }
	});

	setLayout(new BorderLayout());
	add(panel, BorderLayout.CENTER);
    }
}
