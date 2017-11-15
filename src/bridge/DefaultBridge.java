
package bridge;

/** This bridge is neither safe nor fair. 
 */
public class DefaultBridge implements IBridge {
	/** a red car wants to enter the bridge
	 */
	public synchronized void redEnter() throws InterruptedException {}; 
	
	/** a red car exits from the bridge
	 */
	public synchronized void redExit() {}; 
	
	/** a blue car wants to enter the bridge
	 */
	public synchronized void blueEnter() throws InterruptedException {}; 
	
	/** a blue car exits from the bridge
	 */
	public synchronized void blueExit() {}; 
}
