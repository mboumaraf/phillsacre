package uk.org.fordhamchurch.uploader.gui.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import uk.org.fordhamchurch.uploader.entities.Book;


@SuppressWarnings( "serial" )
public class BibleBooksModel extends AbstractListModel implements ComboBoxModel
{
    private Book       _selected;
    private List<Book> _books;


    public BibleBooksModel()
    {
        _books = new ArrayList<Book>();
    }

    public Object getSelectedItem()
    {
        return _selected;
    }

    public void setSelectedItem( Object anItem )
    {
        _selected = (Book) anItem;
    }

    public Object getElementAt( int index )
    {
        return _books.get( index );
    }

    public int getSize()
    {
        return _books.size();
    }

    public void addAll( List<Book> books )
    {
        _books.addAll( books );

        fireContentsChanged( this, 0, _books.size() );
    }
}
