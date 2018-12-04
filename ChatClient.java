import proto.PlayerProtos.Player;
import proto.TcpPacketProtos.TcpPacket.*;
import proto.TcpPacketProtos.TcpPacket;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class ChatClient{
    private final static String server_ip = "202.92.144.45";
    private final static int port = 80;
    private static boolean isConnected = false;
    private static String lobbyId = "AB1L";
    private static Player player;
    private static Scanner sc = new Scanner(System.in);

    private Socket openConnection(String server, int port){
        Socket socket = null;
        try{
            socket = new Socket(server, port);
            this.isConnected = true;
        } catch (IOException e) {
            System.out.println(e);
        }

        return socket;
    }

    private CreateLobbyPacket getLobbyPacket(){   
        System.out.print("Max no. of players: ");
        int max = this.sc.nextInt();

        CreateLobbyPacket.Builder create = CreateLobbyPacket.newBuilder();
        create.setType(PacketType.CREATE_LOBBY).setMaxPlayers(max);

        return create.build();
    }

    private ConnectPacket getConnectPacket(){
        //sample player
        Player.Builder player = Player.newBuilder().setName("Mojica");
        player.build();

        ConnectPacket.Builder connect = ConnectPacket.newBuilder();
        connect.setType(PacketType.CONNECT).setPlayer(player).setLobbyId(this.lobbyId);

        this.player = player.build();

        return connect.build();
    }

    private ChatPacket getChatPacket(){
        ChatPacket.Builder chat = ChatPacket.newBuilder();
        chat.setType(PacketType.CHAT).setPlayer(player).setLobbyId(this.lobbyId);

        String message = this.sc.nextLine();

        chat.setMessage(message);

        return chat.build();
    }

    private void clickCreateLobby(OutputStream output, InputStream input){
        try{
            output.write(getLobbyPacket().toByteArray());
            
            while(input.available() == 0){}

            /*https://stackoverflow.com/questions/1264709/convert-inputstream-to-byte-array-in-java*/
            byte[] response = new byte[input.available()];
            input.read(response);
            CreateLobbyPacket response_parsed = CreateLobbyPacket.parseFrom(response);

            String lobbyId = response_parsed.getLobbyId();
            this.lobbyId = lobbyId;

            System.out.println("Lobby " + this.lobbyId + " created.");
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

            System.out.println("Connected to " + this.lobbyId + " lobby.");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void receiveChat(OutputStream output, InputStream input){
        try{
            Timer timer = new Timer();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try{
                        while(input.available() == 0){}
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }, 2*60*1000);
            
            if(input.available() != 0){
                byte[] receive = new byte[input.available()];
                input.read(receive);
                ChatPacket receive_parsed = ChatPacket.parseFrom(receive);
                System.out.println(receive_parsed.getMessage());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void sendChat(OutputStream output, InputStream input){
        try{
            output.write(getChatPacket().toByteArray());
        } catch(Exception e){
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

            //client.clickCreateLobby(output, input);
            client.clickConnectToLobby(output, input);

            while(client.isConnected){
                System.out.print("Your Message:");
                if(client.sc.hasNext()){
                    client.sendChat(output, input);
                }
                System.out.print("Received Message: ");
                client.receiveChat(output,input);
            }

            output.close();
            input.close();
            socket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
