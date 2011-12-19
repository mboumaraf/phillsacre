/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.presentation;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import uk.me.phillsacre.lyricdisplay.main.events.BlankScreenEvent;
import uk.me.phillsacre.lyricdisplay.main.events.ChangeSetListSelectionEvent;
import uk.me.phillsacre.lyricdisplay.main.events.ControlEvent;
import uk.me.phillsacre.lyricdisplay.main.events.VerseEvent;
import uk.me.phillsacre.lyricdisplay.main.events.utils.Target;
import uk.me.phillsacre.lyricdisplay.ui.models.entities.SetListItem;
import uk.me.phillsacre.lyricdisplay.ui.presentation.display.SongPanel;

/**
 * 
 * @author Phill
 * @since 4 Dec 2011
 */
@SuppressWarnings("rawtypes")
public class PresentationFrame extends JFrame implements EventSubscriber
{
    private static final long         serialVersionUID = -7637114333874163665L;

    private BasePresentationComponent _presentation;
    private SetListItem               _item;

    public PresentationFrame()
    {
	// setAlwaysOnTop(true);
	setSize(800, 600);
	setUndecorated(true);
	setIgnoreRepaint(true);

	setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

	_presentation = new BasePresentationComponent(false);

	setLayout(new BorderLayout());
	add(_presentation, BorderLayout.CENTER);

	EventBus.subscribe(VerseEvent.class, PresentationFrame.this);
	EventBus.subscribe(BlankScreenEvent.class, this);
	EventBus.subscribe(ChangeSetListSelectionEvent.class, this);

	addComponentListener(new ComponentAdapter() {
	    @Override
	    public void componentShown(ComponentEvent e)
	    {
		_presentation.repaint();
	    }
	});
	addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e)
	    {
		EventBus.publish(new ControlEvent(ControlEvent.Type.FORWARD));
	    }
	});
	addKeyListener(new KeyAdapter() {
	    @Override
	    public void keyPressed(KeyEvent e)
	    {
		final int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_DOWN)
		{
		    EventBus.publish(new ControlEvent(ControlEvent.Type.FORWARD));
		}
		else if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_UP)
		{
		    EventBus.publish(new ControlEvent(ControlEvent.Type.BACKWARD));
		}
	    }
	});
    }

    @Override
    public void onEvent(Object event)
    {
	if (event instanceof VerseEvent)
	{
	    final VerseEvent ve = (VerseEvent) event;
	    if (ve.getTarget() == Target.LIVE)
	    {
		_presentation
		        .setDisplayPanel(new SongPanel(_item, ve.getText()));
	    }
	}
	else if (event instanceof BlankScreenEvent)
	{
	    boolean blank = ((BlankScreenEvent) event).isBlank();
	    _presentation.setBlackout(blank);
	}
	else if (event instanceof ChangeSetListSelectionEvent)
	{
	    _item = ((ChangeSetListSelectionEvent) event).getItem();
	}
    }
}
