import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class GameServer implements Runnable{

	int playerCount=0;
	int[][] map1 = new int[10][20];
	
	DatagramSocket serverSocket = null;
	
	byte[] map1Byte;
	
	int port = 4444;
	String sendAddress;
	int sendPort;
	
	 byte[] receiveData = new byte[1024];
     byte[] sendData = new byte[1024];
	
	public GameServer(){
       
//		convertMapToStringToByte(map1);
		
		try {
            serverSocket = new DatagramSocket(port);
			serverSocket.setSoTimeout(100);
		} catch (IOException e) {
            System.exit(-1);
		}

	}
	
	public GameServer( int playersNum, int portNum){
		this.port = portNum;
//		convertMapToStringToByte(map1);
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
	
	public void convertMapToString(int[][] map2D) {
		String mapString = "";
		for(int y = 0; y < map2D.length; y++){
			for(int x = 0; x < map2D[0].length; x++){
				mapString = mapString + map2D[y][x];
			}
		}
		System.out.println(mapString);
	}

	public void convertData(byte[] received){
		
		try {
			String rcvData = new String(received,"UTF-8");
			
//			System.out.println("converting data");
//			System.out.println(rcvData);
			
			// Updates the map
			String rcvDataMap = rcvData.substring(8); 
			if(Integer.parseInt(rcvData.substring(0,1))==0) {
				for(int mapy = 0; mapy < map1.length; mapy++){
					for(int mapx = 0; mapx < map1[0].length; mapx++){
						map1[mapy][mapx] = Integer.parseInt(rcvDataMap.substring((mapy * map1[0].length) + mapx, (mapy * map1[0].length) + mapx+1));
					}
				}
				convertMapToStringToByte(map1);
			}		
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void receiveData(DatagramPacket rcvPacket){
		try {
			serverSocket.receive(rcvPacket);
			sendAddress = rcvPacket.getAddress().getHostAddress();
			sendPort = rcvPacket.getPort();
//			String mapData = new String(rcvPacket.getData(),"UTF-8");
			if(rcvPacket.getData()!=null)
				convertData(rcvPacket.getData());
			else
				System.out.println("empty data");

//			convertMapToStringToByte(map1);
//			System.out.println("received");
		} catch(SocketTimeoutException ste){}
		catch (IOException e) {
			e.printStackTrace();
		}
		// what to consider from receiving
		// player id , length of the buffer, eachplayer.x and eachplayer.y, the entire map
		// 
	}
	
	public void sendData(){
		InetAddress address;
		DatagramPacket sendPacket;
		byte[] sendDataPacket = map1Byte;
//		try {
//			System.out.println(new String(sendDataPacket,"UTF-8"));
//		} catch (UnsupportedEncodingException e1) {
//			e1.printStackTrace();
//		}
		
//		for(int i = 1; i < 3; i++) {
			try {
				address = InetAddress.getLocalHost();
//				address = InetAddress.getByName("localhost");
				sendPacket =  new DatagramPacket(sendDataPacket, sendDataPacket.length, address, sendPort);
				serverSocket.send(sendPacket);
				System.out.println("sent?");
			} catch(SocketTimeoutException exception){
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
//		}
		
//			System.out.println("sent");
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
