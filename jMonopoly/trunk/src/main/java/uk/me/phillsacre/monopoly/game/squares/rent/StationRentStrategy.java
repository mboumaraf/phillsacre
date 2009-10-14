package uk.me.phillsacre.monopoly.game.squares.rent;

import uk.me.phillsacre.monopoly.game.Player;
import uk.me.phillsacre.monopoly.game.squares.PropertySquare;
import uk.me.phillsacre.monopoly.utils.DiceRoll;


public class StationRentStrategy implements RentStrategy
{
    /**
     * @param player
     * @param roll
     */
    @Override
    public Integer calculateRent( PropertySquare property, Player player, DiceRoll roll )
    {
        Player owner = property.getOwner();

        int count = 0;

        for (PropertySquare station : property.getGroup().getSquares())
        {
            if (station.getOwner().equals( owner ))
            {
                count++ ;
            }
        }

        int rent = 0;

        switch (count)
        {
            case 1:
                rent = 25;
                break;
            case 2:
                rent = 50;
                break;
            case 3:
                rent = 100;
                break;
            case 4:
                rent = 200;
                break;
        }

        return rent;
    }

}
