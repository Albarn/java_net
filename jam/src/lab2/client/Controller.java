package lab2.client;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.*;
import java.net.*;

import lab2.server.TimeProtocol;
import lab2.server.Host;

public class Controller {
    public TextField hostTextField;
    public TextField portTextField;
    public Button connectButton;
    public TextArea responseTextField;

    private boolean connected=false;
    private String state1="connect";
    private String state2="disconnect";

    private Host server;
    private DatagramSocket client=Main.getClient();

    //user wants to connect to new server
    //or disconnect from current server
    public void onConnectButtonClick(ActionEvent actionEvent) {
        if(!connected) {
            try {

                //getting socket with given host & port

                String host = hostTextField.getText();
                int port = Integer.parseInt(portTextField.getText());
                server=new Host(InetAddress.getByName(host),port);

                byte[] message=new byte[TimeProtocol.MTU];
                message[TimeProtocol.CMD_BYTE]=TimeProtocol.SUBSCRIBE;
                DatagramPacket p=new DatagramPacket(
                        message,
                        TimeProtocol.MTU,
                        server.getAddress(),
                        server.getPort());

                client.send(p);

                new Thread(this::getResponse).start();

                connected=true;
                connectButton.setText(state2);
            } catch (IOException e) {

                //show exception and return to
                //disconnected statement
                Alert a=new Alert(AlertType.ERROR);
                a.setHeaderText("failed to connect to "+server.getAddress()+"/"+server.getPort());
                a.setContentText(e.getMessage());
                a.show();
                connected=false;
                connectButton.setText(state1);
            }
        }else{

            //if user pushed "disconnect", then
            //we need to send stop message to end using protocol
            try {
            byte[] message=new byte[TimeProtocol.MTU];
            message[TimeProtocol.CMD_BYTE]=TimeProtocol.UNSUBSCRIBE;
            DatagramPacket p=new DatagramPacket(message,
                    TimeProtocol.MTU,
                    server.getAddress(),
                    server.getPort());


                client.send(p);
            } catch (IOException e) {
                e.printStackTrace();
            }

            connected=false;
            connectButton.setText(state1);
            responseTextField.setText("");
        }
    }

    private void getResponse(){
        while (connected) {
            try {
                DatagramPacket p = new DatagramPacket(
                        new byte[TimeProtocol.MTU],
                        TimeProtocol.MTU,
                        server.getAddress(),
                        server.getPort());

                client.receive(p);

                byte[] timeData = p.getData();

                String responseText = timeData[TimeProtocol.HOURS] + ":" +
                        timeData[TimeProtocol.MINUTES] + ":" +
                        timeData[TimeProtocol.SECONDS] + "\n";
                responseText+=responseTextField.getText();
                responseTextField.setText(responseText);
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
