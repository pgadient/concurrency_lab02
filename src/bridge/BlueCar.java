package bridge;

import java.awt.*;

class BlueCar extends Car {
	BlueCar( IBridge b, BridgeCanvas d ) {
		super( b, d ); 
	}
	
	protected void drive() throws InterruptedException {
		display.driveBlue( this ); 
	}
	
	protected void initPosition() {
		img = display.getBlueCar();
		rec = new Rectangle(); 
		rec.width = img.getWidth( null );
		rec.height = img.getHeight( null );
		display.initializeBlue( this ); 
	}
	
	protected Image getImage() {
		return display.getBlueCar(); 
	}
	
	protected void enter() throws InterruptedException {
		control.blueEnter(); 
	}
	
	protected void exit() {
		control.blueExit(); 
	}
	
	public String getCarType() { 
		return "BlueCar"; 
	} 
}
