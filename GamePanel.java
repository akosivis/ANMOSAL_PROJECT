import java.awt.Dimension;
import java.awt.Graphics;
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
	BufferedImage tiles;
	String direction = "NONE";
	
	int[][] map = {{0,0,0,0,0,0,0,0,0,0},
				   {0,0,0,0,0,0,0,0,0,0},
				   {0,0,0,0,0,0,0,0,0,0},
				   {0,0,0,3,0,0,3,0,0,0},
				   {0,0,0,3,0,0,3,0,0,0},
				   {0,0,0,3,0,0,3,0,0,0},
				   {0,0,0,3,0,0,3,0,0,0},
				   {0,0,0,0,0,0,0,0,0,0},
				   {0,0,0,0,0,0,0,0,0,0},
				   {0,0,0,0,0,0,0,0,0,0}};
	
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
		refreshrate.addActionListener(this);
		refreshrate.start();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
//		System.out.println(map.length);
		
		for(int mapy = 0; mapy < map.length; mapy++){
			for(int mapx = 0; mapx < map.length; mapx++){
				g.drawImage(tiles.getSubimage(map[mapy][mapx] * 64, 0, 64, 64), 64 * mapx, 64 * mapy,this);
			}
		}
		g.drawRect(player.x, player.y, player.diameter, player.diameter);
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
			
		if(k1.getSource()== refreshrate){
			int PlayerTileX = (player.x+32) / 64;
			int PlayerTileY = (player.y+40) / 64;
			
			if(map[PlayerTileY][PlayerTileX]!=3)
				map[PlayerTileY][PlayerTileX] = 1;
			else
				direction = "N";
		}
		
		this.repaint();
		
	}

}
