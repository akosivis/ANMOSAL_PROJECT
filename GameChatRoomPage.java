import java.awt.*;
import javax.swing.*;

//GUI window
public class GameChatRoomPage {

	private JPanel panelChats;
	private JPanel panelInsertChat;
	private JPanel panelButton;
	private JFrame frame;
	private JTextField fieldInsertChats;
	private JButton sendButton;
	private JButton disconnectButton;

 	public static void main(String[] args) {
		
		// main frame
		JFrame frame = new JFrame("Conquer");
		frame.setPreferredSize(new Dimension(400, 500));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//------1st panel 
		JTextField fieldChats = new JTextField("");
		fieldChats.setEditable(false);
		fieldChats.setPreferredSize(new Dimension(390, 390));	

		JPanel panelChats = new JPanel();
		panelChats.setPreferredSize(new Dimension(400, 400));

		panelChats.add(fieldChats);

		//------2nd panel 
		JTextField fieldInsertChats = new JTextField("");
		fieldInsertChats.setPreferredSize(new Dimension(390, 45));	

		JPanel panelInsertChat = new JPanel();
		panelInsertChat.setPreferredSize(new Dimension(400, 50));

		panelInsertChat.add(fieldInsertChats);

		//------3rd panel 
		JButton sendButton = new JButton("Send");
		sendButton.setPreferredSize(new Dimension(100, 45));
		JButton disconnectButton = new JButton("Disconnect");	
		disconnectButton.setPreferredSize(new Dimension(100, 45));

		JPanel panelButton = new JPanel();
		panelButton.setPreferredSize(new Dimension(400, 50));

		panelButton.add(sendButton);
		panelButton.add(disconnectButton);	
		panelButton.setLayout(new FlowLayout());

		frame.add(panelChats);
		frame.add(panelInsertChat);
		frame.add(panelButton);	

		frame.setLayout(new FlowLayout());
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);				

	}

}