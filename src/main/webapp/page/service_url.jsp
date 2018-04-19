<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script type="text/javascript">

  function closeURLmodal(){
	  $("#service_url").modal("hide");
  }
</script>
<div class="modal fade" id="service_url" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width: 700px; height: 200px">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">×</button>
				<h4 class="modal-title" id="myModalLabel" style="    font-size: 20px;">服务地址</h4>
			</div>
			<div class="modal-body" id="service_madal_body">
			
				<!-- <span style="position: absolute;">服务地址： </span> -->
				<textArea  id="service_url_input" style="display: inline-block;
    width: 90%;
    resize: none;
    margin-left: 5%;" type="text" class="form-control" rows="3"></textArea>
				
				
			</div>
			<div class="modal-footer" style="text-align: center;margin:0">
					<button id="phone_confirm" type="button" class="btn btn-primary" onclick="closeURLmodal()">关闭</button>
					
									</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
</div>