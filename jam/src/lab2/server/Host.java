package lab2.server;

import java.net.InetAddress;

//describes client or server
//with ip & port
public class Host {
    private InetAddress address;
    private int port;

    public Host(InetAddress address, int port){
        this.address=address;
        this.port=port;
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    //equals method required for ArrayList structure
    @Override
    public boolean equals(Object obj) {
        try{
            Host c=(Host)obj;
            return c.address.equals(address) &&
                    (c.port == port);
        }catch (Exception e){
            return false;
        }
    }
}
