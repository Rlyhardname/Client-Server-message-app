package Main;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import javax.swing.AbstractAction;
import javax.swing.Action;

public class LoginClientGUI {

	private JFrame frame;
	private JTextField username;
	private JTextField password;
	private ClientLogic client;
	private JButton signUp;
	private LoginClientGUI window;
	private JButton login;
	private Thread tr;

	/**
	 * Launch the application.
	 */
	public static void startGUI() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginClientGUI window = new LoginClientGUI();
					ServerGUI.createNewConnection();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginClientGUI() {
		initialize();
		client = new ClientLogic();
		client.start();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new FlowLayout());
		username = new JTextField();
		username.setPreferredSize(new Dimension(200, 50));
		password = new JTextField();
		password.setPreferredSize(new Dimension(200, 50));
		login = new JButton("LOGIN");
		signUp = new JButton("SIGN UP");
		username.setText("account1");
		password.setText("password");
		frame.getContentPane().add(username);
		frame.getContentPane().add(password);
		frame.getContentPane().add(login);
		frame.getContentPane().add(signUp);

		Thread tr = new Thread(new Runnable() {

			@Override
			public void run() {
				frame.addWindowListener(new java.awt.event.WindowAdapter() {
					public void windowClosing(WindowEvent winEvt) {

					}
				});

			}

		});
		tr.start();

		Thread tr1 = new Thread(new Runnable() {

			@Override
			public void run() {
				login.addActionListener(e -> selectionButtonPressed());
			}

		});
		tr1.start();

		Thread tr2 = new Thread(new Runnable() {

			@Override
			public void run() {
				signUp.addActionListener(e -> selectionButtonPressed1());
			}

		});
		tr2.start();

	}

	private Object selectionButtonPressed() {

		String user = username.getText();
		String pass = password.getText().toString();
		client.setUsername(user);
		client.setPassword(pass);
		signUp.setText("LOGIN FAILED");
		// client.setOperation(1);
		try {
			if (client.accessServer("login")) {
				ClientGUI.startClientGUI(client);
				System.out.println("vliza li 1??");
				frame.dispose();
			}
		} catch (RuntimeException e) {
			System.out.println("NIDEI BEEEE");
			frame.dispose();
		}
		return null;
	}

	private Object selectionButtonPressed1() {

		String user = username.getText().toString();
		String pass = password.getText().toString();
		client.setUsername(user);
		client.setPassword(pass);
		
		try {
			if (client.accessServer("signup")) {
				ClientGUI.startClientGUI(client);
				System.out.println("vliza li 1??");
				frame.dispose();
			}
		} catch (RuntimeException e) {
			System.out.println("NIDEI BEEEE");
			frame.dispose();
		}


		return null;
	}

}
