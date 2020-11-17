package Client;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame {
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private String message = "";
	private String serverIP;
	private Socket connection;

	public Client(String host) {
		super("Messenger Client");
		serverIP = host;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				sendMessage(event.getActionCommand());
				userText.setText("");
			}

		});
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(300, 150);
		setVisible(true);
	}

	public void startRunning() {
		try {
			connectToServer();
			setupStream();
			whileChatting();
		} catch (EOFException eofException) {
			showMessage("\n Client terminated connection");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			closeCrap();
		}
	}

	// connect to server
	public void connectToServer() throws UnknownHostException, IOException {
		showMessage("Attempting connection...\n");
		connection = new Socket(InetAddress.getByName(serverIP), 4500);
		showMessage("Connected to: " + connection.getInetAddress().getHostName());
	}

	// set up streams to send and receive message
	private void setupStream() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Dude your streams are now good to go \n");
	}

	private void whileChatting() throws IOException {
		ableToType(true);
		do {
			try {
				message = (String) input.readObject();
				showMessage("\n" + message);
			} catch (ClassNotFoundException classNotFoundException) {
				showMessage("\n I don't know that object type");
			}
		} while (!message.equals("SERVER - END"));

	}

	// close stream and connection
	private void closeCrap() {
		showMessage("\n Closing crap down");
		ableToType(false);
		try {
			input.close();
			output.close();
			connection.close();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	private void sendMessage(String mess) {
		try {
			output.writeObject("Client: " + mess);
			output.flush();
			showMessage("\n CLient: " + message);
		} catch (IOException ioException) {
			chatWindow.append("\n something was failure");
		}
	}

	// change/update message in window
	private void showMessage(final String mess) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				chatWindow.append(mess);
			}
		});
	}

	// give user permission to type crap into the text box
	private void ableToType(final boolean type) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				userText.setEditable(type);
			}
		});
	}
}
