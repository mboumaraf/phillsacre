package uk.me.phillsacre.monopoly.game.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.me.phillsacre.monopoly.game.squares.GameSquare;
import uk.me.phillsacre.monopoly.game.squares.SquareGroup;
import uk.me.phillsacre.monopoly.game.squares.actions.SquareAction;


public class GameData
{
    private List<GameSquare>                               _squares;
    private Map<Class<? extends GameSquare>, SquareAction> _actionMap;
    private Map<String, SquareGroup>                       _groups;
    private GameConfiguration                              _configuration;


    public GameData()
    {
        _squares = new ArrayList<GameSquare>();
        _groups = new HashMap<String, SquareGroup>();
        _configuration = new GameConfiguration();
        _actionMap = new HashMap<Class<? extends GameSquare>, SquareAction>();
    }

    public List<GameSquare> getSquares()
    {
        return _squares;
    }

    public Map<String, SquareGroup> getGroups()
    {
        return _groups;
    }

    public void addGroup( SquareGroup group )
    {
        _groups.put( group.getId(), group );
    }

    public GameConfiguration getConfiguration()
    {
        return _configuration;
    }

    public void addAction( Class<? extends GameSquare> squareClass, SquareAction action )
    {
        _actionMap.put( squareClass, action );
    }

    public SquareAction getAction( Class<? extends GameSquare> clazz )
    {
        return _actionMap.get( clazz );
    }
}
