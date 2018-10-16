import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.Territory;
/**
 * Beschreiben Sie hier die Klasse MyFirstSimpleHamster.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class HomeworkHamsterGame extends SimpleHamsterGame
{ 
    HomeworkHamsterGame(){
        game.initialize();
        game.displayInNewGameWindow();  
    }

    protected final void testPaulesSkills(){
        try {
            this.run();
        } catch (final RuntimeException e) {
            this.game.getInputInterface().showAlert(e);
        }

        testAllGrainsInCave();
        stop();
    }

    private final void testAllGrainsInCave(){
        Territory territory = game.getTerritory();
        if(territory.getNumberOfGrainsAt(new Location(4, 6)) == territory.getTotalGrainCount() 
        && paule.mouthEmpty()){
            paule.write("Wuhu geschafft!");
        }else{
            paule.write("Oh nein! Das hat wohl nicht geklappt");
        }
    }

}
