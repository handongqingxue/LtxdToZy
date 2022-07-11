package com.ltxdToZy.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ltxdToZy.udp.ReceiveDemo;
import com.ltxdToZy.udp.SendDemo;

@Controller
@RequestMapping(MainController.MODULE_NAME)
public class MainController {

	private static final String JM_URL="http://192.168.1.9:8090/";
	public static final String MODULE_NAME="main";

	@RequestMapping(value="/goTest")
	public String goTest() {
		
		return MODULE_NAME+"/test";
	}

	@RequestMapping(value="/goSend")
	public String goSend() {
		
		return MODULE_NAME+"/send";
	}

	@RequestMapping(value="/goReceive")
	public String goReceive() {
		
		return MODULE_NAME+"/receive";
	}
	
	@RequestMapping(value="/newFindRecords")
	@ResponseBody
	public Map<String, Object> newFindRecords() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			JSONObject resultJO = null;
			JSONObject paramJO=new JSONObject();
			paramJO.put("pass", "12345678");
			paramJO.put("personId", "-1");
			paramJO.put("length", -1);
			paramJO.put("index", 0);
			paramJO.put("startTime", "0");
			paramJO.put("endTime", "0");
			paramJO.put("model", -1);
			resultJO = getRespJson("newFindRecords", paramJO);
			String result=resultJO.get("result").toString();
			System.out.println("==="+result);
			resultMap.put("result", result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			return resultMap;
		}
	}

	@RequestMapping(value="/sendUDPData")
	public void sendUDPData() {
		System.out.println("sendUDPData...");
		SendDemo.sendData();
	}

	@RequestMapping(value="/receiveUDPData")
	public void receiveUDPData() {
		System.out.println("receiveUDPData...");
		ReceiveDemo.receiveData();
	}
	
	//post请求后端收不到参数的解决方案：https://blog.csdn.net/xu_lo/article/details/90041606
	public JSONObject getRespJson(String method,JSONObject paramJO) throws Exception {
		// TODO Auto-generated method stub
		//POST的URL
		//建立HttpPost对象
		HttpPost httppost=new HttpPost(JM_URL+method);
		httppost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		//添加参数
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		if(paramJO!=null) {
			Iterator<String> paramJOIter = paramJO.keys();
			int index=0;
			while (paramJOIter.hasNext()) {
				String key = paramJOIter.next();
				String value = paramJO.get(key).toString();
				//System.out.println("key==="+key);
				//System.out.println("value==="+value);
				params.add(index, new BasicNameValuePair(key, value));
				index++;
			}
		}
		if(params!=null)
			httppost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
		//设置编码
		HttpResponse response=new DefaultHttpClient().execute(httppost);
		//发送Post,并返回一个HttpResponse对象
		JSONObject resultJO = null;
		if(response.getStatusLine().getStatusCode()==200){//如果状态码为200,就是正常返回
			String result=EntityUtils.toString(response.getEntity());
			resultJO = new JSONObject(result);
		}
		return resultJO;
	}
}
