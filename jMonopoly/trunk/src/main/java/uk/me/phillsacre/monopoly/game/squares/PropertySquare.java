package uk.me.phillsacre.monopoly.game.squares;

import uk.me.phillsacre.monopoly.game.Player;
import uk.me.phillsacre.monopoly.game.squares.rent.RentStrategy;


public class PropertySquare extends GameSquare
{
    private Integer      _value;
    private Player       _owner;
    private SquareGroup  _group;
    private RentStrategy _rentStrategy;
    private Integer      _numHouses;
    private Integer      _numHotels;


    /**
     * @return the value
     */
    public Integer getValue()
    {
        return _value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue( Integer value )
    {
        _value = value;
    }


    /**
     * @return the owner
     */
    public Player getOwner()
    {
        return _owner;
    }

    /**
     * @param owner
     *            the owner to set
     */
    public void setOwner( Player owner )
    {
        _owner = owner;
    }

    /**
     * @return the group
     */
    public SquareGroup getGroup()
    {
        return _group;
    }

    /**
     * @param group
     *            the group to set
     */
    public void setGroup( SquareGroup group )
    {
        _group = group;
    }

    /**
     * @param rentStrategy
     *            the rentStrategy to set
     */
    public void setRentStrategy( RentStrategy rentStrategy )
    {
        _rentStrategy = rentStrategy;
    }

    /**
     * @return the rentStrategy
     */
    public RentStrategy getRentStrategy()
    {
        return _rentStrategy;
    }

    /**
     * @return the numHouses
     */
    public Integer getNumHouses()
    {
        return _numHouses;
    }

    /**
     * @param numHouses
     *            the numHouses to set
     */
    public void setNumHouses( Integer numHouses )
    {
        _numHouses = numHouses;
    }

    /**
     * @return the numHotels
     */
    public Integer getNumHotels()
    {
        return _numHotels;
    }

    /**
     * @param numHotels
     *            the numHotels to set
     */
    public void setNumHotels( Integer numHotels )
    {
        _numHotels = numHotels;
    }

    @Override
    public String toString()
    {
        return getName() + " (£" + getValue() + ")";
    }
}
