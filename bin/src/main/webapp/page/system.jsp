<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<%
	request.setAttribute("ctx", request.getContextPath());
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="${ctx }/resources/bootstrap/css/bootstrap.min.css"
	rel="stylesheet">
<link href="${ctx }/resources/mdp/css/style.css" rel="stylesheet">
<script src="${ctx }/resources/jquery/jquery.js"></script>
<script src="${ctx }/resources/jquery/ui/js/jquery-ui.min.js"></script>
<title>修改服务系统信息</title>
<style type="text/css">
.p {
	width: 50%;
	margin: 0 auto;
}

.p div {
	width: 100px;
	display: inline-block;
	text-align: right;
}

.p input {
	display: inline-block;
	width: 70%;
	margin-left: 20px;
	margin-top: 20px;
}
</style>
</head>

<script type="text/javascript">
 var ctx = '<%=request.getContextPath()%>';
</script>
<link href="${ctx }/resources/bootstrap/css/bootstrap.min.css"
	rel="stylesheet">
<script src="${ctx }/resources/jquery/jquery.js"></script>
<script src="${ctx }/resources/jquery/ui/js/jquery-ui.min.js"></script>
<script src="${ctx }/resources/bootstrap/js/bootstrap.min.js"></script>
<script src="${ctx }/resources/bootstrap/js/modal.js"></script>
<script src="${ctx }/resources/js/space.js"></script>

<body>


	<div class="" style="padding: 0px; font-size: 15px;margin-top:50px">
		<div class="p">
			<div>服务IP地址：</div>
			<input id="ip_input" type="text" class="form-control"
				placeholder="输入IP地址" id="sys_ipaddress" />
		</div>
		<div class="p">
			<div>端口：</div>
			<input id="port_input" type="text" class="form-control"
				placeholder="输入端口号" id="sys_port" />
		</div>
	</div>
	<div>
		<button id="confirm" type="button" class="btn btn-primary" style="margin-left: 47%;margin-top: 33px;">确认修改</button>
<!-- 		<button id="cancel" type="button" class="btn btn-success">返回</button> -->
	</div>



</body>
</html>

