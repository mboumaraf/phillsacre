/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.components;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 * 
 * @author Phill
 * @since 1 Dec 2011
 */
@Named("versesListRenderer")
public class VersesListRenderer extends DefaultListCellRenderer
{
    private static final long   serialVersionUID = 4264730738664529695L;

    private Map<String, String> _sectionsMap     = new HashMap<String, String>();

    public VersesListRenderer()
    {
	_sectionsMap = new HashMap<String, String>();

	_sectionsMap.put("Verse", "blue");
	_sectionsMap.put("Chorus", "maroon");
	_sectionsMap.put("Bridge", "red");
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value,
	    int index, boolean isSelected, boolean cellHasFocus)
    {
	String text = (String) value;
	text = text.replaceAll("\n", "<br/>");

	for (String section : _sectionsMap.keySet())
	{
	    text = text.replaceAll("(" + section + " ?[\\d]*\\b)",
		    "<span style=\"color:" + _sectionsMap.get(section)
		            + ";font-weight:bold;\">$1</span>");
	}

	text = "<html>" + text + "</html>";

	JLabel label = (JLabel) super.getListCellRendererComponent(list, text,
	        index, isSelected, cellHasFocus);
	if (!isSelected && !cellHasFocus)
	{
	    label.setBackground(SystemColor.text);
	}

	JPanel panel = new JPanel(new GridLayout(1, 1));
	panel.add(label);
	panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
	panel.setBackground(SystemColor.text);

	return panel;
    }
}
