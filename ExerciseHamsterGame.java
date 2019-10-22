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
public class ExerciseHamsterGame extends SimpleHamsterGame
{
    ExerciseHamsterGame(){  
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
    
   
    Hamster getHamster(){
        return paule;
    }

}
