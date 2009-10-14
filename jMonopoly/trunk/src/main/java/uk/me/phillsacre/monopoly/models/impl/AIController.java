package uk.me.phillsacre.monopoly.models.impl;

import uk.me.phillsacre.monopoly.game.Player;
import uk.me.phillsacre.monopoly.game.squares.PropertySquare;
import uk.me.phillsacre.monopoly.models.PlayerController;


public class AIController implements PlayerController
{
    private Player _player;


    public AIController( Player player )
    {
        _player = player;
    }

    @Override
    public void addProperty( PropertySquare property )
    {
        // Do nothing
    }

    @Override
    public boolean wantToBuy( PropertySquare property )
    {
        if (_player.getMoney() >= property.getValue())
        {
            return true;
        }

        // TODO: Implement an algorithm to determine whether to mortgage other properties to buy
        // this one.

        return false;
    }

    @Override
    public void warn( String message )
    {
        // Do nothing
    }

}
