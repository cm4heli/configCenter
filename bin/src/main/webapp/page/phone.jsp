<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="modal fade" id="phone_modal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width: 500px; height: 200px">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">×</button>
				<h4 class="modal-title" id="myModalLabel">管理员电话确认</h4>
			</div>
			<div class="modal-body" id="phone_madal_body">
			
				<p style="margin-left: 25px;"><span>电话： </span><input  id="phone_num" style="display: inline-block;width: 70%;margin-top: 20px;" type="text" class="form-control" placeholder="请输入管理员电话" id="change_phone_num"/>	</p>
				
			</div>
			<div class="modal-footer" style="text-align: center;margin:0">
					<button id="phone_confirm" type="button" class="btn btn-primary" onclick="phone_change()">确认删除</button>
					<button id="phone_cancel" type="button" class="btn btn-success" data-dismiss="modal">取消</button>
				</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
</div>