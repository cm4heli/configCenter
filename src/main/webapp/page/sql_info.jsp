<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!--服务请求参数 -->
<div  id="sqlInfo_panl">
	<div>
	<div class="con_tit"><span class="con_tit_ico"></span>编写SQL脚本</div>
		<h3>
			 <span style="float: right;margin-top: -30px;">
<!-- 				<button type="button" class="btn btn-info btn-xs"
					aria-label="Left Align">
					<span class="glyphicon glyphicon glyphicon-pencil" aria-hidden="true"></span>  生成模板
				</button>
				<button id="useModel" type="button" class="btn btn-warning btn-xs"
					aria-label="Left Align">
					<span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span>  使用模板
				</button> -->
				<button id="formatSQL" type="button" class="btn btn-primary btn-xs"
					aria-label="Left Align">
					<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>格式化SQL
				</button>
				<button id="testSQL" type="button" class="btn btn-success btn-xs"
					aria-label="Left Align">
					<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>测试SQL
				</button>
			</span>
		</h3>

	</div>
	<div>
<!-- 	<button id="helpText" type="button" class="btn btn-warning btn-xs" style="margin-bottom:10px;margin-left:15px"
					aria-label="Left Align">
					<span class="glyphicon glyphicon-question-sign" aria-hidden="true"></span>帮助文档
				</button> -->
		<textarea class="form-control" rows="5" id="sqlInput" placeholder="用:+参数名称表示查询参数,比如:select * from table where id=:id "></textarea>
		<pre class=”prettyprintlinenumsLang-sql” style="display: none;">
		</pre>
	</div>
</div>