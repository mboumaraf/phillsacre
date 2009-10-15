package uk.me.phillsacre.monopoly.models;

import java.util.List;

import org.apache.log4j.Logger;

import uk.me.phillsacre.monopoly.game.GameState;
import uk.me.phillsacre.monopoly.game.Player;
import uk.me.phillsacre.monopoly.game.squares.GameSquare;
import uk.me.phillsacre.monopoly.game.squares.actions.SquareAction;
import uk.me.phillsacre.monopoly.models.PlayerController.JailAction;
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
    private static final Logger _log = Logger.getLogger(MonopolyPM.class);

    private MonopolyUI _ui;
    private GameState _gameState;
    private String _playerName;
    private Integer _playerCount;

    public MonopolyPM(GameState state)
    {
	_gameState = state;
    }

    public void addAIPlayer()
    {
	int count = _gameState.getPlayers().size();
	count += 1;

	String playerName = "Player " + count;

	Player player = newPlayer(playerName);
	player.setController(new AIController(player, _ui));

	_gameState.getPlayers().add(player);

	_log.debug("Added AI player: " + playerName);
    }

    public void startGame()
    {
	Player player = newPlayer(_playerName);
	player.setController(new HumanController(player, _ui));

	_gameState.getPlayers().add(player);

	for (int i = 1; i < _playerCount; i++)
	{
	    addAIPlayer();
	}

	// TODO: Determine who is to start by a dice roll.

	int turns = 0;

	while (!_gameState.isComplete() && turns++ < 20)
	{
	    playTurn();
	}
    }

    public void playTurn()
    {
	for (Player player : _gameState.getPlayers())
	{
	    _ui.setCurrentPlayer(player);
	    setCurrentPlayer(player);

	    boolean turnAgain = false;

	    do
	    {
		DiceRoll roll = DiceRoll.getNext();

		if (player.isInJail())
		{
		    if (!handleJail(roll))
		    {
			continue;
		    }
		}
		else
		{
		    if (roll.isDoubles())
		    {
			turnAgain = true;
		    }
		}

		GameSquare square = getNextSquare(player, roll);
		player.setCurrentSquare(square);
		player.setCurrentDiceRoll(roll);

		_ui.displayDiceRoll(roll, square);

		_log.debug("Player is now at: " + square);

		SquareAction action = _gameState.getData().getAction(
			square.getClass());

		action.doAction(player, square);

		_ui.completeTurn();

	    } while (turnAgain);
	}
    }

    private Player newPlayer(String name)
    {
	Player player = new Player();
	player.setName(name);
	player.setCurrentSquare(_gameState.getInitialSquare());
	player.setMoney(_gameState.getStartingMoney());

	return player;
    }

    /* === Private methods ================================ */

    /**
     * Returns false if the player remains in jail.
     */
    private boolean handleJail(DiceRoll roll)
    {
	Player player = getCurrentPlayer();
	JailAction action = null;
	boolean allowContinue = false;

	if (player.getJailCount() == 2)
	{
	    // Third time - player MUST post bail
	    _ui.info("Third time in jail - player must post bail");

	    action = JailAction.POST_BAIL;
	}
	else
	{
	    action = player.getController().checkJailAction();
	}

	if (action == JailAction.ROLL_FOR_DOUBLES)
	{
	    if (roll.getDice1() == roll.getDice2())
	    {
		player.setJailCount(0);
		player.setInJail(false);

		_ui.info("Player has rolled doubles, continuing");
		allowContinue = true;
	    }
	}
	else if (action == JailAction.POST_BAIL)
	{
	    player.getController().payMoney(null, 50);
	    player.setJailCount(0);
	    player.setInJail(false);

	    allowContinue = true;
	}
	// TODO: Implement using "Get out of jail" card

	return allowContinue;
    }

    private GameSquare getNextSquare(Player player, DiceRoll roll)
    {
	List<GameSquare> squares = _gameState.getData().getSquares();
	GameSquare currentSquare = player.getCurrentSquare();

	int index = squares.indexOf(currentSquare);

	int newIndex = index + roll.getTotal();

	if (newIndex >= squares.size())
	{
	    player.getController().passGo();
	    newIndex = newIndex - squares.size();
	}

	_log.debug("New index is: " + newIndex);

	return squares.get(newIndex);
    }

    /* === Accessors ====================================== */

    public void setPlayerName(String name)
    {
	_playerName = name;
    }

    public void setPlayerCount(Integer count)
    {
	if (count < 2 || count > 8)
	{
	    throw new RuntimeException("Must have been 2 and 8 players");
	}
	_playerCount = count;
    }

    public void setUI(MonopolyUI ui)
    {
	_ui = ui;
    }

    public void setCurrentPlayer(Player player)
    {
	_gameState.setCurrentPlayer(player);
    }

    public Player getCurrentPlayer()
    {
	return _gameState.getCurrentPlayer();
    }
}
