package uk.me.phillsacre.monopoly.ui;

import uk.me.phillsacre.monopoly.game.Player;
import uk.me.phillsacre.monopoly.game.squares.PropertySquare;
import uk.me.phillsacre.monopoly.utils.DiceRoll;


public interface MonopolyUI
{
    public void displayDiceRoll( DiceRoll roll );

    public void setCurrentPlayer( Player player );

    void addProperty( PropertySquare property );

    boolean wantToBuy( PropertySquare property );

    void warn( String message );
}
