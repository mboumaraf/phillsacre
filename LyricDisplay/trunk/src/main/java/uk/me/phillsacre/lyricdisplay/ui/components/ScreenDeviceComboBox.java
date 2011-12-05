/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.components;

import java.awt.Component;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;

import uk.me.phillsacre.lyricdisplay.ui.models.ScreenDeviceModel;

/**
 * 
 * @author Phill
 * @since 4 Dec 2011
 */
public class ScreenDeviceComboBox extends JComboBox<GraphicsDevice>
{
    private static final long serialVersionUID = -7374822832352832309L;

    public ScreenDeviceComboBox()
    {
	final ScreenDeviceModel model = new ScreenDeviceModel();

	setModel(model);
	setRenderer(new ScreenDeviceRenderer());
    }

    private class ScreenDeviceRenderer extends DefaultListCellRenderer
    {
	private static final long serialVersionUID = -8150178353252066893L;

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value,
	        int index, boolean isSelected, boolean cellHasFocus)
	{
	    GraphicsDevice device = (GraphicsDevice) value;

	    DisplayMode displayMode = device.getDisplayMode();

	    String key = String.format("Resolution: %dx%d; %d bit",
		    displayMode.getWidth(), displayMode.getHeight(),
		    displayMode.getBitDepth());

	    return super.getListCellRendererComponent(list, key, index,
		    isSelected, cellHasFocus);
	}

    }
}
