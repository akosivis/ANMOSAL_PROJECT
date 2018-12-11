import java.awt.BorderLayout;

import javax.swing.JFrame;

public class MainPanelClient extends JFrame{
	ChatUI chat_ui;

	public MainPanelClient(String name, String addr, String ipaddr, int port, int player, int team) {
		GamePanel g_panel =  new GamePanel(name, addr, ipaddr,port,player, true, team);
		//ChatUI chat_ui = new ChatUI(name);
		chat_ui = new ChatUI();
		this.setLayout(new BorderLayout());
		
		add(g_panel, BorderLayout.CENTER);
		add(chat_ui, BorderLayout.SOUTH);
		Thread t3 = new Thread(g_panel);
		t3.start();
		
		setResizable(false);
		this.setTitle("Conquer! Connecting to: " + addr  );
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
	}
}
