package uk.org.fordhamchurch.uploader.entities;

public class Speaker
{
    private Long   _id;
    private String _name;


    public Speaker( Long id, String name )
    {
        _id = id;
        _name = name;
    }

    /**
     * @return the id
     */
    public Long getId()
    {
        return _id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId( Long id )
    {
        _id = id;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return _name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName( String name )
    {
        _name = name;
    }

    @Override
    public String toString()
    {
        return _name;
    }
}
