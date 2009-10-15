package uk.me.phillsacre.monopoly.ui.text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;

import uk.me.phillsacre.monopoly.game.Player;
import uk.me.phillsacre.monopoly.game.squares.GameSquare;
import uk.me.phillsacre.monopoly.game.squares.PropertySquare;
import uk.me.phillsacre.monopoly.models.MonopolyPM;
import uk.me.phillsacre.monopoly.models.PlayerController.JailAction;
import uk.me.phillsacre.monopoly.ui.MonopolyUI;
import uk.me.phillsacre.monopoly.utils.DiceRoll;


public class TextBasedUI implements MonopolyUI
{
    private MonopolyPM     _pModel;
    private BufferedReader _reader;


    public TextBasedUI( MonopolyPM pModel )
    {
        _pModel = pModel;
    }

    public void start()
    {
        _reader = new BufferedReader( new InputStreamReader( System.in ) );
        String name = readAnswer( "Player name" );

        _pModel.setPlayerName( name );

        String players = readAnswer( "Number of players" );
        Integer playerCount = Integer.valueOf( players );

        _pModel.setPlayerCount( playerCount );

        _pModel.startGame();
    }

    private String readLine()
    {
        try
        {
            String line = _reader.readLine();
            return line;
        }
        catch (IOException e)
        {
            throw new RuntimeException( "Error while reading input", e );
        }
    }

    /**
     * Wrapper around readLine which does not accept null lines.
     * 
     * @param question
     * @return
     */
    private String readAnswer( String question )
    {
        System.out.print( question + ": " );

        String line;
        do
        {
            line = readLine();
        } while (StringUtils.isBlank( line ));

        return line;

    }

    /* === Implementing MonopolyUI ============================= */

    public void displaySalary()
    {
        System.out.println( "Player has passed Go, collects £200" );
    }

    @Override
    public void displayDiceRoll( DiceRoll roll, GameSquare square )
    {
        System.out.println( "Player rolls: " + roll.getDice1() + ", " + roll.getDice2() + ". Moves to: "
                            + square.getName() );
    }

    public void setCurrentPlayer( Player player )
    {
        System.out.println( "Current player: " + player.getName() );
    }

    @Override
    public void addProperty( PropertySquare property )
    {
        Player player = _pModel.getCurrentPlayer();

        System.out.println( "** " + player.getName() + " has bought: " + property );
        System.out.println( "** Total properties: " + player.getPropertiesOwned().size() + "; money: £"
                            + player.getMoney() );

    }

    @Override
    public boolean wantToBuy( PropertySquare property )
    {
        // TODO: Allow player to mortgage / sell in order to buy
        if (_pModel.getCurrentPlayer().getMoney() >= property.getValue())
        {
            String yn = readAnswer( "Do you want to buy " + property + "? (Y/N)" );
            return yn.equalsIgnoreCase( "y" );
        }

        return false;
    }

    @Override
    public void warn( String message )
    {
        System.out.println( "** Warning: " + message );
    }

    @Override
    public void completeTurn()
    {
        System.out.println( "Press enter to continue..." );
        readLine();
    }

    @Override
    public void moveTo( GameSquare square )
    {
        System.out.println( "Moving to: " + square );
    }

    @Override
    public void payMoney( Player destination, Integer amount )
    {
        String dest = (null == destination) ? "Bank" : destination.getName();

        System.out.println( String.format( "£%d paid to: %s", amount, dest ) );
    }

    @Override
    public void info( String message )
    {
        System.out.println( "** " + message );
    }

    @Override
    public JailAction checkJailAction()
    {
        String which = readAnswer( "Do you want to: [A] Post bail, [B] Roll for doubles?" );

        if (which.equalsIgnoreCase( "a" ))
        {
            return JailAction.POST_BAIL;
        }

        return JailAction.ROLL_FOR_DOUBLES;
    }
}
