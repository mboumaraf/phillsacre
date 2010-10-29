/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.ui.frame;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.bushe.swing.event.EventBus;

import uk.me.phillsacre.lyricdisplay.presenter.events.BlackoutEvent;
import uk.me.phillsacre.lyricdisplay.presenter.events.ChangePresentationStateEvent;
import uk.me.phillsacre.lyricdisplay.presenter.events.ChangeSlideEvent;
import uk.me.phillsacre.lyricdisplay.presenter.events.ChangePresentationStateEvent.State;
import uk.me.phillsacre.lyricdisplay.presenter.ui.slide.TextSlide;

/**
 * 
 * @author Phill
 * @since 25 Oct 2010
 */
public class MainFrame extends JFrame
{
    private static final long      serialVersionUID = 6659658747317605248L;

    private static final MainFrame INSTANCE         = new MainFrame();

    public static MainFrame getInstance()
    {
	return INSTANCE;
    }

    private MainFrame()
    {
	setTitle("Lyric Display Test");
	setSize(800, 600);
	setLocationRelativeTo(null);
	setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	setLayout(new FlowLayout());
	JButton toggleMain = new JButton("Toggle Main");
	toggleMain.addActionListener(new ActionListener() {
	    private boolean _visible = false;

	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
		ChangePresentationStateEvent event = new ChangePresentationStateEvent(
		        _visible ? State.DISABLED : State.ENABLED);
		EventBus.publish(event);

		_visible = !_visible;
	    }
	});

	add(toggleMain);

	JButton blackoutBtn = new JButton("Blackout");
	blackoutBtn.addActionListener(new ActionListener() {
	    private boolean _blackout = false;

	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
		EventBus.publish(new BlackoutEvent(!_blackout));
		_blackout = !_blackout;
	    }

	});

	add(blackoutBtn);

	JButton slideBtn = new JButton("Slide");
	slideBtn.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
		TextSlide slide = new TextSlide(
		        "In Christ alone my hope is found\nHe is my light, my strength, my song");
		EventBus.publish(new ChangeSlideEvent(slide));
	    }
	});

	add(slideBtn);
    }
}
