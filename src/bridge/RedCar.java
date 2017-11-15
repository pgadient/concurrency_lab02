package bridge;

import java.awt.*;

class RedCar extends Car {
	RedCar(IBridge b, BridgeCanvas d) {
		super(b, d); 
	}
	
	protected void drive() throws InterruptedException {
		display.driveRed(this); 
	}
	
	protected Image getImage() {
		return display.getRedCar(); 
	}
	
	protected void initPosition() {
		img = display.getRedCar(); 
		rec = new Rectangle(0, 0); 
		rec.width = img.getWidth(null); 
		rec.height = img.getHeight(null); 
		display.initializeRed(this); 
	}
	
	protected void enter() throws InterruptedException {
		control.redEnter(); 
	}
	
	protected void exit() {
		control.redExit(); 
	}
	
	public String getCarType() { 
		return "RedCar"; 
	}
}

