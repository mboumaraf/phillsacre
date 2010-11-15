/*
 * Copyright © 2010 Clifford Thames Ltd.  All rights reserved.
 * Clifford Thames proprietary and confidential.  
 * Use is subject to license terms.
 */

package uk.me.phillsacre.lyricdisplay.presenter.ui.slide;

import java.awt.Graphics2D;
import java.awt.Rectangle;


/**
 * Static Text slide
 * 
 * @author psacre
 * @since 9 Nov 2010
 */
public class StaticTextSlide extends TextSlide
{
    private final String _text;


    public StaticTextSlide( String text )
    {
        _text = text;
    }

    @Override
    public int getPageCount()
    {
        return 1;
    }

    @Override
    public void render( Graphics2D g, Rectangle bounds, int pageNo )
    {
        String[] lines = _text.split( "<br/>" );
        for ( int i = 0; i < lines.length; i++ )
        {
            lines[ i ] = stripHtml( lines[ i ] );
        }

        float pointSize = getSize( g, bounds, lines );

        drawText( g, bounds, lines, pointSize );
    }
}
