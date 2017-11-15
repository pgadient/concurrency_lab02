package bridge;

import java.awt.*;
import java.awt.event.*;
import java.util.*; 

public class SingleLaneBridge extends Frame {
	private static final long serialVersionUID = 5619898796081129275L;

	BridgeCanvas display;

	Choice bridgePolicyChooser;  
	final static String FAIR = "fair"; 
	final static String SAFE = "safe"; 
	final static String UNSAFE = "unsafe"; 
	int maxCar = 2;

	Vector<Car> cars;  

	public SingleLaneBridge() {
		super();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				stop();
				dispose();
			}
		});

		setLayout( new BorderLayout() );
		display = new BridgeCanvas( this );
		add( display, BorderLayout.CENTER );
		Button restart = new Button( "Restart" );
		restart.addActionListener( new RestartListener( this )); 

		Button freeze = new Button( "Freeze" );
		freeze.addActionListener( new FreezeListener( this )); 
		Button onecar = new Button( "One Car" );
		onecar.addActionListener( new CarsListener( this, 1 )); 
		Button twocar = new Button( "Two Cars" );
		twocar.addActionListener( new CarsListener( this, 2 )); 
		Button threecar = new Button( "Three Cars" );
		threecar.addActionListener( new CarsListener( this, 3 )); 

		bridgePolicyChooser = new Choice(); 
		bridgePolicyChooser.add( UNSAFE ); 
		bridgePolicyChooser.add( SAFE ); 
		bridgePolicyChooser.add( FAIR ); 

		Panel ptop = new Panel();
		ptop.setLayout( new FlowLayout());
		ptop.add( freeze ); 
		ptop.add( restart );
		ptop.add( bridgePolicyChooser );
		add( ptop, BorderLayout.NORTH );

		Panel pbot = new Panel();
		pbot.add( onecar );
		pbot.add( twocar );
		pbot.add( threecar );
		add( pbot, BorderLayout.SOUTH );

		cars = new Vector<Car>(); 

		this.setSize(500,300);
		setResizable(false);
		this.setVisible(true);

		this.start();
	}

	// @SuppressWarnings("unchecked")
	public void start() {
		cars.removeAllElements(); 
		String chosenPolicy = bridgePolicyChooser.getSelectedItem(); 
		display.init(chosenPolicy );
		IBridge b;

		if ( chosenPolicy.equals(FAIR )) {
			b = new FairBridge();
		} else if ( chosenPolicy.equals(SAFE )) {
			b = new SafeBridge();
		} else {
			b = new DefaultBridge();
		}
		for (int i = 0; i<maxCar; i++) {
			Car c = new RedCar(b,display ); 
			display.registerCar(c ); 
			cars.addElement( c ); 
			c = new BlueCar( b,display ); 
			display.registerCar( c ); 
			cars.addElement( c ); 
		}

		for (Car c: cars) {
			c.start();
		}
	}

	public void stop() {
		for (Car c: cars) {
			c.stop();
		}
	}


	public void restart() {
		stop(); 
		start(); 
	}

	public void freeze() {
		stop(); 
	}

	public void setMaxCar(int n) {
		stop();
		maxCar = n;
		start();
	}

	class FreezeListener implements ActionListener {

		final SingleLaneBridge owner;

		public FreezeListener( SingleLaneBridge owner ) {
			this.owner = owner;
		}


		public void actionPerformed( ActionEvent evt ) {
			owner.stop(); 
		}
	}

	class RestartListener implements ActionListener {
		final SingleLaneBridge owner;

		public RestartListener( SingleLaneBridge owner ) {
			this.owner = owner;
		}

		public void actionPerformed( ActionEvent evt ) {
			owner.stop();
			owner.start();
		}
	}

	class CarsListener implements ActionListener {
		final SingleLaneBridge owner;
		int number; 

		public CarsListener( SingleLaneBridge owner, int number ) {
			this.owner = owner;
			this.number = number; 
		}

		public void actionPerformed( ActionEvent evt ) {
			owner.setMaxCar(number); 
		}
	}

	public static void main(String[] args) {
		new SingleLaneBridge();
	}
}
