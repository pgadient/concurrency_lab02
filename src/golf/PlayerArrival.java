package golf;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class PlayerArrival extends Panel {
	private static final long serialVersionUID = -3289388924620852663L;
	GolfClub golfClub;
	private String names = "abcdefghijklnopqrstuvxyz";
	private int nextname = 0;
	
	Button p[];
	
	PlayerArrival( GolfClub g, int n ) {
		super(new FlowLayout());
		golfClub = g;
		p = new Button[n];
		for( int i=0; i<p.length; i++ ) {
			p[i] = new Button( "new Player( "+( i+1 )+" )" );
			p[i].addActionListener( new PlayerArrivalListener( this, i ) ); 
			add( p[i] );
		}
	}
	
	synchronized void playerArrives( int n ) {
		Thread t = new Player( golfClub, n+1, names.substring( nextname, nextname+1 ) );
		t.start();
		nextname = ( nextname+1 ) % names.length();
	}
	
	class PlayerArrivalListener implements ActionListener {
		final PlayerArrival owner;
		int number; 
		
		public PlayerArrivalListener(  PlayerArrival owner, int number  ) {
			this.owner = owner;
			this.number = number; 
		}
		
		public void actionPerformed(  ActionEvent evt  ) {
			owner.playerArrives( number ); 
		}
	}
}

