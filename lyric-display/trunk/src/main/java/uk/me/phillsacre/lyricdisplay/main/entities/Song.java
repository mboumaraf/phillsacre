/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.entities;

/**
 * 
 * @author Phill
 * @since 30 Oct 2010
 */
public class Song
{
    private Integer _id;
    private String  _title;
    private String  _text;


    public Integer getId()
    {
        return _id;
    }

    public void setId( Integer id )
    {
        _id = id;
    }

    public String getTitle()
    {
        return _title;
    }

    public void setTitle( String title )
    {
        _title = title;
    }

    public String getText()
    {
        return _text;
    }

    public void setText( String text )
    {
        _text = text;
    }
}
