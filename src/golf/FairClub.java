
package golf;

@SuppressWarnings("serial")
public class FairClub extends GolfClub {
	protected IAllocator buildAllocator() {
		return new FairAllocator( CAPACITY ); 
	}
}

