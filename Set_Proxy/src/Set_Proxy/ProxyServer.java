/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Set_Proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jayant Kumar Yadav
 */
public class ProxyServer implements Runnable {
    
    proxy_gui gui;
    static String arr[],pro[], prevProxy;
    
    public ProxyServer(proxy_gui gui){
        this.gui = gui;
        prevProxy="";
    }
    
    @Override
    public void run(){
        
        Process cmd;
        String result,out,proxy = "reg query \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\" /v proxyserver";
        BufferedReader in,err;
        
        while(true){
            
            try {
                cmd = Runtime.getRuntime().exec(proxy);
                result="";
                in = new BufferedReader( new InputStreamReader( cmd.getInputStream() ) );
                err = new BufferedReader( new InputStreamReader( cmd.getErrorStream() ) );
                while ( (out = in.readLine() ) != null ) {
                    result += out;
                    //System.out.println(out);
                }
                while ( (out = err.readLine() ) != null ) {
                    //System.out.println(out);
                }
                in.close();
                err.close();
                
                
                //System.out.println(result);
                arr=result.split("    ",4);
                
                //System.out.println(arr[1]);
                pro=arr[3].split(":",2);
                
                //System.out.println(pro[0]);
                //setProxyStatus(pro[0]);
                
                
                if(!prevProxy.equals(pro[0])){
                gui.getCurProxy(pro[0]);
                System.out.println("@"+pro[0]);
                prevProxy=pro[0];
                }
                    
                    Thread.sleep(3000);
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(ProxyServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
        
        synchronized public void setProxyStatus(String proxyServer){
        
        if(proxyServer!=null){
            proxy_gui.curProxy.setText("Current Proxy : "+proxyServer);
            System.out.println(proxyServer);
        }
        else{
            proxy_gui.curProxy.setText("Corrent Proxy : No Proxy");       
        }
    }
        
        
        
    }
