import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class GameServer implements Runnable{

	int dataLength = 10;
	int playerCount=0;
	
	byte[] receiveData = new byte[1024];
    byte[] sendData = new byte[1024];
	
	DatagramSocket serverSocket = null;

	int port = 4444;
	int sendPort;
	
	String sendAddress;
	String filler="";
	
    StringBuffer playerData = new StringBuffer();
    int maxPlayers;
    
    int gameStatus = 0;
    
    String[][] connectedDetails; // stores all addresses and Ports to check if the connection is unique

	public GameServer( int playersNum, int portNum){
		maxPlayers = playersNum;
		this.port = portNum;
		connectedDetails = new String[playersNum][2];
		
		for(int j=0; j < dataLength; j++){
			filler+="X";
		}
		for(int i=0; i < playersNum; i++) {
			playerData.append(i+filler); // i is the id
		}
//		System.out.println(playerData);
//		convertMapToStringToByte(map1);
		try {
			connectedDetails[0][0] = InetAddress.getLocalHost().getHostAddress();
			connectedDetails[0][1] = (port-1)+"";
			playerCount++;
			
			
            serverSocket = new DatagramSocket(portNum);
			serverSocket.setSoTimeout(50);
		} catch (IOException e) {
            System.exit(-1);
		}
	}


	public void convertData(byte[] received, int id){
		try {
			String rcvData = new String(received,"UTF-8");

			int start = (id * dataLength) + (id+1);
			int end = start + (dataLength);
//			System.out.println(start+ ":" + end);
			playerData.replace(start,end,rcvData.substring(0,dataLength));
			
//			System.out.println(playerData);

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
			
			int id=0;
			
			
			for(int i = 0; i < playerCount; i++){
//				System.out.println("sa:" + sendAddress + " sp:" + sendPort);
//				System.out.println("sa:" + connectedDetails[i][0].equals(sendAddress) + " sp:" + Integer.parseInt(connectedDetails[i][1])== sendPort));
				if(connectedDetails[i][0].equals(sendAddress) == true && Integer.parseInt(connectedDetails[i][1]) == sendPort) {
						id = i;
//						System.out.println("Already connected. index:"+i);
						break;
				}
				else if( connectedDetails[i][0].equals(sendAddress) == true && Integer.parseInt(connectedDetails[i][1]) != sendPort && playerCount < maxPlayers && i==playerCount-1) {
						//new Connection
						connectedDetails[i+1][0] = sendAddress;
						connectedDetails[i+1][1] = sendPort+"";
						playerCount++;
						System.out.println("new Connection!! pc:" + playerCount );
						break;
	
				}
				else if(connectedDetails[i][0].equals(sendAddress) == false) {
						//new Connection
					if(playerCount < maxPlayers) {
						connectedDetails[i+1][0] = sendAddress;
						connectedDetails[i+1][1] = sendPort+"";
						playerCount++;
						System.out.println("new Connection!");
					}
				}
			}

			if(rcvPacket.getData()!=null)
				convertData(rcvPacket.getData(), id);
			else
				System.out.println("empty data");


		} catch(SocketTimeoutException ste){}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendData(){
		InetAddress address;
		DatagramPacket sendPacket;
		StringBuffer sendString = new StringBuffer();
		
		sendString.append(gameStatus+""+playerData);
//		System.out.println("sending: " +sendString);
		byte[] sendDataPacket = sendString.toString().getBytes();
		
			try {
				for(int id = 0; id < playerCount; id++) {
				address = InetAddress.getByName(connectedDetails[id][0]);
				sendPort = Integer.parseInt(connectedDetails[id][1]);

				sendPacket =  new DatagramPacket(sendDataPacket, sendDataPacket.length, address, sendPort);
				serverSocket.send(sendPacket);
//				System.out.println("sent?");
				}
			} catch(SocketTimeoutException exception){
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

		
//			System.out.println("sent");
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			if(playerCount==maxPlayers) {
				gameStatus = 1;
			}
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
