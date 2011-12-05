/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.components;

import java.awt.Rectangle;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import uk.me.phillsacre.lyricdisplay.App;

/**
 * 
 * @author Phill
 * @since 1 Dec 2011
 */
public class VersesList extends JList<String>
{
    private static final long serialVersionUID = 996578786650634376L;

    public VersesList(final ListModel<String> listModel)
    {
	super(listModel);

	setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	listModel.addListDataListener(new ListDataListener() {
	    @Override
	    public void intervalAdded(ListDataEvent e)
	    {
	    }

	    @Override
	    public void intervalRemoved(ListDataEvent e)
	    {
	    }

	    @Override
	    public void contentsChanged(ListDataEvent e)
	    {
		setSelectedIndex(0);
		scrollRectToVisible(new Rectangle(0, 0));
	    }
	});

	setCellRenderer(App.getApplicationContext().getBean(
	        VersesListRenderer.class));
    }
}
