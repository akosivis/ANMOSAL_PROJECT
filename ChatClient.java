import proto.PlayerProtos.Player;
import proto.TcpPacketProtos.*;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ChatClient{
    private final static String server_ip = "202.92.144.45";
    private final static int port = 80;


    private Socket openConnection(String server, int port){
        Socket socket = null;
        try{
            socket = new Socket(server, port);
        } catch (IOException e) {
            System.out.println(e);
        }

        return socket;
    }


    public static void main(String args[]){
        try{
            ChatClient client = new ChatClient();
            Socket socket = client.openConnection(server_ip, port);
            System.out.println("Just connected to " + socket.getRemoteSocketAddress());

            OutputStream output = socket.getOutputStream();
            InputStream input = socket.getInputStream();

            
            


            //create Lobby Packet
            TcpPacket.Builder packet = TcpPacket.newBuilder();
            packet.setType(TcpPacket.PacketType.CREATE_LOBBY);
            
            Scanner sc = new Scanner(System.in);
            System.out.println("Max no. of players: ");
            int max = sc.nextInt();

            TcpPacket.CreateLobbyPacket.Builder create = TcpPacket.CreateLobbyPacket.newBuilder();
            create.setType(TcpPacket.PacketType.CREATE_LOBBY).setMaxPlayers(max);
            //somepacket.writeDelimitedTo(output);
            //packet.build().writeTo();
            socket.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
