package uk.me.phillsacre.monopoly.game.utils;

import uk.me.phillsacre.monopoly.game.Player;
import uk.me.phillsacre.monopoly.game.squares.PropertySquare;
import uk.me.phillsacre.monopoly.game.squares.SquareGroup;


public class GameUtils
{
    /**
     * Checks whether a player has a monopoly
     * 
     * @param square
     * @param player
     * @return
     */
    public static boolean isMonopoly( PropertySquare square, Player player )
    {
        SquareGroup group = square.getGroup();
        boolean monopoly = true;
        for (PropertySquare prop : group.getSquares())
        {
            if (prop.getOwner() == null || !prop.getOwner().equals( player ))
            {
                monopoly = false;
                break;
            }
        }

        return monopoly;
    }
}
