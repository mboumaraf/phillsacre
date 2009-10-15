package uk.me.phillsacre.monopoly.game.cards;

import uk.me.phillsacre.monopoly.game.Card;
import uk.me.phillsacre.monopoly.game.GameState;
import uk.me.phillsacre.monopoly.game.Player;

public class AddMoneyCard extends Card
{
    private Integer _amount;

    public AddMoneyCard(String message, Integer amount)
    {
	super(message);

	_amount = amount;
    }

    @Override
    public void doAction(Player player, GameState gameState)
    {
	player.getController().addMoney(null, _amount);
    }

}
