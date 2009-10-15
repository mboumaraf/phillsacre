package uk.me.phillsacre.monopoly.game.cards;

import uk.me.phillsacre.monopoly.game.Card;
import uk.me.phillsacre.monopoly.game.GameState;
import uk.me.phillsacre.monopoly.game.Player;

public class SubtractMoneyCard extends Card
{
    private Integer _amount;

    public SubtractMoneyCard(String name, Integer amount)
    {
	super(name);

	_amount = amount;
    }

    @Override
    public void doAction(Player player, GameState gameState)
    {
	player.getController().payMoney(null, _amount);
    }

}
