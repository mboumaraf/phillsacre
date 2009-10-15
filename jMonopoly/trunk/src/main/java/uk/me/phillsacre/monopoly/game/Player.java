package uk.me.phillsacre.monopoly.game;

import java.util.ArrayList;
import java.util.List;

import uk.me.phillsacre.monopoly.game.squares.GameSquare;
import uk.me.phillsacre.monopoly.game.squares.PropertySquare;
import uk.me.phillsacre.monopoly.models.PlayerController;
import uk.me.phillsacre.monopoly.utils.DiceRoll;



public class Player
{
    private String               _name;
    private Integer              _money;
    private GameSquare           _currentSquare;
    private DiceRoll             _currentDiceRoll;
    private List<PropertySquare> _propertiesOwned;
    private PlayerController     _controller;
    private boolean              _inJail;
    private int                  _jailCount;


    public Player()
    {
        _propertiesOwned = new ArrayList<PropertySquare>();
    }

    @Override
    public boolean equals( Object o )
    {
        if (this == o)
        {
            return true;
        }
        if (null == o || o.getClass() != this.getClass())
        {
            return false;
        }

        Player player = (Player)o;
        return player.getName().equals( _name );
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


    /**
     * @return the inJail
     */
    public boolean isInJail()
    {
        return _inJail;
    }


    /**
     * @param inJail
     *            the inJail to set
     */
    public void setInJail( boolean inJail )
    {
        _inJail = inJail;
    }


    /**
     * @return the jailCount
     */
    public int getJailCount()
    {
        return _jailCount;
    }


    /**
     * @param jailCount
     *            the jailCount to set
     */
    public void setJailCount( int jailCount )
    {
        _jailCount = jailCount;
    }


    /**
     * @return the currentDiceRoll
     */
    public DiceRoll getCurrentDiceRoll()
    {
        return _currentDiceRoll;
    }


    /**
     * @param currentDiceRoll
     *            the currentDiceRoll to set
     */
    public void setCurrentDiceRoll( DiceRoll currentDiceRoll )
    {
        _currentDiceRoll = currentDiceRoll;
    }

}
