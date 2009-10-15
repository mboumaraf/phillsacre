package uk.me.phillsacre.monopoly.models.impl;

import uk.me.phillsacre.monopoly.game.Player;
import uk.me.phillsacre.monopoly.game.squares.GameSquare;
import uk.me.phillsacre.monopoly.game.squares.PropertySquare;
import uk.me.phillsacre.monopoly.models.PlayerController;
import uk.me.phillsacre.monopoly.ui.MonopolyUI;


public abstract class AbstractController implements PlayerController
{
    protected Player     _player;
    protected MonopolyUI _ui;


    public AbstractController( Player player, MonopolyUI ui )
    {
        _player = player;
        _ui = ui;
    }

    @Override
    public void addProperty( PropertySquare property )
    {
        _player.setMoney( _player.getMoney() - property.getValue() );
        _player.getPropertiesOwned().add( property );
        property.setOwner( _player );

        _ui.addProperty( property );
    }

    @Override
    public void moveToSquare( GameSquare gameSquare )
    {
        _player.setCurrentSquare( gameSquare );
        _ui.moveTo( gameSquare );
    }

    @Override
    public void payMoney( Player destination, Integer amount )
    {
        // TODO: check if player does not have enough
        _player.setMoney( _player.getMoney() - amount );

        if (null != destination)
        {
            destination.setMoney( destination.getMoney() + amount );
        }

        _ui.payMoney( destination, amount );
    }

}
