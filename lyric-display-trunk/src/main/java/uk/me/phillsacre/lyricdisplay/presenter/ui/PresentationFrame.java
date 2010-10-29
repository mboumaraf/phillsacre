/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.presenter.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.bushe.swing.event.EventBus;

import uk.me.phillsacre.lyricdisplay.presenter.events.ChangePresentationStateEvent;
import uk.me.phillsacre.lyricdisplay.presenter.events.ChangePresentationStateEvent.State;
import uk.me.phillsacre.lyricdisplay.presenter.ui.slide.Slide;

/**
 * 
 * @author Phill
 * @since 25 Oct 2010
 */
public class PresentationFrame extends JFrame
{
    private static final long serialVersionUID = -7637114333874163665L;

    private Slide             _slide;
    private boolean           _blackout        = false;
    private BufferedImage     _buffer;
    private Timer             _timer;
    private TimerTask         _currentTask;

    public PresentationFrame()
    {
	_timer = new Timer();

	// setAlwaysOnTop(true);
	setSize(800, 600);
	setUndecorated(true);
	setIgnoreRepaint(true);

	setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	addWindowListener(new WindowAdapter() {
	    @Override
	    public void windowClosing(WindowEvent e)
	    {
		ChangePresentationStateEvent event = new ChangePresentationStateEvent(
		        State.DISABLED);
		EventBus.publish(event);
	    }
	});
    }

    public void setSlide(Slide slide)
    {
	_slide = slide;

	render();
    }

    public void setBlackout(boolean blackout)
    {
	_blackout = blackout;

	animateBlackout();
    }

    public void render()
    {
	renderToBuffer();

	if (!_blackout)
	{
	    getGraphics().drawImage(_buffer, 0, 0, null);
	}
    }

    private void renderToBuffer()
    {
	if (null == _buffer)
	{
	    Graphics2D g2d = (Graphics2D) getGraphics();
	    createBuffer(g2d);
	}

	Graphics2D g2d = (Graphics2D) _buffer.getGraphics();

	drawBackground(g2d);
	if (null != _slide)
	{
	    drawText(g2d, _slide.getText());
	}
    }

    private void createBuffer(Graphics2D g2d)
    {
	_buffer = g2d.getDeviceConfiguration().createCompatibleImage(
	        getWidth(), getHeight());
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

    private void animateBlackout()
    {
	if (null != _currentTask)
	{
	    _currentTask.cancel();
	}
	renderToBuffer();

	final BufferedImage original = new BufferedImage(_buffer.getWidth(),
	        _buffer.getHeight(), _buffer.getType());
	copySrcIntoDstAt(_buffer, original, 0, 0);

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
		Graphics2D g2d = _buffer.createGraphics();
		g2d.setColor(Color.BLACK);

		g2d.drawImage(original, 0, 0, null);

		AlphaComposite ac = AlphaComposite.getInstance(
		        AlphaComposite.SRC_OVER, current[0]);

		g2d.setComposite(ac);

		g2d.fillRect(0, 0, getWidth(), getHeight());

		getGraphics().drawImage(_buffer, 0, 0, null);

		current[0] = (startAlpha > endAlpha) ? current[0] - step
		        : current[0] + step;

		g2d.dispose();
	    }
	};

	_timer.scheduleAtFixedRate(_currentTask, 0, 20);
    }

    private void drawBackground(Graphics2D g2d)
    {
	Graphics2D g = (Graphics2D) g2d.create();
	GradientPaint paint = new GradientPaint(0, 0, Color.BLUE, 0,
	        getHeight(), Color.BLACK, true);
	g.setPaint(paint);

	g.fillRect(0, 0, getWidth(), getHeight());
	g.dispose();
    }

    private void drawText(Graphics2D g2d, String text)
    {
	String[] lines = text.split("\n");

	Graphics2D g = (Graphics2D) g2d.create();

	Font f = new Font("Helvetica", Font.PLAIN, 12);
	int pointSize = getSize(f, lines);
	f = f.deriveFont((float) pointSize);

	FontMetrics metrics = g.getFontMetrics(f);

	int width = 0;
	for (String line : lines)
	{
	    int w = metrics.charsWidth(line.toCharArray(), 0, line.length());
	    width = Math.max(width, w);
	}
	int lineHeight = metrics.getHeight();
	int height = lineHeight * (lines.length - 1);

	int left = (getWidth() - width) / 2;
	int top = (getHeight() - height) / 2;

	g.setFont(f);
	g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
	        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

	for (int i = 0; i < lines.length; i++)
	{
	    String line = lines[i];

	    int t = top + (lineHeight * i);

	    g.setColor(Color.BLACK);
	    g.drawString(line, left + 3, t + 3);
	    g.setColor(Color.WHITE);
	    g.drawString(line, left, t);
	}

	g.dispose();
    }

    /**
     * Calculate the maximum point size that text can be in order to fit on the
     * screen.
     * 
     * @param f
     *            The font to use
     * @param lines
     *            The lines of text to display on screen
     * @return
     */
    private int getSize(Font f, String[] lines)
    {
	FontMetrics fm = getGraphics().getFontMetrics(f);

	int maxWidth = 0;
	for (String line : lines)
	{
	    maxWidth = Math.max(maxWidth, fm.stringWidth(line));
	}

	int maxHeight = lines.length * fm.getHeight();

	int marginW = (int) Math.round((float) getWidth() * 0.1);
	int marginH = (int) Math.round((float) getHeight() * 0.1);

	int availableWidth = getWidth() - (marginW * 2);
	int availableHeight = getHeight() - (marginH * 2);

	float scaleFactorW = (float) availableWidth / (float) maxWidth;
	float scaleFactorH = (float) availableHeight / (float) maxHeight;

	float scaleFactor = Math.min(scaleFactorW, scaleFactorH);

	int fontSize = f.getSize();

	return (int) Math.round(fontSize * scaleFactor);
    }

    @Override
    public void paint(Graphics g)
    {
	render();
    }
}
