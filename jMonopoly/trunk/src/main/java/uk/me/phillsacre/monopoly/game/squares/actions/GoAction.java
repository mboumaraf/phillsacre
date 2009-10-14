package uk.me.phillsacre.monopoly.game.squares.actions;

import uk.me.phillsacre.monopoly.game.Player;
import uk.me.phillsacre.monopoly.game.squares.GameSquare;


public class GoAction implements SquareAction
{
    @Override
    public void doAction( Player player, GameSquare currentSquare )
    {
        Integer money = player.getMoney();
        player.setMoney( money + 200 );

        // TODO: Show a message about passing Go
    }
}
