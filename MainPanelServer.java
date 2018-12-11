import java.awt.BorderLayout;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;

public class MainPanelServer extends JFrame {

	GamePanel g_panel;
	GameServer g_server;
	ChatUI chat_ui;
	
	public MainPanelServer(String name, int player, int port, int team , String address) throws UnknownHostException{
	
		System.out.println(address);
		g_panel =  new GamePanel(name, address, address, 4443, player, false, team);
		//chat_ui = new ChatUI(name);
		chat_ui = new ChatUI();
		
		g_server = new GameServer(player, port, address);
		new Thread(g_server).start();
		
		this.setLayout(new BorderLayout());		
		add(g_panel, BorderLayout.CENTER);
		add(chat_ui, BorderLayout.SOUTH);
		
		Thread t2 = new Thread(g_panel);
		t2.start();
		
		this.setTitle("Conquer! Connecting to: " + address);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
	}
}
