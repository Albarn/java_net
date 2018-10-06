package lab3.client;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Controller {
    public TextField hostTextField;
    public Button connectButton;
    public TextArea responseTextField;

    public void onConnectButtonClick(ActionEvent actionEvent) {
        try {

            //connect to given url address
            URL source=new URL(hostTextField.getText());
            URLConnection conn=source.openConnection();
            conn.connect();

            //check that we use http protocol
            if(conn instanceof HttpURLConnection){
                HttpURLConnection httpConn=(HttpURLConnection)conn;
                responseTextField.setText(httpConn.getContentType());

                //check that we got html document
                if(httpConn.getContentType().startsWith("text/html")){

                    //read content from web page
                    BufferedReader in=new BufferedReader(
                            new InputStreamReader(httpConn.getInputStream()));
                    StringBuilder htmlLines= new StringBuilder();
                    for (String line = in.readLine();line != null;line=in.readLine()) {
                        htmlLines.append(line);
                    }
                    String htmlContent=htmlLines.toString();

                    //look for title element content
                    StringBuilder head= new StringBuilder();
                    String PREFIX = "<title>";
                    String POSTFIX = "</title>";
                    for(int i = htmlContent.indexOf(PREFIX)+ PREFIX.length(); i<htmlContent.indexOf(POSTFIX); i++){
                        head.append(htmlContent.charAt(i));
                    }
                    responseTextField.setText(head.toString());
                }else{
                    responseTextField.setText("not text/html");
                }
            }else{
                responseTextField.setText("not http");
            }
        } catch (IOException e) {
            e.printStackTrace();
            responseTextField.setText(e.getMessage());
        }
    }
}
