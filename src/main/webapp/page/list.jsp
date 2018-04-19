<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%> 
	
	<div class="clearfix box inline_any mb20" style="width:100%;">
		<div class="filter_sort clearfix">
			<ul>
				<li class="filter_sort_li">
					<a href="javascript:void(0);" class="filter_sort_link" id="metedata-relation-import" onclick="showconfig()">
						<i class="icon-plus mr5"></i>增加服务
					</a>
				</li>
			</ul>
		</div>
		
		<div class="filter_sort clearfix ml20">
			服务所属:
			<select id="service_owner_search" class=" ml5 mr20" onchange="list(0)" style="width:100px;border: 1px solid #d5d5d5;padding: 3px;margin-top: 7px;">				
			<option value="0">全部</option>
			<option value="1">我的服务</option>
			<option value="2">本组服务</option>
			</select>
		</div>
		
		<div class="filter_sort clearfix ml20" id="service_type_search">
			服务类型:
			<select id="qry_md_model" class=" ml5 mr20" onchange="list(0)" style="width:100px;border: 1px solid #d5d5d5;padding: 3px;margin-top: 7px;">				
			</select>
		</div>
		
		<div class="filter_sort clearfix ml20" id="publish_type_search">
			发布状态:
			<select id="qry_md_model" class=" ml5 mr20" onchange="list(0)" style="width:100px;border: 1px solid #d5d5d5;padding: 3px;margin-top: 7px;">
				<option value="-1">全部</option>
				<option value="1">已发布</option>
				<option value="0">未发布</option>					
			</select>
		</div>
		
		<div class="l pl20 clearfix" style="line-height:40px;">
			<div class="inline_any" style="margin-top: 9px;line-height: 0px;">				
				<input id="searchCondition" type="text" placeholder="请输入关键字..." class="search_sm_input ml5 mr20" style="width:160px;">
			</div>			
			<input type="button" id="searchList" value="查询" class="search_sm_submit btn-qry" style="cursor: pointer;line-height: 24px;">
			<!-- <input type="button"  value="重置" class="search_sm_submit btn-reset" style="cursor: pointer;line-height: 24px;"> -->
		</div>
<!-- 		<div>
			<a style="border-left: solid 3px;float: right;" href="javascript:void(0);" class="filter_sort_link" id="metedata-relation-import" onclick="window.location='page/space.jsp'">
						<i class="icon-trash mr5"></i>修改系统地址
					</a>
		</div> -->
		
	</div>
         
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tabl mb10"
			id="serviceList">
			<thead>
				<tr>
					<th  align="center" >服务名称</th>
					<th  align="center" >创建人</th>
					<th  align="center" >服务类型</th>
					<th  align="center" id="authId"  >授权类型</th>
					<th  align="center">服务描述</th>
					<th align="center" >地址</th>
					<th align="center" >操作</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
		<div class="col-md-12 md-main-table-page" id="pagerDIV" style="margin-bottom:30px;">
			<%@include file="admin-pagination.jsp"%>
		</div>
