package uk.me.phillsacre.monopoly.models.impl;

import uk.me.phillsacre.monopoly.game.Player;
import uk.me.phillsacre.monopoly.game.squares.PropertySquare;
import uk.me.phillsacre.monopoly.ui.MonopolyUI;


public class HumanController extends AbstractController
{
    public HumanController( Player player, MonopolyUI ui )
    {
        super( player, ui );
    }

    @Override
    public boolean wantToBuy( PropertySquare property )
    {
        return _ui.wantToBuy( property );
    }

    @Override
    public void warn( String message )
    {
        _ui.warn( message );
    }

    @Override
    public JailAction checkJailAction()
    {
        return _ui.checkJailAction();
    }
}
