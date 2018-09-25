package lab1.server;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {

    //port searching range
    private static final int MIN_PORT = 1025;
    private static final int MAX_PORT = 65535;

    public static void main(String[] args){

        //create server socket
        //with available port
        ServerSocket server=null;
        for (int i = MIN_PORT; i < MAX_PORT; i++) {
            try {
                server = new ServerSocket(i);
                break;
            } catch (IOException ignored) { }
        }

        //check server state
        if(server==null){
            System.out.print("failed to create server socket\n");
        }
        else{
            System.out.print("server socket created\n");
            try {
                System.out.print(server.getInetAddress().toString()+"\n");
                System.out.print(server.getLocalPort()+"\n");

                //noinspection InfiniteLoopStatement
                while (true){

                    //listen environment until we get a new socket
                    UpperCaseProtocol u=new UpperCaseProtocol(server.accept());

                    //start it in new thread
                    Thread tu=new Thread(u);
                    tu.start();
                }
            } catch (IOException e) {
                System.out.print("failed to create socket");
            } finally {
                if(!server.isClosed()){
                    try {
                        server.close();
                    } catch (IOException ignored) { }
                }
            }
        }
    }
}
