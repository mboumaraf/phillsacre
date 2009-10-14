package uk.me.phillsacre.monopoly.game.squares;

public abstract class GameSquare
{
    private String _name;


    public void setName( String name )
    {
        _name = name;
    }

    public String getName()
    {
        return _name;
    }

    @Override
    public String toString()
    {
        return getName();
    }
}
