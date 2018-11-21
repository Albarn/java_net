package lab4.dependency;

import java.rmi.Remote;
import java.rmi.RemoteException;

//collects url addresses of resources by given filter
public interface ResourceCollector extends Remote {
    String[] getUrls(String filter) throws RemoteException;
}
