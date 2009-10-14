package uk.me.phillsacre.monopoly.game.squares.rent;

import uk.me.phillsacre.monopoly.game.Player;
import uk.me.phillsacre.monopoly.game.squares.PropertySquare;
import uk.me.phillsacre.monopoly.utils.DiceRoll;


public interface RentStrategy
{
    /**
     * Calculate the rent that a specified player will have to pay.
     * 
     * @param player
     * @param roll
     * @return
     */
    Integer calculateRent( PropertySquare property, Player player, DiceRoll roll );
}
