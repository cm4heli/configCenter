<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!--选择表配置 -->

<div id="select_table">
	<div >
		<div class="con_tit"><span class="con_tit_ico"></span>查询主信息</div>

	</div>
	<div style="padding: 0px 0 0 30px;">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tabl" id="selectTable">
			<thead>
				<tr>
					<th>查询模式</th>
					<th>数据库</th>
					<th>表名</th>
				</tr>
				<tr>	
				<td><select id= "searchType"  class=" form-control">
				<option value="2">Inceptor SQL</option>
				<option value="1">JAVA API</option>
				<option value="3">全文检索</option>
				<option value="4">关系数据库</option>
				</select></td>
				<td><select id= "tableSpace" class=" form-control "></select></td>
				<td>
				<div style="width:100%;position: relative;" >
					<div id="suggestSelect" style="float:left;width: 100%;">
						<input id="suggest" type="text" class=" form-control "/>
					</div>
					<div id="clickButton">
						<div id="downShow" style="background-image: url(${ctx}/resources/image/xiabiao.jpg);">
						</div>
					</div>
					</div>
					</td>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
</div>
