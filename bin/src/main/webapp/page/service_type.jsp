<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- 服务基本信息 -->

<div class="col-md-12 col-lg-12">
	<div>
		<div class="con_tit">
			<span class="con_tit_ico"></span>服务类型配置
		</div>
	</div>
	<div style="padding: 0 0 0 15px;">
		<div style="line-height: 29px;">
			<label class="col-md-2 col-lg-2 control-label">服务类型:</label>
			<div class="col-md-2 col-lg-2"> 
				<select style="" id="service_type" class="form-control">
				</select>
			</div>
		</div>
		<div id="author_type"
			style="line-height: 29px;">
			<label style="margin-left: 30px;" class="col-md-2 col-lg-2 control-label">授权类型:</label>
			<div>
				<div class="l mr20">
					<input id="auto" type="radio" name="authKey" value="1">
					<label style="margin: 0;font-weight: normal;" for="auto">授权访问</label>
				</div>
				<div class="l">
					<input id="public" type="radio" name="authKey" value="0" checked="checked">
					<label style="margin: 0;font-weight: normal;" for="public">公开访问</label>
				</div>
			</div>
		</div>
		<div id="showtotalDataDIV" style="line-height: 29px;">
			<div id="showTotalData">
				<label style="    margin-left: 30px;" class="col-md-2 col-lg-2 control-label">显示全部数据:</label>
				<div class="col-md-1 col-lg-1">
					<div class="l mr20"> 
						<input type="radio" id="showTotalData" name="showTotalDataKey" value="1">
						<label style="margin: 0;font-weight: normal;" for="showTotalData">是</label>
					</div> 
					<div class="l"> 
						<input type="radio" id="notShowTotalData" name="showTotalDataKey" value="0" checked="checked">
						<label style="margin: 0;font-weight: normal;" for="notShowTotalData">否</label>
					</div>
				</div>
			</div>
		</div>
		<div id="pagesizeDIV" style="line-height: 29px;" class="hide">
			<label style="margin-left: 30px;" class="col-md-2 col-lg-2 control-label">每页条数:</label>
			<input style="width: 60px; float: left;" type="text" id="pagesize"
				class="form-control" />
		</div>
		<div id="totalPageDIV" style="line-height: 29px;">
			<div id="totalPage">
				<label style="    margin-left: 30px;" class="col-md-2 col-lg-2 control-label">显示总页数:</label>
				<div class="col-md-1 col-lg-1">
					<div class="l mr20"> 
						<input type="radio" id="showTotalPage" name="totalPageKey" value="1">
						<label style="margin: 0;font-weight: normal;" for="showTotalPage">是</label>
					</div> 
					<div class="l"> 
						<input type="radio" id="notShowTotalPage" name="totalPageKey" value="0" checked="checked">
						<label style="margin: 0;font-weight: normal;" for="notShowTotalPage">否</label>
					</div>
				</div>
			</div>
		</div>
		
		
	</div>
</div>
