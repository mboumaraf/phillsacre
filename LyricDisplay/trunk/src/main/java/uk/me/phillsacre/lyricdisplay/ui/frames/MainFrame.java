/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.frames;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import uk.me.phillsacre.lyricdisplay.App;
import uk.me.phillsacre.lyricdisplay.main.dao.SettingsDAO;
import uk.me.phillsacre.lyricdisplay.main.entities.Setting;
import uk.me.phillsacre.lyricdisplay.main.entities.Song;
import uk.me.phillsacre.lyricdisplay.main.events.SongSelectedEvent;
import uk.me.phillsacre.lyricdisplay.ui.actions.SetDefaultBackgroundAction;
import uk.me.phillsacre.lyricdisplay.ui.components.LiveSummaryPanel;
import uk.me.phillsacre.lyricdisplay.ui.components.PreviewSummaryPanel;
import uk.me.phillsacre.lyricdisplay.ui.components.SetListPanel;

/**
 * 
 * @author Phill
 * @since 29 Nov 2011
 */
public class MainFrame extends JFrame
{
    private static final long serialVersionUID = -6943896811812661366L;

    private SettingsDAO       _settingsDAO;
    private JSplitPane        _splitPane;

    public MainFrame()
    {
	setTitle("Lyric Display");
	setSize(800, 600);
	setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	addWindowListener(new WindowAdapter() {
	    public void windowClosing(WindowEvent e)
	    {
		String dimensions = getWidth() + "," + getHeight();

		_settingsDAO.saveSetting("mainFrame.dimensions", dimensions);
	    }
	});

	setLayout(new BorderLayout());

	add(new SetListPanel(), BorderLayout.WEST);

	_splitPane = new JSplitPane();
	_splitPane.setLeftComponent(new PreviewSummaryPanel());
	_splitPane.setRightComponent(new LiveSummaryPanel());

	add(_splitPane, BorderLayout.CENTER);

	JMenuBar menuBar = new JMenuBar();
	JMenu fileMenu = new JMenu("File");
	fileMenu.setMnemonic('f');
	fileMenu.add(new ExitAction());

	JMenu songsMenu = new JMenu("Songs");
	songsMenu.setMnemonic('s');
	songsMenu.add(new NewSongAction());
	songsMenu.add(new EditSongAction());

	JMenu setListMenu = new JMenu("Set List");
	setListMenu.setMnemonic('l');
	setListMenu.add(new SetDefaultBackgroundAction());

	menuBar.add(fileMenu);
	menuBar.add(songsMenu);
	menuBar.add(setListMenu);

	setJMenuBar(menuBar);
    }

    public void init()
    {
	_settingsDAO = App.getApplicationContext().getBean(SettingsDAO.class);

	Setting setting = _settingsDAO.getSetting("mainFrame.dimensions");
	if (null != setting && StringUtils.isNotBlank(setting.getValue()))
	{
	    String[] dims = setting.getValue().split(",");
	    setSize(Integer.parseInt(dims[0]), Integer.parseInt(dims[1]));
	}

	setLocationRelativeTo(null);

	_splitPane.setDividerLocation(.5);
    }

    public void setSettingsDAO(SettingsDAO settingsDAO)
    {
	_settingsDAO = settingsDAO;
    }

    private static class ExitAction extends AbstractAction
    {
	private static final long serialVersionUID = 1L;

	public ExitAction()
	{
	    super("Exit");

	    putValue(MNEMONIC_KEY, (int) 'x');
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
	    System.exit(0);
	}
    }

    private class NewSongAction extends AbstractAction
    {
	private static final long serialVersionUID = 1L;

	public NewSongAction()
	{
	    super("New Song...");

	    putValue(MNEMONIC_KEY, (int) 'n');
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
	    EditSongDialog dialog = new EditSongDialog(MainFrame.this,
		    new Song());
	    dialog.setVisible(true);
	}

    }

    private class EditSongAction extends AbstractAction implements
	    EventSubscriber<SongSelectedEvent>
    {
	private static final long serialVersionUID = 1L;

	private Song              _song;

	public EditSongAction()
	{
	    super("Edit Song...");

	    setEnabled(false);
	    putValue(MNEMONIC_KEY, (int) 'e');

	    EventBus.subscribe(SongSelectedEvent.class, this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
	    EditSongDialog dialog = new EditSongDialog(MainFrame.this, _song);
	    dialog.setVisible(true);
	}

	@Override
	public void onEvent(SongSelectedEvent event)
	{
	    setEnabled((_song = event.getSong()) != null);
	}
    }
}
