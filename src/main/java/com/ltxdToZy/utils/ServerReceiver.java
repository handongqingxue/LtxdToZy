package com.ltxdToZy.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
//https://blog.csdn.net/young_YangY/article/details/84446559/
//https://www.codenong.com/cs106100031/
import org.springframework.beans.factory.annotation.Autowired;

import com.ltxdToZy.entity.v3_1.*;
import com.ltxdToZy.service.v3_1.*;

//���������������Աλ�õ�������Ϣ�ͱ���������Ϣ
@Component
@Controller
@RequestMapping(ServerReceiver.MODULE_NAME)
public class ServerReceiver {

	private static final Logger log=LoggerFactory.getLogger(ServerReceiver.class);
	public static final String MODULE_NAME="/serverReceiver";
	//private static final boolean IS_TEST=true;
	private static final boolean IS_TEST=false;
	
	@Autowired
	private PositionService positionService;

	@RequestMapping(value="/receiveMessage")
	@ResponseBody
	public Map<String, Object> receiveMessage(HttpServletRequest request) {

		//https://blog.csdn.net/Bb15070047748/article/details/112184411
		//https://blog.csdn.net/sinat_31583645/article/details/116766214
		//https://blog.csdn.net/lovekjl/article/details/108616353
		//�������鿴�������������:http://localhost:15672/#/queues/%2F/tenant_msg_F4A1D30F_sc22050664
		System.out.println("receiveMessage������");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
        try {
        	if(IS_TEST) {//���Բ��֣��ڱ��ص���ģ�����������Ϣ
        		//String bodyJOStr = "{\"method\":\"position\",\"params\":{\"absolute\":true,\"altitude\":1.0,\"areaId\":10023,\"beacons\":\"BTI2501FEA6(15000)\",\"entityType\":\"staff\",\"floor\":1,\"inDoor\":1662096250425,\"latitude\":37.041073098658146,\"locationTime\":1666764909608,\"longitude\":119.57507922005624,\"out\":false,\"rootAreaId\":1,\"silent\":false,\"speed\":0.0,\"stateTime\":1666764894643,\"tagId\":\"BTT38206876\",\"volt\":4100,\"voltUnit\":\"mV\",\"x\":81.184,\"y\":176.867,\"z\":0.0}}";
        		String bodyJOStr = "{\"method\":\"keyWarning\",\"params\":{\"tagId\":\"BTT34089197\",\"entityId\":1791,\"areaId\":10023,\"raiseTime\":1666764894643,\"x\":81.184,\"y\":176.867,\"z\":0.0,\"floor\":1}}";
        		com.alibaba.fastjson.JSONObject bodyJO = JSON.parseObject(bodyJOStr);
        		String method = bodyJO.getString("method");
        		if("position".equals(method)) {
        			JSONObject paramsJO = bodyJO.getJSONObject("params");
        			insertPositionData(paramsJO);
        		}
        	}
        	else {//��������������ݶ��н���������Ϣ
        		System.out.println("��ȡ������Ϣ������");
				ConnectionFactory factory = new ConnectionFactory();
	
				//factory.setHost("222.173.86.130");
				factory.setHost(Constant.CONN_FACTORY_HOST);
				factory.setPort(Constant.CONN_FACTORY_PORT);
				factory.setUsername(Constant.CONN_FACTORY_USERNAME);
				factory.setPassword(Constant.CONN_FACTORY_PASSWORD);
	      
	
				// 2.��������
				Connection connection = factory.newConnection();
	
				// 3.����Ƶ��
				Channel channel = connection.createChannel();
	
				// 4.��������(��ҵ���������Ѿ����������ˣ������û��Ҫ����������ᱨ��)
				//channel.queueDeclare("tenant_msg_F4A1D30F_sc22050664", true, false, false, null);
	
				// 5. ������Ϣ
				//tenant_msg_F4A1D30F_sc22050664
				channel.basicConsume("tenant_msg_E9BA5D6C_sc21090413", true, new DefaultConsumer(channel) {
	
				        // �ص�����,���յ���Ϣ֮��,���Զ�ִ�и÷���
				    public void handleDelivery(String consumerTag, Envelope envelope,AMQP.BasicProperties properties,byte[] body) throws IOException {
					    //body:{"method":"position","params":{"absolute":true,"altitude":1.0,"areaId":10023,"beacons":"BTI2501FEA6(15000)","entityType":"staff","floor":1,"inDoor":1662096250425,"latitude":37.041073098658146,"locationTime":1666764909608,"longitude":119.57507922005624,"out":false,"rootAreaId":1,"silent":false,"speed":0.0,"stateTime":1666764894643,"tagId":"BTT38206876","volt":4100,"voltUnit":"mV","x":81.184,"y":176.867,"z":0.0}}
					    String bodyJOStr = new String(body);
					    System.out.println("handleDelivery...");
					    //System.out.println("bodyJOStr===" + bodyJOStr);
						com.alibaba.fastjson.JSONObject bodyJO = JSON.parseObject(bodyJOStr);
						String method = bodyJO.getString("method");
						if("position".equals(method)) {
							JSONObject paramsJO = bodyJO.getJSONObject("params");
							insertPositionData(paramsJO);
						}
				    }
				});
				
				// ���ͷ���Դ,��rabbitmqһֱ����
				//���ͨ�����رջ�һֱ�������ǵĶ���
		        //channel.close();
		        //connection.close();
        	}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        finally {
        	return resultMap;
        }
    }

	@RequestMapping(value="/insertPositionData")
	@ResponseBody
	public Map<String, Object> insertPositionData(JSONObject paramsJO) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			Boolean absolute = paramsJO.getBoolean("absolute");
			Float altitude = paramsJO.getFloat("altitude");
			Integer areaId = paramsJO.getInteger("areaId");
			String beacons = paramsJO.getString("beacons");
			String entityType = paramsJO.getString("entityType");
			Integer floor = paramsJO.getInteger("floor");
			Long inDoor = paramsJO.getLong("inDoor");
			Float latitude = paramsJO.getFloat("latitude");
			Integer locationTime = paramsJO.getInteger("locationTime");
			Float longitude = paramsJO.getFloat("longitude");
			Boolean out = paramsJO.getBoolean("out");
			Integer rootAreaId = paramsJO.getInteger("rootAreaId");
			Boolean silent = paramsJO.getBoolean("silent");
			Float speed = paramsJO.getFloat("speed");
			Integer stateTime = paramsJO.getInteger("stateTime");
			String tagId = paramsJO.getString("tagId");
			Integer volt = paramsJO.getInteger("volt");
			String voltUnit = paramsJO.getString("voltUnit");
			Float x = paramsJO.getFloat("x");
			Float y = paramsJO.getFloat("y");
			Float z = paramsJO.getFloat("z");

			Position position = new Position();
			position.setAbsolute(absolute);
			position.setAltitude(altitude);
			position.setAreaId(areaId);
			position.setBeacons(beacons);
			position.setEntityType(entityType);
			position.setFloor(floor);
			position.setInDoor(inDoor);
			position.setLatitude(latitude);
			position.setLocationTime(locationTime);
			position.setLongitude(longitude);
			position.setOut(out);
			position.setRootAreaId(rootAreaId);
			position.setSilent(silent);
			position.setSpeed(speed);
			position.setStateTime(stateTime);
			position.setTagId(tagId);
			position.setVolt(volt);
			position.setVoltUnit(voltUnit);
			position.setX(x);
			position.setY(y);
			position.setZ(z);

			positionService.add(position);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			return resultMap;
		}
	}
}
