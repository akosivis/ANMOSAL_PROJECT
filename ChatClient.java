import proto.PlayerProtos.Player;
import proto.TcpPacketProtos.TcpPacket.*;
import proto.TcpPacketProtos.TcpPacket;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ChatClient{
    private final static String server_ip = "202.92.144.45";
    private final static int port = 80;
    private static String lobbyId = "AB1L";

    private Socket openConnection(String server, int port){
        Socket socket = null;
        try{
            socket = new Socket(server, port);
        } catch (IOException e) {
            System.out.println(e);
        }

        return socket;
    }

    private CreateLobbyPacket getLobbyPacket(){   
        Scanner sc = new Scanner(System.in);
        System.out.println("Max no. of players: ");
        int max = sc.nextInt();

        CreateLobbyPacket.Builder create = CreateLobbyPacket.newBuilder();
        create.setType(PacketType.CREATE_LOBBY).setMaxPlayers(max);

        return create.build();
    }

    private ConnectPacket getConnectPacket(){
        Player.Builder player = Player.newBuilder().setName("Mojica");

        ConnectPacket.Builder connect = ConnectPacket.newBuilder();
        connect.setType(PacketType.CONNECT).setPlayer(player).setLobbyId(this.lobbyId);

        return connect.build();
    }

    /*https://stackoverflow.com/questions/1264709/convert-inputstream-to-byte-array-in-java*/
    private void clickCreateLobby(OutputStream output, InputStream input){
        try{
            output.write(getLobbyPacket().toByteArray());
            
            while(input.available() == 0){}

            byte[] response = new byte[input.available()];
            input.read(response);
            CreateLobbyPacket response_parsed = CreateLobbyPacket.parseFrom(response);

            String lobbyId = response_parsed.getLobbyId();
            System.out.println(lobbyId);

            this.lobbyId = lobbyId;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clickConnectToLobby(OutputStream output, InputStream input){
        try{
            output.write(getConnectPacket().toByteArray());

            while(input.available() == 0) {}

            byte[] response = new byte[input.available()];
            input.read(response);
            ConnectPacket response_parsed = ConnectPacket.parseFrom(response);

            System.out.println(response_parsed);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String args[]){
        try{
            ChatClient client = new ChatClient();
            Socket socket = client.openConnection(server_ip, port);
            System.out.println("Just connected to " + socket.getRemoteSocketAddress());

            OutputStream output = socket.getOutputStream();
            InputStream input = socket.getInputStream();

            client.clickCreateLobby(output, input);
            client.clickConnectToLobby(output, input);

            output.close();
            input.close();
            socket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
