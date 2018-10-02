
/**
 * Beschreiben Sie hier die Klasse MyFirstSimpleHamster.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class MyFirstSimpleHamster extends SimpleHamsterGame
{
    MyFirstSimpleHamster(){
        game.initialize();
        game.displayInNewGameWindow();   
    }

    /*
     * bewegt Paule ein feld for
     */
    void laufeVor(){
        paule.move();
    }

    /*
     * Dreht Paule nach links
     */
    void nachLinksDrehen(){
        paule.turnLeft();
    }

    /*
     * Läst Paule ein Korn aufheben
     */
    void hebeKornAuf(){
        paule.pickGrain();
    }

    void legeKornHin(){
        paule.putGrain();
    }
    
    void sage(String msg){
        paule.write(msg);
    }

    /*
     * Verwirrt Paule
     */
    String paulesComplexMove(){
        sage("Ich weiß nicht wie :'(");
        paule.move();
        // Helfe Paule dabei durch das Territorium zu laufen 
        // und alle Körner nachhause zu bringen
        return "Hallo";
    }

}
