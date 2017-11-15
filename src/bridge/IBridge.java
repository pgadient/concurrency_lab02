
package bridge;

public interface IBridge {
	/** a red car wants to enter the bridge
	 */
	void redEnter() throws InterruptedException;  
	
	/** a red car exits from the bridge
	 */
	void redExit(); 
	
	/** a blue car wants to enter the bridge
	 */
	void blueEnter() throws InterruptedException;  
	
	/** a blue car exits from the bridge
	 */
	void blueExit(); 
}
