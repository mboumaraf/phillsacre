package uk.me.phillsacre.monopoly.ui;

import uk.me.phillsacre.monopoly.game.Player;
import uk.me.phillsacre.monopoly.game.squares.GameSquare;
import uk.me.phillsacre.monopoly.game.squares.PropertySquare;
import uk.me.phillsacre.monopoly.models.PlayerController.JailAction;
import uk.me.phillsacre.monopoly.utils.DiceRoll;


public interface MonopolyUI
{
    public void displaySalary();

    public void displayDiceRoll( DiceRoll roll, GameSquare square );

    public void setCurrentPlayer( Player player );

    void addProperty( PropertySquare property );

    boolean wantToBuy( PropertySquare property );

    void warn( String message );

    void info( String message );

    void completeTurn();

    void moveTo( GameSquare square );

    void payMoney( Player destination, Integer amount );

    JailAction checkJailAction();
}
