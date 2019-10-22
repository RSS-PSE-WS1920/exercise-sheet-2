import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.Hamster;
import java.io.IOException;
import java.io.File;
import java.io.*;
/**
 * Beschreiben Sie hier die Klasse MyFirstSimpleHamster.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public abstract class HomeworkHamsterGame extends SimpleHamsterGame
{ 
    HomeworkHamsterGame(){
        File terFile = new File ("+libs/territories/example01.ter");
            try(
            InputStream targetStream = new FileInputStream(terFile);
            ) {
            game.initialize(targetStream);
        } catch (IOException ex){
            throw new RuntimeException(ex);
        }
        game.displayInNewGameWindow();
    }

    protected final void testPaulesSkills(){
        game.startGame(false);
        try {
            hamsterRun();
        } catch (final RuntimeException e) {
            this.game.getInputInterface().showAlert(e);
        }

        testAllGrainsInCave();
        game.stopGame();
    }

    private final void testAllGrainsInCave(){
        paule.write("Test:");
        Territory territory = game.getTerritory();
        if(territory.getNumberOfGrainsAt(new Location(4, 6)) == territory.getTotalGrainCount() 
        && paule.mouthEmpty()){
            paule.write("Wuhu geschafft!");
        }else{
            paule.write("Oh nein! Das hat wohl nicht geklappt");
        }
    }
    
    abstract void hamsterRun();
}
