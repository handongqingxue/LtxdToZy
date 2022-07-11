package com.ltxdToZy.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ReceiveDemo {
	public static void main(String[] args) throws IOException {
        
    }
	
	public static void receiveData() {
		try {
			DatagramSocket ds=new DatagramSocket(10003); //接收端口号的消息
			while(true){
			    byte[] bys=new byte[1024];
			    DatagramPacket dp=new DatagramPacket(bys,bys.length);//建立信息包
			    ds.receive(dp);//将socket的信息接收到dp里
			    System.out.println("输入数据为："+new String(dp.getData(),0,dp.getLength()));
			}
			//ds.close();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
