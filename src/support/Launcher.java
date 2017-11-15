package support;

import java.awt.*;
import java.awt.event.*;

import buffer.NestedMonitor;
import readwrite.*; // for multiple RW policies 
import bridge.SingleLaneBridge;
import golf.GolfClub;
// import golf.FairClub;

public class Launcher extends Frame {
    static final long serialVersionUID = -3141061790161982323L;

	public Launcher() {
		super("CP Lab 2 Exercises");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		this.setLayout(new GridLayout(4,1));
		// this.setLayout(new GridLayout(6,1)); // extra buttons for RW policies
		// this.setLayout(new GridLayout(7,1)); // extra button for Fair Golf Club

		Button nestedMonitor = new Button("Nested Monitor");
		nestedMonitor.addActionListener(new ExampleShower(NestedMonitor.class));
		this.add(nestedMonitor);

		Button readwrite = new Button("Readers Writers");
		readwrite.addActionListener(new ExampleShower(SafeReadersWriters.class));
		this.add(readwrite);

		/*
		// policies to be implemented ...
		Button writersPriority = new Button("Writers Priority");
		writersPriority.addActionListener(new ExampleShower(WritersPriority.class));
		this.add(writersPriority);

		Button fairreadwrite = new Button("Fair Readers Writers");
		fairreadwrite.addActionListener(new ExampleShower(FairReadersWriters.class));
		this.add(fairreadwrite);
		*/

		Button bridge = new Button("Single Lane Bridge");
        bridge.addActionListener(new ExampleShower(SingleLaneBridge.class));
		this.add(bridge);

		Button golf = new Button("Golf Club");
		golf.addActionListener(new ExampleShower(GolfClub.class));
		this.add(golf);

		/*
		Button fairgolf = new Button("Fair Golf Club");
		fairgolf.addActionListener(new ExampleShower(FairClub.class));
		this.add(fairgolf);
		*/
		
        this.setResizable(false);
		this.setSize(250,200);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new Launcher();
	}
    
    private class ExampleShower implements ActionListener {
        private Class<?> exampleClass;

        public ExampleShower(Class<?> exampleClass) {
                this.exampleClass = exampleClass;
        }
        
        public void actionPerformed(ActionEvent e) {
                try {
                        this.exampleClass.newInstance();
                } catch (IllegalArgumentException iae) {
                        iae.printStackTrace();
                } catch (SecurityException se) {
                        se.printStackTrace();
                } catch (InstantiationException ie) {
                        ie.printStackTrace();
                } catch (IllegalAccessException iae) {
                        iae.printStackTrace();
                }
        }
    }

}
