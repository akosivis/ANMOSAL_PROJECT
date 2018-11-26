import java.awt.Rectangle;

public class Player extends Rectangle{
	int id;
	int x;
	int y;
	int port;
	String address;
	int speed = 64;
	int diameter = 32;
	
	public Player(int x, int y, int tileDim, int port, String addr){
		this.x = x;
		this.y = y;
		this.port = port;
		this.address = addr;
		speed = tileDim;
		diameter = tileDim/2;
	}
	
	public byte[] dataToString(){
		String data = "";
		
		data+= id;
		data+= port;
		data+= address;
		data+= x;
		data+= y;
		
		System.out.println(data.length());
		
		byte[] data_Byte = data.getBytes();
		
		return data_Byte;
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
