import java.awt.BorderLayout;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;

public class MainPanelServer extends JFrame {

	GamePanel g_panel;
	GameServer g_server;
	ChatForServer chat_server;
	
	public MainPanelServer(String name, int player, int port, int team , String address) throws UnknownHostException{
	
		System.out.println(address);
		g_panel =  new GamePanel(name, address, 4443, player, false, team);
		
<<<<<<< HEAD
		g_server = new GameServer(player, port);
		chat_server = new ChatForServer();
=======
		
		g_server = new GameServer(player, port, address);
>>>>>>> 960be86c1aa635527fd8553feda00086c7b88cc2
		new Thread(g_server).start();
		
		this.setLayout(new BorderLayout());		
		add(g_panel, BorderLayout.CENTER);
		add(chat_server, BorderLayout.SOUTH);
		
		Thread t2 = new Thread(g_panel);
		t2.start();
<<<<<<< HEAD

=======
		
		this.setTitle("Conquer! Connecting to: " + address);
>>>>>>> 960be86c1aa635527fd8553feda00086c7b88cc2
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
	}
}
