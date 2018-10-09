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
    
    void testPaulesRun(){
        
        try {
            this.run();
        } catch (final RuntimeException e) {
            this.game.getInputInterface().showAlert(e);
        }
        
        this.stop();
        
        //runTest();
    }

    private void runTest( ){
        Territory territory = this.game.getTerritory();
        //if(territory.getNumberOfGrainsAt(new Location(10, 5)) == territory.getTotalGrainCount()){
        //    hamsterGame.paule.write("Wuhu geschaft");
        //}else{
        //    hamsterGame.paule.write("Oh nein! Das hat wohl nicht geklappt");
        //}
    }
    
}
