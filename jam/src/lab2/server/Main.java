package lab2.server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {

    //port searching range
    private static final int MIN_PORT = 1025;
    private static final int MAX_PORT = 65535;
    private static final int DEFAULT_LIFE_TIME = 100000;

    public static void main(String[] args){
        int lifeTime = DEFAULT_LIFE_TIME;
        if(args.length>0) {
            lifeTime = Integer.parseInt(args[1]);
        }
        //create server socket
        //with available port
        DatagramSocket server=null;
        for (int i = MIN_PORT; i < MAX_PORT; i++) {
            try {
                server = new DatagramSocket(i);
                break;
            } catch (IOException ignored) { }
        }

        //check server state
        if(server==null){
            System.out.print("failed to create server socket\n");
        }
        else{
            System.out.println("datagram socket created\n");
            try {
                System.out.println(InetAddress.getLocalHost());
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            System.out.println(server.getLocalPort());
            TimeProtocol t=new TimeProtocol(server);
            int finalLifeTime = lifeTime;
            new Thread(()-> {
                try {
                    t.listen(finalLifeTime);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            new Thread(()->{
                try {
                    t.publish(finalLifeTime);
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }).start();

        }

        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
