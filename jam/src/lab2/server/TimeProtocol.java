package lab2.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TimeProtocol {

    public static final int MTU = 3;
    public static final int CMD_BYTE = 0;
    public static final int UNSUBSCRIBE = 0;
    public static final int SUBSCRIBE = 1;
    public static final int HOURS = 0;
    public static final int MINUTES = 1;
    public static final int SECONDS = 2;

    private DatagramSocket udpServer;
    private ArrayList<Host> clients;

    TimeProtocol(DatagramSocket udpServer)  {

        // set given udpServer
        // initialize array for clients

        this.udpServer =udpServer;
        clients=new ArrayList<>();
    }

    //listening to user commands with given time period
    public void listen(int seconds) throws IOException {

        //calculate time to stop
        LocalDateTime stopTime=LocalDateTime.now().plusSeconds(seconds);
        while (true){

            //recieve cmd from client
            DatagramPacket p=new DatagramPacket(new byte[MTU],MTU);
            udpServer.receive(p);

            //if user want to subscribe, then we add him to collection
            if(p.getData()[CMD_BYTE]==SUBSCRIBE){
                clients.add(new Host(p.getAddress(),p.getPort()));
                System.out.println("client subscribed");

                //else, we remove him from set of clients
            }else if(p.getData()[CMD_BYTE]==UNSUBSCRIBE){
                clients.remove(new Host(p.getAddress(),p.getPort()));
                System.out.println("client left");
            }

            //break, when expire time comes
            if(LocalDateTime.now().compareTo(stopTime)>0){
                break;
            }
        }

        //print, that we are done
        System.out.println("listening finished");
    }

    //publish time to clients
    public void publish(int seconds) throws InterruptedException, IOException {
        LocalDateTime stopTime=LocalDateTime.now().plusSeconds(seconds);
        while (true){

            //time to send
            byte[] message=new byte[MTU];
            LocalDateTime now=LocalDateTime.now();
            message[HOURS]= (byte) now.getHour();
            message[MINUTES]=(byte)now.getMinute();
            message[SECONDS]=(byte)now.getSecond();

            //send time to all clients
            for (Host client : clients) {
                DatagramPacket p = new DatagramPacket(
                        message,
                        MTU,
                        client.getAddress(),
                        client.getPort());
                udpServer.send(p);
            }

            //break, when expire time comes
            if(LocalDateTime.now().compareTo(stopTime)>0){
                break;
            }

            //publish each second
            Thread.sleep(1000);
        }

        System.out.println("publishing finished");
    }
}
