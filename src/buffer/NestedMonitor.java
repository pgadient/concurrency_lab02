/*
 File: BoundedBuffer.java
 */

package buffer;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import support.RotatingPanel;

public class NestedMonitor extends Frame {
	private static final long serialVersionUID = -2690492363760041477L;
	Producer producer;	
	Consumer consumer;
	DisplayBuffer buffer;
	
	public NestedMonitor() 	{
		super();
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				producer.stop();
				consumer.stop();
				dispose();
			}
		});
		
		// Setup
		buffer = new DisplayBuffer( 5 );
		RotatingPanel p = new RotatingPanel( "Producer" );
		p.setRotatorColor( Color.blue );
		RotatingPanel c = new RotatingPanel( "Consumer" );
		c.setRotatorColor( Color.yellow );
		
		producer = new Producer( buffer, p );
		consumer = new Consumer( buffer, c );
		
		GridBagLayout gridbag = new GridBagLayout();
		this.setLayout(gridbag);
		
		GridBagConstraints gc = new GridBagConstraints();
		gc.anchor = GridBagConstraints.NORTH;
		gridbag.setConstraints( buffer.getView(), gc );
		gridbag.setConstraints( p, gc );
		gridbag.setConstraints( c, gc );
		this.add( p );
		this.add( buffer.getView() );
		this.add( c );
		
		this.setSize(600, 275);
		setResizable(false);
		this.setVisible(true);
		
		producer.start();
		consumer.start();
		buffer.init();
	}
	
	public static void main(String[] args) {
		new NestedMonitor();
	}
}
