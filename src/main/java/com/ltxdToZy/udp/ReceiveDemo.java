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
			DatagramSocket ds=new DatagramSocket(10003); //���ն˿ںŵ���Ϣ
			while(true){
			    byte[] bys=new byte[1024];
			    DatagramPacket dp=new DatagramPacket(bys,bys.length);//������Ϣ��
			    ds.receive(dp);//��socket����Ϣ���յ�dp��
			    System.out.println("��������Ϊ��"+new String(dp.getData(),0,dp.getLength()));
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
