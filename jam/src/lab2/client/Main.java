package lab2.client;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Main extends Application {

    private static DatagramSocket client;

    {
        try {
            client = new DatagramSocket();
            System.out.println(InetAddress.getLocalHost());
            System.out.println(client.getLocalPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DatagramSocket getClient() {
        return client;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Lab2 641p Shalikov");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                if(client!=null&&!client.isClosed())
                    client.close();
            }
        });
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}
