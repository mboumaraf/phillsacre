package uk.me.phillsacre.monopoly.game;

public abstract class Card
{
    private String _message;

    public Card(String message)
    {
	_message = message;
    }

    public String getMessage()
    {
	return _message;
    }

    public abstract void doAction(Player player, GameState gameState);
}
