package lab4.client;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lab4.dependency.RMIConstant;
import lab4.dependency.ResourceCollector;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Iterator;

public class Controller {
    public TextField filterTextField;
    public Button connectButton;
    public ImageView resourcesView;
    private ResourceCollector h;

    public Controller() throws RemoteException, NotBoundException {
        Registry registry= LocateRegistry.getRegistry("localhost", RMIConstant.RPORT);
        h = (ResourceCollector) registry.lookup(RMIConstant.RID);
    }

    public void onNextButtonClick(ActionEvent actionEvent) {
        resourcesView.setImage(new Image(images.next()));
    }

    private class ImageIterator implements Iterator<String>{

        ImageIterator(String[] urls){
            this.urls=urls;
            index=0;
        }
        @Override
        public boolean hasNext() {
            return true;
        }

        private String[] urls;
        private int index;
        @Override
        public String next() {
            if(urls.length>0){
                index++;
                index%=urls.length;
                return urls[index];
            }else{
                return "";
            }
        }
    }

    private ImageIterator images=new ImageIterator(new String[0]);
    public void onConnectButtonClick(ActionEvent actionEvent) {
        String filter=filterTextField.getText();
        try
        {
            images=new ImageIterator(h.getUrls(filter));
        }
        catch (Exception e)
        {
            System.out.println ("ResourceCollector client exception: " + e);
        }
    }
}
