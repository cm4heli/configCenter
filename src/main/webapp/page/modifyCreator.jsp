<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="modal fade" id="modifyCreatorModal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width: 600px; height: 500px">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">×</button>
				<h4 class="modal-title" id="myModalLabel">更改创建人</h4>
			</div>
			<div class="modal-body" id="madal_body" style="width: 600px; height: 520px">
				<div id="creatorList">
				<div >
				<input type="button" onclick="userList()" id="searchList_user" value="查询" class="search_sm_submit btn-qry" style="cursor: pointer;line-height: 24px;float:right;">
				<div class="inline_any" style="line-height: 0px;float:right;">				
				<input id="searchCondition_user" type="text" placeholder="请输入关键字..." class="search_sm_input ml5" style="width:160px;">
				</div>
			
			</div>
				<div style="min-height: 350px;">
					<table id="userTable" width="100%" border="0" cellspacing="0" cellpadding="0" class="tabl mb10">
					<thead>
						<tr>
							<th width="50px">选择</th>
							<th>用户名</th>
							<th>姓名</th>
							<th>标注</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
					</table>
				</div>
				<div>
				<nav>
			<ul class="pager" style="text-align: right;margin: 0;">
			    <li id="count_user">第1/1</li>
				<li id="prePage_user" pagenum="0" class="disabled"><a href="#">前一页</a></li>
				<li id="nextPage_user" pagenum="2" class="disabled"><a href="#">下一页</a></li>
			</ul>
		</nav>
				</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" onclick="changeUser()">确定</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
</div>