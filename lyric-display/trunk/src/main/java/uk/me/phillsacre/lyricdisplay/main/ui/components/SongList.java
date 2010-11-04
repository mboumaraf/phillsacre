/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.ui.components;

import javax.swing.JList;

import uk.me.phillsacre.lyricdisplay.main.ui.models.SongListModel;

/**
 * 
 * @author Phill
 * @since 31 Oct 2010
 */
public class SongList extends JList
{
    public SongList()
    {
	setModel(new SongListModel());
    }
}
