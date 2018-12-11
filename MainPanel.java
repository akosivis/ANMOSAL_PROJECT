import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class MainPanel extends JFrame implements ActionListener{
	
	private JLabel nameLabel = new JLabel("ENTER NAME:");
	private JTextField nameText = new JTextField(3);
	
	private JLabel teamLabel = new JLabel("TEAM:");
	private JLabel startLabel = new JLabel("Start As:");
	
	private JTextField portText = new JTextField("4444");
	private JTextField hostText = new JTextField("192.168.10.189");
	private JTextField playersText = new JTextField("2");
	private JLabel hostLabel = new JLabel("Host Address:");

	private JLabel ipLabel = new JLabel("Your IP Address:");
	private JTextField ipText = new JTextField();

	private JLabel portLabel = new JLabel("Port:");
	private JLabel playersLabel = new JLabel("Num of Players:");
	private JButton serverButton = new JButton("Start as Server");
	private JButton clientButton = new JButton("Start as Client");
	private JButton start = new JButton("START");
	private JRadioButton server = new JRadioButton("server");
	private JRadioButton client = new JRadioButton("client");
	
	private JRadioButton red = new JRadioButton("RED");
	private JRadioButton blue = new JRadioButton("BLUE");
	
	JPanel centerP =  new JPanel();
	JPanel topP = new JPanel();
	
	ButtonGroup startAsGroup = new ButtonGroup();
	ButtonGroup teamGroup = new ButtonGroup();
	
	int team = 1;
	String startAs="server";
	public void initialize(){

		
		
		serverButton.addActionListener(this);
		clientButton.addActionListener(this);
		start.addActionListener(this);
		red.addActionListener(this);
		blue.addActionListener(this);
		server.addActionListener(this);
		client.addActionListener(this);
		
		startAsGroup.add(server);
		startAsGroup.add(client);
		
		teamGroup.add(red);
		teamGroup.add(blue);
		
		topP.setLayout(new BorderLayout());
		
		JPanel nameP = new JPanel();
		nameP.add(nameLabel);
		nameP.add(nameText);
		topP.add(nameP, BorderLayout.WEST);
		
		JPanel topCenter = new JPanel();
		red.doClick();
		
		topCenter.add(teamLabel);
		topCenter.add(red);
		topCenter.add(blue);
		
		topP.add(topCenter, BorderLayout.CENTER);
		
		JPanel topEast = new JPanel();
		topEast.add(startLabel);
		topEast.add(server);
		topEast.add(client);
		server.doClick();
		
		topP.add(topEast, BorderLayout.EAST);
		
		centerP.setLayout(new GridLayout(4,2));
		centerP.add(hostLabel);
		centerP.add(hostText);
		centerP.add(ipLabel);
		centerP.add(ipText);
		centerP.add(portLabel);
		centerP.add(portText);
		centerP.add(playersLabel);
		centerP.add(playersText);
		
		this.setLayout(new BorderLayout());
		this.add(topP, BorderLayout.NORTH);
		this.add(centerP, BorderLayout.CENTER);
		this.add(start, BorderLayout.SOUTH);
//		this.add(red);
//		this.add(blue);
//		this.add(hostLabel);
//		this.add(hostText);
//		this.add(portLabel);
//		this.add(portText);
//		this.add(playersLabel);
//		this.add(playersText);
//		this.add(serverButton);
//		this.add(clientButton);
		
		setPreferredSize(new Dimension(600,200));
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
		if(b.getSource()==red) {
			team=1;
		}
		
		if(b.getSource()==blue) {
			team=2;
		}
		
		if(b.getSource()==server) {
//			hostText.setEnabled(false);
			ipLabel.setEnabled(false);
			portText.setEnabled(false);
			portText.setText("4444");
			startAs="server";
		}
		
		if(b.getSource()==client) {
			hostText.setEnabled(true);
			portText.setEnabled(true);
			startAs="client";
		}
		
		if(b.getSource() ==  start) {
			if(startAs.equals("server")==true) {
				String name = nameText.getText().substring(0,3);
//				System.out.println(name);
				int port = Integer.parseInt(portText.getText());
				int players = Integer.parseInt(playersText.getText());
				String addr = hostText.getText();
				
				try {
					MainPanelServer mpserver =  new MainPanelServer(name, players, port, team , addr);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				this.dispose();
			}else if(startAs.equals("client")==true) {
				while(nameText.getText().length() <3) {
					nameText.setText(nameText.getText()+ "-");
				}
				
				String ipaddr = ipText.getText();
				String name = nameText.getText().substring(0,3);
				int port = Integer.parseInt(portText.getText());
				String addr = hostText.getText();
				int players = Integer.parseInt(playersText.getText());
				
				try {
					MainPanelClient mpclient =  new MainPanelClient(name, addr, ipaddr, port, players,team);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				this.dispose();
			}
		}
		
		if(b.getSource()==clientButton){
			
		}
		
	}

}

