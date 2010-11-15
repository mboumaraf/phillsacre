/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.presenter.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import uk.me.phillsacre.lyricdisplay.presenter.api.Presentation;
import uk.me.phillsacre.lyricdisplay.presenter.ui.slide.Slide;


/**
 * 
 * @author Phill
 * @since 7 Nov 2010
 */
public class PresentationPanel extends JPanel implements Presentation
{
    private static final long serialVersionUID = 6872606502011968882L;

    private Slide             _slide;
    private boolean           _blackout        = false;
    private BufferedImage     _buffer;
    private Timer             _timer;
    private TimerTask         _currentTask;
    private int               _availableWidth;
    private int               _availableHeight;
    private int               _pageNo;


    public PresentationPanel()
    {
        _timer = new Timer();

        setOpaque( true );

        addComponentListener( new ComponentAdapter()
        {
            @Override
            public void componentResized( ComponentEvent e )
            {
                _buffer = null;
                repaint();
            }
        } );
    }

    public void setSlide( Slide slide, int pageNo )
    {
        _slide = slide;
        _pageNo = pageNo;

        repaint();
    }

    public void setBlackout( boolean blackout )
    {
        _blackout = blackout;

        animateBlackout();
    }

    public void render( Graphics g )
    {
        renderToBuffer();

        if ( !_blackout)
        {
            if (null == g)
            {
                g = getGraphics();
            }
            g.drawImage( _buffer, 0, 0, null );
        }
    }

    private void renderToBuffer()
    {
        if (null == _buffer)
        {
            Graphics2D g2d = (Graphics2D) getGraphics();
            createBuffer( g2d );
        }

        Graphics2D g2d = (Graphics2D) _buffer.getGraphics();

        drawBackground( g2d );
        if (null != _slide)
        {
            _slide.render( g2d, getSlideBounds(), _pageNo );
        }
    }

    private Rectangle getSlideBounds()
    {
        int left = (getWidth() - _availableWidth) / 2;
        int top = (getHeight() - _availableHeight) / 2;

        Rectangle bounds = new Rectangle( left, top, _availableWidth, _availableHeight );

        return bounds;
    }

    private void createBuffer( Graphics2D g2d )
    {
        _buffer = g2d.getDeviceConfiguration().createCompatibleImage( getWidth(), getHeight() );

        int marginW = (int) Math.round( (float) getWidth() * 0.1 );
        int marginH = (int) Math.round( (float) getHeight() * 0.1 );

        _availableWidth = getWidth() - (marginW * 2);
        _availableHeight = getHeight() - (marginH * 2);
    }

    private static void copySrcIntoDstAt( final BufferedImage src, final BufferedImage dst, final int dx, final int dy )
    {
        int[] srcbuf = ((DataBufferInt) src.getRaster().getDataBuffer()).getData();
        int[] dstbuf = ((DataBufferInt) dst.getRaster().getDataBuffer()).getData();
        int width = src.getWidth();
        int height = src.getHeight();
        int dstoffs = dx + dy * dst.getWidth();
        int srcoffs = 0;
        for ( int y = 0; y < height; y++ , dstoffs += dst.getWidth(), srcoffs += width )
        {
            System.arraycopy( srcbuf, srcoffs, dstbuf, dstoffs, width );
        }
    }

    private void animateBlackout()
    {
        if (null != _currentTask)
        {
            _currentTask.cancel();
        }
        renderToBuffer();

        final BufferedImage original = new BufferedImage( _buffer.getWidth(), _buffer.getHeight(), _buffer.getType() );
        copySrcIntoDstAt( _buffer, original, 0, 0 );

        final float startAlpha = (_blackout) ? 0.0f : 1.0f;
        final float endAlpha = (_blackout) ? 1.0f : 0.0f;

        final float step = Math.abs( endAlpha - startAlpha ) / 40;

        final float[] current =
        {
            startAlpha
        };

        _currentTask = new TimerTask()
        {
            @Override
            public void run()
            {
                if (Math.abs( current[ 0 ] - endAlpha ) < 0.01)
                {
                    cancel();
                    _currentTask = null;
                    return;
                }
                Graphics2D g2d = _buffer.createGraphics();
                g2d.setColor( Color.BLACK );

                g2d.drawImage( original, 0, 0, null );

                AlphaComposite ac = AlphaComposite.getInstance( AlphaComposite.SRC_OVER, current[ 0 ] );

                g2d.setComposite( ac );

                g2d.fillRect( 0, 0, getWidth(), getHeight() );

                getGraphics().drawImage( _buffer, 0, 0, null );

                current[ 0 ] = (startAlpha > endAlpha) ? current[ 0 ] - step : current[ 0 ] + step;

                g2d.dispose();
            }
        };

        _timer.scheduleAtFixedRate( _currentTask, 0, 20 );
    }

    private void drawBackground( Graphics2D g2d )
    {
        Graphics2D g = (Graphics2D) g2d.create();
        GradientPaint paint = new GradientPaint( 0, 0, Color.BLUE, 0, getHeight(), Color.BLACK, true );
        g.setPaint( paint );

        g.fillRect( 0, 0, getWidth(), getHeight() );
        g.dispose();
    }

    @Override
    public void paint( Graphics g )
    {
        render( g );
    }

    @Override
    protected void paintComponent( Graphics g )
    {
        super.paintComponent( g );
        render( g );
    }
}
