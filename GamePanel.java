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
		
		player = new Player(0,0);
		
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
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_W){
			direction = "U";
			this.repaint();
		}
		
		if(movementEnabled==true){
			if(e.getKeyCode() == KeyEvent.VK_S){
				direction = "D";
				this.repaint();
			}
			
			if(e.getKeyCode() == KeyEvent.VK_D){
				direction = "R";
				this.repaint();
			}
			
			if(e.getKeyCode() == KeyEvent.VK_A){
				direction = "L";
				this.repaint();
			}
			
			if(e.getKeyCode() == KeyEvent.VK_Q){
				team = (team % 2 ) + 1;
			}
		}else{
			direction="N";
		}
}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent k1) {
		// TODO Auto-generated method stub
		switch (direction) {
		case "U":
			if(player.y >= 0)
				player.moveUP();
			break;
		case "D":
			if(player.y <= 640 - player.diameter)
				player.moveDown();
			break;
		case "L":
			if(player.x >= 0)
				player.moveLeft();
			break;
		case "R":
			if(player.x <= 640 - player.diameter)
				player.moveRight();
			break;
		default:
			break;
		}
		
		if(k1.getSource() == roundTimer){
			if(timer > 0)
				timer= timer -1;
			else{
				roundTimer.stop();
			}
		}
		
		if(k1.getSource()== refreshrate){
			int PlayerTileX = (player.x+32) / 64;
			int PlayerTileY = (player.y+40) / 64;
			
			if(timer == 0){
				movementEnabled = false;
				direction = "N";
			}
			
			for(int i = 0 ; i < collisionRect.length; i ++){
				for(int j = 0 ; j < collisionRect.length; j ++){
					if(player.intersects(collisionRect[i][j])){
						if(map[i][j]==3){
							switch (direction) {
							case "U":
								direction = "D";
								break;
							case "D":
								direction = "U";
								break;
							case "L":
								direction = "R";
								break;
							case "R":
								direction = "L";
								break;
							default:
								direction="N";
								break;
							}
						}
						else{
							map[i][j]= team;
						}
					}
	
				}
			}
		}
		
		this.repaint();
		
	}

}
