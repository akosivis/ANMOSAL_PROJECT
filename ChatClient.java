import proto.PlayerProtos.Player;
import proto.TcpPacketProtos.TcpPacket.*;
import proto.TcpPacketProtos.TcpPacket;

import java.net.*;
import java.io.*;
import java.lang.Thread;

public class ChatClient{
    private final String server_ip = "202.92.144.45";
    private final int port = 80;
    
    public boolean isConnected = false;
    private static String lobbyId = "AB1L";
    private static int max = 3;

    private static Player player;
    private static Socket socket;
    private static OutputStream output;
    private static InputStream input;

    public ChatClient(String name){
        Player.Builder player = Player.newBuilder().setName(name);
        this.player = player.build();
        this.socket = openConnection(this.server_ip, this.port);

        try{
            this.output = this.socket.getOutputStream();
            this.input = this.socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Socket openConnection(String server, int port){
        /* create socket */
        Socket socket = null;
        try{
            this.socket = new Socket(server, port);
            this.isConnected = true;
        } catch (IOException e) {
            System.out.println(e);
        }

        return socket;
    }
        
    private Player getPlayer(){
        return this.player;
    }

    private CreateLobbyPacket makeLobbyPacket(){   
        /* create a CreateLobbyPacket */
        CreateLobbyPacket.Builder create = CreateLobbyPacket.newBuilder();
        create.setType(PacketType.CREATE_LOBBY).setMaxPlayers(max);

        return create.build();
    }

    private ConnectPacket makeConnectPacket(){
        /* create ConnectPacket */
        ConnectPacket.Builder connect = ConnectPacket.newBuilder();
        connect.setType(PacketType.CONNECT).setPlayer(this.player).setLobbyId(this.lobbyId);

        return connect.build();
    }

    private ChatPacket makeChatPacket(String message){
        /* creates ChatPacket */
        ChatPacket.Builder chat = ChatPacket.newBuilder();
        chat.setType(PacketType.CHAT).setPlayer(this.player).setLobbyId(this.lobbyId).setMessage(message);

        return chat.build();
    }

    private DisconnectPacket makeDisconnectPacket(){
        /* create DisconnectPacket */
        DisconnectPacket.Builder dc = DisconnectPacket.newBuilder();
        dc.setType(PacketType.DISCONNECT).setPlayer(this.player);

        return dc.build();
    }

    private String errorChecker(String exp_string, TcpPacket rp, byte[] toBeParsed) throws IOException{
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

        return "";
    }

    public void clickCreateLobby(){
        /* actions when create lobby button is invoked */
        try{
            this.output.write(makeLobbyPacket().toByteArray());
            
            while(this.input.available() == 0){}

            /*https://stackoverflow.com/questions/1264709/convert-inputstream-to-byte-array-in-java*/
            byte[] toParse = new byte[this.input.available()];
            this.input.read(toParse);
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

    public void clickConnectToLobby(){
        /* actions when connect to lobby button is invoked */
        try{
            this.output.write(makeConnectPacket().toByteArray());

            while(this.input.available() == 0) {}

            byte[] toParse = new byte[this.input.available()];
            this.input.read(toParse);
            TcpPacket rp = TcpPacket.parseFrom(toParse);

            if(rp.getType() == TcpPacket.PacketType.CONNECT){
                ConnectPacket suc_res = ConnectPacket.parseFrom(toParse);
                System.out.println("Connected to " + this.lobbyId + "\n");
            }else errorChecker("ConnectPacket", rp, toParse);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String receiveChat(){
        /* receiving chat */
        try{
            while(this.input.available() == 0){}
            
            if(this.input.available() != 0){
                byte[] toParse = new byte[this.input.available()];
                this.input.read(toParse);

                TcpPacket rp = TcpPacket.parseFrom(toParse);
                if(rp.getType() == TcpPacket.PacketType.CHAT){
                    ChatPacket suc_res = ChatPacket.parseFrom(toParse);
                    if(this.player.getName() == suc_res.getPlayer().getName()){
                        return (suc_res.getPlayer().getName() + ": " + suc_res.getMessage());
                    }
                }
                else errorChecker("ChatPacket", rp, toParse);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return "";
    }

    public void sendChat(String message){
        /* to send chat */
        try{
            this.output.write(makeChatPacket(message).toByteArray());
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void clickDisconnect(){
        /* to disconnect */
        try{
            this.output.write(makeDisconnectPacket().toByteArray());
            this.isConnected = false;

            while(this.input.available() == 0){}

            byte[] toParse = new byte[this.input.available()];
            this.input.read(toParse);
            TcpPacket rp = TcpPacket.parseFrom(toParse);

            if(rp.getType() == TcpPacket.PacketType.DISCONNECT){
                DisconnectPacket suc_res = DisconnectPacket.parseFrom(toParse);
                System.out.println(suc_res);
                this.isConnected = false;

                this.output.close();
                this.input.close();
                this.socket.close();

            }else errorChecker("DisconnectPacket", rp, toParse);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void getPlayerList(){
        try{
            PlayerListPacket.Builder list = PlayerListPacket.newBuilder();
            list.setType(PacketType.PLAYER_LIST).getPlayerListList();

            this.output.write(list.build().toByteArray());

            while(this.input.available() == 0){}

            byte[] toParse = new byte[this.input.available()];
            this.input.read(toParse);
            TcpPacket rp = TcpPacket.parseFrom(toParse);

            if(rp.getType() == TcpPacket.PacketType.PLAYER_LIST){
                PlayerListPacket suc_res = PlayerListPacket.parseFrom(toParse);
                System.out.println(suc_res);
            }else errorChecker("PlayerListPacket", rp, toParse);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*public static void main(String args[]){
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
    }*/
}
