<%@ page language="java" pageEncoding="UTF-8"%>
<link type="text/css" rel="stylesheet" href="${ctx}/resources/JqueryPagination/css/mricode.pagination.css"></link>
<script type="text/javascript" src="${ctx}/resources/JqueryPagination/js/jquery.pagination-1.2.7.js"></script>
<script type="text/javascript">
	/**
	*初始分页
	**/
	function initPagination(url,params,total,pageSize,successFun,pageParent,pageIndex)
	{
		getPagenationSelector(pageParent).page({
			total:total,
			pageSize:pageSize,
			firstBtnText:"|<",
			prevBtnText:"<",
			nextBtnText:">",
			lastBtnText:">|",
			remote: {
		        url: url,
		        params: params,
		        pageIndexName: 'pageIndex',     //请求参数，当前页数，索引从0开始
		        pageSizeName: 'pageSize',       //请求参数，每页数量
		        totalName: 'count',             //指定返回数据的总数据量的字段名
		        pageIndex : pageIndex,
		        success: function (result, pageIndex) {
		        	if(successFun) successFun(result,pageIndex);
		        }
		    }
		});
	}
	/**
	**更新分页
	****/
	function updatePagination(url,params,total,pageSize,successFun,pageParent,pageIndex)
	{
		var $page=getPagenationSelector(pageParent);
		var pageData=$page.data("page");
		if(pageData)
		{
			getPagenationSelector(pageParent).page('destroy');
		}
		if(pageIndex == null || pageIndex == undefined){
			pageIndex = 0;
		}
		initPagination(url,params,total,pageSize,successFun,pageParent,pageIndex);
	}
	/**
	**获取分页选择器，根据父节点分页来区分不同的分页
	**/
	function getPagenationSelector(pageParent)
	{
		var $page=$("#q_listPagination");
		if(pageParent!=null && pageParent!="")
		{
			$page=$(pageParent+" > #q_listPagination");
		}
		return $page;
	}
</script>
<div id="q_listPagination" class="m-pagination"  style="float: right;padding-top: 10px;"  data-page-btn-count="10"
        data-show-first-last-btn="true" data-load-first-page="true" 
        data-show-info="true" data-info-format="第{start}-{end}条，共{total}条 "
        data-show-jump="false" data-jump-btn-text="跳转" 
        data-show-page-sizes="false" data-page-size-items="[10,20,30]"></div>