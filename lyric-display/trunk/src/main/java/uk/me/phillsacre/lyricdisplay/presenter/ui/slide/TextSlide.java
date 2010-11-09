/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.presenter.ui.slide;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;


/**
 * 
 * @author Phill
 * @since 27 Oct 2010
 */
public abstract class TextSlide implements Slide
{
    @Override
    public Type getType()
    {
        return Type.TEXT;
    }

    protected String stripHtml( String text )
    {
        return text.replaceAll( "</?html>", "" );
    }

    protected Font getFont()
    {
        return new Font( "Helvetica", Font.PLAIN, 12 );
    }

    protected void drawText( Graphics2D g2d, Rectangle bounds, String[] lines, float pointSize )
    {
        Graphics2D g = (Graphics2D) g2d.create();

        Font f = getFont();
        f = f.deriveFont( pointSize );

        FontMetrics metrics = g.getFontMetrics( f );

        int width = 0;
        for ( String line : lines )
        {
            int w = metrics.charsWidth( line.toCharArray(), 0, line.length() );
            width = Math.max( width, w );
        }
        int lineHeight = metrics.getHeight();

        int left = (int) bounds.getX();
        int top = (int) bounds.getY() + lineHeight;

        g.setFont( f );
        g.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );

        for ( int i = 0; i < lines.length; i++ )
        {
            String line = lines[ i ];

            int t = top + (lineHeight * i);

            g.setColor( Color.BLACK );
            g.drawString( line, left + 3, t + 3 );
            g.setColor( Color.WHITE );
            g.drawString( line, left, t );
        }

        g.dispose();
    }

    /**
     * Calculate the maximum point size that text can be in order to fit on the screen.
     * 
     * @param f The font to use
     * @param lines The lines of text to display on screen
     * @return
     */
    protected float getSize( Graphics2D g2d, Rectangle bounds, String[] lines )
    {
        Font f = getFont();
        
        // TODO: This call always takes about 1 second the first time it is run.
        // Run this on intialisation instead.
        FontMetrics fm = g2d.getFontMetrics( f );

        int maxWidth = 0;
        for ( String line : lines )
        {
            maxWidth = Math.max( maxWidth, fm.stringWidth( line ) );
        }

        int maxHeight = lines.length * fm.getHeight();

        float scaleFactorW = (float) bounds.getWidth() / (float) maxWidth;
        float scaleFactorH = (float) bounds.getHeight() / (float) maxHeight;

        float scaleFactor = Math.min( scaleFactorW, scaleFactorH );

        int fontSize = f.getSize();

        return fontSize * scaleFactor;
    }
}
