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

	final String[] verses = {
	        "Yours for ever! God of love\nhear us from Your throne above;\nYours for ever may we be,\nhere and in eternity.",
	        "Yours for ever! Lord of life,\nshield us through our earthly strife;\nYou the life, the truth, the way,\nguide us to the realms of day.",
	        "Yours for ever! O how blessed\nthey who find in You their rest!\nSaviour, guardian, heavenly friend,\nO defend us to the end." };

	JButton slideBtn = new JButton("Slide");
	slideBtn.addActionListener(new ActionListener() {
	    int idx = 0;

	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
		if (idx == verses.length)
		{
		    idx = 0;
		}
		String verse = verses[idx++];

		TextSlide slide = new TextSlide(verse);
		EventBus.publish(new ChangeSlideEvent(slide));
	    }
	});

	add(slideBtn);
    }
}
