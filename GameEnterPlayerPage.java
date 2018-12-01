import java.awt.*;
import javax.swing.*;

//GUI window
public class GameEnterPlayerPage {

	private JPanel panelLogo;
	private JPanel panelText;
	private JPanel panelTextBox;
	private JPanel panelButton;
	private JFrame frame;
	private JLabel textLabel;
	private JTextField fieldNoOfPlayers;
	private JButton enterButton;

 	public static void main(String[] args) {

		// main frame
		JFrame frame = new JFrame("Conquer");
		frame.setPreferredSize(new Dimension(500, 350));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//--------1st panel
		JPanel panelLogo = new JPanel();
		panelLogo.setPreferredSize(new Dimension(500, 200));


		//--------2nd panel
		JLabel textLabel = new JLabel("Enter no. of players: ", SwingConstants.CENTER);
		textLabel.setPreferredSize(new Dimension(500, 50));	
		JPanel panelText = new JPanel();
		panelText.setPreferredSize(new Dimension(500, 50));

		panelText.add(textLabel);

		//--------3rd panel
		JTextField fieldNoOfPlayers = new JTextField("", 15);
		JPanel panelTextBox = new JPanel();
		panelTextBox.setPreferredSize(new Dimension(500, 50));

		panelTextBox.add(fieldNoOfPlayers);

		//--------4th panel

		//button
		JButton enterButton = new JButton("Enter");
		enterButton.setPreferredSize(new Dimension(200, 50));
		
		JPanel panelButton = new JPanel();
		panelButton.setPreferredSize(new Dimension(500, 50));	

		panelButton.add(enterButton);


		// adds the panels in frame
		frame.add(panelLogo);
		frame.add(panelText);
		frame.add(panelTextBox);
		frame.add(panelButton);

		frame.setLayout(new FlowLayout());
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);
	}

}