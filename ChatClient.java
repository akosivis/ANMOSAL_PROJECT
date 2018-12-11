import proto.PlayerProtos.Player;
import proto.TcpPacketProtos.TcpPacket.*;
import proto.TcpPacketProtos.TcpPacket;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.Thread;

public class ChatClient{
    private final static String server_ip = "202.92.144.45";
    private final static int port = 80;
    private static boolean isConnected = false;
    private static String lobbyId = "AB1L";
    private static Player player;
    private static Scanner sc = new Scanner(System.in);
    private static String id = "0";

    public ChatClient(){

    }

    public ChatClient(Player player){
        this.player = player;
    }

    private Socket openConnection(String server, int port){
        /* create socket */
        Socket socket = null;
        try{
            socket = new Socket(server, port);
            this.isConnected = true;
        } catch (IOException e) {
            System.out.println(e);
        }

        return socket;
    }
        
    private CreateLobbyPacket makeLobbyPacket(){   
        /* create a CreateLobbyPacket */
        System.out.print("Max no. of players: ");
        int max = this.sc.nextInt();

        CreateLobbyPacket.Builder create = CreateLobbyPacket.newBuilder();
        create.setType(PacketType.CREATE_LOBBY).setMaxPlayers(max);

        return create.build();
    }

    private ConnectPacket makeConnectPacket(){
        /* create ConnectPacket */

        //sample player
        Player.Builder player = Player.newBuilder().setName("Mojica");
        this.player = player.build();

        ConnectPacket.Builder connect = ConnectPacket.newBuilder();
        connect.setType(PacketType.CONNECT).setPlayer(this.player).setLobbyId(this.lobbyId);

        return connect.build();
    }

    private ChatPacket makeChatPacket(){
        /* creates ChatPacket */
        ChatPacket.Builder chat = ChatPacket.newBuilder();
        chat.setType(PacketType.CHAT).setPlayer(player).setLobbyId(this.lobbyId);

        String message = this.sc.nextLine();
        chat.setMessage(message);

        return chat.build();
    }

    private DisconnectPacket makeDisconnectPacket(){
        /* create DisconnectPacket */
        DisconnectPacket.Builder dc = DisconnectPacket.newBuilder();
        dc.setType(PacketType.DISCONNECT).setPlayer(player);

        return dc.build();
    }

    private void errorChecker(String exp_string, TcpPacket rp, byte[] toBeParsed) throws IOException{
        /* helper for error messages and other actions */

        if(rp.getType() == TcpPacket.PacketType.DISCONNECT){
            /*disconnect mo bez*/
        }else if(rp.getType() == TcpPacket.PacketType.CREATE_LOBBY){
            /*create lobby daw e*/
        }else if(rp.getType() == TcpPacket.PacketType.CONNECT){
            /*yaan mo siya*/
        }else if(rp.getType() == TcpPacket.PacketType.CHAT){
            System.out.println("ERROR: Received ChatPacket. Expecting " + exp_string);
        }else if(rp.getType() == TcpPacket.PacketType.PLAYER_LIST){
            System.out.println("ERROR: Received PlayerListPacket. Expecting " + exp_string);
        }else if(rp.getType() == TcpPacket.PacketType.ERR_LDNE){
            ErrLdnePacket err_dne = ErrLdnePacket.parseFrom(toBeParsed);
            System.out.println("ERROR: " + err_dne.getErrMessage());
        }else if(rp.getType() == TcpPacket.PacketType.ERR_LFULL){
            ErrLfullPacket err_full = ErrLfullPacket.parseFrom(toBeParsed);
            System.out.println("ERROR: " + err_full.getErrMessage());
        }else if(rp.getType() == TcpPacket.PacketType.ERR){
            ErrPacket err = ErrPacket.parseFrom(toBeParsed);
            System.out.println("ERROR: " + err.getErrMessage());
        }
    }

    private void clickCreateLobby(OutputStream output, InputStream input){
        /* actions when create lobby button is invoked */
        try{
            output.write(makeLobbyPacket().toByteArray());
            
            while(input.available() == 0){}

            /*https://stackoverflow.com/questions/1264709/convert-inputstream-to-byte-array-in-java*/
            byte[] toParse = new byte[input.available()];
            input.read(toParse);
            TcpPacket rp = TcpPacket.parseFrom(toParse);
            
            if(rp.getType() == TcpPacket.PacketType.CREATE_LOBBY){
                CreateLobbyPacket suc_res = CreateLobbyPacket.parseFrom(toParse);
                String lobbyId = suc_res.getLobbyId();
                this.lobbyId = lobbyId;
                System.out.println("Lobby " + this.lobbyId + " created.");
                /* send mo sa game na meron na pong lobby. */
            }
            else errorChecker("CreateLobbyPacket", rp, toParse);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clickConnectToLobby(OutputStream output, InputStream input){
        /* actions when connect to lobby button is invoked */
        try{
            output.write(makeConnectPacket().toByteArray());

            while(input.available() == 0) {}

            byte[] toParse = new byte[input.available()];
            input.read(toParse);
            TcpPacket rp = TcpPacket.parseFrom(toParse);

            if(rp.getType() == TcpPacket.PacketType.CONNECT){
                ConnectPacket suc_res = ConnectPacket.parseFrom(toParse);
                System.out.println("Connected to " + this.lobbyId + "\n");
            }else errorChecker("ConnectPacket", rp, toParse);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void receiveChat(OutputStream output, InputStream input){
        /* receiving chat */
        try{
            /*Timer timer = new Timer();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try{
                        while(input.available() == 0){}
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }, 2*60*1000);*/
            while(input.available() == 0){}
            
            if(input.available() != 0){
                byte[] toParse = new byte[input.available()];
                input.read(toParse);

                TcpPacket rp = TcpPacket.parseFrom(toParse);
                if(rp.getType() == TcpPacket.PacketType.CHAT){
                    ChatPacket suc_res = ChatPacket.parseFrom(toParse);
                    if(this.player.getName() == suc_res.getPlayer().getName()){
                        System.out.println(suc_res.getPlayer().getName() + ": " + suc_res.getMessage());
                    }
                }
                else errorChecker("ChatPacket", rp, toParse);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void sendChat(OutputStream output, InputStream input){
        /* to send chat */
        try{
            output.write(makeChatPacket().toByteArray());
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void clickDisconnect(OutputStream output, InputStream input, Socket socket){
        /* to disconnect */
        try{
            output.write(makeDisconnectPacket().toByteArray());

            while(input.available() == 0){}

            byte[] toParse = new byte[input.available()];
            input.read(toParse);
            TcpPacket rp = TcpPacket.parseFrom(toParse);

            if(rp.getType() == TcpPacket.PacketType.DISCONNECT){
                DisconnectPacket suc_res = DisconnectPacket.parseFrom(toParse);
                System.out.println(suc_res);
                this.isConnected = false;

                output.close();
                input.close();
                socket.close();
            }else errorChecker("DisconnectPacket", rp, toParse);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void getPlayerList(OutputStream output, InputStream input){
        try{
            PlayerListPacket.Builder list = PlayerListPacket.newBuilder();
            list.setType(PacketType.PLAYER_LIST).getPlayerListList();

            output.write(list.build().toByteArray());

            while(input.available() == 0){}

            byte[] toParse = new byte[input.available()];
            input.read(toParse);
            TcpPacket rp = TcpPacket.parseFrom(toParse);

            if(rp.getType() == TcpPacket.PacketType.PLAYER_LIST){
                PlayerListPacket suc_res = PlayerListPacket.parseFrom(toParse);
                System.out.println(suc_res);
            }else errorChecker("PlayerListPacket", rp, toParse);
        }catch (Exception e){
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

            Thread readMessage = new Thread(new Runnable(){ 
                @Override
                public void run() { 
                    while (client.isConnected) { 
                        client.receiveChat(output,input);
                        try{
                            Thread.sleep(2000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } 
                } 
            }); 

            readMessage.start(); 

            while(client.isConnected){
                System.out.print(client.player.getName() + ": ");
                client.sendChat(output, input);
            }

            //client.clickDisconnect(output, input); 

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
