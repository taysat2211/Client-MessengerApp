package Client;

import javax.swing.JFrame;

public class ClientTest {
	public static void main(String[] args) {
		Client mark = new Client("127.0.0.1");
		mark.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mark.startRunning();
	}
}
