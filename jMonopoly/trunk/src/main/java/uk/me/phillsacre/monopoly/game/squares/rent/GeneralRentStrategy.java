package uk.me.phillsacre.monopoly.game.squares.rent;

import uk.me.phillsacre.monopoly.game.Player;
import uk.me.phillsacre.monopoly.game.squares.PropertySquare;
import uk.me.phillsacre.monopoly.utils.DiceRoll;


public class GeneralRentStrategy implements RentStrategy
{
    private Integer _baseValue;
    private Integer _oneHouse;
    private Integer _twoHouses;
    private Integer _threeHouses;
    private Integer _fourHouses;
    private Integer _hotel;


    public GeneralRentStrategy( Integer baseValue,
                                Integer oneHouse,
                                Integer twoHouses,
                                Integer threeHouses,
                                Integer fourHouses,
                                Integer hotel )
    {
        _baseValue = baseValue;
        _oneHouse = oneHouse;
        _twoHouses = twoHouses;
        _threeHouses = threeHouses;
        _fourHouses = fourHouses;
        _hotel = hotel;
    }

    /**
     * @param player
     * @param roll
     */
    @Override
    public Integer calculateRent( PropertySquare property, Player player, DiceRoll roll )
    {
        // Assume that player does not own the property, as this will have already been checked.

        Player owner = property.getOwner();
        boolean ownsAll = true;

        if (property.getNumHotels() > 0)
        {
            // Return Hotel rate
            return _hotel;
        }
        if (property.getNumHouses() > 0)
        {
            switch (property.getNumHouses())
            {
                case 1:
                    return _oneHouse;
                case 2:
                    return _twoHouses;
                case 3:
                    return _threeHouses;
                case 4:
                    return _fourHouses;
                default:
                    throw new RuntimeException( "Wrong number of houses (" + property.getNumHouses() + ")" );
            }
        }

        // Check whether owner owns all houses in group. If so, base rent is doubled.
        for (PropertySquare sibling : property.getGroup().getSquares())
        {
            if ( !sibling.getOwner().equals( owner ))
            {
                ownsAll = false;
                break;
            }
        }

        if (ownsAll)
        {
            // Base rent is doubled.
            return _baseValue * 2;
        }

        return _baseValue;
    }

    /**
     * @return the baseValue
     */
    public Integer getBaseValue()
    {
        return _baseValue;
    }

    /**
     * @param baseValue
     *            the baseValue to set
     */
    public void setBaseValue( Integer baseValue )
    {
        _baseValue = baseValue;
    }

    /**
     * @return the oneHouse
     */
    public Integer getOneHouse()
    {
        return _oneHouse;
    }

    /**
     * @param oneHouse
     *            the oneHouse to set
     */
    public void setOneHouse( Integer oneHouse )
    {
        _oneHouse = oneHouse;
    }

    /**
     * @return the twoHouses
     */
    public Integer getTwoHouses()
    {
        return _twoHouses;
    }

    /**
     * @param twoHouses
     *            the twoHouses to set
     */
    public void setTwoHouses( Integer twoHouses )
    {
        _twoHouses = twoHouses;
    }

    /**
     * @return the threeHouses
     */
    public Integer getThreeHouses()
    {
        return _threeHouses;
    }

    /**
     * @param threeHouses
     *            the threeHouses to set
     */
    public void setThreeHouses( Integer threeHouses )
    {
        _threeHouses = threeHouses;
    }

    /**
     * @return the fourHouses
     */
    public Integer getFourHouses()
    {
        return _fourHouses;
    }

    /**
     * @param fourHouses
     *            the fourHouses to set
     */
    public void setFourHouses( Integer fourHouses )
    {
        _fourHouses = fourHouses;
    }

    /**
     * @return the hotel
     */
    public Integer getHotel()
    {
        return _hotel;
    }

    /**
     * @param hotel
     *            the hotel to set
     */
    public void setHotel( Integer hotel )
    {
        _hotel = hotel;
    }
}
