/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.components;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * 
 * @author Phill
 * @since 1 Dec 2011
 */
public class SetListPanel extends JPanel
{
    private static final long serialVersionUID = -8512902569247645211L;

    public SetListPanel()
    {
	setLayout(new GridLayout(2, 1));

	SetList currentSongsList = new SetList();
	JScrollPane sp1 = new JScrollPane(currentSongsList);
	sp1.setBorder(BorderFactory.createTitledBorder("Current Songs"));
	add(sp1);

	JPanel container = new JPanel();
	container.setLayout(new BorderLayout());

	container.add(new SearchSongsPanel(), BorderLayout.CENTER);

	add(container);
    }
}
