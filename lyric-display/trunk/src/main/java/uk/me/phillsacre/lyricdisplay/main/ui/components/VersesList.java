/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.ui.components;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;

import uk.me.phillsacre.lyricdisplay.main.controller.VersesListController.VersesListUI;
import uk.me.phillsacre.lyricdisplay.presenter.ui.slide.Slide;

/**
 * 
 * @author Phill
 * @since 5 Nov 2010
 */
public class VersesList extends JList implements VersesListUI
{
    private static final long serialVersionUID = 911957910035992863L;

    public VersesList()
    {
	setFont(getFont().deriveFont(Font.PLAIN));
	setCellRenderer(new VersesListCellRenderer());
    }

    private class VersesListCellRenderer extends DefaultListCellRenderer
    {
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
	        int index, boolean isSelected, boolean cellHasFocus)
	{
	    Slide slide = (Slide) value;

	    JLabel label = (JLabel) super.getListCellRendererComponent(list,
		    value, index, isSelected, cellHasFocus);

	    label.setText(slide.getText(index));

	    JPanel panel = new JPanel(new GridLayout(1, 1));
	    panel.add(label);
	    panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
	    panel.setBackground(SystemColor.text);

	    return panel;
	}
    }

    @Override
    public void resetSelection()
    {
	setSelectedIndex(0);
    }

    @Override
    public void setListModel(ListModel listModel)
    {
	this.setModel(listModel);
    }
}
