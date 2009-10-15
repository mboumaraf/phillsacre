package uk.me.phillsacre.monopoly.game.squares.actions;

import uk.me.phillsacre.monopoly.game.Player;
import uk.me.phillsacre.monopoly.game.squares.GameSquare;
import uk.me.phillsacre.monopoly.game.squares.JailSquare;
import uk.me.phillsacre.monopoly.game.utils.GameData;


public class GoToJailAction implements SquareAction
{
    private GameSquare _jailSquare;


    public GoToJailAction( GameData gameData )
    {
        for (GameSquare square : gameData.getSquares())
        {
            if (square instanceof JailSquare)
            {
                _jailSquare = square;
                break;
            }
        }
    }

    /**
     * @param currentSquare
     */
    @Override
    public void doAction( Player player, GameSquare currentSquare )
    {
        player.getController().moveToSquare( _jailSquare );

        player.setInJail( true );
    }

}
