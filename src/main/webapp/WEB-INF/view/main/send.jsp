<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
	String basePath=request.getScheme()+"://"+request.getServerName()+":"
		+request.getServerPort()+request.getContextPath()+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="<%=basePath %>resource/js/jquery-3.3.1.js"></script>
<script type="text/javascript">
var path='<%=basePath %>';
var serverReceiverPath=path+"serverReceiver/";
var macAddress='${requestScope.macAddress}';
var pushSpace='${requestScope.pushSpace}';
$(function(){
	compareMacAddress();
});

function compareMacAddress(){
	$.post(path+"main/getMacAddress",
		function(data){
			console.log(data.macAddress+","+macAddress);
			if(data.macAddress==macAddress){
				receiveMessage();//只有新版真源平台才需要对接这个推送，只要数据有变化就会收到推送消息
				setInterval("sendUDPData()",pushSpace);
			}
		}
	,"json");
}

function receiveMessage(){
	alert(serverReceiverPath)
	$.post(serverReceiverPath+"receiveMessage",
		function(){
		
		}
	,"json");
}

//https://blog.csdn.net/qq_33470469/article/details/81772882
function sendUDPData(){
	$.post(path+"main/sendUDPData",
		function(){
		
		}
	,"json");
}
</script>
<title>Insert title here</title>
</head>
<body>

</body>
</html>