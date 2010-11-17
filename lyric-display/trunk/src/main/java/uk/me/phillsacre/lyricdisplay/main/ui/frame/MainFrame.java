/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.ui.frame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import org.bushe.swing.event.EventBus;

import uk.me.phillsacre.lyricdisplay.main.ui.actions.AddSongAction;
import uk.me.phillsacre.lyricdisplay.main.ui.actions.EditSongAction;
import uk.me.phillsacre.lyricdisplay.main.ui.components.LivePanel;
import uk.me.phillsacre.lyricdisplay.main.ui.components.SongInfoPanel;
import uk.me.phillsacre.lyricdisplay.main.ui.components.SongList;
import uk.me.phillsacre.lyricdisplay.presenter.events.BlackoutEvent;
import uk.me.phillsacre.lyricdisplay.presenter.events.ChangePresentationStateEvent;
import uk.me.phillsacre.lyricdisplay.presenter.events.ChangePresentationStateEvent.State;

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
	setSize(1024, 768);
	setLocationRelativeTo(null);
	setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	setLayout(new BorderLayout());

	JPanel btnPanel = new JPanel();

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

	btnPanel.add(toggleMain);

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

	btnPanel.add(blackoutBtn);

	add(btnPanel, BorderLayout.SOUTH);

	JPanel columnPanel = new JPanel();
	columnPanel.setLayout(new GridLayout(1, 3));

	SongList songList = new SongList();
	JPanel songPanel = new JPanel();
	songPanel.setLayout(new BorderLayout());
	songPanel.setBorder(BorderFactory.createTitledBorder("Songs"));
	songPanel.add(new JScrollPane(songList), BorderLayout.CENTER);

	JPanel songBtnPanel = new JPanel();

	JButton addSongBtn = new JButton(new AddSongAction());
	songBtnPanel.add(addSongBtn);

	JButton editSongBtn = new JButton(new EditSongAction());
	songBtnPanel.add(editSongBtn);

	songPanel.add(songBtnPanel, BorderLayout.SOUTH);

	columnPanel.add(songPanel);

	SongInfoPanel songInfoPanel = new SongInfoPanel();

	columnPanel.add(songInfoPanel);

	LivePanel livePanel = new LivePanel();

	columnPanel.add(livePanel);

	add(columnPanel, BorderLayout.CENTER);
    }
}
