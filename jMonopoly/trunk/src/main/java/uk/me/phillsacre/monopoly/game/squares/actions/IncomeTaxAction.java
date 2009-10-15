package uk.me.phillsacre.monopoly.game.squares.actions;

import uk.me.phillsacre.monopoly.game.Player;
import uk.me.phillsacre.monopoly.game.squares.GameSquare;


public class IncomeTaxAction implements SquareAction
{

    @Override
    public void doAction( Player player, GameSquare currentSquare )
    {
        // TODO: Implement 10% option
        player.getController().payMoney( null, 200 );
    }

}
