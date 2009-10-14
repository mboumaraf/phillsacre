package uk.me.phillsacre.monopoly;

import uk.me.phillsacre.monopoly.game.GameState;
import uk.me.phillsacre.monopoly.game.utils.GameData;
import uk.me.phillsacre.monopoly.game.utils.GameDataLoader;
import uk.me.phillsacre.monopoly.models.MonopolyPM;
import uk.me.phillsacre.monopoly.ui.text.TextBasedUI;


public class Monopoly
{
    public static void main( String[] args )
    {
        GameData data = GameDataLoader.loadData();
        GameState state = new GameState( data );

        MonopolyPM pm = new MonopolyPM( state );

        TextBasedUI ui = new TextBasedUI( pm );
        pm.setUI( ui );

        ui.start();
    }
}
