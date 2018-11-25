import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements KeyListener, ActionListener, Runnable{

	Player player;
	Timer refreshrate;
	Timer roundTimer;
	
	BufferedImage tiles;
	String direction = "NONE";
	
	int team = 1;
	int timer = 30;
	int tileDimension = 32;
	
	int PlayerTileX;
	int PlayerTileY;
	
	boolean movementEnabled = true;
	
	int[][] map= new int[10][20];
	
	byte[] sendData = new byte[1024];
	DatagramSocket clientSocket = null;
//	Rectangle[][] collisionRect = new Rectangle[10][10]; 
	
	public GamePanel(){
	
		try {
			tiles = ImageIO.read(new File("img/TileSet.png"));
		} catch (Exception e){

		}
	
		this.setPreferredSize(new Dimension(tileDimension * 20,tileDimension * 10));
		this.setFocusable(true);
		this.requestFocusInWindow();
		this.addKeyListener(this);
		
		player = new Player( tileDimension/4, tileDimension/4, tileDimension);
		
		try {
			clientSocket = new DatagramSocket(4443, InetAddress.getByName("localhost"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		refreshrate = new Timer (100,this);
		roundTimer =  new Timer (1000,this);
		
		roundTimer.start();
		refreshrate.start();
		
		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		int blueCount = 0;
		int redCount =  0;
		
		for(int mapy = 0; mapy < map.length; mapy++){
			for(int mapx = 0; mapx < map[0].length; mapx++){
				g.drawImage(tiles.getSubimage(map[mapy][mapx] * 64, 0, 64, 64), tileDimension * mapx, tileDimension * mapy,this);
//				collisionRect[mapy][mapx] = new Rectangle(mapx * tileDimension, mapy * tileDimension ,tileDimension,tileDimension);
				
				if(map[mapy][mapx] == 1){
					redCount++;
				}else if(map[mapy][mapx] == 2){
					blueCount++;
				}
			}
		}
		// Draws hitbox for player(s)
		g.drawRect(player.x, player.y, player.diameter, player.diameter);
		
		g.setColor(Color.GRAY);
		g.drawString(timer + "", 300 , 20);
		
		if(timer == 0){
			showScore(g, redCount,blueCount);
		}
	}
	public void convertData(byte[] received){
		try {
			String rcvData = new String(received,"UTF-8");
//			System.out.println(rcvData);
			// Updates the map
			for(int mapy = 0; mapy < map.length; mapy++){
//				System.out.println(rcvData.substring(,mapy * map.length + map[0].length));	
				for(int mapx = 0; mapx < map[0].length; mapx++){
					map[mapy][mapx] = Integer.parseInt(rcvData.substring((mapy * map[0].length) + mapx, (mapy * map[0].length) + mapx+1));
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void showScore(Graphics g, int red, int blue){
		g.setColor(Color.decode("#ff5441"));
		g.fillRect(0, 0, this.getWidth()/2, this.getHeight());
		
		g.setColor(Color.decode("#806fff"));
		g.fillRect(this.getWidth()/2, 0, this.getWidth()/2, this.getHeight());
	
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));;
		g.setColor(Color.WHITE);
		g.drawString(red + "", this.getWidth()/4 - 40 , this.getHeight()/2 - 40);
		g.drawString(blue + "", (3 * this.getWidth())/4 - 40, this.getHeight()/2 - 40);
	}
	
	@Override
	public void keyPressed(KeyEvent k1) {
		
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		
		if(e.getKeyCode() == KeyEvent.VK_W){
			if(player.y - tileDimension > 0){
				direction = "U";
				if(map[PlayerTileY-1][PlayerTileX]!=3)
					player.y = player.y - tileDimension;
			}
			this.repaint();
		}
		
		if(movementEnabled==true){
			if(e.getKeyCode() == KeyEvent.VK_S){
				if(player.y + tileDimension < this.getHeight()){
					direction = "D";
					if(map[PlayerTileY+1][PlayerTileX]!=3)
					player.y = player.y + tileDimension;
				}
				this.repaint();
			}
			
			if(e.getKeyCode() == KeyEvent.VK_D){
				if(player.x + tileDimension < this.getWidth()){
					direction = "R";
					if(map[PlayerTileY][PlayerTileX+1]!=3)
						player.x = player.x + tileDimension;
				}
				this.repaint();
			}
			
			if(e.getKeyCode() == KeyEvent.VK_A){
				if(player.x - tileDimension > 0){
					direction = "L";
					if(map[PlayerTileY][PlayerTileX-1]!=3)
					player.x = player.x - tileDimension;
				}
				this.repaint();
			}
			
			if(e.getKeyCode() == KeyEvent.VK_Q){
				team = (team % 2 ) + 1;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_E){
				System.out.println(player.dataToString());
				 try {
					String decode = new String(player.dataToString(),"UTF-8");
					System.out.println(decode);
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
			}
		}
		
}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent k1) {
		// TODO Auto-generated method stub
		
		
		if(k1.getSource() == roundTimer){
			if(timer > 0)
				timer= timer -1;
			else{
				roundTimer.stop();
			}
		}
		
		if(k1.getSource()== refreshrate){
			PlayerTileX = (player.x) / tileDimension;
			PlayerTileY = (player.y) / tileDimension;
			
			map[PlayerTileY][PlayerTileX] = team;

			if(timer == 0){
				movementEnabled = false;
				direction = "N";
			}
		}
		
		this.repaint();
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		while(true){
			
			try { 
//				InetAddress IPAddress = InetAddress.getByName("localhost");
				byte[] receiveData = new byte[1024];

				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

				clientSocket.receive(receivePacket);
				
				convertData(receivePacket.getData());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
