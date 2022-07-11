package com.ltxdToZy.udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

//https://blog.csdn.net/qq_40662086/article/details/115165744
//http://c.biancheng.net/view/1203.html
public class SendDemo {
	public static void main(String[] args) throws IOException {
		sendData();
    }
	
	public static void sendData() {
		DatagramSocket ds=null;
		try {
			ds=new DatagramSocket(); //建立通讯socket
			 
			System.out.println("嬲");
			BufferedReader br=new BufferedReader(new InputStreamReader(System.in));//读取键盘输入流
			InetAddress ia = InetAddress.getByName("192.168.2.222");
			//InetAddress ia = InetAddress.getByName("127.0.0.1");
		    int port=10003;
		    
		    /*
			String line;
			while((line=br.readLine())!=null){
			    if("886".equals(line))
			        break;
			    byte[] bys=line.getBytes();
			    DatagramPacket dp=new DatagramPacket(bys,bys.length,ia ,port);//建立数据包，声明长度，接收端主机，端口号
			    ds.send(dp);//发送数据
			}
			*/
			
		    for(int i=0;i<5;i++)
		    {
		    	String text="{\"name\":\"李天亯\",\"x\":100,\"y\":200}";
		    	//text=new String(text.getBytes("ISO-8859-1"),"UTF-8");
		        byte[] bys=text.getBytes();
		        DatagramPacket dp=new DatagramPacket(bys,bys.length,ia,port);
		        ds.send(dp);
		        Thread.sleep(1000);
		    }
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			ds.close();
		}
	}
}
