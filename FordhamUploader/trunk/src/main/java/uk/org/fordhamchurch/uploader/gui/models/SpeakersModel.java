package uk.org.fordhamchurch.uploader.gui.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import uk.org.fordhamchurch.uploader.dao.FordhamDAO;
import uk.org.fordhamchurch.uploader.entities.Speaker;


@SuppressWarnings( "serial" )
public class SpeakersModel extends AbstractListModel implements ComboBoxModel
{
    private List<Speaker> _speakers;
    private Speaker       _selected;


    public SpeakersModel()
    {
        _speakers = new ArrayList<Speaker>();

        loadSpeakers();
    }

    public Object getElementAt( int index )
    {
        return _speakers.get( index );
    }

    public int getSize()
    {
        return _speakers.size();
    }

    public Object getSelectedItem()
    {
        return _selected;
    }

    public void setSelectedItem( Object anItem )
    {
        _selected = (Speaker)anItem;
    }

    /* === Private Methods ==================================== */

    /**
     * Load books from the DAO asynchronously.
     */
    private void loadSpeakers()
    {
        Runnable r = new Runnable()
        {
            public void run()
            {
                List<Speaker> books = FordhamDAO.getInstance().getSpeakers();
                _speakers.clear();
                _speakers.addAll( books );

                fireContentsChanged( SpeakersModel.this, 0, _speakers.size() );
            }
        };

        Thread t = new Thread( r );
        t.start();
    }
}