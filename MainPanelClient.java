import java.awt.BorderLayout;

import javax.swing.JFrame;

public class MainPanelClient extends JFrame{
	
	ChatForClient chat_client;

	public MainPanelClient(String name, String addr, int port, int player, int team) {
		GamePanel g_panel =  new GamePanel(name, addr,port,player, true, team);
		chat_client = new ChatForClient();
		this.setLayout(new BorderLayout());
		
		add(g_panel, BorderLayout.CENTER);
		add(chat_client, BorderLayout.SOUTH);

		Thread t3 = new Thread(g_panel);
		t3.start();
		
		setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
	}
}
