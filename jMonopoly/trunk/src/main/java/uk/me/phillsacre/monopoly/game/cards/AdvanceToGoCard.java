package uk.me.phillsacre.monopoly.game.cards;

import uk.me.phillsacre.monopoly.game.Card;
import uk.me.phillsacre.monopoly.game.GameState;
import uk.me.phillsacre.monopoly.game.Player;
import uk.me.phillsacre.monopoly.game.squares.GameSquare;

public class AdvanceToGoCard extends Card
{
    public AdvanceToGoCard(String message)
    {
	super(message);
    }

    @Override
    public void doAction(Player player, GameState gameState)
    {
	GameSquare goSquare = gameState.getData().getSquares().get(0);
	player.getController().moveToSquare(goSquare);
	player.getController().passGo();
    }
}
