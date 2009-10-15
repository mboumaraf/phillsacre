package uk.me.phillsacre.monopoly.models;

import uk.me.phillsacre.monopoly.game.Player;
import uk.me.phillsacre.monopoly.game.squares.GameSquare;
import uk.me.phillsacre.monopoly.game.squares.PropertySquare;


public interface PlayerController
{
    public enum JailAction
    {
        ROLL_FOR_DOUBLES,
        POST_BAIL,
        USE_CARD
    };


    /**
     * Checks whether the user wants to buy a specific property. This should allow them to sell / mortgage as necessary
     * in order to secure the money if they wish.
     * 
     * @param property
     * @return
     */
    boolean wantToBuy( PropertySquare property );

    /**
     * Indicates that the user has acquired a property.
     * 
     * @param property
     */
    void addProperty( PropertySquare property );

    /**
     * Sends a warning message to the user
     * 
     * @param message
     */
    void warn( String message );

    /**
     * Moves a user to a particular square.
     * 
     * @param gameSquare
     */
    void moveToSquare( GameSquare gameSquare );

    /**
     * Pay money to another player or, if the destination is null, the bank.
     * 
     * @param destination
     * @param amount
     */
    void payMoney( Player destination, Integer amount );

    /**
     * Checks what action the player wants to take
     * 
     * @return
     */
    JailAction checkJailAction();
}
