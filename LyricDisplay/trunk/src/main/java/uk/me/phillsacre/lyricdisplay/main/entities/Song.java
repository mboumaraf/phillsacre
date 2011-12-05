/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.entities;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * 
 * @author Phill
 * @since 29 Nov 2011
 */
public class Song
{
    private Integer               _id;
    private String                _title;
    private String                _author;
    private String                _publisher;
    private int                   _year;
    private String                _copyright;
    private String                _text;

    private PropertyChangeSupport _changeSupport;

    public Song()
    {
	_changeSupport = new PropertyChangeSupport(this);
    }

    public Song(Song song)
    {
	this();

	_id = song.getId();
	_title = song.getTitle();
	_publisher = song.getPublisher();
	_year = song.getYear();
	_copyright = song.getCopyright();
	_author = song.getAuthor();
	_text = song.getText();
    }

    public void addPropertyChangeListener(PropertyChangeListener l)
    {
	_changeSupport.addPropertyChangeListener(l);
    }

    public void addPropertyChangeListener(String propertyName,
	    PropertyChangeListener l)
    {
	_changeSupport.addPropertyChangeListener(propertyName, l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l)
    {
	_changeSupport.removePropertyChangeListener(l);
    }

    public void removePropertyChangeListener(String propertyName,
	    PropertyChangeListener l)
    {
	_changeSupport.removePropertyChangeListener(propertyName, l);
    }

    public Integer getId()
    {
	return _id;
    }

    public void setId(Integer id)
    {
	_id = id;
    }

    public String getTitle()
    {
	return _title;
    }

    public void setTitle(String title)
    {
	String oldValue = _title;
	_title = title;

	_changeSupport.firePropertyChange("title", oldValue, title);
    }

    public String getText()
    {
	return _text;
    }

    public void setText(String text)
    {
	String oldValue = _text;
	_text = text;

	_changeSupport.firePropertyChange("text", oldValue, _text);

    }

    public boolean equals(Object o)
    {
	if (null == o || !(o instanceof Song))
	{
	    return false;
	}

	if (o == this)
	{
	    return true;
	}

	Song s = (Song) o;

	if (_id == null || s.getId() == null)
	{
	    return false;
	}

	return _id.equals(s.getId());
    }

    public int hashCode()
    {
	return _id == null ? super.hashCode() : _id.hashCode();
    }

    public String getAuthor()
    {
	return _author;
    }

    public void setAuthor(String author)
    {
	String oldValue = _author;
	_author = author;

	_changeSupport.firePropertyChange("author", oldValue, _author);

    }

    public String getPublisher()
    {
	return _publisher;
    }

    public void setPublisher(String publisher)
    {
	String oldValue = _title;
	_publisher = publisher;

	_changeSupport.firePropertyChange("publisher", oldValue, _publisher);

    }

    public int getYear()
    {
	return _year;
    }

    public void setYear(int year)
    {
	int oldYear = _year;
	_year = year;

	_changeSupport.firePropertyChange("year", oldYear, _year);

    }

    public String getCopyright()
    {
	return _copyright;
    }

    public void setCopyright(String copyright)
    {
	String oldValue = _copyright;
	_copyright = copyright;

	_changeSupport.firePropertyChange("copyright", oldValue, _copyright);

    }
}
