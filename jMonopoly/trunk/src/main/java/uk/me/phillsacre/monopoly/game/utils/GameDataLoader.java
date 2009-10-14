package uk.me.phillsacre.monopoly.game.utils;

import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import uk.me.phillsacre.monopoly.game.squares.ChanceSquare;
import uk.me.phillsacre.monopoly.game.squares.CommunityChestSquare;
import uk.me.phillsacre.monopoly.game.squares.FreeParkingSquare;
import uk.me.phillsacre.monopoly.game.squares.GameSquare;
import uk.me.phillsacre.monopoly.game.squares.GoSquare;
import uk.me.phillsacre.monopoly.game.squares.GoToJailSquare;
import uk.me.phillsacre.monopoly.game.squares.IncomeTaxSquare;
import uk.me.phillsacre.monopoly.game.squares.JailSquare;
import uk.me.phillsacre.monopoly.game.squares.PropertySquare;
import uk.me.phillsacre.monopoly.game.squares.SquareGroup;
import uk.me.phillsacre.monopoly.game.squares.SuperTaxSquare;
import uk.me.phillsacre.monopoly.game.squares.actions.ChanceAction;
import uk.me.phillsacre.monopoly.game.squares.actions.CommunityChestAction;
import uk.me.phillsacre.monopoly.game.squares.actions.FreeParkingAction;
import uk.me.phillsacre.monopoly.game.squares.actions.GoAction;
import uk.me.phillsacre.monopoly.game.squares.actions.GoToJailAction;
import uk.me.phillsacre.monopoly.game.squares.actions.IncomeTaxAction;
import uk.me.phillsacre.monopoly.game.squares.actions.JailAction;
import uk.me.phillsacre.monopoly.game.squares.actions.PropertyAction;
import uk.me.phillsacre.monopoly.game.squares.actions.SuperTaxAction;
import uk.me.phillsacre.monopoly.game.squares.rent.StationRentStrategy;


public class GameDataLoader
{
    private static final Logger _log = Logger.getLogger( GameDataLoader.class );

    private GameData            _gameData;


    public static GameData loadData()
    {
        GameDataLoader loader = new GameDataLoader();
        loader.loadData0();

        return loader._gameData;
    }

    private GameDataLoader()
    {
        // prevent instantiation
    }

    @SuppressWarnings( "unchecked" )
    private void loadData0()
    {
        _gameData = new GameData();

        try
        {
            URL url = GameDataLoader.class.getClassLoader().getResource( "game-data.xml" );
            SAXBuilder builder = new SAXBuilder();
            builder.setIgnoringElementContentWhitespace( true );
            builder.setValidation( false );

            Document doc = builder.build( url );
            Element root = doc.getRootElement();

            _log.debug( "Root element is: " + root.getName() );

            Element confElt = root.getChild( "configuration" );
            _gameData.getConfiguration().setStartingMoney( Integer.valueOf( confElt.getChildText( "starting-money" ) ) );

            _log.debug( "Starting money is: " + _gameData.getConfiguration().getStartingMoney() );

            Element groupElts = root.getChild( "groups" );

            for (Element groupElt : (List<Element>)groupElts.getChildren( "group" ))
            {
                SquareGroup group = createGroup( groupElt );
                _gameData.addGroup( group );
            }

            Element squares = root.getChild( "squares" );

            for (Element squareElt : (List<Element>)squares.getChildren( "square" ))
            {
                GameSquare square = loadSquare( squareElt );
                _gameData.getSquares().add( square );
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException( "Could not load data", e );
        }

        initActions();
    }

    private void initActions()
    {
        _gameData.addAction( ChanceSquare.class, new ChanceAction() );
        _gameData.addAction( GoSquare.class, new GoAction() );
        _gameData.addAction( FreeParkingSquare.class, new FreeParkingAction() );
        _gameData.addAction( CommunityChestSquare.class, new CommunityChestAction() );
        _gameData.addAction( GoToJailSquare.class, new GoToJailAction() );
        _gameData.addAction( IncomeTaxSquare.class, new IncomeTaxAction() );
        _gameData.addAction( JailSquare.class, new JailAction() );
        _gameData.addAction( PropertySquare.class, new PropertyAction() );
        _gameData.addAction( SuperTaxSquare.class, new SuperTaxAction() );
    }

    private SquareGroup createGroup( Element groupElt )
    {
        String id = groupElt.getAttributeValue( "id" );
        String colour = groupElt.getChildText( "colour" );

        SquareGroup group = new SquareGroup( id );
        group.setColour( colour );

        return group;
    }

    private GameSquare loadSquare( Element squareElt )
    {
        String type = squareElt.getAttributeValue( "type" );

        GameSquare square = null;

        if ("property".equals( type ))
        {
            PropertySquare prop = new PropertySquare();

            String groupId = squareElt.getChildText( "group" );

            prop.setName( squareElt.getChildText( "name" ) );
            prop.setValue( Integer.valueOf( squareElt.getChildText( "value" ) ) );
            prop.setGroup( _gameData.getGroups().get( groupId ) );

            Element rentChild = squareElt.getChild( "rent" );
            String rentType = rentChild.getAttributeValue( "type" );
            if ("station".equals( rentType ))
            {
                prop.setRentStrategy( new StationRentStrategy() );
            }
            else if ("default".equals( rentType ))
            {
                prop.setRentStrategy( new DefaultRentStategy() );
            }

            square = prop;
        }
        else if ("go".equals( type ))
        {
            square = new GoSquare();
        }
        else if ("community-chest".equals( type ))
        {
            square = new CommunityChestSquare();
        }
        else if ("income-tax".equals( type ))
        {
            square = new IncomeTaxSquare();
        }
        else if ("chance".equals( type ))
        {
            square = new ChanceSquare();
        }
        else if ("jail".equals( type ))
        {
            square = new JailSquare();
        }
        else if ("go-to-jail".equals( type ))
        {
            square = new GoToJailSquare();
        }
        else if ("free-parking".equals( type ))
        {
            square = new FreeParkingSquare();
        }
        else if ("super-tax".equals( type ))
        {
            square = new SuperTaxSquare();
        }
        else
        {
            throw new RuntimeException( "Invalid type: " + type );
        }

        _log.debug( "Created square: " + square.toString() );

        return square;
    }
}
