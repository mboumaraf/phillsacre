package uk.me.phillsacre.monopoly.game;

import java.util.ArrayList;
import java.util.List;

import uk.me.phillsacre.monopoly.game.squares.GameSquare;
import uk.me.phillsacre.monopoly.game.utils.GameData;


public class GameState
{
    private List<Player> _players;
    private GameData     _data;
    private boolean      _complete;
    private Player       _currentPlayer;
    private GameSquare   _currentSquare;


    public GameState( GameData data )
    {
        _data = data;
        _players = new ArrayList<Player>();
        _complete = false;
    }

    public GameSquare getInitialSquare()
    {
        // FIXME Assume that 'Go' square is first one for now.
        return _data.getSquares().get( 0 );
    }

    public Integer getStartingMoney()
    {
        return _data.getConfiguration().getStartingMoney();
    }


    /**
     * @return the players
     */
    public List<Player> getPlayers()
    {
        return _players;
    }


    /**
     * @return the data
     */
    public GameData getData()
    {
        return _data;
    }

    public boolean isComplete()
    {
        return _complete;
    }

    public void setComplete( boolean complete )
    {
        _complete = complete;
    }

    /**
     * @return the currentPlayer
     */
    public Player getCurrentPlayer()
    {
        return _currentPlayer;
    }

    /**
     * @param currentPlayer
     *            the currentPlayer to set
     */
    public void setCurrentPlayer( Player currentPlayer )
    {
        _currentPlayer = currentPlayer;
    }

    /**
     * @return the currentSquare
     */
    public GameSquare getCurrentSquare()
    {
        return _currentSquare;
    }

    /**
     * @param currentSquare
     *            the currentSquare to set
     */
    public void setCurrentSquare( GameSquare currentSquare )
    {
        _currentSquare = currentSquare;
    }
}
