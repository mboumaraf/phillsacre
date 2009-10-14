package uk.me.phillsacre.monopoly.game;

import java.util.ArrayList;
import java.util.List;

import uk.me.phillsacre.monopoly.game.squares.GameSquare;
import uk.me.phillsacre.monopoly.game.squares.PropertySquare;
import uk.me.phillsacre.monopoly.models.PlayerController;



public class Player
{
    private String               _name;
    private Integer              _money;
    private GameSquare           _currentSquare;
    private List<PropertySquare> _propertiesOwned;
    private PlayerController     _controller;


    public Player()
    {
        _propertiesOwned = new ArrayList<PropertySquare>();
    }


    /**
     * @return the name
     */
    public String getName()
    {
        return _name;
    }


    /**
     * @param name
     *            the name to set
     */
    public void setName( String name )
    {
        _name = name;
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


    /**
     * @return the propertiesOwned
     */
    public List<PropertySquare> getPropertiesOwned()
    {
        return _propertiesOwned;
    }


    /**
     * @return the money
     */
    public Integer getMoney()
    {
        return _money;
    }


    /**
     * @param money
     *            the money to set
     */
    public void setMoney( Integer money )
    {
        _money = money;
    }

    public void setController( PlayerController controller )
    {
        _controller = controller;
    }

    public PlayerController getController()
    {
        return _controller;
    }

}
