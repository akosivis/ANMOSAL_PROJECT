import java.awt.Rectangle;

public class Player extends Rectangle{
	int id;
	int x;
	int y;
	int speed = 20;
	int diameter = 32;
	
	public Player(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void moveUP(){
		if(y - speed >= 0) {
			y -= speed;
		}
		
		update();
	}
	
	public void moveDown(){
		if(y + diameter + speed <= 800)
			y += speed;

		update();
	}
	
	public void moveLeft(){
		if(x - speed >= 0)
			x -= speed;

		update();
	}
	
	public void moveRight(){
		if(x + diameter + speed <= 800)
			x += speed;

		update();
	}
	
	public void eat(){
		diameter++;
		//speed gets reduced by 1 per increase of size 20
		
		if(speed!=1 && (diameter-50)%20==0)
			speed--;
		
//		System.out.println(speed);
		update();
	}
	
	public void update(){
		this.setLocation(x,y);
		this.setSize(diameter,diameter);
	}
}
