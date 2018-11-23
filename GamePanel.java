import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements KeyListener, ActionListener{

	Player player;
	Timer refreshrate;
	Timer roundTimer;
	
	BufferedImage tiles;
	String direction = "NONE";
	
	int team = 1;
	int timer = 30;
	
	int PlayerTileX;
	int PlayerTileY;
	
	boolean movementEnabled = true;
	
	int[][] map = {{0,0,0,0,0,0,0,0,0,0},
				   {0,0,0,0,0,0,0,0,0,0},
				   {0,0,0,0,0,0,0,0,0,0},
				   {0,0,0,3,0,0,3,0,0,0},
				   {0,0,0,0,0,0,0,0,0,0},
				   {0,0,0,0,0,0,0,0,0,0},
				   {0,0,0,3,0,0,3,0,0,0},
				   {0,0,0,0,0,0,0,0,0,0},
				   {0,0,0,0,0,0,0,0,0,0},
				   {0,0,0,0,0,0,0,0,0,0}};
	
	Rectangle[][] collisionRect = new Rectangle[10][10]; 
	
	public GamePanel(){
	
		try {
			tiles = ImageIO.read(new File("img/TileSet.png"));
		} catch (Exception e){

		}
	
		this.setPreferredSize(new Dimension(640,640));
		this.setFocusable(true);
		this.requestFocusInWindow();
		this.addKeyListener(this);
		
		player = new Player(16,16);
		
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
			for(int mapx = 0; mapx < map.length; mapx++){
				g.drawImage(tiles.getSubimage(map[mapy][mapx] * 64, 0, 64, 64), 64 * mapx, 64 * mapy,this);
				collisionRect[mapy][mapx] = new Rectangle(mapx * 64, mapy * 64 ,64,64);
				
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
			if(player.y - 64 > 0){
				direction = "U";
				if(map[PlayerTileY-1][PlayerTileX]!=3)
					player.y = player.y - 64;
			}
			this.repaint();
		}
		
		if(movementEnabled==true){
			if(e.getKeyCode() == KeyEvent.VK_S){
				if(player.y + 64 < this.getHeight()){
					direction = "D";
					if(map[PlayerTileY+1][PlayerTileX]!=3)
					player.y = player.y + 64;
				}
				this.repaint();
			}
			
			if(e.getKeyCode() == KeyEvent.VK_D){
				if(player.x + 64 < this.getWidth()){
					direction = "R";
					if(map[PlayerTileY][PlayerTileX+1]!=3)
						player.x = player.x + 64;
				}
				this.repaint();
			}
			
			if(e.getKeyCode() == KeyEvent.VK_A){
				if(player.x - 64 > 0){
					direction = "L";
					if(map[PlayerTileY][PlayerTileX-1]!=3)
					player.x = player.x - 64;
				}
				this.repaint();
			}
			
			if(e.getKeyCode() == KeyEvent.VK_Q){
				team = (team % 2 ) + 1;
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
			PlayerTileX = (player.x) / 64;
			PlayerTileY = (player.y) / 64;
			
			map[PlayerTileY][PlayerTileX] = team;

			if(timer == 0){
				movementEnabled = false;
				direction = "N";
			}
		}
		
		this.repaint();
		
	}

}
