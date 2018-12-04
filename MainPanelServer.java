import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;

public class MainPanelServer extends JFrame {

	GamePanel g_panel;
	GameServer g_server;
	
	public MainPanelServer(int player, int port) throws UnknownHostException{
		String pubAddress = InetAddress.getLocalHost().getHostAddress();
		System.out.println(pubAddress);
		g_panel =  new GamePanel(pubAddress,4443,false);

		g_server = new GameServer(player, port);
		new Thread(g_server).start();
		
		add(g_panel);
		Thread t2 = new Thread(g_panel);
		t2.start();
		

		
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		pack();
	}
}
