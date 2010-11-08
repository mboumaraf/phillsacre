/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.ui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import uk.me.phillsacre.lyricdisplay.LyricDisplay;
import uk.me.phillsacre.lyricdisplay.main.controller.SongInfoPanelController;
import uk.me.phillsacre.lyricdisplay.presenter.ui.PresentationPanel;

/**
 * 
 * @author Phill
 * @since 6 Nov 2010
 */
public class SongInfoPanel extends JPanel
{
    private static final long serialVersionUID = -6292136061056477178L;

    public SongInfoPanel()
    {
	SongInfoPanelController controller = LyricDisplay
	        .getApplicationContext().getBean(SongInfoPanelController.class);

	PresentationPanel presentation = new PresentationPanel();
	presentation.setPreferredSize(new Dimension(0, 200));

	controller.setPresentation(presentation);

	setLayout(new BorderLayout());
	setBorder(BorderFactory.createTitledBorder("Song Info"));

	VersesList versesList = new VersesList();
	add(new JScrollPane(versesList), BorderLayout.CENTER);

	add(presentation, BorderLayout.SOUTH);
    }
}
