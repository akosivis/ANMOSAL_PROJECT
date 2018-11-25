import java.awt.Dimension;

import javax.swing.JFrame;

public class MainPanelServer extends JFrame{

	GamePanel g_panel;
	GameServer g_server;
	
	public MainPanelServer(){
		g_panel =  new GamePanel();
		g_server = new GameServer();
		
		add(g_panel);
		new Thread(g_server).start();
		Thread t2 = new Thread(g_panel);
		t2.start();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		pack();
	}
}
