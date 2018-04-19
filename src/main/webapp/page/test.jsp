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

<script type="text/javascript">
 $.ajax({
	 url : "http://10.6.10.105:8080/cloudservice/api/pop_info_base?pname=%E5%BC%A0%E4%B8%89",
	 jsonp:"jsonpcallback",
	 dataType:"jsonp",
	 type:"post",
	 success : function(data){  
		 debugger
         console.log(data);  
     },  
	 
     error:function(data){
    	 debugger
     }
	 
 })

</script>
<script type="text/javascript"
	src="${ctx }/resources/bootstrap/js/prettify.js"></script>
	</head>
	<body>
	
		123
	</body>