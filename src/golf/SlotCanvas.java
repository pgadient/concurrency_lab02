
package golf;

import java.awt.*;

/**************************************************************/

public class SlotCanvas extends Panel {
	private static final long serialVersionUID = 3139194605512766420L;
	Label title; 
	private final static String EMPTY = "     ";
	Label buffer[]; 
	
	public final static Font DEFAULT_FONT = new Font("Times",Font.BOLD, 18); 
	public SlotCanvas(String title, Color c, int slots) {
		super(new BorderLayout());
		Panel cells = new Panel(new FlowLayout(FlowLayout.CENTER, 1, 1 )); 
		this.title = new Label(title, Label.CENTER);
		this.title.setFont(DisplayAllocator.TITLE_FONT);
		setBackground(c);
		add(this.title, BorderLayout.NORTH); 
		Panel p = new Panel();  p.add(cells);  
		add(p, BorderLayout.CENTER);
		cells.setBackground(Color.black); 
		
		buffer = new Label[slots]; 
		for (int i=0; i<slots; i++) {
			Label l = new Label(EMPTY, Label.CENTER); 
			buffer[i] = l; 
			l.setFont(DEFAULT_FONT); 
			l.setBackground(Color.white); 
			cells.add(buffer[i]); 
		}
	}
	
	/** Searches for the first occurence of the given 
	 *  argument, testing for equality
	 *  using the equals method
	 *  @returns the index of the element (-1 when not found)
	 */
	private int indexOf(String s) {
		for(int i=0; i<buffer.length; i++) {
			if(buffer[i].getText().equals(s)) {
				return i;
			}
		}
		return -1;
	}
	
	private void insertElementAt(String s, int index) {
		buffer[index].setText(s); 
	}
	
	/** stores name into the buffer, provided there
	 *  is an empty element
	 *  @returns true when the name is stored.  
	 */
	public synchronized boolean enter(String name) {
		int i = indexOf(EMPTY);
		if (i>=0) {
			insertElementAt(name, i);
			return true; 
		} else {
			return false; 
		}
	}
	
	/** removes a String from the buffer, 
	 *  does nothing when the string cannot be found
	 */
	public synchronized void leave(String name) {
		int i = indexOf(name);
		if (i>=0) {
			insertElementAt(EMPTY, i);
		}
	}
	
	public synchronized boolean full() {
		int i = indexOf(EMPTY);
		return !(i >= 0);
	}
	
}
