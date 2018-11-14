import javax.swing.JFrame;

public class MainPanel extends JFrame{
	
	public void initialize(){

		GamePanel g_panel =  new GamePanel();
		
		this.add(g_panel);
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
}
