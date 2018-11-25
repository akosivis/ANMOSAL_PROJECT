import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class GameServer implements Runnable{

	int playerCount=0;
	int[][] map1 = 
			  {{0,3,3,3,3,0,0,0,0,0,0,0,0,0,0,3,3,3,3,0},
			   {0,3,3,3,3,0,0,0,0,0,0,0,0,0,0,3,3,3,3,0},
			   {0,3,3,3,3,0,0,0,0,0,0,0,0,0,0,3,3,3,3,0},
			   {0,0,0,0,0,0,0,0,3,0,0,3,0,0,0,0,0,0,0,0},
			   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			   {0,0,0,0,0,0,0,0,3,0,0,3,0,0,0,0,0,0,0,0},
			   {0,3,3,3,3,0,0,0,0,0,0,0,0,0,0,3,3,3,3,0},
			   {0,3,3,3,3,0,0,0,0,0,0,0,0,0,0,3,3,3,3,0},
			   {0,3,3,3,3,0,0,0,0,0,0,0,0,0,0,3,3,3,3,0}};
	
	DatagramSocket serverSocket = null;
	
	byte[] map1Byte;
	
	int port = 4444;
	
	 byte[] receiveData = new byte[1024];
     byte[] sendData = new byte[1024];
	
	public GameServer(){
       
		convertMapToStringToByte(map1);
		try {
            serverSocket = new DatagramSocket(port);
			serverSocket.setSoTimeout(100);
		} catch (IOException e) {
            System.exit(-1);
		}

	}
	
	public GameServer(int portNum, int playersNum){
		
		try {
            serverSocket = new DatagramSocket(portNum);
			serverSocket.setSoTimeout(100);
		} catch (IOException e) {
            System.exit(-1);
		}
	}
	public void convertMapToStringToByte(int[][] map2D){
		String mapString = "";
		for(int y = 0; y < map2D.length; y++){
			for(int x = 0; x < map2D[0].length; x++){
				mapString = mapString + map2D[y][x];
			}
		}
		
		map1Byte = mapString.getBytes();
	}
	
	public void sendData(){
		InetAddress address;
		byte[] sendDataPacket = map1Byte;
//		System.out.println(sendDataPacket.length);
		try {
			address = InetAddress.getByName("localhost");
			DatagramPacket sendPacket =
	                new DatagramPacket(sendDataPacket, sendDataPacket.length, address, 4443);
			this.serverSocket.send(sendPacket);
//			System.out.println(sendPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void receiveData(DatagramPacket rcvPacket){
		System.out.println();
		// what to consider from receiving
		// player id , length of the buffer, eachplayer.x and eachplayer.y, the entire map
		// 
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
//			sendPacket = new DatagramPacket(buf, buf.length, player.getAddress(),player.getPort());
			try{
//				serverSocket.receive(packet);
//				receiveData(packet);
				sendData();
//				System.out.println(packet.getData());
//				serverSocket.send(sendPacket);
			}catch(Exception ioe){}
		}
	}
}
