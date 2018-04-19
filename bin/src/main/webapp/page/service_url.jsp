<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="modal fade" id="service_url" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width: 700px; height: 200px">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">×</button>
				<h4 class="modal-title" id="myModalLabel">服务地址（拷贝仅支持IE浏览器，其他浏览器请手动复制）</h4>
			</div>
			<div class="modal-body" id="service_madal_body">
			
				<p style="margin-left: 25px;"><span>服务地址： </span><input  id="service_url_input" style="display: inline-block;width: 70%;margin-top: 20px;" type="text" class="form-control" />	</p>
				
			</div>
			<div class="modal-footer" style="text-align: center;margin:0">
					<button id="phone_confirm" type="button" class="btn btn-primary" onclick="copyServiceUrl()">拷贝地址</button>
					
									</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
</div>