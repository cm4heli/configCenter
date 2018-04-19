<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- 服务基本信息 -->
<div>
	<style>
		#bodyDiv {
    		margin: 15px 0 0;
		}
		.con_tit {
    		padding: 0;
		}
		.form-group {
    		margin-bottom: 5px;
    		line-height: 29px;
		}
		.col-md-2 {
    		width: inherit;
    		padding-top: 0 !important;
		}
		.col-md-1 {
    		width: inherit;
    		padding: 0;
		}
		.form-control {
		    height: 29px;
		    padding: 0px 12px;
	    }
	    .tabl th {
	    	height: 34px;
	    }
	    .tabl td {
		    height: 34px;
		    padding: 0 2px;
		}
		select.form-control{
			padding:0 0 0 12px;
		}
	    
	</style>
	<div >
		
		<div class="con_tit" style="padding:0;">
			<span class="con_tit_ico"></span>
			配置服务基本信息
			<span >
			<button type="button" class="btn btn-primary btn-xs" style="margin-bottom:10px;margin-left:15px"
					aria-label="Left Align" onclick="show(1)">
					&nbsp返回&nbsp
				</button>
			</span>
			<span style="float:right;"><button id="helpText" type="button" class="btn btn-warning btn-xs" style="margin-bottom:10px;margin-left:15px"
					aria-label="Left Align">
					<span class="glyphicon glyphicon-question-sign" aria-hidden="true"></span>帮助文档
				</button></span>
		</div>		
	</div>
	<div style="padding-bottom: 0px;">
		<form class="form-horizontal" style="margin-left: 30px;">
			<div class="form-group">
				<label class="col-md-2 col-lg-2 control-label">服务名称:</label>
				<div class="col-md-5 col-lg-5">
					<input type="text" class="form-control" value="${config.serviceName}" placeholder="请输入服务名称" id="routeName">
				</div>
				<div class="col-md-1 col-lg-1">
					<div style="color: red;">*</div>
				</div>
				<div class="col-md-4 col-lg-4">
					<div style="color: red;">用于描述服务的名称</div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-2 col-lg-2 control-label">服务地址:</label>
				<div class="col-md-5 col-lg-5 ">
					<input type="text" class="form-control" placeholder="以英文字母开头且只能是英文和数字的组合" id="serviceAddress">
				</div>
				<div class="col-md-1 col-lg-1">
					<div style="color: red;">*</div>
				</div>
				<div class="col-md-4 col-lg-4">
					<div style="color: red;">外部访问地址(例 myurl或者 url1/url2/myurl)</div>
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-md-2 col-lg-2 control-label">记录日志:</label>
				<div class="col-md-5 col-lg-5">
					<div class="l mr20">
						<input id="log" type="radio" name="isLog" value="1" checked="checked">
						<label style="margin: 0;font-weight: normal;" for="log">记录</label>
					</div> 
					<div class="l">
						<input id="nolog" type="radio" name="isLog" value="0">
						<label style="margin: 0;font-weight: normal;" for="nolog">不记录</label>
					</div>
				</div>
				<div class="col-md-1 col-lg-1">
					<div style="color: red;"></div>
				</div>
				<div class="col-md-4 col-lg-4">
					<div style="color: red;">配置该服务是否需要进行日志记录</div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-2 col-lg-2 control-label">允许删除:</label>
				<div class="col-md-5 col-lg-5">
					<div class="l mr20">
						<input id="allowDel" type="radio" name="allowDel" value="1" checked="checked">
						<label style="margin: 0;font-weight: normal;" for="allowDel">允许</label>
					</div> 
					<div class="l">
						<input id="notallowDel" type="radio" name="allowDel" value="0">
						<label style="margin: 0;font-weight: normal;" for="notallowDel">不允许</label>
					</div>
				</div>
				<div class="col-md-1 col-lg-1">
					<div style="color: red;"></div>
				</div>
				<div class="col-md-4 col-lg-4">
					<div style="color: red;">是否允许删除服务</div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-2 col-lg-2 control-label">服务描述:</label>
				<div class="col-md-5 col-lg-5">
					<textarea class="form-control" rows="3" placeholder="服务描述" id="routeDesc"></textarea>
				</div>
				<div class="col-md-1 col-lg-1">
					<div style="color: red;"></div>
				</div>
				<div class="col-md-4 col-lg-4">
					<div style="color: red;">描述服务功能及应用场景等信息</div>
				</div>
			</div>
		</form>
	</div>
</div>
 