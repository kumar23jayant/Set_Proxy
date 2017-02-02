/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Set_Proxy;

import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jayant Kumar Yadav
 */
public class connection implements Runnable {
    
    proxy_gui gui;
    boolean change;
    
    
    public connection(proxy_gui gui){
        this.gui = gui;
        change=false;
    }
    
    @Override
    public void run(){
        
        HttpURLConnection con=null;
        URL url;
        try {
            url = new URL("http://172.31.9.69/dc/proxy.php");
            
        
        
        while(true){
        
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("HEAD");
            con.setConnectTimeout(4000);
            con.connect();
            System.out.println("checking response");
            if(con.getResponseCode()==HttpURLConnection.HTTP_OK){
                if(!change){
                    gui.getConStatus(true);
                    System.out.println("reached");
                    change = true;
                }
                System.out.println("response code 200." +change);
                con.disconnect();
            }
            else{
                System.out.println("response code not 200."+change);
                gui.getConStatus(false);
                con.disconnect();
                change = false;
            }
        }
            
        }catch(Exception ex){
            //Logger.getLogger(connection.class.getName()).log(Level.SEVERE, null, ex);
            gui.getConStatus(false);
            System.out.println("response code not 200."+change);
            con.disconnect();
            change = false;
            
        } 
        
        /*catch (MalformedURLException | UnknownHostException | NoRouteToHostException | ConnectException ex) {
            Logger.getLogger(connection.class.getName()).log(Level.SEVERE, null, ex);
            gui.getConStatus(false);
            change = false;
            
        } catch (IOException ex) {
            Logger.getLogger(connection.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
               
        
        
        
    }
    
    synchronized public void setConnection(int state){
        
        if(state==1){
            gui.status.setText("Connction Status : Connected");
        }
        else{
            gui.status.setText("Connction Status : disconnected");       
        }
    }
    
}
