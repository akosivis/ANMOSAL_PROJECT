import javax.swing.JFrame;

public class MainPanelClient extends JFrame{

	public MainPanelClient(String name, String addr, int port, int player, int team) {
		GamePanel g_panel =  new GamePanel(name, addr,port,player, true, team);
		
		add(g_panel);
		Thread t3 = new Thread(g_panel);
		t3.start();
		
		setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
	}
}
