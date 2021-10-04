package swinghello;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class SwingHello extends JFrame {

	public SwingHello(String msg) {
		super(msg);
		initUI(msg);
	}

	private void initUI(String msg) {
		add(new JLabel(msg));
		pack();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		SwingHello hello = new SwingHello("Hello World");
		// when either of these windows is closed, the whole program terminates
		SwingHello goodbye = new SwingHello("Goodbye cruel world");
	}

}
