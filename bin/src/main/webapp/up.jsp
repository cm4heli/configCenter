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
<link href="${ctx }/resources/css/createService.css" rel="stylesheet">
<link href="${ctx }/resources/autosuggest.js-master/autosuggest.css" rel="stylesheet">
<link href="${ctx }/resources/bootstrap-table-master/src/bootstrap-table.js">
<link href="${ctx }/resources/dialog/css/sweet-alert.css"
	rel="stylesheet">
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="${ctx }/resources/jquery/jquery.js"></script>
<script src="${ctx }/resources/jquery/ui/js/jquery-ui.min.js"></scr ipt>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="${ctx }/resources/bootstrap/js/bootstrap.min.js"></script>
<script src="${ctx }/resources/bootstrap/js/modal.js"></script>
<script src="${ctx }/resources/bootstrap-table-master/src/bootstrap-table.js"></script>
<script src="${ctx }/resources/dialog/js/sweet-alert.js"></script>
<script src="${ctx }/resources/autosuggest.js-master/autosuggest.js"></script>
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
<body style="position:relative">
 <form action="${ctx}/fileUpload" method="post" enctype="multipart/form-data">  
    选择文件:<input type="file" name="file">  
    <input type="submit" value="提交">   
</form>  
</body>
</html>
