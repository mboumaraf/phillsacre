package uk.me.phillsacre.monopoly.game.squares.actions;

import org.apache.log4j.Logger;

import uk.me.phillsacre.monopoly.game.Player;
import uk.me.phillsacre.monopoly.game.squares.GameSquare;
import uk.me.phillsacre.monopoly.game.squares.PropertySquare;
import uk.me.phillsacre.monopoly.game.squares.rent.RentStrategy;
import uk.me.phillsacre.monopoly.models.PlayerController;


public class PropertyAction implements SquareAction
{
    private static final Logger _log = Logger.getLogger( PropertyAction.class );


    @Override
    public void doAction( Player player, GameSquare currentSquare )
    {
        PropertySquare property = (PropertySquare)currentSquare;
        PlayerController controller = player.getController();
        Player owner = property.getOwner();

        if (null == owner)
        {
            if (controller.wantToBuy( property ))
            {
                controller.addProperty( property );
            }
        }
        else
        {
            if ( !owner.equals( player ))
            {
                RentStrategy rentStrategy = property.getRentStrategy();
                Integer rent = rentStrategy.calculateRent( property, player, player.getCurrentDiceRoll() );

                player.getController().payMoney( owner, rent );
            }
            else
            {
                _log.debug( "Player owns property - doing nothing" );
            }
        }
    }

}
