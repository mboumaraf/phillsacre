package uk.me.phillsacre.monopoly.game.squares.actions;

import org.apache.log4j.Logger;

import uk.me.phillsacre.monopoly.game.Player;
import uk.me.phillsacre.monopoly.game.squares.GameSquare;
import uk.me.phillsacre.monopoly.game.squares.PropertySquare;
import uk.me.phillsacre.monopoly.models.PlayerController;


public class PropertyAction implements SquareAction
{
    private static final Logger _log = Logger.getLogger( PropertyAction.class );


    @Override
    public void doAction( Player player, GameSquare currentSquare )
    {
        PropertySquare property = (PropertySquare)currentSquare;
        PlayerController controller = player.getController();

        if (null == property.getOwner())
        {
            if (controller.wantToBuy( property ))
            {
                if (player.getMoney() >= property.getValue())
                {
                    player.setMoney( player.getMoney() - property.getValue() );
                    property.setOwner( player );
                    player.getPropertiesOwned().add( property );

                    _log.debug( String.format( "Player [%s] has bought property [%s]", player.getName(), property ) );

                    controller.addProperty( property );
                }
                else
                {
                    controller.warn( "You do not have enough money to buy this property!" );
                }
            }
        }
        else
        {
            if ( !property.getOwner().equals( player ))
            {
                
            }
            else
            {
                _log.debug( "Player owns property - doing nothing" );
            }
        }
    }

}
