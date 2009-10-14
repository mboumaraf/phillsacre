package uk.me.phillsacre.monopoly.models;

import java.util.List;

import org.apache.log4j.Logger;

import uk.me.phillsacre.monopoly.game.GameState;
import uk.me.phillsacre.monopoly.game.Player;
import uk.me.phillsacre.monopoly.game.squares.GameSquare;
import uk.me.phillsacre.monopoly.game.squares.actions.SquareAction;
import uk.me.phillsacre.monopoly.models.impl.AIController;
import uk.me.phillsacre.monopoly.models.impl.HumanController;
import uk.me.phillsacre.monopoly.ui.MonopolyUI;
import uk.me.phillsacre.monopoly.utils.DiceRoll;


/**
 * Monopoly Presentation Model
 * 
 * @author psacre
 * @since 13 Oct 2009
 */
public class MonopolyPM
{
    private static final Logger _log = Logger.getLogger( MonopolyPM.class );

    private MonopolyUI          _ui;
    private GameState           _gameState;
    private String              _playerName;
    private Integer             _playerCount;


    public MonopolyPM( GameState state )
    {
        _gameState = state;
    }

    public void addAIPlayer()
    {
        int count = _gameState.getPlayers().size();
        count += 1;

        String playerName = "Player " + count;

        Player player = newPlayer( playerName );
        player.setController( new AIController( player ) );

        _gameState.getPlayers().add( player );

        _log.debug( "Added AI player: " + playerName );
    }

    public void startGame()
    {
        Player player = newPlayer( _playerName );
        player.setController( new HumanController( _ui ) );

        _gameState.getPlayers().add( player );

        for (int i = 1; i < _playerCount; i++ )
        {
            addAIPlayer();
        }

        // TODO: Determine who is to start by a dice roll.

        while ( !_gameState.isComplete())
        {
            playTurn();
        }
    }

    public void playTurn()
    {
        for (Player player : _gameState.getPlayers())
        {
            _ui.setCurrentPlayer( player );
            setCurrentPlayer( player );

            DiceRoll roll = DiceRoll.getNext();
            _ui.displayDiceRoll( roll );

            GameSquare square = getNextSquare( player, roll );
            player.setCurrentSquare( square );

            _log.debug( "Player is now at: " + square );

            SquareAction action = _gameState.getData().getAction( square.getClass() );

            action.doAction( player, square );
        }

        _gameState.setComplete( true );
    }

    private Player newPlayer( String name )
    {
        Player player = new Player();
        player.setName( name );
        player.setCurrentSquare( _gameState.getInitialSquare() );
        player.setMoney( _gameState.getStartingMoney() );

        return player;
    }

    /* === Private methods ================================ */

    private GameSquare getNextSquare( Player player, DiceRoll roll )
    {
        List<GameSquare> squares = _gameState.getData().getSquares();
        GameSquare currentSquare = player.getCurrentSquare();

        int index = squares.indexOf( currentSquare );

        int newIndex = index + roll.getTotal();

        if (newIndex > squares.size())
        {
            passGo( player );
            newIndex = newIndex - squares.size();
        }

        _log.debug( "New index is: " + newIndex );

        return squares.get( newIndex );
    }

    private void passGo( Player player )
    {
        _log.debug( "Player has passed go, adding 200 salary" );
        player.setMoney( player.getMoney() + 200 );
    }

    /* === Accessors ====================================== */

    public void setPlayerName( String name )
    {
        _playerName = name;
    }

    public void setPlayerCount( Integer count )
    {
        if (count < 2 || count > 8)
        {
            throw new RuntimeException( "Must have been 2 and 8 players" );
        }
        _playerCount = count;
    }

    public void setUI( MonopolyUI ui )
    {
        _ui = ui;
    }

    public void setCurrentPlayer( Player player )
    {
        _gameState.setCurrentPlayer( player );
    }

    public Player getCurrentPlayer()
    {
        return _gameState.getCurrentPlayer();
    }
}
