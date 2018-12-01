import java.awt.*;
import javax.swing.*;

//GUI window
public class GameChooseLobbyOrChatPage {
	private int input1; 
	private int input2;

	private JPanel panelChooseLobby;
	private JPanel panelChooseChat;
	private JPanel panelButton;

	private JFrame frame;
 
	private JButton lobbyButton;	
	private JButton chatButton;
	private JButton backButton;	


 	public static void main(String[] args) {
 		String number;
 		int numOfPlayers; 
		// main frame
		JFrame frame = new JFrame("Conquer");
		frame.setPreferredSize(new Dimension(500, 300));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//--------1st panel
		JButton lobbyButton = new JButton("Create Lobby");
		lobbyButton.setPreferredSize(new Dimension(200, 80));
		//lobbyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        lobbyButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String number = JOptionPane.showInputDialog(frame,
                        "Max number of players:", null);
                int numOfPlayers = Integer.parseInt(number.trim());

                if (numOfPlayers < 3) {
                	JOptionPane.showMessageDialog(frame,"Illegal input!", "Warning", JOptionPane.WARNING_MESSAGE);
                } 
                //else {                }
            }


        });

		JPanel panelChooseLobby = new JPanel();
		panelChooseLobby.setPreferredSize(new Dimension(500, 100));

		panelChooseLobby.add(lobbyButton);

		//--------2nd panel
		JButton chatButton = new JButton("Enter Chat Room");
		chatButton.setPreferredSize(new Dimension(200, 80));
		//chatButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		

		JPanel panelChooseChat = new JPanel();
		panelChooseChat.setPreferredSize(new Dimension(500, 100));

		panelChooseChat.add(chatButton);

		//--------
		JButton backButton = new JButton("Back");
		backButton.setPreferredSize(new Dimension(100, 40));
		backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel panelButton = new JPanel();
		panelButton.setPreferredSize(new Dimension(500, 50));

		panelButton.add(backButton);

		frame.add(panelChooseLobby);
		frame.add(panelChooseChat);
		frame.add(panelButton);	

		frame.setLayout(new FlowLayout());
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);
	}

}