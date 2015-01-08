package fr.viewpoint.utils;

/**
 * The aim is to easily mock date
 * 
 * @author jpoirier
 * @see http 
 *      ://stackoverflow.com/questions/4563584/how-to-mock-the-default-constructor
 *      -of-the-date-class-with-jmockit
 * 
 */
public interface TimeSource {

	/**
	 * Return current millisecond to build date
	 * 
	 * @return
	 */
	long millis();

}
