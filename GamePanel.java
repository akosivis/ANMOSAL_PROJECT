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
	Player[] otherPlayers;
	int plyDataLength=0;
	Timer refreshrate;
	Timer roundTimer;
	
	BufferedImage tiles;
	
	String direction = "NONE";
	
	int maxPlayers = 0;
	int gameStatus = 0;
	int team = 1;
	int timer = 100;
	int tileDimension = 32;
	int connectedPlayers = 1;
	int PlayerTileX;
	int PlayerTileY;
	int tCenter; 	// tile Center
	boolean isClient;
	boolean movementEnabled;
	
	int[][] map = {{0,3,3,3,3,0,0,0,0,0,0,0,0,0,0,3,3,3,3,0},
			   {0,3,3,3,3,0,0,0,0,0,0,0,0,0,0,3,3,3,3,0},
			   {0,3,3,3,3,0,0,0,0,0,0,0,0,0,0,3,3,3,3,0},
			   {0,0,0,0,0,0,0,0,3,0,0,3,0,0,0,0,0,0,0,0},
			   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			   {0,0,0,0,0,0,0,0,3,0,0,3,0,0,0,0,0,0,0,0},
			   {0,3,3,3,3,0,0,0,0,0,0,0,0,0,0,3,3,3,3,0},
			   {0,3,3,3,3,0,0,0,0,0,0,0,0,0,0,3,3,3,3,0},
			   {0,3,3,3,3,0,0,0,0,0,0,0,0,0,0,3,3,3,3,0}};;
		  
	String mapData;
	
	byte[] sendData = new byte[1024];
	
	DatagramSocket clientSocket = null;
	String addr;
	
	DatagramPacket receivePacket; 
	DatagramPacket sendPacket;
	
	int port;
	
	//gamePanel with Parameters from Main
	public GamePanel(int numOfPlayers, int id, int port, int addr){
		
	}
	
	public GamePanel(String name, String addr, int port, int numPlayers, boolean isClient, int team){
		
		this.maxPlayers = numPlayers;
		this.addr = addr;
		this.port = port;
		this.team = team;
		this.isClient = isClient;
		
		tCenter = (tileDimension/4);
		otherPlayers = new Player[numPlayers-1];
		
		try {
			tiles = ImageIO.read(new File("img/TileSet.png"));
			
		} catch (Exception e){

		}
	
		this.setPreferredSize(new Dimension(tileDimension * 20,tileDimension * 10));
		this.setFocusable(true);
		this.requestFocusInWindow();
		this.addKeyListener(this);
		
		for(int i=0;i<numPlayers-1; i++) {
			otherPlayers[i] = new Player("???", 999, 999, tileDimension, 0);
		}
		
		try {
			if(isClient == true)
				player = new Player( name, tCenter, tCenter, tileDimension , port, InetAddress.getLocalHost(),1, team);
			else
				player = new Player( name, tCenter + (tileDimension*9), tCenter, tileDimension , port, InetAddress.getLocalHost(),0, team);
			
			plyDataLength = player.dataToString().length();
			movementEnabled = player.movementEnabled;
			
			byte[] receiveData = new byte[1024];
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket = new DatagramSocket(player.port, player.address);
//			System.out.println(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		refreshrate = new Timer (10,this);
		roundTimer =  new Timer (1000,this);
		
//		roundTimer.start();
		refreshrate.start();
		
		
	}
	
	public String convertMapToString(int[][] map2D){
		String mapString = "";
		for(int y = 0; y < map2D.length; y++){
			for(int x = 0; x < map2D[0].length; x++){
				mapString = mapString + map2D[y][x];
			}
		}
		
		return mapString;
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
		
		for(int i = 0; i < otherPlayers.length;i++) {
			g.drawRect(otherPlayers[i].x, otherPlayers[i].y, otherPlayers[i].diameter, otherPlayers[i].diameter);
		}
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

			gameStatus = Integer.parseInt(rcvData.substring(0,1));
			
			for(int p = 0; p < maxPlayers; p++){
				int start = (p*plyDataLength) + p+2;
				int end   = start + plyDataLength;
				
				if(rcvData.substring(start,start+3).equals(player.name)== true) {
					continue;
				}else {
					for(int j = 0; j < maxPlayers - 1; j++) {
//						System.out.println(rcvData);
						if(otherPlayers[j].name.equals(rcvData.substring(start,start+3))==true){
//							System.out.println("player exists");
							otherPlayers[j].x = Integer.parseInt(rcvData.substring(start+4,start+7));
							otherPlayers[j].y = Integer.parseInt(rcvData.substring(start+7,start+10));
							otherPlayers[j].update();
							break;
//							System.out.println("tik");
							
						}else if(otherPlayers[j].name.equals("???")==true && rcvData.substring(start,start+3).equals("XXX")==false) {
							otherPlayers[j].name = rcvData.substring(start,start+3);
							otherPlayers[j].team = Integer.parseInt(rcvData.substring(start+3,start+4));
							otherPlayers[j].x = Integer.parseInt(rcvData.substring(start+4,start+7));
							otherPlayers[j].y = Integer.parseInt(rcvData.substring(start+7,start+10));
							otherPlayers[j].update();
							
							connectedPlayers++;
							System.out.println(rcvData.substring(start,start+3) + "::" + otherPlayers[j].name + ":::" + j + "-" + p + "cp:" + connectedPlayers);
							j=maxPlayers;
//							System.out.println(connectedPlayers);
							break;
						}
					}
				}
				
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("-----------------");
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
			
//			if(e.getKeyCode() == KeyEvent.VK_Q){
//				team = (team % 2 ) + 1;
//			}
			
			if(e.getKeyCode() == KeyEvent.VK_E){
				 try {
					String decode = new String(sendData,"UTF-8");
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
			if(connectedPlayers == maxPlayers) {
				for(int j = 0; j < maxPlayers - 1; j++) {
					int opTileX = otherPlayers[j].x / tileDimension;
					int opTileY = otherPlayers[j].y / tileDimension;
					
					map[opTileY][opTileX] = otherPlayers[j].team;
					
					PlayerTileX = (player.x) / tileDimension;
					PlayerTileY = (player.y) / tileDimension;
					
					map[PlayerTileY][PlayerTileX] = team;
					
					roundTimer.start();
				} 
			}
			
			

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
		if(gameStatus == 1) {
			roundTimer.start();
		}
		while(true){
			try { 
				String fullData = player.dataToString();
//				System.out.println(fullData);
				sendData = fullData.getBytes();
				sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(addr), 4444);
				clientSocket.send(sendPacket);
//				System.out.println("sent");
				
				clientSocket.receive(receivePacket);
				convertData(receivePacket.getData());
				
//			    clientSocket.close();
//			    System.out.println("here");
			} catch(SocketException e1){}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
