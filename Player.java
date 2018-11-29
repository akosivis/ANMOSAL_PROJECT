import java.awt.Rectangle;
import java.net.InetAddress;

public class Player extends Rectangle{
	int id; int speed = 64; int diameter = 32;
	int x; int y;
	
	int port;
	int isClient;
	InetAddress address;
	
	boolean movementEnabled = true;
	
	
	
	
	public Player(int x, int y, int tileDim, int port, InetAddress addr, int client){
		this.x = x;
		this.y = y;
		this.port = port;
		this.address = addr;
		this.isClient = client;
		speed = tileDim;
		diameter = tileDim/2;
	}
	
	public String dataToString() {
		String data = "";
		
		data+= isClient;
		data+= id;
//		data+= port;
//		data+= address;
		
		if(x<10)
			data+= "00"+x;
		else if(x<100)
			data+= "0"+ x;
		else
			data+= x;
		
		if(y<10)
			data+= "00"+y;
		else if(y<100)
			data+= "0"+ y;
		else
			data+= y;
		
		System.out.println(data.length());
		return data;
	}
	public byte[] dataToByte(){
		String data = this.dataToString();
		
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
