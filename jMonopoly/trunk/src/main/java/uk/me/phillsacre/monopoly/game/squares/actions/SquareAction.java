package uk.me.phillsacre.monopoly.game.squares.actions;

import uk.me.phillsacre.monopoly.game.Player;
import uk.me.phillsacre.monopoly.game.squares.GameSquare;


public interface SquareAction
{
    void doAction( Player player, GameSquare currentSquare );
}
