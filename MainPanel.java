import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class MainPanel extends JFrame implements ActionListener{
	
	private JTextField portText = new JTextField("4443");
	private JTextField udpText = new JTextField("2");
	private JTextField hostText = new JTextField("192.168.10.189");
	private JTextField playersText = new JTextField("2");
	private JLabel hostLabel = new JLabel("Host Address (for client):");
	private JLabel portLabel = new JLabel("Port:");
	private JLabel playersLabel = new JLabel("Num of Players:");
	private JButton serverButton = new JButton("Start as Server");
	private JButton clientButton = new JButton("Start as Client");

	public void initialize(){

		serverButton.addActionListener(this);
		clientButton.addActionListener(this);
		
		this.setLayout(new GridLayout(4,2));
		this.add(hostLabel);
		this.add(hostText);
		this.add(portLabel);
		this.add(portText);
		this.add(playersLabel);
		this.add(playersText);
		this.add(serverButton);
		this.add(clientButton);
		
		setPreferredSize(new Dimension(300,150));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		pack();
		setVisible(true);
	}
	
	public MainPanel(){
		initialize();
	}
	
	public static void main(String[] args) {
		MainPanel win = new MainPanel();
	}

	@Override
	public void actionPerformed(ActionEvent b) {
		// TODO Auto-generated method stub
		if(b.getSource()==serverButton){
			int port = Integer.parseInt(portText.getText());
			int players = Integer.parseInt(playersText.getText());
			
			try {
				MainPanelServer mpserver =  new MainPanelServer(players, port);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.dispose();
		}
		
		if(b.getSource()==clientButton){
			int port = Integer.parseInt(portText.getText());
			String addr = hostText.getText();
			
			try {
				MainPanelClient mpclient =  new MainPanelClient(addr, port);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.dispose();
		}
		
	}

}

