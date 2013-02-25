package uk.org.fordhamchurch.uploader.gui.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import uk.org.fordhamchurch.uploader.entities.Speaker;


@SuppressWarnings( "serial" )
public class SpeakersModel extends AbstractListModel implements ComboBoxModel
{
    private List<Speaker> _speakers;
    private Speaker       _selected;


    public SpeakersModel()
    {
        _speakers = new ArrayList<Speaker>();
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
        _selected = (Speaker) anItem;
    }

    public void addAll( List<Speaker> speakers )
    {
        _speakers.addAll( speakers );
        Collections.sort( _speakers );

        fireContentsChanged( this, 0, _speakers.size() );
    }
}
