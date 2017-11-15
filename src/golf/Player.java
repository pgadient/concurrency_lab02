package golf;

/** A Player Class.  
 */
class Player extends Thread {
	final GolfClub golfclub;
	final String name;
	final int nballs;
	
	Player( GolfClub g, int n, String s ) {
		golfclub = g;
		name = s;
		nballs = n;
	}
	
	public void run() {
		try {
			boolean playing = golfclub.getGolfBalls( nballs, name );
			if (!playing) return; 
			Thread.sleep( golfclub.getPlayTime() );
			golfclub.relGolfBalls( nballs, name );
		} catch (InterruptedException e) { }
	}
}
