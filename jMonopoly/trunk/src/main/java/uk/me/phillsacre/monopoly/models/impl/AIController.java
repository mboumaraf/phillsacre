package uk.me.phillsacre.monopoly.models.impl;

import uk.me.phillsacre.monopoly.game.Player;
import uk.me.phillsacre.monopoly.game.squares.PropertySquare;
import uk.me.phillsacre.monopoly.ui.MonopolyUI;


public class AIController extends AbstractController
{
    public AIController( Player player, MonopolyUI ui )
    {
        super( player, ui );
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

    @Override
    public JailAction checkJailAction()
    {
        if (_player.getMoney() > (50 * 2))
        {
            return JailAction.POST_BAIL;
        }

        return JailAction.ROLL_FOR_DOUBLES;
    }
}
