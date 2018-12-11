import java.awt.BorderLayout;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;

public class MainPanelServer extends JFrame {

	GamePanel g_panel;
	GameServer g_server;
	ChatForServer chat_server;
	
	public MainPanelServer(String name, int player, int port, int team) throws UnknownHostException{
		String pubAddress = InetAddress.getLocalHost().getHostAddress();
		System.out.println(pubAddress);
		g_panel =  new GamePanel(name, pubAddress, 4443, player, false, team);
		
		g_server = new GameServer(player, port);
		chat_server = new ChatForServer();
		new Thread(g_server).start();
		
		this.setLayout(new BorderLayout());		
		add(g_panel, BorderLayout.CENTER);
		add(chat_server, BorderLayout.SOUTH);
		
		Thread t2 = new Thread(g_panel);
		t2.start();

		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
	}
}
