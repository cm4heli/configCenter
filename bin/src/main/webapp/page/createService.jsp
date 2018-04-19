<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
<title>工作平台</title>
<%
	request.setAttribute("ctx", request.getContextPath());
%>
<script type="text/javascript">
 var ctx = '<%=request.getContextPath()%>';
</script>
<!-- Bootstrap -->
<link href="${ctx }/resources/bootstrap/css/bootstrap.min.css"
	rel="stylesheet">
	<link href="${ctx }/resources/mdp/css/style.css"
	rel="stylesheet">
<link href="${ctx }/resources/css/createService.css" rel="stylesheet">
<link href="${ctx }/resources/autosuggest.js-master/autosuggest.css" rel="stylesheet">
<link href="${ctx }/resources/bootstrap-table-master/src/bootstrap-table.js">
<link href="${ctx }/resources/dialog/css/sweet-alert.css"
	rel="stylesheet">
<link href="${ctx }/resources/mdp/ace/assets/css/font-awesome.min.css " rel="stylesheet"></link>
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="${ctx }/resources/jquery/jquery.js"></script>
<script src="${ctx }/resources/jquery/ui/js/jquery-ui.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="${ctx }/resources/bootstrap/js/bootstrap.min.js"></script>
<script src="${ctx }/resources/bootstrap/js/modal.js"></script>
<script src="${ctx }/resources/bootstrap-table-master/src/bootstrap-table.js"></script>
<script src="${ctx }/resources/dialog/js/sweet-alert.js"></script>
<script src="${ctx }/resources/autosuggest.js-master/autosuggest.js"></script>
<script src="${ctx }/resources/fileup/src/lrz.js"></script>
<script src="${ctx }/resources/js/ajaxfileupload.js"></script>

<script type="text/javascript"
	src="${ctx }/resources/bootstrap/js/prettify.js"></script>
	
<script src="${ctx }/resources/js/main.js"></script>
<style type="text/css">
.sx{
  background: url("${ctx }/resources/img/sx.png") no-repeat right ;
  background-size:10px 10px;
  cursor: pointer;
}
.editcss{
  cursor:pointer;
}
.panel-margin{
  margin:5px;
}
</style>
</head>
<body style="position:relative;overflow-x: hidden;">
<div id="loadingDIV" class="hide">
	<div id='imgBox'>
	<img src="${ctx }/resources/image/bigLoading.gif"/>
	</div>
</div>
<div id="bodyDiv" class="mt15">
	<%@include file="import.jsp"%>
	<div id="listId" class="container-fluid">
		<%@include file="list.jsp"%>
	</div>
	<div id="configId" class="container-fluid">
		<div class="row">
			<div class="col-md-12 col-lg-12">
				<!-- 服务基本信息 -->
				<%@include file="service_info.jsp"%>
			</div>
			<div>
				<%@include file="service_type.jsp" %>
			</div>
			<div class="col-md-12 col-lg-12">
				<!-- 服务基本信息 -->
				<%@include file="select_table.jsp"%>
			</div>
			
			<div class="col-md-12 col-lg-12 hbaseReq" >
				<!-- hbase 配置请求参数 -->
				<%@include file="request_args.jsp"%>
			</div>
			<div class="col-md-12 col-lg-12 hbaseReq">
				<!-- hbase 配置响应参数 -->
				<%@include file="response_args.jsp"%>
			</div>
			<div class="col-md-12 col-lg-12 inceptorReq" >
				<!-- inceptor  请求参数 -->
				<%@include file="request_args_sql.jsp"%>
			</div>
			<div class="col-md-12 col-lg-12 inceptorReq" >
				<!-- inceptor 响应参数 -->
				<%@include file="response_args_sql.jsp"%>
			</div>
			<div class="col-md-12 col-lg-12 oracleReq_01" >
				<!-- oracle  请求参数 -->
				<%@include file="request_args_oracle.jsp"%>
			</div>
			<div class="col-md-12 col-lg-12 oracleReq_03" >
				<!-- oracle 03类服务  请求参数 -->
				<%@include file="request_oracle_03.jsp"%>
			</div>
			<div class="col-md-12 col-lg-12 oracleReq" >
				<!-- oracle 响应参数 -->
				<%@include file="response_args_oracle.jsp"%>
			</div>
			<div id="sqldiv" class="col-md-12 col-lg-12">
				<!-- sql脚本 -->
				<%@include file="sql_info.jsp"%>
			</div>
			<div class="col-md-6 col-lg-6"
				style="margin-bottom: 10px; float: right;    margin-bottom: 10px;text-align: center;width: 100%;">
				<button type="button" class="btn btn-success" id="publishServiceBtn">保存服务</button>
				<button type="button" class="btn btn-warning" id="cancel" onclick="show(1)">返回</button>
			</div>
		</div>
	</div>
	<!-- 列表进入编辑页面时隐藏字段 -->
	<!--<form name="edit" action="${ctx}/pageAction/edit" method="post" >
	   <input type="hidden" name="id"  id="editID" />
	</form>
	<!-- 编辑时隐藏字段 -->
	<input type="hidden"  name="ruleConfigId" value="${config.id}" />
	<input type="hidden"  id="pageType" value="${pageType}" /> 
	<%@include file ="help_text.jsp" %>
	<%@include file ="handbook.jsp" %>
	<%@include file ="serviceTest.jsp" %>
	<%@include file ="phone.jsp" %>
	<%@include file ="service_url.jsp" %>
	<%@include file ="publishService.jsp" %>
	<%@include file ="modifyCreator.jsp" %>
		</div>
	<script>

  /*  $('#authId').on("click",function(){
        
   }); */
</script>
</body>
</html>
