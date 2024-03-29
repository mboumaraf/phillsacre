/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.components;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import uk.me.phillsacre.lyricdisplay.main.utils.Utils;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * 
 * @author Phill
 * @since 5 Dec 2011
 */
public class SearchSongsPanel extends JPanel
{
    private static final long  serialVersionUID = 1907539444370949868L;

    private AvailableSongsList _availableSongsList;
    private JTextField         _searchField;

    public SearchSongsPanel()
    {
	setLayout(new BorderLayout());
	setBorder(BorderFactory.createTitledBorder("Available Songs"));

	_availableSongsList = new AvailableSongsList();
	JScrollPane sp = new JScrollPane(_availableSongsList);

	add(sp, BorderLayout.CENTER);


	FormLayout layout = new FormLayout("fill:p:grow, 3dlu, p, 1dlu, p");
	DefaultFormBuilder builder = new DefaultFormBuilder(layout);
	builder.append(_searchField = new JTextField());
	builder.append(new JButton(new SearchAction()));
	builder.append(new JButton(new ClearAction()));

	_searchField.setAction(new SearchAction());
	
	add(builder.getPanel(), BorderLayout.NORTH);
    }

    private class SearchAction extends AbstractAction
    {
	private static final long serialVersionUID = 429314799542624551L;

	public SearchAction()
	{
	    putValue(SMALL_ICON,
		    new ImageIcon(Utils.getImageURL("images/search.png")));
	    putValue(SHORT_DESCRIPTION,
		    "Search for a song containing the specified text");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
	    String value = _searchField.getText();
	    _availableSongsList.search(value);
	}
    }

    private class ClearAction extends AbstractAction
    {
	private static final long serialVersionUID = -3538212839192343416L;

	public ClearAction()
	{
	    putValue(SMALL_ICON,
		    new ImageIcon(Utils.getImageURL("images/cross.png")));
	    putValue(SHORT_DESCRIPTION, "Clear the search box");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
	    _searchField.setText(null);
	    _availableSongsList.search(null);
	}
    }
}
