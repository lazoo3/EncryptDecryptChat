package encryptdecryptchat;

import java.awt.EventQueue;

import javax.swing.JFrame;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ChatWindow {
	// Initializing variables for the GUI
	private JFrame frame;
	private JTextField jtfNick;
	private JTextField jtfIp;
	private JLabel lblPort;
	private JTextField jtfPort;
	private JTextField jtfKey;
	private JLabel lblKey;
	private BufferedReader reader;
	private PrintWriter writer;
	private String message;
	private JTextArea jtfText;
	private JPanel panel;
	private JTextArea jtaMessage;
	private JPanel panel_1;
	private JScrollPane scrollPane;
	private Socket socket;
	private String text;
	private String username;
	private EncryptDecrypt secureMessage;
	private String secureKey;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatWindow window = new ChatWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}// end -> main(String[])

	/**
	 * Create the application.
	 */
	public ChatWindow() {
		// securekey for the cipher and setting it to EncryptDecrypt constructor
		secureKey = "ThisIsTheSecureK";
		secureMessage = new EncryptDecrypt(secureKey);

		initialize();

	}// end -> ChatWindow()

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblUsrName = new JLabel("Nickname");
		lblUsrName.setBounds(10, 11, 46, 14);
		frame.getContentPane().add(lblUsrName);

		jtfNick = new JTextField();
		jtfNick.setBounds(66, 8, 200, 20);
		frame.getContentPane().add(jtfNick);
		jtfNick.setColumns(10);

		JLabel lblIp = new JLabel("IP: ");
		lblIp.setBounds(10, 36, 46, 14);
		frame.getContentPane().add(lblIp);

		jtfIp = new JTextField();
		jtfIp.setBounds(66, 33, 200, 20);
		frame.getContentPane().add(jtfIp);
		jtfIp.setColumns(10);

		lblPort = new JLabel("Port: ");
		lblPort.setBounds(10, 61, 46, 14);
		frame.getContentPane().add(lblPort);

		jtfPort = new JTextField();
		jtfPort.setBounds(66, 58, 200, 20);
		frame.getContentPane().add(jtfPort);
		jtfPort.setColumns(10);

		jtfKey = new JTextField();
		jtfKey.setBounds(66, 87, 200, 20);
		jtfKey.setText(secureKey);
		jtfKey.setEnabled(false);
		frame.getContentPane().add(jtfKey);
		jtfKey.setColumns(10);

		lblKey = new JLabel("Key:");
		lblKey.setBounds(10, 90, 46, 14);
		frame.getContentPane().add(lblKey);

		// adding mouse listener to the Send button
		JButton jbSend = new JButton("Send");
		jbSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// getting the text and username for encrypting the message
				text = jtaMessage.getText();
				username = jtfNick.getText();
				String encryptedText = "";
				try {
					// calling method encrypt from EncryptDecrypt class and
					// setting the text from client input to encrypt
					encryptedText = secureMessage.encrypt(text);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// printing with PrintWriter username and encrypted text
				writer.println(username + ":" + encryptedText);
				// flushing the writer
				writer.flush();
				jtaMessage.setText(null);
			}
		});
		jbSend.setBounds(10, 227, 89, 23);
		frame.getContentPane().add(jbSend);

		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Chat", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel.setBounds(270, 11, 304, 212);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		jtfText = new JTextArea();
		jtfText.setBounds(6, 16, 261, 185);
		jtfText.setEnabled(false);

		scrollPane = new JScrollPane(jtfText);
		scrollPane.setBounds(6, 16, 288, 185);
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		panel.add(scrollPane);

		panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Message",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(4, 137, 269, 86);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);

		jtaMessage = new JTextArea();

		jtaMessage.setBounds(10, 16, 249, 59);
		panel_1.add(jtaMessage);

		JButton jbConnect = new JButton("Connect");
		// adding actionListener to Connect button
		jbConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String socketNum;
				int socketStringToNum = 0;
				try {
					// checking if all input fields are typed
					// if they are then continue and connect to server and start
					// the thread
					if (jtfPort.getText().equals("")
							|| jtfNick.getText().equals("")
							|| jtfIp.getText().equals("")) {
						JOptionPane.showMessageDialog(null,
								"Please input all fields!");
						;
					}// end -> if
					else {
						socketNum = jtfPort.getText();
						socketStringToNum = Integer.parseInt(socketNum);
						socket = new Socket(jtfIp.getText(), socketStringToNum);

						writer = new PrintWriter(new OutputStreamWriter(socket
								.getOutputStream()), true);

						MyClientThread mct = new MyClientThread(socket);
						mct.start();
						jbConnect.setEnabled(false);
						jtfText.setText("You have connected!\n");

					}// end -> else

				}// end -> try
				catch (Exception e) {
					e.printStackTrace();
				}// end -> catch(Exception)

			}// end -> actionPerformed()
		});// end -> actionListener()
		jbConnect.setBounds(10, 114, 89, 23);
		frame.getContentPane().add(jbConnect);

	}// end -> initialize()
		// Thread class for reading messages

	class MyClientThread extends Thread {
		Socket socket;

		// Creating MyClientThread constructor
		public MyClientThread(Socket socket) {
			this.socket = socket;
		}

		// Creating run method
		public void run() {
			try {
				username = jtfNick.getText();
				reader = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				// reading line until it is null
				while ((message = reader.readLine()) != null) {
					// separating username and message for decryption
					String usernamePart = "";
					String messagePart = "";
					if (message.contains(":")) {
						String parts[] = message.split(":");
						usernamePart = parts[0];
						messagePart = parts[1];
					}
					// calling method decrypt from EncryptDecrypt class
					// to decrypt the message that has been sent by client
					String decryptedString = secureMessage.decrypt(messagePart);
					// appending decrypted text to text field
					jtfText.append(usernamePart + ": " + decryptedString + "\n");
				}
			} catch (SocketException se) {
				JOptionPane.showMessageDialog(null, "Server has disconnected");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}// end -> run()
	}// end of MyClientThread
}// end -> ChatWindow class
