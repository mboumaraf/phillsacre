/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.ui.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import uk.me.phillsacre.lyricdisplay.main.entities.Song;
import uk.me.phillsacre.lyricdisplay.main.events.SelectSongEvent;

/**
 * 
 * @author Phill
 * @since 6 Nov 2010
 */
public class SongInfoListModel extends AbstractListModel
{
    private static final long serialVersionUID = -7475366613550379719L;
    
    private Song         _song;
    private List<String> _verses;

    public SongInfoListModel()
    {
	EventBus.subscribeStrongly(SelectSongEvent.class,
	        new EventSubscriber<SelectSongEvent>() {
		    @Override
		    public void onEvent(SelectSongEvent event)
		    {
		        updateSong(event.getSong());
		    }
	        });
    }

    @Override
    public Object getElementAt(int index)
    {
	return _verses.get(index);
    }

    @Override
    public int getSize()
    {
	return _verses == null ? 0 : _verses.size();
    }

    private void updateSong(Song song)
    {
	_song = song;

	String text = song.getText();
	_verses = parseSong(text);

	fireContentsChanged(this, 0, _verses.size());
    }

    private static List<String> parseSong(String text)
    {
	String[] lines = text.split("\n");
	List<String> verses = new ArrayList<String>();

	StringBuilder verse = new StringBuilder();
	verse.append("<html>");

	for (String line : lines)
	{
	    if (line.isEmpty())
	    {
		verse.append("</html>");
		verses.add(verse.toString());
		verse = new StringBuilder("<html>");
	    }
	    else
	    {
		verse.append(line).append("<br/>");
	    }
	}

	verses.add(verse.toString());

	return verses;
    }
}
