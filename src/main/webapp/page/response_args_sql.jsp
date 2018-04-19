<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!--服务响应参数 -->
<div id="response_panel_sql">
	<div>
	<div class="con_tit"><span class="con_tit_ico"></span>配置服务响应参数</div>
		<!-- <h3>
			<span style="float: right;margin-top: -30px;">
			<button type="button" class="btn btn-primary btn-xs"
				aria-label="Left Align" >
				<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>新增参数
			</button>
			<button type="button" class="btn btn-danger btn-xs"
				aria-label="Left Align" >
				<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>删除参数
			</button>
			</span>
		</h3> -->

	</div>
	<div style="padding: 0px 0 0 30px;">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tabl" id="responseTable_sql">
			<thead>
				<tr>
					<th width="50px"><input type="checkbox" /></th>
					<th width="200px">参数名称&nbsp;&nbsp;&nbsp; <span style="color:red;">*</span></th>
					<th>中文名称</th>
					<th width="70px">显示顺序</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
	<div style="    float: right;">
			<span style="margin-top: -30px;">
			<button type="button" class="btn btn-primary btn-xs"
				aria-label="Left Align" >
				<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>新增参数
			</button>
			<button type="button" class="btn btn-danger btn-xs"
				aria-label="Left Align" >
				<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>删除参数
			</button>
			</span>
		</div>
</div>