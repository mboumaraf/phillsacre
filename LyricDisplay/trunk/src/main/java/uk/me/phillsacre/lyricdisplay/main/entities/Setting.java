/**
 * 
 */
package uk.me.phillsacre.lyricdisplay.main.entities;

import java.io.Serializable;

/**
 * 
 * @author Phill
 * @since 29 Nov 2011
 */
public class Setting implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer           _id;
    private String            _name;
    private String            _value;

    public Integer getId()
    {
	return _id;
    }

    public void setId(Integer id)
    {
	_id = id;
    }

    public String getName()
    {
	return _name;
    }

    public void setName(String name)
    {
	_name = name;
    }

    public String getValue()
    {
	return _value;
    }

    public void setValue(String value)
    {
	_value = value;
    }
}
