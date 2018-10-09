import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.Hamster;
/**
 * Beschreiben Sie hier die Klasse MyFirstSimpleHamster.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class ExerciseHamsterGame extends SimpleHamsterGame
{
    ExerciseHamsterGame(){
        game.initialize();
        game.displayInNewGameWindow();   
    }

    Hamster getHamster(){
        return paule;
    }

}
