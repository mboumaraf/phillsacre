/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.ui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.bushe.swing.event.EventBus;

import uk.me.phillsacre.lyricdisplay.LyricDisplay;
import uk.me.phillsacre.lyricdisplay.main.controller.SongInfoPanelController;
import uk.me.phillsacre.lyricdisplay.presenter.ui.PresentationPanel;

import com.jgoodies.forms.builder.ButtonBarBuilder2;

/**
 * 
 * @author Phill
 * @since 6 Nov 2010
 */
public class SongInfoPanel extends JPanel
{
    private static final long       serialVersionUID = -6292136061056477178L;

    private PresentationPanel       _presentation;
    private SongInfoPanelController _controller;

    public SongInfoPanel()
    {
	_controller = LyricDisplay.getApplicationContext().getBean(
	        SongInfoPanelController.class);

	_presentation = new PresentationPanel();
	_presentation.setBorder(BorderFactory.createEtchedBorder());

	_controller.setPresentation(_presentation);

	setLayout(new BorderLayout());
	setBorder(BorderFactory.createTitledBorder("Song Info"));

	ButtonBarBuilder2 btnBar = new ButtonBarBuilder2();
	btnBar.addGlue();
	btnBar.addButton(new GoLiveAction());
	btnBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

	add(btnBar.getPanel(), BorderLayout.NORTH);

	VersesList versesList = new VersesList();
	add(new JScrollPane(versesList), BorderLayout.CENTER);

	add(_presentation, BorderLayout.SOUTH);

	addComponentListener(new ComponentAdapter() {
	    @Override
	    public void componentResized(ComponentEvent event)
	    {
		int width = event.getComponent().getWidth();
		int height = (int) Math.round(width * 0.75);

		_presentation.setPreferredSize(new Dimension(width, height));
	    }
	});
    }

    private class GoLiveAction extends AbstractAction
    {
	public GoLiveAction()
	{
	    super("Go Live >");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
	    _controller.goLive();
	}

    }
}
