<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="modal fade" id="handbook" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width: 600px; height: 400px">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">×</button>
				<h4 class="modal-title" id="myModalLabel">模板列表(双击选择)</h4>
			</div>
			<div class="modal-body" id="model_madal_body" style="width:597px;height:400px;max-height: 380px;overflow: auto;padding: 11px;font-size: 15px;">
			
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
</div>


<div class="modal fade" id="savebook" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width: 600px; height: 400px">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">×</button>
				<h4 class="modal-title" id="myModalLabel">sql模板编写</h4>
			</div>
			<div class="modal-body" id="madal_body" style="width:597px;height:320px;max-height: 380px;overflow: auto;padding: 11px;font-size: 15px;">
				<span>模板名称：</span><span><input type="text" id="modelName" class="form-control"/></span><br />
				<span>模板内容：</span><span><textarea id="modelArea" class="form-control"/></textarea></span><br />
				<span>模板描述：</span><span><textarea id="modelDesc" class="form-control"/></textarea></span><br />
			</div>
			<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button id="modelsave" type="button" class="btn btn-primary" data-dismiss="modal">保存</button>
				</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
</div>