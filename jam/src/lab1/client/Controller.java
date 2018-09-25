package lab1.client;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.*;
import java.net.Socket;

public class Controller {
    public TextField hostTextField;
    public TextField portTextField;
    public Button connectButton;
    public TextField messageTextField;
    public TextArea responseTextField;

    private boolean connected=false;
    private String state1="connect";
    private String state2="disconnect";

    private Socket socket;
    private BufferedReader reader =null;
    private PrintWriter writer =null;

    //user wants to connect to new server
    //or disconnect from current server
    public void onConnectButtonClick(ActionEvent actionEvent) {
        if(!connected) {
            try {

                //getting socket with given host & port
                String host = hostTextField.getText();
                int port = Integer.parseInt(portTextField.getText());
                socket = new Socket(host, port);

                //getting i/o streams from socket
                reader =new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                writer =new PrintWriter(
                        new BufferedWriter(
                                new OutputStreamWriter(socket.getOutputStream())));

                //check, that we use uppercase server
                Thread serverCheck = new Thread(() -> {
                    try {
                        connected = reader
                                .readLine()
                                .equals("it's uppercase server");
                    } catch (IOException ignored) { }
                });

                //wait for 1 sec for greeting from server
                serverCheck.start();
				Thread.sleep(1000);

				//if we are not connected,
                //it's not uppercase server
				if(connected){
                    connectButton.setText(state2);
                }else{
				    throw new IOException("wrong server");
                }
            } catch (IOException | InterruptedException e) {

                //show exception and return to
                //disconnected statement
				Alert a=new Alert(AlertType.ERROR);
				a.setHeaderText("failed to connect");
				a.setContentText(e.getMessage());
				a.show();
				connected=false;
				connectButton.setText(state1);
            }
        }else{

            //if user pushed "disconnect", then
            //we need to send stop message to end using protocol
            if(socket!=null&&!socket.isClosed()){
                writer.println("stop");
                writer.flush();
            }
            connected=false;
            connectButton.setText(state1);
            responseTextField.setText("");
        }
    }

    //send message to server
    //and get response
    public void onSendButtonClick(ActionEvent actionEvent) {
        if(connected && socket!=null && !socket.isClosed()){
            try {

                //send text to server
                writer.println(messageTextField.getText());
                writer.flush();

                //get response
                String r= reader.readLine();

                //if there is some text in area
                //we add new line before current response
                String text=responseTextField.getText();
                if(text.length()>0){
                    text+="\n";
                }
                responseTextField.setText(text+r);

                //if user sent stop message, then we disconnect from server
				if(messageTextField.getText().equals("stop")){
					connected=false;
					connectButton.setText(state1);
					responseTextField.setText("");
				}
            } catch (IOException ignored) { }
        }
    }
}
