package uk.org.fordhamchurch.uploader.gui.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import uk.org.fordhamchurch.uploader.dao.FordhamDAO;
import uk.org.fordhamchurch.uploader.entities.Book;


@SuppressWarnings( "serial" )
public class BibleBooksModel extends AbstractListModel implements ComboBoxModel
{
    private Book       _selected;
    private List<Book> _books;


    public BibleBooksModel()
    {
        _books = new ArrayList<Book>();

        loadBooks();
    }

    public Object getSelectedItem()
    {
        return _selected;
    }

    public void setSelectedItem( Object anItem )
    {
        _selected = (Book)anItem;
    }

    public Object getElementAt( int index )
    {
        return _books.get( index );
    }

    public int getSize()
    {
        return _books.size();
    }

    /* === Private Methods ==================================== */

    /**
     * Load books from the DAO asynchronously.
     */
    private void loadBooks()
    {
        Runnable r = new Runnable()
        {
            public void run()
            {
                List<Book> books = FordhamDAO.getInstance().getBooks();
                _books.clear();
                _books.addAll( books );

                fireContentsChanged( BibleBooksModel.this, 0, _books.size() );
            }
        };

        Thread t = new Thread( r );
        t.start();
    }

}
