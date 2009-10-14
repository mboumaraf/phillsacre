package uk.me.phillsacre.monopoly.game.squares;

public class IncomeTaxSquare extends GameSquare
{
    private Integer _value;


    public IncomeTaxSquare()
    {
        setName( "Income Tax" );
    }

    /**
     * @return the value
     */
    public Integer getValue()
    {
        return _value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue( Integer value )
    {
        _value = value;
    }
}
