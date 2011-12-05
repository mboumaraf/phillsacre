/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionListener;

import uk.me.phillsacre.lyricdisplay.ui.controllers.SummaryPanelController;
import uk.me.phillsacre.lyricdisplay.ui.controllers.SummaryPanelController.SummaryPanelUI;
import uk.me.phillsacre.lyricdisplay.ui.models.entities.SetListItem;

/**
 * 
 * @author Phill
 * @since 1 Dec 2011
 */
public abstract class SummaryPanel extends JPanel implements SummaryPanelUI
{
    private static final long   serialVersionUID = -2832831339058955175L;

    private JList<String>       _versesList;
    private PreviewPresentation _previewPanel;

    public SummaryPanel(SummaryPanelController controller, String title)
    {
	controller.setUI(this);

	setLayout(new BorderLayout());
	setBorder(BorderFactory.createTitledBorder(title));

	JToolBar toolBar = createToolBar();
	toolBar.setFloatable(false);
	add(toolBar, BorderLayout.NORTH);

	JPanel container = new JPanel();
	container.setLayout(new GridLayout(1, 1));

	_previewPanel = new PreviewPresentation(controller);
	_previewPanel.setMinimumSize(new Dimension(100, 100));
	_versesList = new VersesList(controller.getVersesListModel());

	add(new JScrollPane(_versesList), BorderLayout.CENTER);

	container.add(_previewPanel);
	container.setBorder(BorderFactory.createEtchedBorder());
	add(container, BorderLayout.SOUTH);

	controller.init();

	setPresentationSize();

	addComponentListener(new ComponentAdapter() {
	    @Override
	    public void componentResized(ComponentEvent e)
	    {
		setPresentationSize();
	    }
	});
    }

    public String getSelectedVerse()
    {
	return (String) _versesList.getSelectedValue();
    }

    public void addListSelectionListener(ListSelectionListener l)
    {
	_versesList.addListSelectionListener(l);
    }

    @Override
    public void setDisplayVerse(SetListItem item, String verse)
    {
	_previewPanel.setVerse(item, verse);
    }

    /**
     * Attempt to set the presentation size to the approximately correct aspect
     * ratio (4:3).
     */
    private void setPresentationSize()
    {
	float width = getWidth();

	float height = (width / 4.0f) * 3.0f;

	_previewPanel
	        .setPreferredSize(new Dimension((int) width, (int) height));
    }

    public abstract JToolBar createToolBar();
}
