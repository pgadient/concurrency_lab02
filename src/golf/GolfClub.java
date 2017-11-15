/*
 @author  j.n.magee 25/04/98
 */
package golf;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GolfClub extends Frame {
	private static final long serialVersionUID = -2624303622372602003L;
	SlotCanvas waiting;
	SlotCanvas playing;
	DisplayAllocator allocator;
	Label messageLabel;
	
	final static int PLAYTIME = 10000;
	final static int CAPACITY = 5;
	final static int MAX_PLAYING = CAPACITY; 
	final static int MAX_WAITING = CAPACITY; 
	
	/** a Player can take at most NO_BALL
	 *  Warning: NO_BALL should not be bigger than CAPACITY 
	 */
	final static int NO_BALL = 5; 
	
	/** factory method to create Allocator for this GolfClub. 
	 *  overwrite it and create more sophisticated Allocators.  
	 */
	protected IAllocator buildAllocator() {
		return new SimpleAllocator( CAPACITY ); 
	}
	
	public GolfClub() {
		super();
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		
		setLayout( new BorderLayout() );
		
		allocator = new DisplayAllocator(buildAllocator());
		
		Panel scene = new Panel(new BorderLayout());
		waiting = new SlotCanvas( "waiting bar", Color.yellow, MAX_WAITING );
		playing = new SlotCanvas( "playing..", Color.green, MAX_PLAYING );
		Panel slots = new Panel(); 
		slots.add( waiting ); 
		slots.add( playing ); 
		scene.add( allocator, BorderLayout.NORTH );
		scene.add( slots, BorderLayout.CENTER );
		add( scene, BorderLayout.NORTH );
		
		add( new PlayerArrival( this, NO_BALL ), BorderLayout.CENTER );
		
		messageLabel = new Label();
		add(messageLabel, BorderLayout.SOUTH);
		
		this.setSize(700, 260);
		setResizable(false);
		this.setVisible(true);
	}
	
	/** a player with a name wants to get n balls
	 *  return false to signal that the player cannot be served, 
	 *  nor put in the waiting hall. 
	 */
	boolean getGolfBalls( int n,  String name ) throws InterruptedException {
		String s = name + n;
		boolean waitingInBar = waiting.enter( s );
		if ( !waitingInBar ) {
			displayMessage( "No space in the bar for player " + s ); 
			return false; 
		} else {
			displayMessage(""); 
		}
		allocator.get( n );
		waiting.leave( s );
		playing.enter( s );
		return true; 
	}
	
	
	/** returns the time a player may play. 
	 *  Could be a random number in a range 
	 */
	public int getPlayTime() {
		return PLAYTIME;
	}
	
	/** a player with name releases n balls
	 */
	void relGolfBalls( int n,  String name ) throws InterruptedException {
		String s = name + n;
		allocator.put( n );
		playing.leave( s );
	}
	
	void displayMessage(String msg) {
		messageLabel.setText(msg);
	}

	public static void main(String[] args) {
		new GolfClub();
	}
}
