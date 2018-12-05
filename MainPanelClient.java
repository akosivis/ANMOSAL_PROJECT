import javax.swing.JFrame;

public class MainPanelClient extends JFrame{

	public MainPanelClient(String addr, int port) {
		GamePanel g_panel =  new GamePanel(addr,port,true);
		
		add(g_panel);
		Thread t3 = new Thread(g_panel);
		t3.start();
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.pack();
	}
}
