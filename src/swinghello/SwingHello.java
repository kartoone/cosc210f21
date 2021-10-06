package swinghello;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class SwingHello extends JFrame {

	public SwingHello(String msg) {
		super(msg);
		initUI(msg);
	}

	private void handleSave() {
		System.out.println("saving file now!");
	}
	
	private void initUI(String msg) {
		var pane = getContentPane();
		pane.setLayout(new FlowLayout());
		pane.add(new JLabel(msg));
		JMenuBar menubar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem save = new JMenuItem("Save");
		JMenuItem exit = new JMenuItem("Exit");
		file.add(save);
		file.add(exit);
		menubar.add(file);
		setJMenuBar(menubar);
		exit.addActionListener((event) -> System.exit(0));
		save.addActionListener((event) -> handleSave());
		
//		JButton msgButton = new JButton("display message");
//		pane.add(msgButton);
		setSize(200,300);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			SwingHello hello = new SwingHello("Hello World");
		});
		// when either of these windows is closed, the whole program terminates
		// note that the line below create a whole new window
		// SwingHello goodbye = new SwingHello("Goodbye cruel world");
	}

}
