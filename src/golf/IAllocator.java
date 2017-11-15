
package golf;
/** Interface to specify the allocation policy for
 *  balls. 
 *  The class @see DisplayAllocator wraps an allocator to 
 *  make it visible. 
 */
public interface IAllocator {
	
	/** allocate n items 
	 */
	public void get(int n) throws InterruptedException;
	
	/** return n items
	 */
	public void put(int n);
	
	/** return maximal capacity
	 */
	public int getCapacity(); 
	
	/** return Type/Title/Policy for this allocator
	 *  e.g. a simple allocator returns "SimpleAllocator"
	 *	   a fair allocator may return "FairAllocator" 
	 */
	public String getTitle(); 
}
