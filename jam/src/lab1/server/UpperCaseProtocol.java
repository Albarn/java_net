package lab1.server;

import java.io.*;
import java.net.Socket;

public class UpperCaseProtocol implements Runnable {

    //number of created sockets for client id
	private static int socketNumber=0;

	private Socket socket;
	private String client;
    
	UpperCaseProtocol(Socket socket){

	    //set required fields
        this.socket=socket;
		client="c"+socketNumber++;

		//print, that we got new socket
        System.out.print("new client: "+client+"\n");
    }

    @Override
    public void run() {
        try {

            //we need streams decorators to send/receive text messages
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream())));

            //tell client that we use lab1.server.UpperCaseProtocol
			writer.println("it's uppercase server");
			writer.flush();

			//uppercase echo, util we get "stop" message
            for(String m = bufferedReader.readLine(); !m.equals("stop"); m=bufferedReader.readLine()){
                writer.println(m.toUpperCase());
                writer.flush();
            }
            writer.println("STOP");
            writer.flush();
        }catch (IOException e){
            System.out.print("uppercase protocol exception\n");
        }
        finally {

            //closing socket in any cases
            if(socket!=null&&!socket.isClosed()){
                try {
                    socket.close();
                } catch (IOException ignored) { }
            }
            System.out.print("lab1/client " +client+" disconnected\n");
        }
    }
}
