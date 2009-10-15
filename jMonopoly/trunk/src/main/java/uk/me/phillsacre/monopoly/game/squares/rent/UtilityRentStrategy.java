package uk.me.phillsacre.monopoly.game.squares.rent;

import uk.me.phillsacre.monopoly.game.Player;
import uk.me.phillsacre.monopoly.game.squares.PropertySquare;
import uk.me.phillsacre.monopoly.utils.DiceRoll;


public class UtilityRentStrategy implements RentStrategy
{
    @Override
    public Integer calculateRent( PropertySquare property, Player player, DiceRoll roll )
    {
        Player owner = property.getOwner();
        boolean ownsAll = true;

        for (PropertySquare siblings : property.getGroup().getSquares())
        {
            if ( !owner.equals( siblings.getOwner() ))
            {
                ownsAll = false;
                break;
            }
        }

        int multiplier = ownsAll ? 10 : 4;

        return roll.getTotal() * multiplier;
    }

}
