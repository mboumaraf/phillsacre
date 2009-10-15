package uk.me.phillsacre.monopoly.utils;

import java.security.SecureRandom;

public class DiceRoll
{
    private static SecureRandom _random = new SecureRandom();

    private Integer _dice1;
    private Integer _dice2;

    private DiceRoll()
    {
	// Prevent instantiation
    }

    public static DiceRoll getNext()
    {
	DiceRoll roll = new DiceRoll();

	roll._dice1 = _random.nextInt(6) + 1;
	roll._dice2 = _random.nextInt(6) + 1;

	return roll;
    }

    /**
     * @return the dice1
     */
    public Integer getDice1()
    {
	return _dice1;
    }

    /**
     * @return the dice2
     */
    public Integer getDice2()
    {
	return _dice2;
    }

    public Integer getTotal()
    {
	return _dice1 + _dice2;
    }

    public boolean isDoubles()
    {
	return _dice1 == _dice2;
    }
}
