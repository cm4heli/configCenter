<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<script src="${ctx }/resources/js/publishService.js"></script>
<div class="modal fade" id="pub_modal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width: 600px; height: 400px">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">×</button>
				<h4 class="modal-title" id="myModalLabel">发布服务</h4>
			</div>
			<div class="modal-body" id="madal_body"
				style="width: 100%; height: 300px;">
				<!-- <i class="prev hide" title="上一页"> 《 </i>
       			 <i class="next" title="下一页">》</i> -->
				<div class='pub pub_left'>
				
					<div id="topicName"><h2 style="font-weight: bold;font-size:16px"></h2></div>
					<div id="service_topic"></div>

					<div id="fieldName"><h2 style="font-weight: bold;font-size:16px"></h2></div>
					<div id="service_field">
					</div>
				</div>
				
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button type="button" class="btn btn-primary" onclick="publishServiceConfirm()">确定</button>
			</div>
		</div>
	</div>
</div>