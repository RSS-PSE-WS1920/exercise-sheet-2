package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model;

/**
 * Interface to interact with a UI or mock object for reading values from the user.
 * @author Steffen Becker
 *
 */
public interface InputInterface {
    
    /**
     * Read an integer value from a user.
     * @param message The message used in the prompt for the number.
     * @return The integer value read.
     */
    public int readInteger(String message);
    
    /** 
     * Read a string value from a user.
     * @param message The message used in the prompt for the string.
     * @return The string value read.
     */
    public String readString(String message);
    
    /**
     * Inform a user about an abnormal execution aborting
     * @param t The throwable which lead to aborting the program.
     */
    public void showAlert(Throwable t);
}
