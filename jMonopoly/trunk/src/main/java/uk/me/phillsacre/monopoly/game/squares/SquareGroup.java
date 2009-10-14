package uk.me.phillsacre.monopoly.game.squares;

import java.util.ArrayList;
import java.util.List;


public class SquareGroup
{
    private String               _id;
    private String               _colour;
    private List<PropertySquare> _squares;


    public SquareGroup( String id )
    {
        _id = id;
        _squares = new ArrayList<PropertySquare>();
    }

    public String getId()
    {
        return _id;
    }

    /**
     * @return the colour
     */
    public String getColour()
    {
        return _colour;
    }

    /**
     * @param colour
     *            the colour to set
     */
    public void setColour( String colour )
    {
        _colour = colour;
    }

    public List<PropertySquare> getSquares()
    {
        return _squares;
    }
}
