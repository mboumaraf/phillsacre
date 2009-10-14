package uk.me.phillsacre.monopoly.game.squares;

import uk.me.phillsacre.monopoly.game.Player;
import uk.me.phillsacre.monopoly.game.squares.rent.RentStrategy;


public class PropertySquare extends GameSquare
{
    private Integer      _value;
    private Player       _owner;
    private SquareGroup  _group;
    private RentStrategy _rentStrategy;


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

    @Override
    public String toString()
    {
        return getName() + " (£" + getValue() + ")";
    }
}
