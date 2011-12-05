/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.ui.presentation;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComponent;

/**
 * 
 * @author Phill
 * @since 28 Nov 2011
 */
public class BasePresentationComponent extends JComponent
{
    private static final long serialVersionUID = 7686617521758348824L;

    private BufferedImage     _buffer;
    private DisplayPanel      _displayPanel;
    private Timer             _timer;
    private TimerTask         _currentTask;
    private boolean           _blackout;

    public BasePresentationComponent(boolean blackout)
    {
	_blackout = blackout;
	_timer = new Timer();

	setOpaque(true);

	addComponentListener(new ComponentAdapter() {
	    @Override
	    public void componentResized(ComponentEvent e)
	    {
		_buffer = null;
		repaint();
	    }
	});

    }

    public void setBlackout(boolean blackout)
    {
	_blackout = blackout;

	animateBlackout();
    }

    public void setDisplayPanel(DisplayPanel displayPanel)
    {
	_displayPanel = displayPanel;

	repaint();
    }

    public void render(Graphics g)
    {
	renderToBuffer();

	if (!_blackout)
	{
	    if (null == g)
	    {
		g = getGraphics();
	    }

	    g.drawImage(_buffer, 0, 0, null);
	}
	else if (_currentTask == null)
	{
	    g.setColor(Color.black);
	    g.fillRect(0, 0, getWidth(), getHeight());
	}
    }

    private BufferedImage getBuffer()
    {
	if (null == _buffer)
	{
	    Graphics2D g2d = (Graphics2D) getGraphics();
	    createBuffer(g2d);
	}

	return _buffer;
    }

    private void renderToBuffer()
    {
	final BufferedImage buffer = getBuffer();

	Graphics2D g2d = (Graphics2D) buffer.getGraphics();

	if (null != _displayPanel)
	{
	    _displayPanel.render(g2d, getWidth(), getHeight());
	}
    }

    private void createBuffer(Graphics2D g2d)
    {
	_buffer = g2d.getDeviceConfiguration().createCompatibleImage(
	        getWidth(), getHeight());
    }

    private void animateBlackout()
    {
	if (null != _currentTask)
	{
	    _currentTask.cancel();
	}
	renderToBuffer();

	final BufferedImage buffer = getBuffer();
	final BufferedImage original = new BufferedImage(buffer.getWidth(),
	        buffer.getHeight(), buffer.getType());
	copySrcIntoDstAt(buffer, original, 0, 0);

	final float startAlpha = (_blackout) ? 0.0f : 1.0f;
	final float endAlpha = (_blackout) ? 1.0f : 0.0f;

	final float step = Math.abs(endAlpha - startAlpha) / 40;

	final float[] current = { startAlpha };

	_currentTask = new TimerTask() {
	    @Override
	    public void run()
	    {
		if (Math.abs(current[0] - endAlpha) < 0.01)
		{
		    cancel();
		    _currentTask = null;
		    return;
		}
		final BufferedImage buffer = getBuffer();
		Graphics2D g2d = buffer.createGraphics();
		g2d.setColor(Color.BLACK);

		g2d.drawImage(original, 0, 0, null);

		AlphaComposite ac = AlphaComposite.getInstance(
		        AlphaComposite.SRC_OVER, current[0]);

		g2d.setComposite(ac);

		g2d.fillRect(0, 0, getWidth(), getHeight());

		getGraphics().drawImage(buffer, 0, 0, null);

		current[0] = (startAlpha > endAlpha) ? current[0] - step
		        : current[0] + step;

		g2d.dispose();
	    }
	};

	_timer.scheduleAtFixedRate(_currentTask, 0, 20);
    }

    private static void copySrcIntoDstAt(final BufferedImage src,
	    final BufferedImage dst, final int dx, final int dy)
    {
	int[] srcbuf = ((DataBufferInt) src.getRaster().getDataBuffer())
	        .getData();
	int[] dstbuf = ((DataBufferInt) dst.getRaster().getDataBuffer())
	        .getData();
	int width = src.getWidth();
	int height = src.getHeight();
	int dstoffs = dx + dy * dst.getWidth();
	int srcoffs = 0;
	for (int y = 0; y < height; y++, dstoffs += dst.getWidth(), srcoffs += width)
	{
	    System.arraycopy(srcbuf, srcoffs, dstbuf, dstoffs, width);
	}
    }

    @Override
    public void paint(Graphics g)
    {
	render(g);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
	super.paintComponent(g);
	render(g);
    }

}
