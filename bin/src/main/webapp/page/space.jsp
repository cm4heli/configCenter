<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>工作平台</title>

<%
	request.setAttribute("ctx", request.getContextPath());
%>
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
</head>
<body>
	
<div class="modal fade" id="sys_init" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width: 600px; height: 250px">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">×</button>
				<h4 class="modal-title" style="text-align:center;">系统配置</h4>
			</div>
			<div class="modal-body" style="width:597px;height:250px;max-height: 380px;max-width: 597px;overflow: auto;padding: 0px;font-size: 15px;">
				<form action="" method="post">
				<p style="margin-left: 25px;"><span>服务IP地址： </span><input  id="ip_input" style="display: inline-block;width: 70%;margin-top: 20px;" type="text" class="form-control" placeholder="输入IP地址" id="sys_ipaddress"/>	</p>
				<p style="margin-left: 25px;"><span>端口： </span><input  id="port_input"  style="display: inline-block;width: 70%;margin-left: 41px;margin-top: 20px;" type="text" class="form-control" placeholder="输入端口号" id="sys_port"/>	</p>	
				</form>
			</div>
			<div class="modal-footer" style="text-align: center;">
					<button id="confirm" type="button" class="btn btn-primary">确认</button>
					<button id="cancel" type="button" class="btn btn-success">返回</button>
				</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
</div>

</body>