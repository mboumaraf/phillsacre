/*
 * Copyright © 2010 Clifford Thames Ltd.  All rights reserved.
 * Clifford Thames proprietary and confidential.  
 * Use is subject to license terms.
 */

package uk.me.phillsacre.lyricdisplay.presenter.ui.slide;

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
    private List<String> _verses;

    public SongSlide(Song song)
    {
	_verses = parseSong(song.getText());
    }

    public int getPageCount()
    {
	return _verses == null ? 0 : _verses.size();
    }

    @Override
    public void render(Graphics2D g2d, Rectangle bounds, int pageNo)
    {
	float size = Float.MAX_VALUE;

	// Calculate text size across all verses, so that it will be consistent
	for (String verse : _verses)
	{
	    size = Math.min(size, getSize(g2d, bounds, verse.split("\n")));
	}

	String verse = _verses.get(pageNo);
	verse = verse.replaceAll("\\b(Verse|Chorus) ?[\\d]*\\n", "");
	drawText(g2d, bounds, verse.split("\n"), size);
    }

    @Override
    public String getText(int pageNo)
    {
	String verse = _verses.get(pageNo);

	verse = "<html>" + verse + "</html>";
	verse = verse.replaceAll("\n", "<br/>");
	verse = verse.replaceAll("(Verse ?[\\d]*\\b)",
	        "<span style=\"color:blue;font-weight:bold;\">$1</span>");
	verse = verse.replaceAll("(Chorus ?[\\d]*\\b)",
	        "<span style=\"color:maroon;font-weight:bold;\">$1</span>");

	return verse;
    }

    private static List<String> parseSong(String text)
    {
	String[] lines = text.split("\n");
	List<String> verses = new ArrayList<String>();

	StringBuilder verse = new StringBuilder();

	for (String line : lines)
	{
	    if (line.isEmpty())
	    {
		verses.add(verse.toString());
		verse = new StringBuilder();
	    }
	    else
	    {
		verse.append(line).append("\n");
	    }
	}

	verses.add(verse.toString());

	return verses;
    }
}
