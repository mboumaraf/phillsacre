package uk.org.fordhamchurch.uploader.entities;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

import uk.org.fordhamchurch.uploader.gui.models.CDDriveModel.Drive;


public class Upload
{
    private PropertyChangeSupport _changeSupport;

    private File                  _file;
    private Drive                 _cdLocation;
    private String                _title;
    private Speaker               _speaker;
    private Book                  _book;
    private String                _chapter;
    private String                _date;


    public Upload()
    {
        _changeSupport = new PropertyChangeSupport( this );
    }

    public void addPropertyChangeListener( PropertyChangeListener l )
    {
        _changeSupport.addPropertyChangeListener( l );
    }

    public void addPropertyChangeListener( String propertyName, PropertyChangeListener l )
    {
        _changeSupport.addPropertyChangeListener( propertyName, l );
    }

    public void removePropertyChangeListener( PropertyChangeListener l )
    {
        _changeSupport.removePropertyChangeListener( l );
    }

    public void removePropertyChangeListener( String propertyName, PropertyChangeListener l )
    {
        _changeSupport.removePropertyChangeListener( propertyName, l );
    }

    /**
     * @return the cdLocation
     */
    public Drive getCdLocation()
    {
        return _cdLocation;
    }

    /**
     * @param cdLocation the cdLocation to set
     */
    public void setCdLocation( Drive cdLocation )
    {
        _cdLocation = cdLocation;
    }

    /**
     * @return the title
     */
    public String getTitle()
    {
        return _title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle( String title )
    {
        _title = title;

        _changeSupport.firePropertyChange( "title", null, title );
    }

    /**
     * @return the speaker
     */
    public Speaker getSpeaker()
    {
        return _speaker;
    }

    /**
     * @param speaker the speaker to set
     */
    public void setSpeaker( Speaker speaker )
    {
        _speaker = speaker;

        _changeSupport.firePropertyChange( "speaker", null, speaker );
    }

    /**
     * @return the book
     */
    public Book getBook()
    {
        return _book;
    }

    /**
     * @param book the book to set
     */
    public void setBook( Book book )
    {
        _book = book;

        _changeSupport.firePropertyChange( "book", null, book );
    }

    /**
     * @return the chapter
     */
    public String getChapter()
    {
        return _chapter;
    }

    /**
     * @param chapter the chapter to set
     */
    public void setChapter( String chapter )
    {
        _chapter = chapter;

        _changeSupport.firePropertyChange( "chapter", null, chapter );
    }

    /**
     * @return the date
     */
    public String getDate()
    {
        return _date;
    }

    /**
     * @param date the date to set
     */
    public void setDate( String date )
    {
        _date = date;

        _changeSupport.firePropertyChange( "date", null, date );
    }

    public File getFile()
    {
        return _file;
    }

    public void setFile( File file )
    {
        _file = file;

        _changeSupport.firePropertyChange( "file", null, file );
    }

}
