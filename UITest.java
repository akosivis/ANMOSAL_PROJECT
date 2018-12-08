import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JScrollPane;

public class UITest {
	/* Lobby */
	private JPanel panelText;
	private JPanel panelTextBox;
	private JPanel panelButton;
	private JFrame frameLobby;
	private JLabel textLabel;
	private JTextField fieldNoOfPlayers;
	private JButton enterButton;

	/* Main CHat Lobby */


	/* Original */
	//private JFrame frame;
	private JTextField textField;
    private static JTextArea textArea;
    private static Socket con;
    DataInputStream input;
    DataOutputStream output;
    private JScrollPane scrollPane;
    
	/**
	 * Launch the application.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UITest window = new UITest();
					window.frameLobby.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		con = new Socket("127.0.0.1", 5032);
		 while (true) {
			try {
				
				DataInputStream input = new DataInputStream(con.getInputStream());
				String string = input.readUTF();
				textArea.setText(textArea.getText() + "\n" + "Server: " + string);
			} catch (Exception ev) {
				 textArea.setText(textArea.getText()+" \n" +"Network issues ");

				 try {
						Thread.sleep(2000);
						System.exit(0);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
	}

	/**
	 * Create the application.
	 */
	public UITest() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		/*for lobby*/

// main frame
		frameLobby = new JFrame("Conquer");
		frameLobby.setPreferredSize(new Dimension(300, 250));
		frameLobby.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//--------2nd panel
		textLabel = new JLabel("Enter no. of players: ");
		textLabel.setHorizontalAlignment(JLabel.CENTER);
		textLabel.setVerticalAlignment(JLabel.CENTER);	
		textLabel.setPreferredSize(new Dimension(300, 50));	

		panelText = new JPanel();
		panelText.setPreferredSize(new Dimension(300, 50));

		panelText.add(textLabel);

		//--------3rd panel
		fieldNoOfPlayers = new JTextField("", 15);
		fieldNoOfPlayers.setFont(new Font("Lato Medium", Font.PLAIN, 15));
		panelTextBox = new JPanel();
		panelTextBox.setPreferredSize(new Dimension(300, 50));

		panelTextBox.add(fieldNoOfPlayers);

		//--------4th panel

		//button
		enterButton = new JButton("Enter");
		enterButton.setPreferredSize(new Dimension(200, 50));
		
		panelButton = new JPanel();
		panelButton.setPreferredSize(new Dimension(300, 50));	

		panelButton.add(enterButton);

		// adds the panels in frame
		frameLobby.add(panelText);
		frameLobby.add(panelTextBox);
		frameLobby.add(panelButton);

		frameLobby.setLayout(new FlowLayout());
		frameLobby.setLocationRelativeTo(null);
		frameLobby.pack();
		frameLobby.setVisible(true);



		/*
		frame = new JFrame();
		frame.getContentPane().setBackground(UIManager.getColor("MenuBar.highlight"));
		frame.setBounds(100, 100, 605, 378);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.setFont(new Font("Lato Medium", Font.PLAIN, 22));
		textField.setForeground(Color.ORANGE);
		textField.setBackground(Color.DARK_GRAY);
		textField.setBounds(12, 45, 344, 38);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Send");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (textField.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Please write some text !");
				}else  {
					textArea.setText(textArea.getText() + "\n" + "Client : " + textField.getText());
					try {
						DataOutputStream output = new DataOutputStream(con.getOutputStream());
						output.writeUTF(textField.getText());
					} catch (IOException e1) {
						textArea.setText(textArea.getText() + "\n " + " Network issues");
						try {
							Thread.sleep(2000);
							System.exit(0);
						} catch (InterruptedException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}

					}
					textField.setText("");
				}
			
			
			}
					
		});
		btnNewButton.setFont(new Font("Georgia", Font.BOLD, 22));
		btnNewButton.setForeground(Color.WHITE);
		btnNewButton.setBackground(Color.RED);
		btnNewButton.setBounds(398, 45, 164, 38);
		frame.getContentPane().add(btnNewButton);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 149, 557, 157);
		frame.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setFont(new Font("Comic Sans MS", Font.PLAIN, 22));
		textArea.setForeground(new Color(255, 255, 255));
		textArea.setBackground(new Color(0, 128, 128));
		*/
	}
}
