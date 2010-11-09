/*
 * Copyright © 2010 Clifford Thames Ltd.  All rights reserved.
 * Clifford Thames proprietary and confidential.  
 * Use is subject to license terms.
 */

package uk.me.phillsacre.lyricdisplay.presenter.ui.slide;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import uk.me.phillsacre.lyricdisplay.main.entities.Song;


/**
 * TODO Add type description
 * 
 * @author psacre
 * @since 9 Nov 2010
 */
public class SongSlide extends TextSlide
{
    private Song         _song;
    private int          _verse;
    private List<String> _verses;


    public SongSlide( Song song, int verse )
    {
        _song = song;
        _verse = verse;

        _verses = parseSong( song.getText() );
    }

    @Override
    public void render( Graphics2D g2d, Rectangle bounds )
    {
        float size = Float.MAX_VALUE;

        // Calculate text size across all verses, so that it will be consistent
        for ( String verse : _verses )
        {
            size = Math.min( size, getSize( g2d, bounds, verse.split( "\n" ) ) );
        }

        drawText( g2d, bounds, _verses.get( _verse ).split( "\n" ), size );
    }

    private static List<String> parseSong( String text )
    {
        String[] lines = text.split( "\n" );
        List<String> verses = new ArrayList<String>();

        StringBuilder verse = new StringBuilder();

        for ( String line : lines )
        {
            if (line.isEmpty())
            {
                verses.add( verse.toString() );
                verse = new StringBuilder();
            }
            else
            {
                verse.append( line ).append( "\n" );
            }
        }

        verses.add( verse.toString() );

        return verses;
    }
}
