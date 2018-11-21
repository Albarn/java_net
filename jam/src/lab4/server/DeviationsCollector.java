package lab4.server;

import lab4.dependency.ResourceCollector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class DeviationsCollector extends UnicastRemoteObject implements ResourceCollector {

    DeviationsCollector() throws RemoteException{ }

    @Override
    public String[] getUrls(String filter) throws RemoteException {
        String TARGET = "https://www.deviantart.com/tag/";
        ArrayList<String> urls=new ArrayList<>();
        try {

            //connect to given url address
            URL source=new URL(TARGET+filter);
            URLConnection conn=source.openConnection();
            conn.connect();

            //check that we use http protocol
            if(conn instanceof HttpURLConnection){
                HttpURLConnection httpConn=(HttpURLConnection)conn;
                System.out.println(httpConn.getContentType());

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
                    final String PREFIX = "<img data-sigil=\"torpedo-img\" src=\"";
                    final String POSTFIX = "\"";
                    final int IMAGE_NUMBER_LIMIT=20;
                    for(int i=0;i<htmlContent.length()&&urls.size()<IMAGE_NUMBER_LIMIT;i++){
                        StringBuilder imgUrl= new StringBuilder();
                        for(i=htmlContent.indexOf(PREFIX,i)+PREFIX.length();i<htmlContent.indexOf(POSTFIX,i);i++){
                            imgUrl.append(htmlContent.charAt(i));
                        }
                        urls.add(imgUrl.toString());
                        System.out.println(imgUrl.toString());
                    }
                }else{
                    System.out.println("not text/html");
                }
            }else{
                System.out.println("not http");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return urls.toArray(new String[0]);
    }
}
