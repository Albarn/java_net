package lab4.server;

import lab4.dependency.RMIConstant;

import java.nio.channels.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RcServer {
    public static void main (String[] argv) throws RemoteException, AlreadyBoundException
    {
        try
        {
            DeviationsCollector h = new DeviationsCollector();
            Registry registry= LocateRegistry.createRegistry(RMIConstant.RPORT);
            registry.bind(RMIConstant.RID,h);
            System.out.println("server started");
        }
        catch (Exception e)
        {
            System.out.println ("ResourceCollector server failed: " + e);
        }
    }
}
