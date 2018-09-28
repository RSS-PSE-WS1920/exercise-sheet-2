/**
 * First hamster program used in lecture 2. The initial version
 * used the predefined reference to paule (the default hamster) and
 * exemplifies calling methods on that object.
 *
 * @author Steffen Becker
 *
 */
class Example01 extends SimpleHamsterGame {

    /**
     * First hamster programm. The idea is to move paule to the grain,
     * let paule collect the grain, and finally return to his initial
     * tile.
     */
    @Override
    void run() {
        game.initialize();
        game.displayInNewGameWindow();

        paule.write("Hallo!");
        paule.move();
        paule.move();
        paule.pickGrain();
        paule.pickGrain();
        paule.turnLeft();
        paule.turnLeft();
        paule.move();
        paule.move();
        paule.turnLeft();
        paule.turnLeft();
    }

}
