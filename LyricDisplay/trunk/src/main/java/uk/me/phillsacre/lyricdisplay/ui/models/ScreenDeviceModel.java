/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.models;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

/**
 * 
 * @author Phill
 * @since 3 Dec 2011
 */
public class ScreenDeviceModel implements ComboBoxModel<GraphicsDevice>
{
    private List<GraphicsDevice> _devices;
    private GraphicsDevice       _selected;

    public ScreenDeviceModel()
    {
	_devices = new ArrayList<GraphicsDevice>();

	GraphicsEnvironment ge = GraphicsEnvironment
	        .getLocalGraphicsEnvironment();
	try
	{
	    GraphicsDevice[] devices = ge.getScreenDevices();
	    GraphicsDevice defaultDevice = ge.getDefaultScreenDevice();

	    for (GraphicsDevice device : devices)
	    {
		_devices.add(device);

		if (!device.equals(defaultDevice))
		{
		    setSelectedItem(device);
		}
	    }

	    if (_devices.size() == 1)
	    {
		setSelectedItem(_devices.get(0));
	    }
	}
	catch (Exception e)
	{
	    throw new RuntimeException(e);
	}
    }

    @Override
    public int getSize()
    {
	return _devices.size();
    }

    @Override
    public GraphicsDevice getElementAt(int index)
    {
	return _devices.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l)
    {
	// TODO Implement
    }

    @Override
    public void removeListDataListener(ListDataListener l)
    {
	// TODO Implement
    }

    @Override
    public void setSelectedItem(Object anItem)
    {
	_selected = (GraphicsDevice) anItem;
    }

    @Override
    public Object getSelectedItem()
    {
	return _selected;
    }
}
