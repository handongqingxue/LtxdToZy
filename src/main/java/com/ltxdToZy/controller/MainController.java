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
import com.ltxdToZy.entity.*;
import com.ltxdToZy.udp.*;
import com.ltxdToZy.utils.*;
import com.ltxdToZy.service.*;

@Controller
@RequestMapping(MainController.MODULE_NAME)
public class MainController {

	private static final String PUBLIC_URL="http://"+Constant.SERVICE_IP+":8081/position/public/embeded.smd";
	private static final String SERVICE_URL="http://"+Constant.SERVICE_IP+":8081/position/service/embeded.smd";
	private static final String JM_URL="http://192.168.1.9:8090/";
	public static final String MODULE_NAME="main";
	public static final String TEST_USER_Id="test001";
	
	@Autowired
	private LoginUserService loginUserService;
	@Autowired
	private EntityService entityService;
	@Autowired
	private LocationService locationService;

	@RequestMapping(value="/goTest")
	public String goTest(HttpServletRequest request) {
		
		request.setAttribute("tenantId",Constant.TENANT_ID);
		request.setAttribute("userId",Constant.USER_ID);
		request.setAttribute("password",Constant.PASSWORD);
		
		return MODULE_NAME+"/test";
	}

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
	@ResponseBody
	public Map<String, Object> sendUDPData() {
		System.out.println("sendUDPData...");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//SendDemo.sendData();
		DatagramSocket ds=null;
		try {
			ds=new DatagramSocket(); //????????socket
			 
			BufferedReader br=new BufferedReader(new InputStreamReader(System.in));//??????????????
			InetAddress ia = InetAddress.getByName("192.168.2.222");
			//InetAddress ia = InetAddress.getByName("127.0.0.1");
		    int port=10003;

			List<Location> locationList=locationService.selectEntityLocation();
			//System.out.println("size==="+locationList.size());
			String text=JSON.toJSONString(locationList);
	    	//String text="[{\"name\":\"??????\",\"x\":100,\"y\":200},{\"name\":\"??????\",\"x\":100,\"y\":200},{\"name\":\"??????\",\"x\":100,\"y\":200}]";
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
	 * 2.1.1 ??????????
	 * @param tenantId
	 * @param userId
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getCode")
	@ResponseBody
	public Map<String, Object> getCode(String tenantId, String userId,HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			JSONObject bodyParamJO=new JSONObject();
			bodyParamJO.put("jsonrpc", "2.0");
			JSONObject paramJO=new JSONObject();
			paramJO.put("tenantId", tenantId);
			paramJO.put("userId", userId);
			bodyParamJO.put("params", paramJO);
			bodyParamJO.put("method", "getCode");
			bodyParamJO.put("id", 1);
			JSONObject resultJO = postBody(PUBLIC_URL,bodyParamJO,"getCode",request);
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
	
	/**
	 * 2.1.2 ????
	 * @param tenantId
	 * @param userId
	 * @param password
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/login")
	@ResponseBody
	public Map<String, Object> login(String tenantId, String userId, String password,HttpServletRequest request){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			JSONObject bodyParamJO=new JSONObject();
			bodyParamJO.put("jsonrpc", "2.0");
			JSONObject paramJO=new JSONObject();
			paramJO.put("tenantId", tenantId);
			paramJO.put("userId", userId);
			String vsCode = getCode(tenantId,userId,request).get("result").toString();
			paramJO.put("key", SHA256Utils.getSHA256(tenantId+userId+password+vsCode));
			bodyParamJO.put("params", paramJO);
			bodyParamJO.put("method", "login");
			bodyParamJO.put("id", 1);
			JSONObject resultJO = postBody(PUBLIC_URL,bodyParamJO,"login",request);
			String resultStr = resultJO.toString();
			System.out.println("resultJO==="+resultStr);
			resultMap=JSON.parseObject(resultStr, Map.class);
			
			if("ok".equals(resultJO.get("status").toString())) {
				Map<String, Object> resultJOMap = (Map<String, Object>)resultMap.get("result");
				
				HttpSession session = request.getSession();
				LoginUser loginUser=(LoginUser)session.getAttribute("loginUser");
				loginUser.setUserId(userId);
				loginUser.setRole(Integer.valueOf(resultJOMap.get("role").toString()));
			}
			else {
				resultMap.put("message", "??????????");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			return resultMap;
		}
	}
	
	@RequestMapping(value="/insertEntityData")
	@ResponseBody
	public Map<String, Object> insertEntityData(HttpServletRequest request) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> erMap = getEntities("staff", request);
		List<Entity> entityList = JSON.parseArray(erMap.get("result").toString(),Entity.class);
		int count=entityService.add(entityList);
		if(count==0) {
			resultMap.put("status", "no");
			resultMap.put("message", "??????????????????");
		}
		else {
			resultMap.put("status", "ok");
			resultMap.put("message", "??????????????????");
		}
		return resultMap;
	}

	/**
	 * 2.6.9 ????????????
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getEntities")
	@ResponseBody
	public Map<String, Object> getEntities(String entityType, HttpServletRequest request) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			JSONObject bodyParamJO=new JSONObject();
			bodyParamJO.put("jsonrpc", "2.0");
			bodyParamJO.put("method", "getEntities");
			JSONObject paramJO=new JSONObject();
			paramJO.put("entityType", entityType);
			//paramJO.put("pageIndex", "0");
			//paramJO.put("maxCount", "100");
			bodyParamJO.put("params", paramJO);
			bodyParamJO.put("id", 1);
			JSONObject resultJO = postBody(SERVICE_URL,bodyParamJO,"getEntities",request);
			System.out.println("getEntities:resultJO==="+resultJO.toString());
			/*
			 {"result":[
			 {"tagId":"BTT1E7D5EB9","entityType":"staff","sex":1,"departmentId":0,"photo":null,"pid":"phone001","userId":"5EB9","post":"","phone":"1","name":"??????????????","dutyId":1,"id":128813,"age":"1"},
			 {"post":"????","phone":"3316","tagId":"BTT32003607","entityType":"staff","sex":1,"departmentId":4,"name":"????-test","photo":null,"dutyId":1,"pid":"FUPY00000","id":24092,"userId":"3607"},
			 {"security":"","post":"","phone":"3222","tagId":"BTT32003729","entityType":"staff","departmentId":33,"name":"??????","photo":"","dutyId":1,"pid":"FUPYJX008","id":28041,"userId":"3729"},
			 {"post":"????","phone":"123","tagId":"BTT32003618","entityType":"staff","departmentId":13,"name":"??????","photo":null,"dutyId":4,"pid":"FUPY20013","id":24103,"userId":"3618"},
			 {"post":"????","phone":"123","tagId":"BTT32003628","entityType":"staff","departmentId":13,"name":"????","photo":null,"dutyId":4,"pid":"FUPY19008","id":24111,"userId":"3628"},
			 {"post":"????","phone":"123","tagId":"BTT32003660","entityType":"staff","departmentId":13,"name":"????","photo":null,"dutyId":4,"pid":"FUPY20007","id":24132,"userId":"3660"},
			 {"post":"????","phone":"123","tagId":"BTT32003668","entityType":"staff","departmentId":13,"name":"????","photo":null,"dutyId":4,"pid":"FUPY13042","id":24140,"userId":"3668"},
			 {"post":"????","phone":"123","tagId":"BTT32003605","entityType":"staff","departmentId":5,"name":"??????","photo":null,"dutyId":3,"pid":"FUPY12006","id":24090,"userId":"3605"},
			 {"post":"????","phone":"123","tagId":"BTT32003611","entityType":"staff","departmentId":6,"name":"??????","photo":null,"dutyId":3,"pid":"FUPY14064","id":24096,"userId":"3611"},
			 {"post":"????","phone":"123","tagId":"BTT32003624","entityType":"staff","departmentId":5,"name":"??????","photo":null,"dutyId":3,"pid":"FUPY12001","id":24109,"userId":"3624"},
			 {"post":"????","phone":"123","tagId":"BTT32003658","entityType":"staff","departmentId":6,"name":"??????","photo":null,"dutyId":3,"pid":"FUPY17025","id":24130,"userId":"3658"},
			 {"post":"????","phone":"123","tagId":"BTT32003688","entityType":"staff","departmentId":6,"name":"????","photo":null,"dutyId":3,"pid":"FUPY13038","id":24154,"userId":"3688"},
			 {"post":"????","phone":"123","tagId":"BTT32003828","entityType":"staff","departmentId":22,"name":"??????","photo":null,"dutyId":3,"pid":"FUPYYC15011","id":24137,"userId":"3828"},{"post":"????","tagId":"BTT32003603","entityType":"staff","sex":1,"departmentId":3,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY20018","id":24088,"userId":"3603"},{"post":"????","tagId":"BTT32003602","entityType":"staff","sex":1,"departmentId":2,"name":"????","photo":null,"dutyId":1,"pid":"FUPY20017","id":24087,"userId":"3602"},{"post":"????","tagId":"BTT32003604","entityType":"staff","sex":1,"departmentId":4,"name":"????","photo":null,"dutyId":1,"pid":"FUPY20019","id":24089,"userId":"3604"},{"post":"????","tagId":"BTT32003608","entityType":"staff","sex":1,"departmentId":4,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY17017","id":24093,"userId":"3608"},{"post":"????","tagId":"BTT32003609","entityType":"staff","sex":1,"departmentId":17,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY88888","id":24094,"userId":"3609"},{"post":"????","tagId":"BTT32003612","entityType":"staff","sex":1,"departmentId":7,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY12020","id":24097,"userId":"3612"},{"post":"????","tagId":"BTT32003613","entityType":"staff","sex":1,"departmentId":8,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY20091","id":24098,"userId":"3613"},{"post":"????","tagId":"BTT32003614","entityType":"staff","sex":1,"departmentId":9,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY13065","id":24099,"userId":"3614"},{"post":"????","tagId":"BTT32003616","entityType":"staff","sex":1,"departmentId":11,"name":"????","photo":null,"dutyId":1,"pid":"YC10055","id":24101,"userId":"3616"},{"post":"????","tagId":"BTT32003617","entityType":"staff","sex":1,"departmentId":12,"name":"????","photo":null,"dutyId":1,"pid":"FUPY17002","id":24102,"userId":"3617"},{"post":"????","tagId":"BTT32003619","entityType":"staff","sex":1,"departmentId":4,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY19003","id":24104,"userId":"3619"},{"post":"????","tagId":"BTT32003620","entityType":"staff","sex":1,"departmentId":14,"name":"????","photo":null,"dutyId":1,"pid":"FUPY13058","id":24105,"userId":"3620"},{"post":"????","tagId":"BTT32003621","entityType":"staff","sex":1,"departmentId":15,"name":"??????","photo":null,"dutyId":1,"pid":"FEITYZ0007","id":24106,"userId":"3621"},{"post":"????","tagId":"BTT32003622","entityType":"staff","sex":1,"departmentId":4,"name":"??????","photo":null,"dutyId":1,"pid":"YC18006","id":24107,"userId":"3622"},{"post":"????","tagId":"BTT32003623","entityType":"staff","sex":1,"departmentId":9,"name":"????","photo":null,"dutyId":1,"pid":"FUPY15037","id":24108,"userId":"3623"},{"post":"????","tagId":"BTT32003626","entityType":"staff","sex":1,"departmentId":16,"name":"????","photo":null,"dutyId":1,"pid":"FUPY13060","id":24110,"userId":"3626"},{"post":"????","tagId":"BTT32003631","entityType":"staff","sex":1,"departmentId":4,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY17024","id":24113,"userId":"3631"},{"post":"????","tagId":"BTT32003633","entityType":"staff","sex":1,"departmentId":17,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY13012","id":24114,"userId":"3633"},{"post":"????","tagId":"BTT32003634","entityType":"staff","sex":1,"departmentId":4,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY14083","id":24115,"userId":"3634"},{"post":"????","tagId":"BTT32003636","entityType":"staff","sex":1,"departmentId":9,"name":"????","photo":null,"dutyId":1,"pid":"FUPY13046","id":24116,"userId":"3636"},{"post":"????","tagId":"BTT32003638","entityType":"staff","sex":1,"departmentId":14,"name":"????","photo":null,"dutyId":1,"pid":"FUPY15041","id":24117,"userId":"3638"},{"post":"????","tagId":"BTT32003639","entityType":"staff","sex":1,"departmentId":7,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY13076","id":24118,"userId":"3639"},{"post":"????","tagId":"BTT32003642","entityType":"staff","sex":1,"departmentId":14,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY13055","id":24119,"userId":"3642"},{"post":"????","tagId":"BTT32003643","entityType":"staff","sex":1,"departmentId":7,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY19001","id":24120,"userId":"3643"},{"post":"????","tagId":"BTT32003644","entityType":"staff","sex":1,"departmentId":4,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY17001","id":24121,"userId":"3644"},{"post":"????","tagId":"BTT32003645","entityType":"staff","sex":1,"departmentId":9,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY15030","id":24122,"userId":"3645"},{"post":"????","tagId":"BTT32003646","entityType":"staff","sex":1,"departmentId":4,"name":"????","photo":null,"dutyId":1,"pid":"FUPY19006","id":24123,"userId":"3646"},{"post":"????","tagId":"BTT32003650","entityType":"staff","sex":1,"departmentId":16,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY17004","id":24124,"userId":"3650"},{"post":"????","tagId":"BTT32003653","entityType":"staff","sex":1,"departmentId":18,"name":"??????","photo":null,"dutyId":1,"pid":"YC12002","id":24125,"userId":"3653"},{"post":"????","tagId":"BTT32003654","entityType":"staff","sex":1,"departmentId":7,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY18022","id":24126,"userId":"3654"},{"post":"????","tagId":"BTT32003655","entityType":"staff","sex":1,"departmentId":9,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY13030","id":24127,"userId":"3655"},{"post":"????","tagId":"BTT32003656","entityType":"staff","sex":1,"departmentId":16,"name":"????","photo":null,"dutyId":1,"pid":"FUPY18005","id":24128,"userId":"3656"},{"post":"????","tagId":"BTT32003657","entityType":"staff","sex":1,"departmentId":4,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY17016","id":24129,"userId":"3657"},{"post":"????","tagId":"BTT32003659","entityType":"staff","sex":1,"departmentId":19,"name":"??????","photo":null,"dutyId":1,"pid":"FEITYZ0034","id":24131,"userId":"3659"},{"post":"????","tagId":"BTT32003661","entityType":"staff","sex":1,"departmentId":12,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY14172","id":24133,"userId":"3661"},{"post":"????","tagId":"BTT32003662","entityType":"staff","sex":1,"departmentId":20,"name":"??????","photo":null,"dutyId":1,"pid":"YCK99001","id":24134,"userId":"3662"},{"post":"????","tagId":"BTT32003663","entityType":"staff","sex":1,"departmentId":21,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY14056","id":24135,"userId":"3663"},{"post":"????","tagId":"BTT32003666","entityType":"staff","sex":1,"departmentId":9,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY12015","id":24138,"userId":"3666"},{"post":"????","tagId":"BTT32003667","entityType":"staff","sex":1,"departmentId":16,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY13061","id":24139,"userId":"3667"},{"post":"????","tagId":"BTT32003669","entityType":"staff","sex":1,"departmentId":8,"name":"????","photo":null,"dutyId":1,"pid":"FUPY12012","id":24141,"userId":"3669"},{"post":"????","tagId":"BTT32003671","entityType":"staff","sex":1,"departmentId":15,"name":"????","photo":null,"dutyId":1,"pid":"FEITYZ0005","id":24142,"userId":"3671"},{"post":"????","tagId":"BTT32003672","entityType":"staff","sex":1,"departmentId":16,"name":"????","photo":null,"dutyId":1,"pid":"FUPY15001","id":24143,"userId":"3672"},{"post":"????","tagId":"BTT32003673","entityType":"staff","sex":1,"departmentId":16,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY16003","id":24144,"userId":"3673"},{"post":"????","tagId":"BTT32003676","entityType":"staff","sex":1,"departmentId":16,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY14052","id":24145,"userId":"3676"},{"post":"????","tagId":"BTT32003677","entityType":"staff","sex":1,"departmentId":4,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY20005","id":24146,"userId":"3677"},{"post":"????","tagId":"BTT32003678","entityType":"staff","sex":1,"departmentId":4,"name":"????","photo":null,"dutyId":1,"pid":"FUPY14072","id":24147,"userId":"3678"},{"post":"????","tagId":"BTT32003679","entityType":"staff","sex":1,"departmentId":21,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY17010","id":24148,"userId":"3679"},{"post":"????","tagId":"BTT32003680","entityType":"staff","sex":1,"departmentId":11,"name":"??????","photo":null,"dutyId":1,"pid":"YC10051","id":24149,"userId":"3680"},{"post":"????","tagId":"BTT32003681","entityType":"staff","sex":1,"departmentId":23,"name":"????","photo":null,"dutyId":1,"pid":"FUPY19014","id":24150,"userId":"3681"},{"post":"????","tagId":"BTT32003682","entityType":"staff","sex":1,"departmentId":4,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY14087","id":24151,"userId":"3682"},{"post":"????","tagId":"BTT32003683","entityType":"staff","sex":1,"departmentId":4,"name":"????","photo":null,"dutyId":1,"pid":"FUPY14060","id":24152,"userId":"3683"},{"post":"????","tagId":"BTT32003685","entityType":"staff","sex":1,"departmentId":15,"name":"??????","photo":null,"dutyId":1,"pid":"FEITYZ0002","id":24153,"userId":"3685"},{"post":"????","tagId":"BTT32003689","entityType":"staff","sex":1,"departmentId":24,"name":"????","photo":null,"dutyId":1,"pid":"FUPY15024","id":24155,"userId":"3689"},{"post":"????","tagId":"BTT32003693","entityType":"staff","sex":1,"departmentId":16,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY13062","id":24156,"userId":"3693"},{"post":"????","tagId":"BTT32003695","entityType":"staff","sex":1,"departmentId":4,"name":"????","photo":null,"dutyId":1,"pid":"FUPY15038","id":24157,"userId":"3695"},{"post":"????","tagId":"BTT32003696","entityType":"staff","sex":1,"departmentId":18,"name":"??????","photo":null,"dutyId":1,"pid":"FUPYYC12005","id":24158,"userId":"3696"},{"post":"????","phone":"123","tagId":"BTT32003615","entityType":"staff","departmentId":10,"name":"??????","photo":null,"dutyId":4,"pid":"FUPY12035","id":24100,"userId":"3615"},{"tagId":"BTTFCC6E902","entityType":"staff","sex":1,"departmentId":15,"photo":"","pid":"FEITYZ1000","userId":"6E902","security":"","post":"","phone":"2515","name":"????????????","dutyId":1,"id":38466},{"tagId":"BTT32003709","entityType":"staff","sex":2,"departmentId":15,"photo":"","pid":"feityz1000","userId":"3709","security":"","post":"","phone":"2515","name":"??????????","dutyId":1,"id":129978},{"post":"????","phone":"123","tagId":"BTT32003741","entityType":"staff","departmentId":26,"name":"??????","photo":null,"dutyId":3,"pid":"FUPY14055","id":24187,"userId":"3741"},{"post":"????","phone":"123","tagId":"BTT32003762","entityType":"staff","departmentId":32,"name":"??????","photo":null,"dutyId":3,"pid":"FUPY20009","id":24199,"userId":"3762"},{"post":"????","phone":"123","tagId":"BTT32003763","entityType":"staff","departmentId":22,"name":"??????","photo":null,"dutyId":3,"pid":"FUPY13004","id":24200,"userId":"3763"},{"post":"????","phone":"123","tagId":"BTT32003774","entityType":"staff","departmentId":6,"name":"??????","photo":null,"dutyId":3,"pid":"FUPY13108","id":24205,"userId":"3774"},{"post":"????","phone":"123","tagId":"BTT32003777","entityType":"staff","departmentId":6,"name":"??????","photo":null,"dutyId":3,"pid":"FUPY12033","id":24207,"userId":"3777"},{"post":"????","phone":"123","tagId":"BTT32003785","entityType":"staff","departmentId":6,"name":"??????","photo":null,"dutyId":3,"pid":"FUPY20010","id":24213,"userId":"3785"},{"post":"????","phone":"123","tagId":"BTT32003765","entityType":"staff","departmentId":30,"name":"??????","photo":null,"dutyId":6,"pid":"FUPY13005","id":24202,"userId":"3765"},{"post":"????","phone":"123","tagId":"BTT32003791","entityType":"staff","departmentId":30,"name":"????","photo":null,"dutyId":6,"pid":"FUPY18021","id":24217,"userId":"3791"},{"post":"????","phone":"123","tagId":"BTT32003794","entityType":"staff","departmentId":30,"name":"??????","photo":null,"dutyId":6,"pid":"FUPY14034","id":24220,"userId":"3794"},{"post":"????","phone":"123","tagId":"BTT32003795","entityType":"staff","departmentId":30,"name":"????","photo":null,"dutyId":6,"pid":"FUPY14002","id":24221,"userId":"3795"},{"post":"????","phone":"123","tagId":"BTT32003800","entityType":"staff","departmentId":30,"name":"??????","photo":null,"dutyId":6,"pid":"FUPY17006","id":24226,"userId":"3800"},{"post":"????","phone":"123","tagId":"BTT32003802","entityType":"staff","departmentId":30,"name":"????","photo":null,"dutyId":6,"pid":"FUPY18027","id":24228,"userId":"3802"},{"post":"????","tagId":"BTT32003697","entityType":"staff","sex":1,"departmentId":15,"name":"??????","photo":null,"dutyId":1,"pid":"FEITYZ0036","id":24159,"userId":"3697"},{"post":"????","phone":"123","tagId":"BTT32003714","entityType":"staff","departmentId":6,"name":"??????","photo":null,"dutyId":3,"pid":"FUPY13083","id":24169,"userId":"3714"},{"post":"????","tagId":"BTT32003698","entityType":"staff","sex":1,"departmentId":4,"name":"????","photo":null,"dutyId":1,"pid":"FUPY14098","id":24160,"userId":"3698"},{"post":"????","tagId":"BTT32003699","entityType":"staff","sex":1,"departmentId":16,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY19017","id":24161,"userId":"3699"},{"post":"????","tagId":"BTT32003700","entityType":"staff","sex":1,"departmentId":4,"name":"????","photo":null,"dutyId":1,"pid":"FUPY14110","id":24162,"userId":"3700"},{"post":"????","tagId":"BTT32003707","entityType":"staff","sex":1,"departmentId":4,"name":"????","photo":null,"dutyId":1,"pid":"FUPY18002","id":24163,"userId":"3707"},{"post":"????","tagId":"BTT32003708","entityType":"staff","sex":1,"departmentId":4,"name":"????","photo":null,"dutyId":1,"pid":"FUPY14097","id":24164,"userId":"3708"},{"post":"????","tagId":"BTT32003711","entityType":"staff","sex":1,"departmentId":8,"name":"????","photo":null,"dutyId":1,"pid":"FUPY20008","id":24166,"userId":"3711"},{"post":"????","tagId":"BTT32003712","entityType":"staff","sex":1,"departmentId":16,"name":"????","photo":null,"dutyId":1,"pid":"FUPY14189","id":24167,"userId":"3712"},{"post":"????","tagId":"BTT32003713","entityType":"staff","sex":1,"departmentId":4,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY14078","id":24168,"userId":"3713"},{"post":"????","tagId":"BTT32003716","entityType":"staff","sex":1,"departmentId":18,"name":"????","photo":null,"dutyId":1,"pid":"YC14001","id":24170,"userId":"3716"},{"post":"????","tagId":"BTT32003717","entityType":"staff","sex":1,"departmentId":11,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY18006","id":24171,"userId":"3717"},{"post":"????","tagId":"BTT32003718","entityType":"staff","sex":1,"departmentId":25,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY18010","id":24172,"userId":"3718"},{"post":"????","tagId":"BTT32003720","entityType":"staff","sex":1,"departmentId":20,"name":"????","photo":null,"dutyId":1,"pid":"YC17003","id":24173,"userId":"3720"},{"post":"????","tagId":"BTT32003721","entityType":"staff","sex":1,"departmentId":4,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY20015","id":24174,"userId":"3721"},{"post":"????","tagId":"BTT32003722","entityType":"staff","sex":1,"departmentId":14,"name":"??????","photo":null,"dutyId":1,"pid":"FUPY13057","id":24175,"userId":"3722"}],"id":1,"jsonrpc":"2.0"}
			 */
			resultMap=JSON.parseObject(resultJO.toString(), Map.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			return resultMap;
		}
	}

	/**
	 * ????Mac????
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

	/**
	 * ????????????
	 * @param request
	 * @param response
	 * @param json
	 * @return
	 */
	@RequestMapping(value="/receiveData", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> receiveData(HttpServletRequest request, HttpServletResponse response, @RequestBody String json) {

		//http://127.0.0.1:8080/LtxdToZy/main/receiveData
		//System.out.println("receiveData...");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		com.alibaba.fastjson.JSONObject jsonJO = JSON.parseObject(json);
		if(jsonJO.containsKey("Location")) {//????????
			System.out.println("????????????...");
			JSONArray locationJA = jsonJO.getJSONArray("Location");
			
			/*
			System.out.println("size==="+jsonJO.getJSONArray("Location").size());
			System.out.println("deviceType==="+jsonJO.getJSONArray("Location").getJSONObject(0).getString("deviceType"));
			System.out.println("uid==="+jsonJO.getJSONArray("Location").getJSONObject(0).getString("uid"));
			System.out.println("rootAreaId==="+jsonJO.getJSONArray("Location").getJSONObject(0).getInteger("rootAreaId"));
			System.out.println("areaId==="+jsonJO.getJSONArray("Location").getJSONObject(0).getInteger("areaId"));
			System.out.println("locationTime==="+jsonJO.getJSONArray("Location").getJSONObject(0).getLong("locationTime"));
			System.out.println("lostTime==="+jsonJO.getJSONArray("Location").getJSONObject(0).getLong("lostTime"));
			System.out.println("x==="+jsonJO.getJSONArray("Location").getJSONObject(0).getFloat("x"));
			System.out.println("y==="+jsonJO.getJSONArray("Location").getJSONObject(0).getFloat("y"));
			System.out.println("z==="+jsonJO.getJSONArray("Location").getJSONObject(0).getFloat("z"));
			System.out.println("abslute==="+jsonJO.getJSONArray("Location").getJSONObject(0).getBoolean("abslute"));
			System.out.println("speed==="+jsonJO.getJSONArray("Location").getJSONObject(0).getFloat("speed"));
			System.out.println("LocationFloor==="+jsonJO.getJSONArray("Location").getJSONObject(0).getInteger("floor"));
			System.out.println("out==="+jsonJO.getJSONArray("Location").getJSONObject(0).getBoolean("out"));
			System.out.println("longitude==="+jsonJO.getJSONArray("Location").getJSONObject(0).getFloat("longitude"));
			System.out.println("latitude==="+jsonJO.getJSONArray("Location").getJSONObject(0).getFloat("latitude"));
			System.out.println("altitude==="+jsonJO.getJSONArray("Location").getJSONObject(0).getFloat("altitude"));
			*/
			
			for(int i=0;i<locationJA.size();i++) {
				com.alibaba.fastjson.JSONObject locationJO = locationJA.getJSONObject(i);
				String deviceType = locationJO.getString("deviceType");
				String uid = locationJO.getString("uid");
				Integer rootAreaId = locationJO.getInteger("rootAreaId");
				Integer areaId = locationJO.getInteger("areaId");
				Long locationTime = locationJO.getLong("locationTime");
				Long lostTime = locationJO.getLong("lostTime");
				Float x = locationJO.getFloat("x");
				Float y = locationJO.getFloat("y");
				Float z = locationJO.getFloat("z");
				Boolean abslute = locationJO.getBoolean("abslute");
				Float speed = locationJO.getFloat("speed");
				Integer floor = locationJO.getInteger("floor");
				Boolean out = locationJO.getBoolean("out");
				Float longitude = locationJO.getFloat("longitude");
				Float latitude = locationJO.getFloat("latitude");
				Float altitude = locationJO.getFloat("altitude");
				
				Location location = new Location();
				location.setDeviceType(deviceType);
				location.setUid(uid);
				location.setRootAreaId(rootAreaId);
				location.setAreaId(areaId);
				location.setLocationTime(locationTime);
				location.setLostTime(lostTime);
				location.setX(x);
				location.setY(y);
				location.setZ(z);
				location.setAbslute(abslute);
				location.setSpeed(speed);
				location.setFloor(floor);
				location.setOut(out);
				location.setLongitude(longitude);
				location.setLatitude(latitude);
				location.setAltitude(altitude);
				
				locationService.add(location);
			}
		}
		return resultMap;
	}
	
	public JSONObject postBody(String serverURL, JSONObject bodyParamJO, String method, HttpServletRequest request) {
		JSONObject resultJO = null;
		try {
			StringBuffer sbf = new StringBuffer(); 
			String strRead = null; 
			URL url = new URL(serverURL); 
			HttpURLConnection connection = (HttpURLConnection)url.openConnection(); 
			
			//connection.setInstanceFollowRedirects(false); 
			
			HttpSession session = request.getSession();
			if(serverURL.contains("service")) {
				//connection.setRequestProperty("Cookie", "JSESSIONID=849CB322A20324C2F7E11AD0A7A9899E;Path=/position; Domain=139.196.143.225; HttpOnly;");
				//connection.setRequestProperty("Cookie", "JSESSIONID=E1CD97E8E9AA306810805BFF21D7FD7D; Path=/position; HttpOnly");
				String cookie = null;
				Object LoginUserObj = session.getAttribute("loginUser");
				//System.out.println("LoginUserObj==="+LoginUserObj);
				if(LoginUserObj!=null) {
					LoginUser loginUser = (LoginUser)LoginUserObj;
					cookie = loginUser.getCookie();
				}
				
				if(cookie==null)
					cookie = loginUserService.getCookieByUserId(TEST_USER_Id);
					
				if(!StringUtils.isEmpty(cookie))
					connection.setRequestProperty("Cookie", cookie);
			}
			connection.setRequestMethod("POST");//????post????
			connection.setDoInput(true); 
			connection.setDoOutput(true); 
			//header????????????????set    
			//connection.setRequestProperty("key", "value");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.connect(); 
			
			
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(),"UTF-8"); 
			//body??????????
			String bodyParamStr = bodyParamJO.toString();
			//System.out.println("bodyParamStr==="+bodyParamStr);
			writer.write(bodyParamStr);
			//writer.write("{ \"jsonrpc\": \"2.0\", \"params\":{\"tenantId\":\"ts000000061\",\"userId\":\"test001\"}, \"method\":\"getCode\", \"id\":1 }"); 
			writer.flush();
			InputStream is = connection.getInputStream(); 
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8")); 
			while ((strRead = reader.readLine()) != null) { 
				sbf.append(strRead); 
				sbf.append("\r\n"); 
			}
			reader.close(); 
			
			if(serverURL.contains("public")&&"login".equals(method)) {
				if(!checkCookieInSession(session)) {
					getCookieFromHeader(connection,session);
				}
			}
			
			connection.disconnect();
			String result = sbf.toString();
			//System.out.println("result==="+result);
			if(result.contains("DOCTYPE")) {
				resultJO = new JSONObject();
				resultJO.put("status", "no");
			}
			else if(result.contains("error")) {
				resultJO = new JSONObject(result);
				resultJO.put("status", "no");
			}
			else {
				resultJO = new JSONObject(result);
				resultJO.put("status", "ok");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			return resultJO;
		}
	}
	
	public boolean checkCookieInSession(HttpSession session) {
		Object loginUserObj = session.getAttribute("loginUser");
		if(loginUserObj==null)
			return false;
		else {
			LoginUser loginUser = (LoginUser)loginUserObj;
			if(loginUser==null)
				return false;
			else {
				String cookie = loginUser.getCookie();
				if(StringUtils.isEmpty(cookie))
					return false;
				else
					return true;
			}
		}
	}
	
	public String getCookieFromHeader(HttpURLConnection connection,HttpSession session) {
		Map<String,List<String>> map = connection.getHeaderFields();
		for (String key : map.keySet()) {
			String value = map.get(key).get(0);
			if(value.contains("JSESSIONID=")) {
				 System.out.println("key==="+value);
				 LoginUser loginUser=new LoginUser();
				 loginUser.setCookie(value);
				 session.setAttribute("loginUser", loginUser);
			}
		}
		return "";
	}
	
	//post??????????????????????????????https://blog.csdn.net/xu_lo/article/details/90041606
	public JSONObject getRespJson(String method,JSONObject paramJO) throws Exception {
		// TODO Auto-generated method stub
		//POST??URL
		//????HttpPost????
		HttpPost httppost=new HttpPost(JM_URL+method);
		httppost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		//????????
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
		//????????
		HttpResponse response=new DefaultHttpClient().execute(httppost);
		//????Post,??????????HttpResponse????
		JSONObject resultJO = null;
		if(response.getStatusLine().getStatusCode()==200){//????????????200,????????????
			String result=EntityUtils.toString(response.getEntity());
			resultJO = new JSONObject(result);
		}
		return resultJO;
	}
}
