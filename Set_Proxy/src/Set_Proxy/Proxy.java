/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Set_Proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.NoRouteToHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Jayant Kumar Yadav
 */
public class Proxy implements Runnable{
    
//public static void main(String[] args) throws IOException {
        //Validate.isTrue(args.length == 1, "usage: supply url to fetch");
        //String url = args[0];
    
    private ArrayList<String> proxyList;
    boolean stop;
    Process cmd;
    String url = "http://172.31.9.69/dc/proxy.php";
    Document doc;
    Elements para;
    String proxy, proxydow, bps, avgbps, row, port, proxyEnable, proxyServer;
    float maxSpeed, avgSpeed, speed;
    proxy_gui object;
    
    public Proxy(ArrayList<String> proxies, proxy_gui obj){
        proxyList = new ArrayList<String>();
        proxyList = proxies;
        object = obj;
    }
    
    @Override
    public void run(){  
    
        url = "http://172.31.9.69/dc/proxy.php";
        port="3128";
        //String password = "edcguest";
        //String username = "edcguest";
    
    while(!Thread.currentThread().isInterrupted()){
        
        try{
           
        print("Fetching %s...", url);

        doc = Jsoup.connect(url).get();
        /*Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");*/
        para = doc.select("tr:has(td)");
        
        print("\npara: (%d)", para.size());
        proxy = null;
        proxydow = null;
        maxSpeed = avgSpeed = 0;
        bps = "KB/s";
        avgbps = "KB/s)";
        
        for (Element text : para) {
            
            row = text.text();
            String arr[] = row.split(" ", 10);
            
            if(row.contains("Working")){
                
                speed = Float.parseFloat(arr[5]);
                if(proxyList.contains(arr[0])){
                    if(arr[6].equals(bps)){
                        if(Float.compare(speed, maxSpeed)>0){
                            proxy=arr[0];
                            maxSpeed=speed;
                        }
                    }
                    else{
                        if(arr[6].equals("MB/s") && bps.equals("KB/s")){
                            proxy=arr[0];
                            maxSpeed=speed;
                            bps=arr[6];
                        }
                    }
                print("* %s %s %s%s\n",arr[0],arr[2],arr[5],arr[6]);
                }
            }
            else{
                speed = Float.parseFloat(arr[7]);
                if(proxyList.contains(arr[0])){
                    if(arr[8].equals(avgbps)){
                        if(Float.compare(speed, avgSpeed)>0){
                            proxydow=arr[0];
                            avgSpeed=speed;
                        }
                    }
                    else{
                        if(arr[8].equals("MB/s)") && avgbps.equals("KB/s)")){
                            proxydow=arr[0];
                            avgSpeed=speed;
                            avgbps=arr[8];
                        }
                    }
                print("* %s %s %s%s\n",arr[0],arr[2],arr[7],arr[8]);
                }
            }
        }
        
            if(proxy == null){
                proxy = proxydow;
            }
        
            print("\n* max speed proxy is : %s with speed of %.2f%s",proxy,maxSpeed,bps);
        
            proxyEnable = "REG ADD \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\" /v ProxyEnable /t REG_DWORD /d 1 /f";
            proxyServer = "REG ADD \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\" /v ProxyServer /t REG_SZ /d "+proxy+":"+port+" /f";
        
            String out = null;
            cmd = Runtime.getRuntime().exec(proxyEnable);
            
            BufferedReader in = new BufferedReader( new InputStreamReader( cmd.getInputStream() ) );
            BufferedReader err = new BufferedReader( new InputStreamReader( cmd.getErrorStream() ) );
            while ( (out = in.readLine() ) != null ) {
                System.out.println(out);
                //if (out.equalsIgnoreCase("The operation completed successfully.")) {       System.out.println("succesfull.\n"); }
            }
            while ( (out = err.readLine() ) != null ) {
                System.out.println(out);
                //if (out.equalsIgnoreCase("The operation completed successfully.")) {       System.out.println("succesfull.\n"); }
            }
            in.close();
            err.close();
            
            cmd = Runtime.getRuntime().exec(proxyServer);
        
            in = new BufferedReader( new InputStreamReader( cmd.getInputStream() ) );
            err = new BufferedReader( new InputStreamReader( cmd.getErrorStream() ) );
            while ( (out = in.readLine() ) != null ) {
                System.out.println(out);
                //if (out.equalsIgnoreCase("The operation completed successfully.")) {       System.out.println("succesfull.\n"); }
            }
            while ( (out = err.readLine() ) != null ) {
                System.out.println(out);
                //if (out.equalsIgnoreCase("The operation completed successfully.")) {       System.out.println("succesfull.\n"); }
            }
            in.close();
            err.close();
            
            /*String infoMessage = proxy+" has been set for you.";
            String titleBar = "Set_Proxy";
            JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);*/
            
            
            
            print("\n*The system proxy %s is been set by my software.Thankyou for using.\n",proxy);
            
            /*for(String ele : proxyList){
                System.out.println(ele);
            }*/
            Thread.sleep(120000);
        
        }catch (IOException | NumberFormatException ex) {
                Logger.getLogger(Proxy.class.getName()).log(Level.SEVERE, null, ex);
                continue;
            }catch (InterruptedException ex) {
                    break;
            }
        
    
        
        System.out.println("finally thread se bahar aa gya h yaar.");
    
    }
    }
    
    /*synchronized public void setProxy(){
        object.curProxy.setText("Current Proxy : "+proxy);
    }*/
    
    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

}
