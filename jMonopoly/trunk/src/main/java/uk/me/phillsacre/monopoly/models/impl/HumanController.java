package uk.me.phillsacre.monopoly.models.impl;

import uk.me.phillsacre.monopoly.game.squares.PropertySquare;
import uk.me.phillsacre.monopoly.models.PlayerController;
import uk.me.phillsacre.monopoly.ui.MonopolyUI;


public class HumanController implements PlayerController
{
    private MonopolyUI _ui;


    public HumanController( MonopolyUI ui )
    {
        _ui = ui;
    }


    @Override
    public void addProperty( PropertySquare property )
    {
        _ui.addProperty( property );
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
}
