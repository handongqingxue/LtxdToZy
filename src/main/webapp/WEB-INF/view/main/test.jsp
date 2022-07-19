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
$(function(){
	insertEntityData();
});

function newFindRecords(){
	$.post(path+"main/newFindRecords",
		function(){
		
		}
	,"json");
}

function login(){
	$.post(path+"main/login",
		{tenantId:"sc21090414",userId:"test001",password:"test001"},
		function(data){
			console.log(data);
		}
	,"json");
}

function insertEntityData(){
	$.post(path+"main/insertEntityData",
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