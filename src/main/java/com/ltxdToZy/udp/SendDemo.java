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
        
 
    }
	
	public static void sendData() {
		DatagramSocket ds=null;
		try {
			ds=new DatagramSocket(); //����ͨѶsocket
			 
			BufferedReader br=new BufferedReader(new InputStreamReader(System.in));//��ȡ����������
			String line;
			while((line=br.readLine())!=null){
			    if("886".equals(line))
			        break;
			    byte[] bys=line.getBytes();
			    DatagramPacket dp=new DatagramPacket(bys,bys.length, InetAddress.getByName("192.168.2.222"),10003);//�������ݰ����������ȣ����ն��������˿ں�
			    ds.send(dp);//��������
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
