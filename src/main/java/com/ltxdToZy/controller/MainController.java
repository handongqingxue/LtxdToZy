package com.ltxdToZy.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ltxdToZy.udp.*;
import com.ltxdToZy.utils.*;
import com.ltxdToZy.entity.v3_1.*;
import com.ltxdToZy.service.v3_1.*;

@Controller
@RequestMapping(MainController.MODULE_NAME)
public class MainController {

	public static final String MODULE_NAME="main";
	
	@Autowired
	private PositionService positionService;

	@RequestMapping(value="/goTest")
	public String goTest(HttpServletRequest request) {
		
		request.setAttribute("tenantId",Constant.TENANT_ID);
		request.setAttribute("userId",Constant.USER_ID);
		request.setAttribute("password",Constant.PASSWORD);
		
		return MODULE_NAME+"/test";
	}

	//http://127.0.0.1:8081/position/vueIndex.html#/CurrentLocation
	//http://localhost:8080/LtxdToZy/main/goSend
	@RequestMapping(value="/goSend")
	public String goSend(HttpServletRequest request) {

		request.setAttribute("macAddress",Constant.MAC_ADDRESS);
		request.setAttribute("pushSpace",Constant.PUSH_SPACE);
		
		return MODULE_NAME+"/send";
	}

	@RequestMapping(value="/goReceive")
	public String goReceive() {
		
		return MODULE_NAME+"/receive";
	}

	@RequestMapping(value="/sendUDPData")
	@ResponseBody
	public Map<String, Object> sendUDPData() {
		System.out.println("sendUDPData...");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//SendDemo.sendData();
		DatagramSocket ds=null;
		try {
			ds=new DatagramSocket(); //建立通讯socket
			 
			BufferedReader br=new BufferedReader(new InputStreamReader(System.in));//读取键盘输入流
			InetAddress ia = InetAddress.getByName("192.168.1.211");
			//InetAddress ia = InetAddress.getByName("192.168.2.222");
			//InetAddress ia = InetAddress.getByName("127.0.0.1");
		    int port=10003;

		    /*
			List<Location> locationList=locationService.selectEntityLocation();
			//System.out.println("size==="+locationList.size());
			String text=JSON.toJSONString(locationList);
			*/
			
			List<Position> positionList=positionService.queryELList();
			//System.out.println("size==="+positionList.size());
			String text=JSON.toJSONString(positionList);
			
	    	//String text="[{\"name\":\"李天亯\",\"x\":100,\"y\":200},{\"name\":\"李天亯\",\"x\":100,\"y\":200},{\"name\":\"李天亯\",\"x\":100,\"y\":200}]";
			//System.out.println("text==="+text);
	        byte[] bys=text.getBytes();
	        DatagramPacket dp=new DatagramPacket(bys,bys.length,ia,port);
	        ds.send(dp);
	        
	        resultMap.put("status", "ok");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			ds.close();
			return resultMap;
		}
	}

	@RequestMapping(value="/receiveUDPData")
	public void receiveUDPData() {
		System.out.println("receiveUDPData...");
		ReceiveDemo.receiveData();
	}

	/**
	 * 获得Mac地址
	 * @return
	 */
	@RequestMapping(value="/getMacAddress", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getMacAddress() {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			//http://www.fyepb.cn/news/paotui/176249.html
			//https://blog.csdn.net/qq_43080741/article/details/124237926
			StringBuilder sb = new StringBuilder();
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			byte[] mac = null;
			while (allNetInterfaces.hasMoreElements()) {
			    NetworkInterface netInterface = allNetInterfaces.nextElement();
			    if (netInterface.isLoopback() || netInterface.isVirtual() || netInterface.isPointToPoint() || !netInterface.isUp()) {
			        continue;
			    } else {
			        mac = netInterface.getHardwareAddress();
			        if (mac != null) {
			            for (int i = 0; i < mac.length; i++) {
			                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : "\n"));
			            }
			        }
			    }
			}
			String macAddress = sb.toString();
			//System.out.println("macAddress==="+macAddress);
			resultMap.put("macAddress", macAddress);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			return resultMap;
		}
	}
}
