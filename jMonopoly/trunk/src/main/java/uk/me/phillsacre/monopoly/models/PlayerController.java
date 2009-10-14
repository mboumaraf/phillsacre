package uk.me.phillsacre.monopoly.models;

import uk.me.phillsacre.monopoly.game.squares.PropertySquare;


public interface PlayerController
{
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
}
