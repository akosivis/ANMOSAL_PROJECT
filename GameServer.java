import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

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
		} catch(SocketTimeoutException exception){
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

	public void convertData(byte[] received){
		try {
			String rcvData = new String(received,"UTF-8");
//			System.out.println(rcvData);
			// Updates the map
			for(int mapy = 0; mapy < map1.length; mapy++){
//				System.out.println(rcvData.substring(,mapy * map.length + map[0].length));	
				for(int mapx = 0; mapx < map1[0].length; mapx++){
					map1[mapy][mapx] = Integer.parseInt(rcvData.substring((mapy * map1[0].length) + mapx, (mapy * map1[0].length) + mapx+1));
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void receiveData(DatagramPacket rcvPacket){
		try {
			serverSocket.receive(rcvPacket);
//			String mapData = new String(rcvPacket.getData(),"UTF-8");
			convertData(rcvPacket.getData());
		} catch(SocketTimeoutException ste){}
		catch (IOException e) {
			e.printStackTrace();
		}
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
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try{
//				serverSocket.receive(packet);
				
				receiveData(receivePacket);
				sendData();

//				serverSocket.close();

			}catch(Exception ioe){}
		}
	}
}
