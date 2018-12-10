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
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements KeyListener, ActionListener, Runnable{

	Player player;
	Player[] otherPlayers;
	int plyDataLength=0;
	Timer refreshrate;
	Timer roundTimer;
	Timer roundTransition;
	
	BufferedImage tiles;
	
	String direction = "NONE";
	
	int[] roundWinner = new int[3];
	int currentRound =1;
	int maxPlayers = 0;
	int gameStatus = 0;
	int team = 1;
	int timer = 50 , transition = 3;
	int tileDimension = 32;
	int connectedPlayers = 1; // 1 because you are connected already
	int PlayerTileX;
	int PlayerTileY;
	int tCenter; 	// tile Center
	boolean isClient;
	boolean movementEnabled = false; // disables movement
	
	int[][] map = {{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			   {0,3,3,3,3,0,0,0,0,0,0,0,0,0,0,3,3,3,3,0},
			   {0,3,3,3,3,0,0,0,0,0,0,0,0,0,0,3,3,3,3,0},
			   {0,0,0,0,0,0,0,0,3,0,0,3,0,0,0,0,0,0,0,0},
			   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			   {0,0,0,0,0,0,0,0,3,0,0,3,0,0,0,0,0,0,0,0},
			   {0,3,3,3,3,0,0,0,0,0,0,0,0,0,0,3,3,3,3,0},
			   {0,3,3,3,3,0,0,0,0,0,0,0,0,0,0,3,3,3,3,0},
			   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};;
	
	int[][] map2 = {{4,0,0,0,0,0,0,3,3,3,3,3,3,0,0,0,0,0,0,4},
					{0,0,0,0,0,0,0,0,3,3,0,0,0,0,0,0,0,0,0,0},
					{3,0,0,0,0,0,0,0,3,3,0,0,0,0,0,0,0,0,0,3},
					{3,0,3,3,3,3,0,0,3,3,0,0,0,0,0,0,0,0,0,3},
					{3,0,3,3,3,3,0,0,0,0,0,0,0,0,0,0,0,0,0,3},
					{3,0,0,0,0,0,0,0,0,0,0,0,0,0,3,3,3,3,3,3},
					{3,0,0,0,0,0,0,0,0,0,3,3,0,0,3,3,3,3,3,3},
					{3,0,0,0,0,0,0,0,0,0,3,3,0,0,0,0,0,0,0,3},
					{0,0,0,0,0,0,0,0,0,0,3,3,0,0,0,0,0,0,0,0},
					{4,0,0,0,0,0,0,3,3,3,3,3,3,0,0,0,0,0,0,4}};
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
		
		tCenter = (tileDimension/12);
		otherPlayers = new Player[numPlayers-1];
		
		// Loads the tileset 
		try {
			tiles = ImageIO.read(new File("img/TileSet.png"));
			
		} catch (Exception e){

		}
	
		this.setPreferredSize(new Dimension(tileDimension * 20, (tileDimension * 10) + 30));
		this.setFocusable(true);
		this.requestFocusInWindow();
		this.addKeyListener(this);
		
		// Finds a spawn point
		int xSpawn = 0;
		int ySpawn = 0;
		
		Random spawn = new Random();
		xSpawn = spawn.nextInt(20);
		ySpawn = spawn.nextInt(10);
		
		while(map[ySpawn][xSpawn]==3) {
			xSpawn = spawn.nextInt(20);
			ySpawn = spawn.nextInt(10);
		}
		
		// Spawns players
		System.out.println(xSpawn + ":" + ySpawn);
		for(int i=0;i<numPlayers-1; i++) {
			otherPlayers[i] = new Player("???", 999, 999, tileDimension, 0);
		}
		

		try {
			if(isClient == true)
				player = new Player( name, tCenter, tCenter, tileDimension , port, InetAddress.getLocalHost(),1, team);
			else
				player = new Player( name, 1 + (tileDimension*xSpawn), tCenter+ (tileDimension*ySpawn), tileDimension , port, InetAddress.getLocalHost(),0, team);
			
			plyDataLength = player.dataToString().length();
			
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
		refreshrate = new Timer (5,this);
		roundTimer =  new Timer (1000,this);
		roundTransition = new Timer (3000,this);
		
//		roundTimer.start();
		refreshrate.start();
		
		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		int blueCount = 0;
		int redCount =  0;
		
		for(int mapy = 0; mapy < map.length; mapy++){
			for(int mapx = 0; mapx < map[0].length; mapx++){
				g.drawImage(tiles.getSubimage(map[mapy][mapx] * 64, 0, 64, 64), tileDimension * mapx, tileDimension * mapy, tileDimension, tileDimension,this);
				
				if(map[mapy][mapx] == 1){
					redCount++;
				}else if(map[mapy][mapx] == 2){
					blueCount++;
				}
			}
		}
		
		// Render other players
		for(int i = 0; i < otherPlayers.length;i++) {
			g.drawImage(tiles.getSubimage((4 + otherPlayers[i].team) * 64, 0, 64, 64), otherPlayers[i].x, otherPlayers[i].y, tileDimension, tileDimension,this);
			g.drawString(otherPlayers[i].name, otherPlayers[i].x + 2 , otherPlayers[i].y + tileDimension + 3);
		}
		
		// Render Current Player (always on top of others)
		g.drawImage(tiles.getSubimage((4 + team) * 64, 0, 64, 64), player.x, player.y, tileDimension, tileDimension,this);
		g.drawString(player.name, player.x + 2 , player.y + tileDimension + 3);
		

		g.setColor(Color.BLACK);
		if(timer<20) {
			g.setColor(Color.RED);
		}
		
		g.drawString(timer + "", 300 , (tileDimension * 10) + 15);
		
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
				if(player.y + tileDimension < this.getHeight() - 30){
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
			// Checks if all players are connected
			if(connectedPlayers == maxPlayers) {
				for(int j = 0; j < maxPlayers - 1; j++) {
					int opTileX = otherPlayers[j].x / tileDimension;
					int opTileY = otherPlayers[j].y / tileDimension;
					
					map[opTileY][opTileX] = otherPlayers[j].team;
					
					PlayerTileX = (player.x) / tileDimension;
					PlayerTileY = (player.y) / tileDimension;
					
					map[PlayerTileY][PlayerTileX] = team;
					
					movementEnabled = true;
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
