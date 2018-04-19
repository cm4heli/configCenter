<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="modal fade" id="imageModal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width: 600px; height: 600px">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">×</button>
				<h4 class="modal-title" id="myModalLabel">图片操作</h4>
			</div>
			<div class="modal-body" id="madal_body" style="width:600px;height:400px">
				<div>
				<input id="fileup" type="file" accept="image/*" />
				</div>
				<div id="imageArea"><img src="" id="preImg" style="    width: 200px;height: 200px;margin-left: 27%;"></div>
				<div><button type="button" style="margin-left: 40%;margin-top: 30px;" class="btn" onclick="fileUpload()">上传</button></div>
				<div class="modal-footer">
					<!-- <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal">确定</button> -->
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
</div>