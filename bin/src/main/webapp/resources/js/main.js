var CONSTANS = {
	varType : {
		request : 0,
		response : 1
	}
}
var checkId;

var lastType = 2;
var lastTable = "请选择";

// 登录首先检测系统是否首次使用
function isFirstUse() {

	var isFirst = false;
	$.ajax({
		url : ctx + "/system/getData",
		data : "",
		dataType : "json",
		type : "post",
		async : false,
		success : function(data) {
			if (null == data.data) {
				isFirst = true;
			}
		},
		error : function(data) {
			alert("初始化失败，请刷新重试！", "", 0);
		}
	})

	return isFirst;
}

$(function() {

	// 设置ajax的缓存机制为false，不缓存数据，主要针对IE浏览器的缓存问题
	$.ajaxSetup({
		cache : false
	});

	// //////////////////////////////////////////////////

	// 列表chekbox事件
	if (isFirstUse()) {
		window.location = 'page/space.jsp';
		return;
	}

	// 初始化字典表里面的数据库字段类型
	getDBType();
	// ///////////////
	// 动态组装服务类型下拉列表
	assembleServiceType();
	// ////////

	assembleOwner();
	$('body').on('click', '.checkboxList', function() {
		operationID = "";
		var checked = $(this).is(':checked');
		var current = $(this);
		if (checked) {
			$(".checkboxList").each(function() {
				$(this).not(current).prop("checked", false);
			});
		}
	})

	$("#searchList").click(function() {
		list(0);
	})

	$('#searchCondition').bind('keypress', function(event) {
		if (event.keyCode == "13")
			list(0);
	})
	
	$("#searchCondition_user").bind('keypress', function(event) {
		if (event.keyCode == "13")
			userList(0);
	})

	// //////图片操作///////////////
	$("#fileup").change(function() {
		var objUrl = getObjectURL(this.files[0]);
		console.log("objUrl = " + objUrl);
		if (objUrl) {
			$("#preImg").attr("src", objUrl);
		}
	})
	//
	showApplyDetail();
	cacheTableInfo();
	$("#registerService").click(function() {
		try {
			var url = ctx + "/pageAction/registService";
			var data = getPageData();
			if (null == data || undefined == data) {
				return;
			}
			$.ajax({
				url : url,
				method : "post",
				data : {
					data : JSON.stringify(data)
				},
				success : function(data) {
					if (!data.hasError) {
						swaAlert("服务发布成功", "", 1);
						show(1);
						return;
					}
					swaAlert("服务发布失败", data.errorMsg, 0);
				},
				error : function(data) {
					swaAlert("服务发布失败", "后台程序出错", 0);
				}
			});
		} catch (e) {
			swaAlert("服务创建失败", e.message, 0);
		}
	});
	// 发布服务
	$("#publishServiceBtn").click(function() {
		try {
			var url = ctx + "/service/addService";
			var data = getPageData();
			if (null == data || undefined == data) {
				return;
			}
			$.ajax({
				url : url,
				method : "post",
				data : {
					data : JSON.stringify(data)
				},
				success : function(data) {
					if (!data.hasError) {
						swaAlert("服务保存成功", "", 1);
						show(1);
						return;
					}
					swaAlert("服务保存失败", data.errorMsg, 0);

				},
				error : function(data) {
					swaAlert("服务保存失败", "后台程序出错", 0);
				}
			});
		} catch (e) {
			swaAlert("服务保存失败", e.message, 0);
		}

	});
	$.TableContainer = {};

	function deleteRow(el) {
		var table = $.getTable(el);
		table.deleteRow();
	}
	;

	function getPageData() {
		var serviceName = $("#routeName").val();
		if (serviceName == "") {
			swaAlert("服务名称不能为空", "", 0);
			$("#routeName").focus();
			return;
		}
		var serviceAddr = $("#serviceAddress").val();
		if (serviceAddr == "") {
			swaAlert("服务地址不能为空", "", 0);
			$("#serviceAddress").focus();
			return;
		}
		if ($("#serviceAddress").val() == '') {
			swaAlert("请编辑服务地址", "", 0);
			$("#serviceAddress").focus();
			return;
		}

		var ok = /^[a-zA-Z][a-zA-Z0-9_:\.\/]*$/.test(serviceAddr);
		if (!ok) {
			swaAlert("", "服务地址必须以英文字母开头且只能是英文数字下划线组合", 0);
			$("#serviceAddress").select();
			$("#serviceAddress").focus();
			return;
		}

		var showToal = $('input:radio[name=totalPageKey]:checked').val();
		var authType = $('input:radio[name=authKey]:checked').val();
		var logFlag = $('input:radio[name=isLog]:checked').val();
		var serviceDesc = $("#routeDesc").val();
		var table = $.getTable("requestTable");
		var requestData = table.getData(CONSTANS.varType.request);
		table = $.getTable("responseTable");
		var responseData = table.getData(CONSTANS.varType.response);
		// inceptor-----
		if ($("#searchType").val() == "2" || $("#searchType").val() == "3") {
			table = $.getTable("requestTable_sql");
			requestData = table.getData(CONSTANS.varType.request);
			table = $.getTable("responseTable_sql");
			responseData = table.getData(CONSTANS.varType.response);
		}
		// 关系数据库
		else if ($("#searchType").val() == "4") {
			if ($("#service_type").val() == '03') {
				requestData = $.getTable("requestTable_oracle_03").getData(
						CONSTANS.varType.request);
			} else {
				table = $.getTable("requestTable_oracle");
				requestData = table.getData(CONSTANS.varType.request);
			}
			for (var i = 0; i < requestData.length; i++) {
				requestData[i].type = CONSTANS.varType.request;
			}
			table = $.getTable("responseTable_oracle");
			responseData = table.getData(CONSTANS.varType.response);
			for (var i = 0; i < responseData.length; i++) {
				responseData[i].type = CONSTANS.varType.response;
			}
			requestData.type = CONSTANS.varType.response;

		}
		// -----
		var sql = "";
		var tableData = {
			searchType : $("#searchType").val(),
			tableSpace : $("#tableSpace").val(),
			tableName : $("#suggest").attr('realVal')
		};
		if ($("#searchType").val() == "2" || $("#searchType").val() == "3"
				|| $("#searchType").val() == "4") {
			sql = $("#sqlInput").val();
			if (sql == "") {
				swaAlert("sql语句不能为空", "", 0);
				return;
			}
			if ($("#tableSpace").val() == "") {
				swaAlert("数据库不能为空", "", 0);
				$("#tableSpace").focus();
				return;
			}
			chekcedSql(sql);
		}

		if (responseData.length == 0) {
			swaAlert("响应参数不能为空", "", 0);
			return;
		}
		var authType = $('input:radio[name=authKey]:checked').val();
		var logFlag = $('input:radio[name=isLog]:checked').val();
		var serviceDesc = $("#routeDesc").val();
		var table = $.getTable("requestTable");
		var showTotalData = $('input:radio[name=showTotalDataKey]:checked').val();
		
		var searchHref = location.search;
		var requestURLData = GetURLRequest();
		var data = {
			serviceName : serviceName,
			serviceAddr : serviceAddr,
			use_type : $("#service_type").val(),
			pageSize : $("#pagesize").val(),
			showTotal : showToal,
			showTotalData : showTotalData,
			id : uid,
			reId : reId,
			authType : authType,
			logFlag : logFlag,
			serviceDesc : serviceDesc,
			requestData : JSON.stringify(requestData),
			responseData : JSON.stringify(responseData),
			tableData : JSON.stringify(tableData),
			sql : sql,
			creator : requestURLData.loginName
		};
		return data;
	}

	requestRow = 0;
	responseRow = 0;
	// 新增请求参数
	$("#request_panel").find(".btn-primary").click(function() {
		if ($("#suggest").val() == "" || $("#suggest").val() == "请选择") {
			swal("请先选择数据表", "", "error");
			$("#suggest").focus();
			return;
		}
		var table = $.getTable("requestTable");
		var html = getTr();
		table.addRow(html);
		requestRow++;
		initColumnData($("#requestTable tr:eq(" + requestRow + ")"));
	});
	// 删除请求参数
	$("#request_panel").find(".btn-danger").click(function() {
		requestRow--;
		deleteRow("requestTable");
	});
	$("#response_panel").find(".btn-primary").click(function() {
		var table = $.getTable("responseTable");
		var html = getResponseStr();
		table.addRow(html);
		responseRow++
		initColumnData($("#responseTable tr:eq(" + responseRow + ")"));
	});
	// 删除请求参数
	$("#response_panel").find(".btn-danger").click(function() {
		responseRow--;
		deleteRow("responseTable");
	});

	// *******************************list事件
	$('#cancelRegister').click(function() {
		unRegistService();
	})

	// ***************************测试联想事件
	$("#suggest").click(function() {
		$("#suggest").next(".as-menu").css("display", "block");
		if ($("#suggest").val() == "请选择" || $("#suggest").val() == "") {

			$("#suggest").val('');

			return;
		}
		return;
	})

	$("#downShow").click(function() {
		var isFocus = $("#suggest").is(":focus");
		var displayValue = $("#suggest").next(".as-menu").css("display");
		if (displayValue == "none") {
			$("#suggest").next(".as-menu").css("display", "block");
		} else {
			$("#suggest").next(".as-menu").css("display", "none");
		}
	})

	// ****************************
	$("#request_panel_sql").find(".btn-primary").click(function() {
		if ($("#tableSpace").val() == "") {
			swal("请先选择数据库", "", "error");
			$("#tableSpace").focus();
			return;
		}
		var table = $.getTable("requestTable_sql");
		var html = getTr_sql(1);
		table.addRow(html);
		responseRow++
		// initColumnData($("#requestTable_sql tr:eq(" + responseRow+ ")"));
	});
	$("#request_panel_sql").find(".btn-danger").click(function() {
		responseRow--;
		deleteRow("requestTable_sql");
	});
	$("#response_panel_sql").find(".btn-primary").click(function() {
		var table = $.getTable("responseTable_sql");
		var html = getTr_sql(2);
		table.addRow(html);
		responseRow++
		// initColumnData($("#requestTable_sql tr:eq(" + responseRow+ ")"));
	});
	$("#response_panel_sql").find(".btn-danger").click(function() {
		responseRow--;
		deleteRow("responseTable_sql");
	});

	// ---------------------------------------------
	$("#oracle_request_panel").find(".btn-primary").click(function() {
		if ($("#tableSpace").val() == "") {
			swal("请先选择数据库", "", "error");
			$("#tableSpace").focus();
			return;
		}
		var table = $.getTable("requestTable_oracle");
		var html = getOracleTr(1);
		table.addRow(html);
		//填入默认排序数值
		var max = 0;
		$("#requestTable_oracle").find("tr").each(function(){
			var order = $(this).find("td:eq(5) input").val();
			if(Number(order) > max ) max = Number(order);
		})
		requestRow = $("#requestTable_oracle").find("tr").length - 1;
		$("#requestTable_oracle").find("tr:eq(" + requestRow + ")").find("td:eq(5) input").val(Number(max) + 1);
		// initColumnData($("#requestTable_sql tr:eq(" + responseRow+ ")"));
	});
	$("#oracle_request_panel").find(".btn-danger").click(function() {
		responseRow--;
		deleteRow("requestTable_oracle");
	});
	$("#oracle_response_panel").find(".btn-primary").click(function() {
		var table = $.getTable("responseTable_oracle");
		var html = getOracleTr(2);
		table.addRow(html);
		//填入默认排序数值
		var max = 0;
		$("#responseTable_oracle").find("tr").each(function(){
			var order = $(this).find("td:eq(4) input").val();
			if(Number(order) > max ) max = Number(order);
		})
		responseRow = $("#responseTable_oracle").find("tr").length - 1;
		$("#responseTable_oracle").find("tr:eq(" + responseRow + ")").find("td:eq(4) input").val(Number(max) + 1);
		// initColumnData($("#requestTable_sql tr:eq(" + responseRow+ ")"));
	});

	$('#oracle_response_panel').find(".btn-danger").click(function() {
		responseRow--;
		deleteRow("responseTable_oracle");
	})
	// oracle 03类型删除行
	$("#oracle_request_03").find(".btn-danger").click(function() {
		requestRow--;
		deleteRow("requestTable_oracle");
	});

	// oracle 03类型增加行
	$("#oracle_request_03").find(".btn-primary").click(function() {
		if ($("#tableSpace").val() == "") {
			swal("请先选择数据库", "", "error");
			$("#tableSpace").focus();
			return;
		}
		var table = $.getTable("requestTable_oracle_03");
		var html = get03OracleTr();
		table.addRow(html);
		//填入默认排序数值
		var max = 0;
		$("#requestTable_oracle_03").find("tr").each(function(){
			var order = $(this).find("td:eq(6) input").val();
			if(Number(order) > max ) max = Number(order);
		})
		requestRow = $("#requestTable_oracle_03").find("tr").length - 1;
		$("#requestTable_oracle_03").find("tr:eq(" + requestRow + ")").find("td:eq(6) input").val(Number(max) + 1);
		// initColumnData($("#requestTable_sql tr:eq(" + responseRow+ ")"));
	});

	$("#oracle_request_03").find(".btn-danger").click(function() {
		responseRow--;
		deleteRow("requestTable_oracle_03");
	});

	// -----------------------------------

	$("#helpText")
			.click(
					function() {
						$("#help_text").find(".modal-body").html('');
						var path = ctx
								+ "/resources/system_help/cloudservice.htm";
						var html = '<iframe width="100%" frameborder="0" height="350px" src="'
								+ path + '"></iframe>'
						$("#help_text").find(".modal-body").append(html);
						$("#help_text").modal("show");

					})

	// sql参考文档编辑
	$("#sqlInfo_panl").find(".btn-info").click(function() {
		$("#modelArea").val('');
		$("#modelName").val('');
		$("#modelDesc").val('');
		$("#modelArea").val($("#sqlInput").val());
		$("#savebook").modal('show');
		$("#modelName").focus();

	})

	// sql参考文档使用
	$("#useModel")
			.click(
					function() {
						$
								.ajax({
									url : ctx + "/pageAction/getModel",
									type : "get",
									data : "",
									dataType : "json",
									success : function(data) {

										if (data.hasError) {
											swaAlert("模板初始化失败", "", 0);
											return;
										}
										udata = [];
										if ($("#searchType").val() == '2') {

											for (var i = 0; i < data.data.length; i++) {
												if (data.data[i].TYPE == '2') {
													udata.push(data.data[i]);
												}
											}
										} else if ($("#searchType").val() == '3') {
											for (var i = 0; i < data.data.length; i++) {
												if (data.data[i].TYPE == '3') {
													udata.push(data.data[i]);
												}
											}
										} else if ($("#searchType").val() == '4') {
											for (var i = 0; i < data.data.length; i++) {
												if (data.data[i].TYPE == '4') {
													udata.push(data.data[i]);
												}
											}
										}
										var html = '';
										for (var j = 0; j < udata.length; j++) {
											html += '<div id="'
													+ udata[j].ID
													+ '"class="handText" style="margin-top:10px" ondblclick="showModel(this)" onclick="modelClick(this)">';
											html += '<span style="margin-right: 10px;" class="glyphicon glyphicon-hand-right"></span>'
											html += udata[j].MODEL_NAME + ' : '
													+ udata[j].MODEL_DESC;
											html += '</div>';
											html += '<div id="u'
													+ udata[j].ID
													+ '" class="datailInfo hide handText colorbox" style="border: 1px solid #ccc;border-radius: 4px;margin-bottom: 15px;" ondblclick="showModel(this)">';
											html += '<div class="arrow-range"> <b class="arrow-outer"></b>  <b class="arrow-inner"></b>  </div>';
											html += '<p style="margin-left:15px;margin-top:6px">模板名称 :  '
													+ udata[j].MODEL_NAME
													+ '; ';
											html += '<p style="margin-left:15px;margin-top:6px">模板描述:  '
													+ udata[j].MODEL_DESC
													+ '; ';
											html += '<p style="margin-left:15px;margin-top:6px">模板内容 :  '
													+ udata[j].MODEL_INFO
													+ '; </div></div><hr style="border-color:rgba(31, 20, 20, 0.3);"/>';
										}
										$("#model_madal_body").html(html);

										$("#handbook").modal('show');
									}
								})
					})

	// 保存模板
	$("#modelsave").click(function() {
		var data = {
			name : $("#modelName").val(),
			desc : $("#modelDesc").val(),
			info : $("#modelArea").val(),
			type : $("#searchType").val(),
			bl : ""
		}
		$.ajax({
			url : ctx + "/pageAction/saveModel",
			type : "post",
			data : {
				config : JSON.stringify(data)
			},
			dataType : "json",
			success : function(data) {
				if (data.hasError) {
					swaAlert("保存失败", data.errorMsg, 0);
					return;
				}
				swaAlert("保存模板成功", "", 1);
				$("#savebook").modal('hide');

			}
		})

	})

	// 格式化SQL
	$("#formatSQL").click(function() {
		var sql = $("#sqlInfo_panl").find("textarea").val();
		if (sql == "") {
			swaAlert("sql为空", "", 0);
			return;
		}
		$("#sqlInfo_panl").find("pre").html(sql);
		prettyPrint();
		$("#sqlInfo_panl").find("pre").show();
	});

	// 测试SQL
	$("#testSQL").click(function() {

		var nameSpace = $("#tableSpace").val();
		var type = $("#searchType").val();
		if (nameSpace == "") {
			swaAlert("数据库不能为空", "", 0);
			$("#tableSpace").focus();
			return;
		}
		var sql = $("#sqlInput").val();
		if (sql == "") {
			swaAlert("SQL语句不能为空", "", 0);
			$("#sqlInput").focus();
			return;
		}
		try {
			chekcedSql(sql);
		} catch (e) {
			swaAlert(e.message, 0);
			return;
		}
		var rowData;
		if (type == 4) {

			var table = $.getTable("requestTable_oracle");
			if ($("#service_type").val() == '03') {
				table = $.getTable("requestTable_oracle_03");
			}
			var requestData = table.getData(0);
			table = $.getTable("responseTable_oracle");
			var responseData = table.getData(1);
			rowData = {
				requestData : requestData,
				responseData : responseData
			}
		} else {
			var table = $.getTable("requestTable_sql");
			var requestData = table.getData(0);
			var responseData = $.getTable("responseTable_sql").getData(1);
			rowData = {
				requestData : requestData,
				responseData : responseData
			}
		}

		$('#loadingDIV').removeClass('hide');
		var url = ctx + "/pageAction/testSQL";
		$.ajax({
			url : url,
			dataType : "json",
			method : "post",
			data : {
				sql : sql,
				rowData : JSON.stringify(rowData),
				database : nameSpace,
				type : type
			},
			success : function(data) {
				$('#loadingDIV').addClass('hide');
				if (data.hasError) {
					swaAlert("sql无效", data.errorMsg, 0);
					return;
				}
				swaAlert("测试成功", "", 1);
			},
			error : function(data) {
				$('#loadingDIV').addClass('hide');
				swaAlert("测试失败", "", 0);
			}
		});
	});
	// ****************************

	function Table(el) {
		var table = $("#" + el);
		this.deleteRow = function() {
			table.find("tbody input[type='checkbox']").each(function() {
				var checked = $(this).is(':checked');
				if (checked) {
					$(this).parent().parent().remove();
					return;
				}
				;
			});
		};
		this.getData = function(type) {
			var data = [];
			table.find("tbody tr").each(function() {
				if ($("#searchType").val() == "1") {
					if (type == CONSTANS.varType.request) {
						data.push(getRequestTrData($(this)));
					} else {
						data.push(getResponseTrData($(this)));
					}
					return data;
				} else if ($("#searchType").val() == "4") {
					if (type == CONSTANS.varType.request) {
						if ($("#service_type").val() == '03') {
							data.push(get03OracleTrData($(this)));
						} else {
							data.push(getOracleTrData($(this), 0));
						}
					} else {
						data.push(getOracleTrData($(this), 1));
					}
				} else {
					data.push(getSqlTrData($(this), type));
					return data;
				}
			})
			var list = [];
			for(var i=0;i<data.length;i++){
				var order = data[i].order;
				if(order != null && order !=undefined){
					if(list.indexOf(order) != -1){
						throw new Error("排序序号相同，请修改");
					}
					list.push(order);
				}
			}
			return data;
		};
		this.addRow = function(tr) {
			table.find("tbody input[type='checkbox']").each(function() {
				$(this).prop("checked", false);
			});
			table.find("tbody").append(tr);
			if (el == "requestTable") {
				table.find('tbody').find(':last-child').find('td:eq(1) select')
						.change(
								function() {
									var ovar = $(this).parent().parent().find(
											'td:eq(3) input').val();
									if (ovar != "") {
										return;
									}
									var val = $(this).val();
									var enterVal = "";
									if (val.indexOf(":") == -1) {
										enterVal = val;
									} else {
										var valSplit = val.split(":");
										enterVal = valSplit[1];
									}
									$(this).parent().parent().find(
											'td:eq(3) input').val(enterVal);
								})
			} else {
				table.find('tbody').find(':last-child').find('td:eq(2) select')
						.change(
								function() {
									var inVal = $(this).parent().parent().find(
											'td:eq(1) input').val();
									if (inVal != "") {
										return;
									}
									var val = $(this).val();
									if (val == "") {
										$(this).parent().parent().find(
												'td:eq(1) input').val("");
									}
									var enterVal = "";
									if (val.indexOf(":") == -1) {
										enterVal = val;
									} else {
										var valSplit = val.split(":");
										enterVal = valSplit[1];
									}
									$(this).parent().parent().find(
											'td:eq(1) input').val(enterVal);
								})
			}

			checkBoxBind();
		};
		function checkBoxBind() {
			table.find("thead input[type='checkbox']").click(function() {
				var checked = $(this).is(':checked');
				if (checked) {
					table.find("tbody input[type='checkbox']").each(function() {
						$(this).prop("checked", true);
					});
				} else {
					table.find("tbody input[type='checkbox']").each(function() {
						$(this).prop("checked", false);
					});
				}

			})

			table.find("tbody input[type='checkbox']").click(function() {
				var checked = $(this).is(':checked');
				var current = $(this);
				if (checked) {
					table.find("tbody input[type='checkbox']").each(function() {
						$(this).not(current).prop("checked", false);
					});
				}
			});
		}
		;
		checkBoxBind();
	}
	;
	$.TableContainer["requestTable"] = new Table("requestTable");
	$.TableContainer["responseTable"] = new Table("responseTable");
	$.TableContainer["requestTable_sql"] = new Table("requestTable_sql");
	$.TableContainer["responseTable_sql"] = new Table("responseTable_sql");
	$.TableContainer["requestTable_oracle"] = new Table("requestTable_oracle");
	$.TableContainer["responseTable_oracle"] = new Table("responseTable_oracle");
	$.TableContainer["requestTable_oracle_03"] = new Table(
			"requestTable_oracle_03");
	$.getTable = function(el) {
		return $.TableContainer[el];
	};

	// ---------------------------列表服务操作JS-------------------------//

	$('#searchType').on(
			"change",
			function() {
				requestRow = 0;
				responseRow = 0;
				changeType($("#service_type").val());
				var clickValue = 0;
				if ($('#requestTable tbody tr').length > 0
						|| $('#responseTable tbody tr').length > 0
						|| $('#requestTable_sql tbody tr').length > 0
						|| $('#responseTable_sql tbody tr').length > 0
						|| $('#responseTable_oracle tbody tr').length > 0
						|| $('#requestTable_oracle tbody tr').length > 0
						|| $('#requestTable_oracle_03 tbody tr').length > 0) {
					swal({
						title : "是否确认改变",
						text : "修改查询方式会删除请求、返回参数",
						type : "warning",
						showCancelButton : true,
						confirmButtonColor : "#DD6B55",
						cancelButtonText : "取消",
						confirmButtonText : "确认",
						closeOnConfirm : true,

					}, function(isConfirm) {
						if (isConfirm) {
							$("#requestTable tr:not(:first)").remove();
							$("#responseTable tr:not(:first)").remove();
							$("#requestTable_sql tr:not(:first)").remove();
							$("#responseTable_sql tr:not(:first)").remove();
							$("#requestTable_oracle tr:not(:first)").remove();
							$("#responseTable_oracle tr:not(:first)").remove();
							$("#requestTable_oracle_03 tr:not(:first)")
									.remove();
							changeContorl();
							lastType = $("#searchType").val();
						} else {
							$("#searchType").val(lastType);
						}
					})
				} else {
					changeContorl();
				}

			});

	$('#prePage,#nextPage').on("click", function() {
		if ($(this).hasClass("disabled"))
			return;
		if ($(this).attr("pageNum") != "0") {
			list($(this).attr("pageNum"));
		}
	});
	
	//注册admin管理员下面更改创建人的上下页事件
	$('#prePage_user,#nextPage_user').on("click", function() {
		if ($(this).hasClass("disabled"))
			return;
		if ($(this).attr("pageNum") != "0") {
			userList($(this).attr("pageNum"));
		}
	});
	
	
	//表单选择框-不重复选择-事件
	$('body').on('click', '.user_list', function() {
		var checked = $(this).is(':checked');
		var current = $(this);
		if (checked) {
			operationID = $(this).val();
			$(".user_list").each(function() {
				$(this).not(current).prop("checked", false);
			});
		}
	})
	
	

	$('#searchButton').on("click", function() {
		list(0);
	});

	// ---------------------------------------列表服务操作JS------------------//

	// --------------------------------服务类型操作JS----------

	// 控制显示是否授权
	$('#service_type').change(function() {
		var type = $('#service_type').val();
		changeType(type);
//		var searchType = $("#searchType").val();
//
//		$('#author_type').addClass('hide');
//		$('#pagesizeDIV').addClass('hide');
//		if (type != "02" && type != '03') {
//			$('#author_type').removeClass('hide');
//		}
//		if (type == "03" && searchType == '4') {
//			$('#pagesizeDIV').removeClass('hide');
//		}
//		showParam();
	})
	
	
	
	
	//////////////单选框是否显示总页数改变事件
	$('input:radio[name=showTotalDataKey]').change(function(){
		changeType($("#service_type").val());
	})

});

// 改变搜索方式操作JS
function changeContorl() {
	$("#suggest").val("请选择");
	$("#suggest").attr("realVal", "");
	if ($("#searchType").val() == '1') {
		$('#sqldiv').hide();
		$("#tableSpace").val('');
		$("#tableSpace").attr('disabled', 'disabled');
		$("#suggest").removeAttr('disabled');
		$("#downShow").show();
	} else {
		assembleDatabase();
		if (searchType == '2' || searchType == '3') {
			$('#sqlInput').val('');
			if (searchType == '2') {
				$('#sqlInput').attr('placeholder',
						'用:+参数名称表示查询参数,比如:select * from table where id=:id ');
			} else {
				$('#sqlInput')
						.attr('placeholder',
								'用:+参数名称表示查询参数,比如:select * from table where contains(id, "prefix \':id\'") ');
			}

			$('#tableSpace').val('');
			$("#tableSpace").removeAttr('disabled');
			$("#suggest").attr('disabled', 'disabled');
			$("#downShow").hide();
			$('#sqldiv').show();
		} else {
			$('#tableSpace').val('');
			$("#tableSpace").removeAttr('disabled');
			$("#suggest").attr('disabled', 'disabled');
			$("#downShow").hide();
			$('#sqldiv').show();
		}
	}
	showParam();

}

/**
 * 根据
 */
function assembleDatabase() {
	var callback = function() {
		for (var i = 0; i < databaseInfo.length; i++) {
			var type = databaseInfo[i].dbtype;
			if (type == "inceptor") {
				type = 2;
			} else if (type == "hbase") {
				type = 1;
			} else if (type == "elasticSearch") {
				type = 3;
			} else
				type = 4;

			if (type == searchType) {
				var opt = databaseInfo[i].INFO_CODE;
				var map;
				if ("" != databaseInfo[i].INFO_NAME
						&& null != databaseInfo[i].INFO_NAME) {
					opt += "--" + databaseInfo[i].INFO_NAME;
				}
				map = {
					key : databaseInfo[i].INFO_CODE,
					value : opt
				}
				database.push(map);
			}
		}

		assembleTable($("#tableSpace"), database);
	}
	var searchType = $("#searchType").val();
	var database = [];
	var databaseUrl = ctx + "/service/getDataSource";
	if (null == databaseInfo) {
		$.ajax({
			url : databaseUrl,
			type : "post",
			dataType : "json",
			data : "",
			success : function(data) {
				if (data.hasError || data.data == null) {
					swaAlert("页面初始化失败(初始化)，请刷新后再试", "", 0);
					return;
				}
				databaseInfo = data.data;
				callback();
			}
		})
		return;

	} else {
		callback();
	}

}
/**
 * 获取单行表格信息
 * 
 * @param obj
 * @param type
 * @returns {___anonymous9007_9007}
 */
function getRequestTrData(obj) {
	var paramId = "";
	if (null != obj.attr("value") && obj.attr("value") != "undefined") {
		paramId = obj.attr("value");
	}
	var paramName = obj.find("td:eq(3) input").val();
	if (paramName == "") {
		obj.find("td:eq(3) input").focus();
		throw new Error("参数名称不能为空");
	}
	var ok = /^[a-zA-Z][a-zA-Z0-9_\s]*$/.test(paramName);
	if (!ok) {
		obj.find("td:eq(3) input").select();
		obj.find("td:eq(3) input").focus();
		throw new Error("参数名称必须以英文字母开头且只能是英文数字下划线组合");
	}
	paramName = paramName.replace(/[ ]/g, "");
	var columnName = obj.find("td:eq(1) select").val();
	if (columnName == "") {
		obj.find("td:eq(1) select").focus();
		throw new Error("数据库字段不能为空");
	}
	var zhName = obj.find("td:eq(4) input").val();
	var defaultValue = obj.find("td:eq(5) input").val();

	var expresstion = obj.find("td:eq(2) select").val();
	var o = Object();
	o.paramName = paramName;
	o.columnName = columnName;
	o.zhName = zhName;
	o.defaultValue = defaultValue;
	o.expresstion = expresstion;
	o.paramId = paramId;
	return o;

};

function getResponseTrData(obj) {
	var paramId = "";
	if (null != obj.attr("value") && obj.attr("value") != "undefined") {
		paramId = obj.attr("value");
	}
	var paramName = obj.find("td:eq(1) input").val();
	if (paramName == "") {
		obj.find("td:eq(1) input").focus();
	}
	// var ok = /^[a-zA-Z][a-zA-Z0-9_\s]*$/.test(paramName);
	// if (!ok) {
	// obj.find("td:eq(1) input").select();
	// obj.find("td:eq(1) input").focus();
	// throw new Error("参数名称必须以英文字母开头且只能是英文数字下划线组合");
	// }
	paramName = paramName.replace(/[ ]/g, "");
	var columnName = obj.find("td:eq(2) select").val();
	if (columnName == "") {
		obj.find("td:eq(2) select").focus();
		throw new Error("数据库字段不能为空");
	}

	var zhName = obj.find("td:eq(3) input").val();
	var order = obj.find("td:eq(4) input").val();

	var order_reg = '/^[0-9]+$/';
	if (!order_reg.test(order)) {
		obj.find("td:eq(4) input").focus();
		throw new Error("请输入数字编号");
	}
	var o = Object();
	o.paramName = paramName;
	o.columnName = columnName;
	o.zhName = zhName;
	o.order = order;
	o.paramId = paramId;
	return o;
}

function getOracleTrData(obj, type) {
	var parameterDesc = obj.find("td:eq(2) input").val();
	if (parameterDesc == "") {
		obj.find("td:eq(2) input").focus();
		throw new Error("中文名称不能为空");
	}
	var name = obj.find("td:eq(1) input").val();
	if (name == "") {
		obj.find("td:eq(1) input").focus();
	}
	var dataType = '';
	var order;
	if (type == 0) {
		dataType = obj.find("td:eq(4) select").val();
		order = obj.find("td:eq(5) input").val();
		var order_reg = /^[0-9]+$/;
		if (order == null || !order_reg.test(order) ) {
			obj.find("td:eq(5) input").focus();
			throw new Error("请输入数字编号");
		}
	} else {
		dataType = obj.find("td:eq(3) select").val();

		order = obj.find("td:eq(4) input").val();
		var order_reg = /^[0-9]+$/;
		if (!order_reg.test(order)) {
			obj.find("td:eq(4) input").focus();
			throw new Error("请输入数字编号");
		}

	}
	var defaultValue = "";
	if (type == 0) {
		defaultValue = obj.find("td:eq(3) input").val();
	}
	var o = Object();
	o.paramName = name;
	o.zhName = parameterDesc;
	o.defaultValue = defaultValue;
	o.dataType = dataType;
	o.order = order;
	return o;
}

function get03OracleTrData(obj) {
	var parameterDesc = obj.find("td:eq(2) input").val();
	if (parameterDesc == "") {
		obj.find("td:eq(2) input").focus();
		throw new Error("中文名称不能为空");
	}
	var name = obj.find("td:eq(1) input").val();
	if (name == "") {
		obj.find("td:eq(1) input").focus();
	}

	var sql = obj.find("td:eq(4) input").val();
	
	if (/drop\s/.test(sql)) {
		obj.find("td:eq(4) input").focus();
		throw new Error("SQL语句中不能包含【drop】关键字");
	}
	if (/delete\s/.test(sql)) {
		obj.find("td:eq(4) input").focus();
		throw new Error("SQL语句中不能包含【delete】关键字");
	}
	if (/update\s/.test(sql)) {
		obj.find("td:eq(4) input").focus();
		throw new Error("SQL语句中不能包含【update】关键字");
	}
	if (/insert\s/.test(sql)) {
		obj.find("td:eq(4) input").focus();
		throw new Error("SQL语句中不能包含【insert】关键字");
	}
	var dataType = obj.find("td:eq(5) select").val();
	var order = obj.find("td:eq(6) input").val();
	var order_reg = /^[0-9]+$/;
	if (order == null || !order_reg.test(order) ) {
		obj.find("td:eq(4) input").focus();
		throw new Error("请输入数字编号");
	}
	var o = Object();
	o.paramName = name;
	o.zhName = parameterDesc;
	o.defaultValue = obj.find("td:eq(3) input").val();
	o.codeSQL = obj.find("td:eq(4) input").val();
	o.dataType = dataType;
	o.order = order;
	return o;
}

function getSqlTrData(obj, type) {
	var paramId = "";
	if (null != obj.attr("value") && obj.attr("value") != "undefined") {
		paramId = obj.attr("value");
	}
	var paramName = obj.find("td:eq(1) input").val();
	// var ok = /^[a-zA-Z][a-zA-Z0-9_\s]*$/.test(paramName);
	// if (!ok) {
	// obj.find("td:eq(1) input").select();
	// obj.find("td:eq(1) input").focus();
	// throw new Error("参数名称必须以英文字母开头且只能是英文数字下划线组合");
	// }
	paramName = paramName.replace(/[ ]/g, "");
	var zhName = obj.find("td:eq(2) input").val();
	var defaultValue = '';
	var order;
	if (type == 0) {
		defaultValue = obj.find("td:eq(3) input").val();
		order = obj.find("td:eq(4) input").val();
		var order_reg = /^[0-9]+$/;
		if (null == order || !order_reg.test(order)) {
			obj.find("td:eq(4) input").focus();
			throw new Error("请输入数字编号");
		}
	} else {
		order = obj.find("td:eq(3) input").val();
		var order_reg = /^[0-9]+$/;
		if (!order_reg.test(order)) {
			obj.find("td:eq(3) input").focus();
			throw new Error("请输入数字编号");
		}
	}
	var o = Object();
	o.paramName = paramName;
	o.zhName = zhName;
	o.defaultValue = defaultValue;
	o.paramId = paramId;
	o.order = order;
	return o;
}
// 获取请求参数html
function getTr(id) {
	var html = '<tr value="' + id
			+ '"><td><input type="checkbox" checked="checked"/></td>';
	html += '<td><select class="table_column form-control">';
	html += '<td><select class="form-control">';
	html += '<option value="大于">大于</option>';
	html += '<option value="小于">小于</option>';
	html += '<option value="等于">等于</option>';
	html += '<option value="大于等于">大于等于</option>';
	html += '<option value="小于等于">小于等于</option>';
	html += '<option value="不等于">不等于</option>';
	html += '<option value="like">like</option></td>';
	html += '<td><input type="text" placeholder="只能以英文字母开头且只能是英文与数字的组合"';
	html += ' class="form-control"/></td>';
	html += '<td><input type="text" placeholder="中文名称或描述" ';
	html += 'class="form-control"/></td>';
	html += '<td><input type="text" placeholder="默认值" ';
	html += 'class="form-control"/></td>';
	html += '</tr>';
	html = $(html);
	return html;
};
// 获取响应参数html
function getResponseStr(id) {
	var html = '<tr value="' + id
			+ '"><td><input type="checkbox" checked="checked"/></td>';
	html += '<td><input type="text" placeholder="只能以英文字母开头且只能是英文与数字的组合"';
	html += ' class="form-control"/></td>';
	html += '<td><select class="table_column form-control">';
	html += '<td><input type="text" placeholder="中文名称或描述" ';
	html += 'class="form-control"/></td>';
	html += '<td><input type="text" class="order form-control" placeholder="显示顺序" ';
	html += 'class="form-control"/></td>';
	html += '</tr>';
	html = $(html);
	return html;
}
function getTr_sql(type, id) {
	var html = '<tr value="' + id
			+ '"><td><input type="checkbox" checked="checked"/></td>';
	html += '<td><input type="text" placeholder="只能以英文字母开头且只能是英文与数字的组合"';
	html += ' class="form-control"/></td>';
	html += '<td><input type="text" placeholder="中文名称或描述" ';
	html += 'class="form-control"/></td>';
	if (type == 1) {
		html += '<td><input type="text" placeholder="默认值" ';
	}
	html += 'class="form-control"/></td>';
//	if (type == 2) {
		html += '<td><input type="text" class="order form-control" placeholder="显示顺序" ';
//	}
	html += '</tr>';
	html = $(html);
	return html;
};

function getOracleTr(type) {
	var html = '<tr><td><input type="checkbox" checked="checked"/></td>';
	html += '<td><input type="text" placeholder="只能以英文字母开头且只能是英文与数字的组合"';
	html += ' class="form-control"/></td>';
	html += '<td><input type="text" placeholder="中文名称或描述" ';
	html += 'class="form-control"/></td>';
	if (type == 1) {
		html += '<td><input type="text" placeholder="默认值" ';
	}
	html += 'class="form-control"/></td>';
	html += getDBTypeTr();
//	if (type == 2) {
		html += '<td><input type="text" class="order form-control" placeholder="显示顺序" />';
//	}
	html += '</tr>';
	html = $(html);
	return html;
};

function get03OracleTr() {
	var html = '<tr><td><input type="checkbox" checked="checked"/></td>';
	html += '<td><input type="text" placeholder="只能以英文字母开头且只能是英文与数字的组合"';
	html += ' class="form-control"/></td>';
	html += '<td><input type="text" placeholder="中文名称或描述" ';
	html += 'class="form-control"/></td>';
	html += '<td><input type="text" placeholder="默认值" ';
	html += 'class="form-control"/></td>';
	html += '<td><input type="text" placeholder="输入sql语句" class="form-control"/></td>';
	html += getDBTypeTr();
	html += '<td><input type="text" class="order form-control" placeholder="显示顺序" /></td>';
	html += '</tr>';
	html = $(html);
	return html;
};

// 检查SQL关键字
function chekcedSql(sql) {
	sql = sql.toLowerCase();
	if (/drop\s/.test(sql)) {
		throw new Error("SQL语句中不能包含【drop】关键字");
	}
	if (/delete\s/.test(sql)) {
		throw new Error("SQL语句中不能包含【delete】关键字");
	}
	if (/update\s/.test(sql)) {
		throw new Error("SQL语句中不能包含【update】关键字");
	}
	if (/insert\s/.test(sql)) {
		throw new Error("SQL语句中不能包含【insert】关键字");
	}
};

// 获取get参数
function GetRequest() {
	var url = location.search; // 获取url中"?"符后的字串
	var theRequest = new Object();
	if (url.indexOf("?") != -1) {
		var str = url.substr(1);
		strs = str.split("&");
		for (var i = 0; i < strs.length; i++) {
			theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
		}
	}
	return theRequest;
}

// 是否显示applyDetail.jsp
function showApplyDetail() {
	var Request = new Object();
	Request = GetRequest();
	var authId = Request["authId"];
	var resourceId = Request["resourceId"];
	var applyUserId = Request["applyUserId"];
	var authCode = Request["authCode"];
	if (authId != undefined) {
		$("#applyDetailPanel").show();
		// 隐藏删除行 增加行按钮
		$("#response_panel").find(".btn-primary").hide();
		$("#response_panel").find(".btn-danger").hide();

		$
				.ajax({
					url : CTX
							+ '/app/http/sps/authCodeAssosiationHandler/getApplyDetail',
					dataType : "json",
					method : "post",
					data : {
						resourceId : resourceId,
						applyUserId : applyUserId,
						authId : authId
					},
					success : function(data) {
						var user = data.applyUser;
						var catalogueInfo = data.catalogueInfo;
						var checkedList = data.checkedList;
						$("#applyDetailPanel").find("span:eq(1)").html(
								catalogueInfo.resourceName);
						$("#applyDetailPanel").find("span:eq(3)")
								.html(authCode);
						for (var i = 0; i < checkedList.length; i++) {
							var table = $.getTable("responseTable");
							var html = newRow(checkedList[i].field.pyName,
									checkedList[i].field.fieldName);
							table.addRow(html);
						}
					}
				});
	}

}

function newRow(pyName, fieldName) {
	var html = '<tr><td><input type="checkbox" /></td>';
	html += '<td><input   value="' + pyName
			+ '" type="text" placeholder="只能以英文字母开头且只能是英文与数字的组合"';
	html += ' class="form-control"/></td>';
	html += '<td><input   value="' + fieldName
			+ '" type="text" placeholder="中文名称或描述" ';
	html += 'class="form-control"/></td>';
	html += '<td><input type="text" placeholder="默认值" ';
	html += 'class="form-control"/></td>';
	html += '<td><select class="form-control">';
	html += '<option value="string">字符型</option>';
	html += '<option value="int">整型</option>';
	html += '<option value="float">浮点型</option>';
	html += '<option value="date">日期类型</option></td>';
	html += '</tr>';
	html = $(html);
	return html;
};

var tableInfo, namespaceInfo, databaseInfo;
function cacheTableInfo(callback) {
	var url = ctx + "/pageAction/initpage";
	$.ajax({
		url : url,
		dataType : "json",
		data : "",
		method : "get",
		success : function(data) {
			if (null == data || "" == data) {
				swaAlert("页面初始化失败，请刷新后再试", "", 0);
				return;
			}
			tableInfo = data;

			initPage(data);
			initSuggest(data);
		}

	})

	var databaseUrl = ctx + "/service/getDataSource";

	$.ajax({
		url : databaseUrl,
		type : "post",
		dataType : "json",
		data : "",
		success : function(data) {
			if (data.hasError || data.data == null) {
				swaAlert("页面初始化失败(初始化)，请刷新后再试", "", 0);
				return;
			}
			databaseInfo = data.data;
		}
	})

}

function initPage(data) {
	// 初始化页面数据
	$("#routeName").val('');
	$("#serviceAddress").val('');
	$("#routeDesc").val('');
	$("#limitVisit").attr('');
	$("input[type=radio][name='isLog'][value='1']").prop("checked", true);
	$("input[type=radio][name='authKey'][value='0']").prop("checked", true);
	requestRow = 0;
	responseRow = 0;
	$("#requestTable tr:not(:first)").remove();
	$("#responseTable tr:not(:first)").remove();
	$("#requestTable_sql tr:not(:first)").remove();
	$("#responseTable_sql tr:not(:first)").remove();
	$("#requestTable_oracle tr:not(:first)").remove();
	$("#responseTable_oracle tr:not(:first)").remove();
	$('#requestTable_oracle_03 tr:not(:first)').remove();
	$("#publishServiceBtn").removeClass('hide');
	$('#sqlInput').val('');
	hbaseTableList = tableInfo.hbase;
	changeType(1);
	$('#searchType').val(4);
	$("input[type=radio][name='showTotalDataKey'][value='0']").prop(
			"checked", true);
	$('#service_type').val('01');
	assembleDatabase();

	var spaceName = $('#tableSpace').val();

	$('#tableSpace').attr('disabled', false);
	$("#suggest").val("请选择");
	$("#suggest").attr("realVal", "");
	$("#downShow").hide();
	// assembleTableInfo(spaceName);
	$('#sqldiv').show();

	$('#tableSpace').change(function() {
		var spaceName = $('#tableSpace').val();
		// assembleTableInfo(spaceName);
		$("#suggest").val("请选择");
		$("#suggest").attr("realVal", "");
		// initColumnData();
	})
}

/**
 * 改变服务类型方法
 * 
 * @param type
 */
function changeType(type) {
	var showTotal = $('input:radio[name=showTotalDataKey]:checked').val();
	$('#service_type').val(type);

	var searchType = $("#searchType").val();
	$('#author_type').addClass('hide');
	$('#pagesizeDIV').addClass('hide');
	$('#totalPageDIV').addClass('hide');
    $('#showtotalDataDIV').addClass('hide');
	if (searchType == '4') {
		
		 $('#showtotalDataDIV').removeClass('hide');
	}
	
	if(searchType == '4' && showTotal == 0){
		$('#totalPageDIV').removeClass('hide');
	}

	if (type != '02' && type != '03') {
		$('#author_type').removeClass('hide');
	}
	if (type == '03' && showTotal == 0) {
		$('#pagesizeDIV').removeClass('hide');
	}
	showParam();
}

function assembleTableInfo(spaceName) {
	var tableList = [];
	if ($("#searchType").val() == 1) {
		for (var i = 0; i < hbaseTableList.length; i++) {
			var info = hbaseTableList[i].tableName;
			var desc = hbaseTableList[i].table_desc;
			if (null != desc && "" != desc) {
				info += "--" + desc;
			}
			var endInfo = {
				value : info,
				key : hbaseTableList[i].tableName
			}
			tableList.push(endInfo);
		}
		// assembleTable($('#suggest'), tableList);
		return;
	}
	for (var m = 0; m < namespaceInfo.length; m++) {
		if (spaceName == namespaceInfo[m].namespaceName) {
			for (var n = 0; n < namespaceInfo[m].tableList.length; n++) {
				var info = namespaceInfo[m].tableList[n].tableName;
				var desc = namespaceInfo[m].tableList[n].table_desc;
				if (null != desc && "" != desc) {
					info += "--" + desc;
				}
				var endInfo = {
					value : info,
					key : namespaceInfo[m].tableList[n].tableName
				}
				tableList.push(endInfo);
			}
		}
	}
	// assembleTable($('#suggest'), tableList);
}

/* 初始化传入传出参数的列 */
function initColumnData(obj, sourceName) {

	var spaceName = $('#tableSpace').val();
	if (undefined == sourceName || null == sourceName) {
		sourceName = $("#suggest").attr('realVal');
	}
	var columnData = [];

	if ($('#searchType').val() == 1) {
		for (var i = 0; i < hbaseTableList.length; i++) {
			if (sourceName == hbaseTableList[i].tableName) {
				for (var t = 0; t < hbaseTableList[i].columnList.length; t++) {
					var info = hbaseTableList[i].columnList[t].COLUMN_NAME;
					var desc = hbaseTableList[i].columnList[t].column_desc;
					if (null != desc && "" != desc) {
						info += "--" + desc;
					}
					var endInfo = {
						value : info,
						key : hbaseTableList[i].columnList[t].COLUMN_NAME
					}
					columnData.push(endInfo);
				}
			}
		}
		assembleTable(obj.find(".table_column"), columnData);
		return;
	}
	// 循环遍历namespaceInfo，找到table 下对应的列
	for (var m = 0; m < namespaceInfo.length; m++) {
		if (spaceName == namespaceInfo[m].namespaceName) {
			for (var n = 0; n < namespaceInfo[m].tableList.length; n++) {
				if (sourceName == namespaceInfo[m].tableList[n].tableName) {
					for (var t = 0; t < namespaceInfo[m].tableList[n].columnList.length; t++) {
						var info = namespaceInfo[m].tableList[n].columnList[t].COLUMN_NAME;
						var desc = namespaceInfo[m].tableList[n].columnList[t].column_desc;
						if (null != desc && "" != desc) {
							info += "--" + desc;
						}
						var endInfo = {
							value : info,
							key : namespaceInfo[m].tableList[n].columnList[t].COLUMN_NAME
						}
						columnData.push(endInfo);
					}
				}
			}
		}
	}
	assembleTable(obj.find(".table_column"), columnData);
}

/* 更新指定下拉选择框的数据 */
function assembleTable(obj, data) {
	obj.html('');
	obj.append('<option value="" >请选择</option');
	for (var i = 0; i < data.length; i++) {
		var option = '<option value="' + data[i].key + '">' + data[i].value
				+ '</option>';
		obj.append(option);
	}
}

/* 显示配置页 */
function showconfig() {
	
	uid = "";
	reId = "";
	initPage();
	// if ($('#serviceAddress').val() == '')
	// $('#serviceAddress').val(
	// "http://" + window.location.host + ctx + '/api/');
	show(2);
}

function show(type) {
	if (type == 1) {
		$('#configId').hide();
		$('#listId').show();
		$("#listli").addClass('active');
		$("#configli").removeClass('active');
		list();
	} else {
		$('#configId').show();
		$('#listId').hide();

		$("#listli").removeClass('active');
		$("#configli").addClass('active');
	}
	showParam();
};

function showParam() {
	if ($("#searchType").val() == "1") {
		$(".inceptorReq").hide();
		$(".oracleReq_01").hide();
		$(".oracleReq_03").hide();
		$(".oracleReq").hide();
		$(".hbaseReq").show();
	} else if ($("#searchType").val() == "2" || $("#searchType").val() == "3") {
		$(".oracleReq_01").hide();
		$(".oracleReq_03").hide();
		$(".oracleReq").hide();
		$(".hbaseReq").hide();
		$(".inceptorReq").show();
		$("#suggest").attr('disabled', 'disabled');
		// $("#suggest").removeAttr('disabled');
	} else {
		$(".oracleReq_01").hide();
		$(".oracleReq_03").hide();
		$(".oracleReq").hide();
		$(".inceptorReq").hide();
		$(".hbaseReq").hide();
		if ($('#service_type').val() == '03') {
			$('.oracleReq_03').show();
			$('.oracleReq_01').hide();
		} else {
			$('.oracleReq_01').show();
			$('.oracleReq_03').hide();
		}
		$(".oracleReq").show();
		$("#suggest").attr('disabled', 'disabled');
	}
}

var currentPage;
function list(startPage) {

	var url = ctx + '/service/getPageService';
	if (startPage == null) {
		if (currentPage != null && currentPage != undefined) {
			startPage = currentPage;
		} else {
			startPage = 1;
		}
	}
	currentPage = startPage;
	
	//存储当前用户下面用户组的所有成员用户名
	var userGroup;

	//获取当前用户的用户名
	var searchHref = location.search;
	var requestURLData = GetURLRequest();
	var user = requestURLData.loginName;
	var isGroup = function(creator){
		if("admin" == user){
			return true;
		}
		if(null == userGroup || undefined == userGroup || creator == "" || creator == null){
			return false;
		}
		for(var i=0;i<userGroup.length;i++){
			if(creator == userGroup[i].LOGIN_NAME){
				return true;
			}
		}
		return false;
	}
	
	var listfunc = function() {
		$.ajax({
					url : url,
					dataType : "json",
					method : "post",
					data : {
						startPage : startPage,
						searchCondition : $("#searchCondition").val(),
						type : $("#service_type_search select").val(),
						publish : $("#publish_type_search select").val(),
						owner : $("#service_owner_search").val(),
						user : user
					},
					success : function(data) {
						$('#pageType').val("");
						if (data == null || data.data == null) {
							$('#serviceList > tbody').html("");
							$('#nextPage').addClass("disabled");
							$('#prePage').addClass("disabled");
							$('#countId').html("当前页 " + 0 + "/" + 0);
							return false;
						}
						var html = "";
						for (var i = 0; i < data.data.length; i++) {
							var content = data.data[i];
							html += '<tr id="' + content.res_id + '" typeid="'
									+ content.use_type + '">';

							html += '<td style="width:200px;word-break:break-all; word-wrap:break-word;" >'
									+ content.res_nm + '</td>';
//							if (content.is_publish == 1) {
//								html += '<td class="publish_type" style="width:90px;word-break:break-all; word-wrap:break-word;" >已发布</td>';
//							} else {
//								html += '<td class="publish_type" style="width:90px;word-break:break-all; word-wrap:break-word;" >未发布</td>';
//							}
							var create_name = content.name == null?((content.creator==null || content.creator == "")?"--":content.creator):content.name ;
							html += '<td class="publish_type" style="width:110px;word-break:break-all; word-wrap:break-word;" >' + create_name + '</td>';

							var use_type;
							var codetype = content.use_type;
							if (content.use_type == ""
									|| null == content.use_type) {
								codetype = '01';
							}
							for (var mi = 0; mi < apiType.length; mi++) {
								if (apiType[mi].CODE == codetype) {
									use_type = apiType[mi].NAME;
								}
							}
							html += '<td value="'
									+ content.use_type
									+ '" style="width:100px;word-break:break-all; word-wrap:break-word;">'
									+ use_type + '</td>'

							if (content.use_type == 2 || content.use_type == 3) {
								html += '<td style="width:70px;">系统应用</td>';
							} else {
								html += '<td style="width:90px;">'
										+ (content.is_auth == 0 ? '公开访问'
												: '授权访问') + '</td>';
							}

							html += '<td style="word-break:break-all;width:350px; word-wrap:break-word;">'
									+ (content.res_desc == null ? ""
											: content.res_desc) + '</td>';

							html += '<td service_url="'
									+ content.show_url
									+ '" style="width: 50px;word-break:break-all; word-wrap:break-word;" >'
									+ ' <a class="op-icon md-dep-icon" href="javascript:void(0);" resid="9fd4770b-6897-4bae-a1a8-9e3a1e78e860" onclick="showServiceUrl($(this))" title="'
									+ content.show_url
									+ '" data-componentid="a804e0a7-f376-4284-bb22-c1e2420bba04" data-componentname="字符云"><i class="icon icon-more-info"></i>	</a></td>';

							html += '<td><div class="visible-md visible-lg hidden-sm hidden-xs action-buttons">';
							if (content.is_publish == 0) {
								if(isGroup(content.creator) || content.creator == user){
									html += '<a class="op-icon md-edit-icon service_edit" resid="'
										+ content.res_id
										+ '"  onclick="enterEdit($(this))" href="javascript:void(0);" title="修改服务"> '
										+ '<i class="icon icon-pencil" aria-hidden="true"></i>'
										+ '</a>';
								}
								else{
									html += '<a class="op-icon md-edit-icon service_edit" resid="'
										+ content.res_id
										+ '"  onclick="enterEdit($(this))" href="javascript:void(0);" title="查看"> '
										+ '<i class="icon icon-pencil" aria-hidden="true"></i>'
										+ '</a>';
								}
							} else {
								html += '<a class="op-icon md-edit-icon service_edit" resid="'
									+ content.res_id
									+ '"  onclick="enterEdit($(this))" href="javascript:void(0);" title="查看"> '
									+ '<i class="icon icon-pencil" aria-hidden="true"></i>'
									+ '</a>';
							}

							html += ' <a class="op-icon md-dep-icon" href="javascript:void(0);" resid="'
									+ content.res_id
									+ '" onclick="serviceTest($(this))" title="服务测试" data-componentid="a804e0a7-f376-4284-bb22-c1e2420bba04" data-componentname="字符云">'
									+ '<i class="icon icon-test"></i>	</a>';
							//权限控制发布状态
							if(content.is_publish == 0){
								if(isGroup(content.creator) || content.creator == user){
									$(".service_edit").removeClass("hide");
									html += '<a class="op-icon publi"  href="javascript:void(0);" use_type = "'
										+ content.use_type
										+ '" title="发布" mode="'
										+ content.is_publish
										+ '" resid="'
										+ content.res_id
										+ '" onclick="changeServiceMode($(this))"><i class="icon icon-unlock-alt" aria-hidden="true"></i></a>';
								}
								else{
									$(".service_edit").addClass("hide");
									html += '<a class="op-icon publi hide"  use_type = "'
											+ content.use_type
											+ '" href="javascript:void(0);" title="发布" mode="'
											+ content.is_publish
											+ '" resid="'
											+ content.res_id
											+ '" onclick="changeServiceMode($(this))"><i class="icon icon-lock mr5"></i></a>';
								}
							}
							else{
								if(isGroup(content.creator) || content.creator == user){
									$(".service_edit").addClass("hide");
									html += '<a class="op-icon publi"  use_type = "'
											+ content.use_type
											+ '" href="javascript:void(0);" title="取消发布" mode="'
											+ content.is_publish
											+ '" resid="'
											+ content.res_id
											+ '" onclick="changeServiceMode($(this))"><i class="icon icon-lock mr5"></i></a>';
								}
								else{
									$(".service_edit").addClass("hide");
									html += '<a class="op-icon publi hide"  use_type = "'
											+ content.use_type
											+ '" href="javascript:void(0);" title="取消发布" mode="'
											+ content.is_publish
											+ '" resid="'
											+ content.res_id
											+ '" onclick="changeServiceMode($(this))"><i class="icon icon-lock mr5"></i></a>';
								}
							}
							if (content.is_publish == 0 && (user == content.creator || user == "admin")) {
								
								
								html += '<a class="op-icon del"  href="javascript:void(0);" title="删除" resid="'
										+ content.res_id
										+ '" onclick="removeService($(this))"><i class="icon-trash mr5"></i></a>';
								
							} else {
								html += '<a class="op-icon del hide" style="color: #afafaf" href="javascript:void(0);" title="删除" resid="'
										+ content.res_id
										+ '" onclick="changeModeClick()"><i class="icon-trash mr5"></i></a>';
							}
							
							if("admin" == user || user == content.creator){
								html += '<a class="op-icon"  href="javascript:void(0);" title="修改创建人" resid="'
									+ content.res_id
									+ '" onclick="modifyCreator($(this))"><i class="icon-user mr5"></i></a>';
							}

							html += '</div></td>';
							html += '</tr>';
						}
						$('#serviceList > tbody').html(html);
						$('#countId').html(
								"当前页 " + data.currentPage + "/"
										+ data.totalPage);
						$('#prePage,#nextPage').removeAttr("pageNum");
						$('#prePage').attr("pageNum", data.previousPage);
						$('#nextPage').attr("pageNum", data.nextPage);

						$('#prePage,#nextPage').removeClass("disabled");
						if (data.currentPage == data.totalPage) {
							$('#nextPage').addClass("disabled");
						}
						if (data.currentPage == 1) {
							$('#prePage').addClass("disabled");
						}
						$(".checkboxList").each(function() {
							if ($(this).val() == operationID) {
								$(this).prop('checked', true);
							}
						})
					}
				})
	};
	
	
	$.ajax({
		url: ctx + "/service/getGroupUser",
		type:"post",
		data:{userName:user},
		dataType:"json",
		success:function(data){
			if(!data.hasError){
				//过滤admin超级管理员和其他人一组的情况
				if(user == "admin"){
					userGroup = null;
				}
				userGroup = data.data;
				listfunc();
			}
			else{
				swal("初始化失败，请刷新页面","","error");
			}
		}
	})
	

}

/**
 * 删除服务的弹出框
 */
function changeModeClick() {
	swal("请先取消发布", "", "success");
}
function searchList(e) {
	if (e.keyCode == '13')
		list(0);
}
var uid, reId;
function enterEdit(obj) {
	var id = obj.attr('resid');

	var publishMode = obj.parent().find(".publi").attr("mode");
	operationID = id;
	requestRow = 0;
	responseRow = 0;
	initPage();
	if (null == id || "" == id) {
		return;
	}
	uid = id;
	
	//存储当前用户下面用户组的所有成员用户名
	var userGroup;

	//获取当前用户的用户名
	var searchHref = location.search;
	var user = searchHref == null || searchHref == "" ? "" : searchHref
			.split("?")[1].split("=")[1].split("&")[0];
	var isGroup = function(creator){
		if("admin" == user){
			return true;
		}
		if(null == userGroup || undefined == userGroup || creator == "" || creator == null){
			return false;
		}
		for(var i=0;i<userGroup.length;i++){
			if(creator == userGroup[i].LOGIN_NAME){
				return true;
			}
		}
		return false;
	}
	var enterfunc = function(){
		$.ajax({
			url : ctx + "/service/getServiceById",
			data : {
				id : id
			},
			dataType : "json",
			mothod : "post",
			success : function(data) {
				if (null == data) {
					swaAlert("获取数据失败", "", 0);
					return;
				}
				//只有是同一组下面的才有修改权限
				if(publishMode == 0 && (isGroup(data.creator) || user == data.creator)){
					$("#publishServiceBtn").removeClass('hide');
				}
				else{
					$("#publishServiceBtn").addClass('hide');
				}
				requestRow = 0;
				responseRow = 0;
				// 显示页面
				show(2);
				var searchType = data.search_type == 0
				|| data.search_type == null ? 4 : data.search_type;
				$("#searchType").val(searchType);
				lastType = searchType;
				var codetype = data.use_type;
				if (data.use_type == "" || data.use_type == null) {
					codetype = '01';
				}
				$("#pagesize").val(data.pageSize);
				var showalldata = data.showTotalData == null?0:data.showTotalData;
				$("input[type=radio][name=showTotalDataKey][value=" + showalldata + "]").prop("checked",true);
				$("input[type=radio][name=totalPageKey][value=" + data.showTotal + "]").prop("checked",true);
				changeType(codetype);
				// 显示哪种参数
				showParam();
				if (searchType == 1) {
					$('#sqldiv').hide();
					$("#tableSpace").attr('disabled', 'disabled');
					$("#suggest ").val(getTableInfoByName(data.table_name));
					$("#suggest").attr('realVal', data.table_name);
					$("#downShow").show();
					lastTable = $("#suggest").val();
					$("#suggest").removeAttr('disabled');
				} else {
					
					// TODO
					$('#sqldiv').show();
					$("#tableSpace").removeAttr('disabled');
					$("#downShow").hide();
					$("#suggest").attr('disabled', 'disabled');
					$("#suggest").val("请选择");
					$("#suggest").attr("realVal", "");
					assembleDatabase();
					$("#tableSpace").val(data.database_identity);
					$('#sqlInput').val(data.sql_script);
				}
				$("#routeName").val(data.res_nm);
				$("#serviceAddress").val(data.publish_url);
				$("#routeDesc").val(data.res_desc);
				
				// if()
				$(
						"input[type=radio][name='isLog'][value='"
						+ data.write_log + "']").prop("checked",
								true);
				$(
						"input[type=radio][name='authKey'][value='"
						+ data.is_auth + "']")
						.prop("checked", true);
				
				reId = data.res_id;
				
				// assembleTableInfo($("#tableSpace").val());
				// $("#suggest").val("请选择");
				
				var param = data.params;
				if (searchType == 2 || searchType == 3) {
					for (var i = 0; i < param.length; i++) {
						if (param[i].parameter_type == CONSTANS.varType.request) {
							var table = $.getTable("requestTable_sql");
							var html = getTr_sql(1, param[i].parameter_id);
							table.addRow(html);
							requestRow++;
							$(
									"#requestTable_sql tr:eq(" + requestRow
									+ ") td:eq(1) input").val(
											param[i].parameter_name);
							$(
									"#requestTable_sql tr:eq(" + requestRow
									+ ") td:eq(3) input").val(
											param[i].default_value);
							$(
									"#requestTable_sql tr:eq(" + requestRow
									+ ") td:eq(2) input").val(
											param[i].parameter_desc);
						} else {
							var table = $.getTable("responseTable_sql");
							var html = getTr_sql(2, param[i].parameter_id);
							table.addRow(html);
							responseRow++;
							$(
									"#responseTable_sql tr:eq("
									+ responseRow
									+ ") td:eq(1) input").val(
											param[i].parameter_name);
							$(
									"#responseTable_sql tr:eq("
									+ responseRow
									+ ") td:eq(3) input").val(
											param[i].default_value);
							$(
									"#responseTable_sql tr:eq("
									+ responseRow
									+ ") td:eq(2) input").val(
											param[i].parameter_desc);
							$(
									"#responseTable_sql tr:eq("
									+ responseRow
									+ ") td:eq(3) input").val(
											param[i].parameter_order);
						}
					}
					return;
				} else if (searchType == 4) {
//					$(
//							"input[type=radio][name='totalPageKey'][value='"
//							+ data.showTotal + "']").prop(
//									"checked", true);
//					var showalldata = data.showTotalData == null?0:data.showTotalData;
//					$("input[type=radio][name='showTotalDataKey'][value='" + showalldata +"']").prop(
//									"checked", true);
					for (var i = 0; i < param.length; i++) {
						if (param[i].parameter_type == CONSTANS.varType.request) {
							if (data.use_type == '03') {
								var table = $
								.getTable("requestTable_oracle_03");
								var html = get03OracleTr();
								table.addRow(html);
								requestRow++;
								$("#pagesize").val(data.pageSize);
								$(
										"#requestTable_oracle_03 tr:eq("
										+ requestRow
										+ ") td:eq(1) input").val(
												param[i].parameter_name);
								$(
										"#requestTable_oracle_03 tr:eq("
										+ requestRow
										+ ") td:eq(2) input").val(
												param[i].parameter_desc);
								$(
										"#requestTable_oracle_03 tr:eq("
										+ requestRow
										+ ") td:eq(3) input").val(
												param[i].default_value);
								$(
										"#requestTable_oracle_03 tr:eq("
										+ requestRow
										+ ") td:eq(4) input").val(
												param[i].codeSQL);
								$(
										"#requestTable_oracle_03 tr:eq("
										+ requestRow
										+ ") td:eq(5) select").val(
												param[i].data_type);
								$(
										"#requestTable_oracle_03 tr:eq("
										+ requestRow
										+ ") td:eq(6) input").val(
												param[i].parameter_order);
							} else {
								var table = $
								.getTable("requestTable_oracle");
								var html = getOracleTr(1);
								table.addRow(html);
								requestRow++;
								$(
										"#requestTable_oracle tr:eq("
										+ requestRow
										+ ") td:eq(1) input").val(
												param[i].parameter_name);
								$(
										"#requestTable_oracle tr:eq("
										+ requestRow
										+ ") td:eq(2) input").val(
												param[i].parameter_desc);
								$(
										"#requestTable_oracle tr:eq("
										+ requestRow
										+ ") td:eq(3) input").val(
												param[i].default_value);
								$(
										"#requestTable_oracle tr:eq("
										+ requestRow
										+ ") td:eq(4) select").val(
												param[i].data_type);
								$(
										"#requestTable_oracle tr:eq("
										+ requestRow
										+ ") td:eq(5) input").val(
												param[i].parameter_order);
							}
						}
						
						else {
							var table = $.getTable("responseTable_oracle");
							var html = getOracleTr(2);
							table.addRow(html);
							responseRow++;
							$(
									"#responseTable_oracle tr:eq("
									+ responseRow
									+ ") td:eq(1) input").val(
											param[i].parameter_name);
							$(
									"#responseTable_oracle tr:eq("
									+ responseRow
									+ ") td:eq(2) input").val(
											param[i].parameter_desc);
							// $("#responseTable_oracle tr:eq(" +
							// responseRow+ ") td:eq(3)
							// input").val(param[i].default_value);
							$(
									"#responseTable_oracle tr:eq("
									+ responseRow
									+ ") td:eq(3) select").val(
											param[i].data_type);
							$(
									"#responseTable_oracle tr:eq("
									+ responseRow
									+ ") td:eq(4) input").val(
											param[i].parameter_order);
						}
					}
					return;
				}
				requestRow = 0;
				responseRow = 0;
				for (var i = 0; i < param.length; i++) {
					if (param[i].parameter_type == CONSTANS.varType.request) {
						var table = $.getTable("requestTable");
						var html = getTr(param[i].parameter_id);
						table.addRow(html);
						requestRow++;
						initColumnData($("#requestTable tr:eq('"
								+ requestRow + "')"), data.table_name);
						$("#requestTable tr").eq(requestRow).find("td").eq(
								3).find("input").val(
										param[i].parameter_name);
						$("#requestTable tr").eq(requestRow).find("td").eq(
								1).find("select").val(param[i].column_name);
						$(
								"#requestTable tr:eq(" + requestRow
								+ ") td:eq(4) input").val(
										param[i].parameter_desc);
						$(
								"#requestTable tr:eq(" + requestRow
								+ ") td:eq(5) input").val(
										param[i].default_value);
						$(
								"#requestTable tr:eq(" + requestRow
								+ ") td:eq(2)").find("select").val(
										param[i].expression);
						
					} else {
						var table = $.getTable("responseTable");
						var html = getResponseStr(param[i].parameter_id);
						table.addRow(html);
						responseRow++;
						initColumnData($("#responseTable tr:eq('"
								+ responseRow + "')"), data.table_name);
						
						$(
								"#responseTable tr:eq(" + responseRow
								+ ") td:eq(1) input").val(
										param[i].parameter_name);
						$(
								"#responseTable tr:eq(" + responseRow
								+ ") td:eq(2) select").val(
										param[i].column_name);
						$(
								"#responseTable tr:eq(" + responseRow
								+ ") td:eq(3) input").val(
										param[i].parameter_desc);
						$(
								"#responseTable tr:eq(" + responseRow
								+ ") td:eq(4) input").val(
										param[i].parameter_order);
					}
					
				}
				
			}
		})
	}
	
	$.ajax({
		url: ctx + "/service/getGroupUser",
		type:"post",
		data:{userName:user},
		dataType:"json",
		success:function(data){
			if(!data.hasError){
				//过滤admin超级管理员和其他人一组的情况
				if(user == "admin"){
					userGroup = null;
				}
				userGroup = data.data;
				enterfunc();
			}
			else{
				swal("初始化失败，请刷新页面","","error");
			}
		}
	})
}

function removeService(obj) {
	var id = obj.attr('resid');
	if (id == null) {
		return;
	}
	var type = $("#" + id).attr("typeid");
	if (type == 2 || type == 3) {
		$("#phone_modal").modal('show');
		$("#phone_modal").attr("service_id", id);
		$("#phone_num").val('');
	} else {
		showConfirm("是否确认删除", "", function() {
			deletefunc(id);
		})
	}

}

/**
 * 改变服务发布未发布的状态
 * 
 * @param obj
 */
function changeServiceMode(obj) {
	var mode = obj.attr("mode");
	if (mode == 1) {
		mode = 0
	} else {
		mode = 1;
	}
	var id = obj.attr("resid");
	var use_type = obj.attr("use_type");
	var publishFunc = function() {
		$.ajax({
			url : ctx + "/service/unpublishService",
			type : "post",
			data : {
				id : id,
				mode : mode
			},
			dataType : "json",
			success : function(data) {
				if (data.hasError) {
					swal(data.errorMsg, "", "error");
				} else {
					var html;
					if (mode == 1) {
						obj.parent().parent().parent().find(".publish_type")
								.html("已发布");
						obj.parent().find(".service_edit").addClass("hide");
						obj.parent().find(".del").addClass("hide");
						obj.attr("title", "取消发布");
						html = "<i class='icon icon-lock mr5'></i>";
					} else {
						obj.parent().parent().parent().find(".publish_type")
								.html("未发布");
						obj.parent().find(".service_edit").removeClass("hide");
						obj.parent().find(".del").removeClass("hide");
						obj.attr("title", "发布");
						html = "<i class='icon icon-unlock-alt mr5'></i>";
					}

					obj.attr("mode", mode);
					obj.html(html);
					list();
				}
			}
		})
	}
	if (mode == 0) {
		showConfirm("是否取消发布", "", function() {
			publishFunc();
			return;
		})
	} else {
		if (use_type != '03' && use_type != '02') {

			$('.field_check,.topic_check').each(function() {
				$(this).prop("checked", false);
			})
			showTopic();
			oprate_id = obj.attr('resid');

			$.ajax({
				url : ctx + "/service/getServiceTopic",
				type : "post",
				data : {
					id : oprate_id
				},
				dataType : "json",
				success : function(data) {
					if (data.hasError) {
						swal(data.errorMsg, "", "error");
						return;
					}
					for (var i = 0; i < data.data.length; i++) {
						$(".topic_check,.field_check").each(function() {
							if ($(this).val() == data.data[i].TYPEID) {
								$(this).prop("checked", true);
							}
						})
					}
					$('#pub_modal').modal('show');
				}
			})
		}

		else {
			publishFunc();
		}

	}

}

/**
 * 删除02、03类服务
 */
function phone_change(obj) {
	var phone = $('#phone_num').val();
	$.ajax({
		url : ctx + "/system/getPhone",
		data : "",
		dataType : "json",
		type : "post",
		success : function(data) {
			if (data.data.USER_TEL == phone ) {
				deletefunc($("#phone_modal").attr("service_id"));
			} else {
				swal("电话号码错误", "", "error");
			}
		}
	})

}

var deletefunc = function(id) {
	if (id == null) {
		return;
	}
	$.ajax({
		url : ctx + "/service/deleteService",
		data : {
			id : id
		},
		type : "post",
		success : function(data) {
			if (!data.hasError) {
				swal("删除成功!", "", "success");
			} else {
				swal("删除失败!", data.errorMsg, "error");
			}
			$("#phone_modal").modal('hide');
			list();
		},
		error : function(data) {
			swal("删除失败!", "后台运行错误", "error");
		}
	})
}

var operationID = "";
/**
 * 测试服务
 * 
 * @param serviceID
 */
function serviceTest(obj) {
	var serviceID = obj.attr('resid');
	if (serviceID == null) {
		return;
	}
	operationID = serviceID;
	var url = ctx + "/service/edit";
	$
			.ajax({
				url : url,
				data : {
					id : serviceID
				},
				dataType : "json",
				mothod : "post",
				success : function(data) {
					var html = '';
					html += '<form id="test_form" action="'
							+ data.serviceInfo.publish_url
							+ '" method="post" >';
					html += '<p>服务名称 ： <input disabled=true id="modal_serviceName" class="form-control" type="text" value="123" /></p>';

					html += '<p>访问地址：<input disabled=true id="modal_serviceAddr" class="form-control"'
					html += 'type="text" /> </p>'
					html += '<p>服务描述：<textarea disabled=true id="modal_serviceDesc" class="form-control"></textarea></p>'
					html += '<div id="paramTest">';

					$("#paramTest").html('');
					$("#right_modal").html('');
					var html1 = '';
					var params = data.serviceInfo.params;
					var responseHtml = '<div id="load" class="textResult hide" ><img src="'
							+ 	ctx
							+ '/resources/image/loading.gif" style="margin: 200px;"></div>';
					responseHtml += '<div id="error"  class="textResult hide" ><img src="'
							+ ctx
							+ '/resources/image/error.png" style="width:25px;height:25px;margin: 200px;"></div>';
					responseHtml += '<table id="result_table" data-pagination="true"style="width:100% ;">';
					responseHtml += '<thead id="table_head"><tr>';
					for (var i = 0; i < params.length; i++) {
						var disName = params[i].parameter_desc == null ? params[i].parameter_name
								: params[i].parameter_desc;
						if (params[i].parameter_type == CONSTANS.varType.response) {
							var type = data.serviceInfo.search_type;
							var datafield = params[i].parameter_name;
							if (null == type || "4" == type || 4 == type
									|| undefined == type) {
								datafield = params[i].parameter_name;
							}

							responseHtml += "<th data-height='246' data-field='"
									+ datafield + "'";
							responseHtml += " data-align='center' style='text-align:center'>";
							responseHtml += disName + "</th>";
							continue;
						}

						html += disName + ":";
						html += "<input class='form-control' name='"
								+ params[i].parameter_name
								+ "' type='text' id='"
								+ params[i].parameter_name + "_modal'>"
					}
					html += '</div>';

					var id = data.serviceInfo.res_id;
					html += "<button type='button' onclick='testSer()' idv='"
							+ id
							+ "' value='执行' typev='"
							+ data.serviceInfo.search_type
							+ "' id='test_confirm' class='btn btn-primary'>执行</button>";

					html += '<div id="costtime" class="hide"></div>'
					html += '</form></div></table>';
					$('#left_modal').html(html);

					$("#modal_serviceName").val(data.serviceInfo.res_nm);
					$("#modal_serviceAddr").val(data.serviceInfo.show_url);
					$("#modal_serviceDesc").val(data.serviceInfo.res_desc);

					responseHtml += "</tr></thead>";
					$("#right_modal").html(responseHtml);

					$("#myModal").modal('show');

				}
			})

}

/**
 * 测试服务点击响应事件
 * 
 * @param address
 */
function testSer(id) {
	$("#error").addClass('hide');
	$("#load").removeClass('hide');

	var id = $("#test_confirm").attr("idv");
	var type = $("#test_confirm").attr("typev");
	var url = ctx + "/service/serTest";

	var senddata = $('#test_form').serialize();

	senddata += "&&url=" + $('#modal_serviceAddr').val() + "&&routeid=" + id
			+ "&&type=" + type + "&&pageSize=15";
	var routeid = id;
	$.ajax({
		url : url,
		data : senddata,
		type : 'post',
		dataType : 'json',
		success : function(data) {
			if ((undefined != data.hasError && data.hasError)
					|| (undefined != data.success && !data.success)) {
				var errMsg = '';
				if (undefined != data.hasError) {
					errMsg = data.errorMsg;
				}
				if (undefined != data.success) {
					errMsg = data.msg;
				}
				swaAlert("获取数据失败", errMsg, 0);
				$("#load").addClass('hide');
				$("#error").removeClass('hide');
				return;
			}

			$('#result_table').bootstrapTable('load', data.data);
			$("#costtime").html("耗时：  " + data.costTime + " ms");
			$("#costtime").removeClass('hide');
			$("#load").addClass('hide');
			// $(".pagination-info").addClass('hide');
			// $(".pagination").find("button").addClass('hide');
		},
		error : function(data) {
			swaAlert("获取数据失败", data.errorMsg, 0);
			$("#load").addClass('hide');
			$("#error").removeClass('hide');
		}
	})

}

/**
 * 获取选中行的ID
 */
function getChecked() {
	var id = null;
	$(".checkboxList").each(function() {
		if ($(this).is(':checked')) {
			id = $(this).val();
		}
	})
	return id;
}

function getCheckedStatus() {
	var id = null;
	$(".checkboxList").each(function() {
		if ($(this).is(':checked')) {
			id = $(this).attr('status');
		}
	})
	return id;
}
/**
 * 注册服务
 */
function registService() {
	var id = getChecked();
	if (null == id) {
		return;
	}

	var status = getCheckedStatus();
	if (status == 1) {
		swaAlert("该服务已经注册", "", 0);
		return;
	}
	$("#loadingDIV").removeClass('hide');

	$.ajax({
		url : ctx + "/pageAction/registerOnly",
		data : {
			id : id
		},
		dataType : "json",
		method : "post",
		success : function(data) {
			$("#loadingDIV").addClass('hide');
			if (data.hasError) {
				swaAlert("注册失败", data.errorMsg, 0);
			} else {
				swaAlert("注册成功", "", 1);
			}
			list();
		},
		error : function(data) {
			$("#loadingDIV").addClass('hide');
			swaAlert("注册失败", "", 0);
		}

	})
}

/**
 * 取消注册服务
 */
function unRegistService() {

	var id = getChecked();
	if (null == id) {
		return;
	}

	showConfirm("是否确认取消注册?", "", function() {
		var status = getCheckedStatus();
		if (status == 0) {
			swaAlert("该服务未注册", "", 0);
			return;
		}

		var result = null;

		$.ajax({
			url : ctx + "/pageAction/deleteSSP",
			data : {
				id : id
			},
			dataType : "json",
			method : "post",
			success : function(data) {
				if (!data.hasError) {
					swal("取消注册成功!", "", "success");
				} else {
					swal("取消注册失败!", data.errorMsg, "error");
				}
				list();
			}
		})
	})
}

/**
 * 显示对话框
 * 
 * @param code
 * @param msg
 * @param callback
 */
function showConfirm(title, msg, callback) {
	swal({
		title : title,
		text : msg,
		type : "warning",
		showCancelButton : true,
		confirmButtonColor : "#DD6B55",
		confirmButtonText : "确认",
		cancelButtonText : "取消",
		closeOnConfirm : true
	}, function(isConfirm) {
		if (isConfirm) {
			callback()
		}
	})

}

/**
 * swa的对话框
 * 
 * @param type
 * @param msg
 */
function swaAlert(title, msg, type) {
	if (type == undefined || null == type) {
		swal(title, msg);
		return;
	}

	if (type == 1) {
		swal(title, msg, "success");
	} else {
		swal(title, msg, "error");
	}
}

function initSuggest(data) {
	// var a =
	// [{value:"请选择",id:"default",label:"请选择"},{value:"123",label:"123"},{value:"234",label:"234"},{value:"345",label:"345"}];
	var hbaseData = data.hbase;
	var suggestData = [ {
		value : "请选择",
		id : "default",
		label : "请选择"
	} ];
	for (var i = 0; i < hbaseData.length; i++) {
		var innerData = {
			value : hbaseData[i].tableName,
			id : hbaseData[i].tableName,
			label : hbaseData[i].tableName + "---"
					+ $.trim(hbaseData[i].table_desc)
		}
		suggestData.push(innerData);
	}
	$('#suggest').autosuggest({
		data : suggestData,
		minLength : 0,
		maxNum : suggestData.length,
		align : 'left',
		method : 'post',
		firstNext : true,
		highlight : true,
		immediate : true,
		queryParamName : 'city',
		firstSelected : true,
		nextStep : function() {

			tableChange();
		},
		split : ' ',
		open : function() {
			console.log("start open");
		},
		close : function() {
			console.log("start close");
		}
	});
	$("#suggest").next('.as-menu').hide();
	$("#suggest").val("请选择");
	$("#suggest").attr("realVal", "");
}

function tableChange() {

	if (lastTable == "请选择") {
		$("#requestTable tr:not(:first)").remove();
		$("#responseTable tr:not(:first)").remove();
		$("#requestTable_sql tr:not(:first)").remove();
		$("#responseTable_sql tr:not(:first)").remove();
		requestRow = 0;
		responseRow = 0;
		lastTable = $("#suggest").val();
		return;
	}
	if ($('#requestTable tbody tr').length > 0
			|| $('#responseTable tbody tr').length > 0
			|| $('#requestTable_sql tbody tr').length > 0
			|| $('#responseTable_sql tbody tr').length > 0
			|| $('#requestTable_oracle tbody tr').length > 0
			|| $('#responseTable_oracle tbody tr').length > 0) {

		var clickValue = 0;
		swal({
			title : "是否确认改变",
			text : "修改查询表会删除请求、返回参数",
			type : "warning",
			showCancelButton : true,
			confirmButtonColor : "#DD6B55",
			cancelButtonText : "取消",
			confirmButtonText : "确认",
			closeOnConfirm : true,

		}, function(isConfirm) {
			if (isConfirm) {
				$("#requestTable tr:not(:first)").remove();
				$("#responseTable tr:not(:first)").remove();
				$("#requestTable_sql tr:not(:first)").remove();
				$("#responseTable_sql tr:not(:first)").remove();
				requestRow = 0;
				responseRow = 0;
				lastTable = $("#suggest").val();
			} else {
				$("#suggest").val(lastTable);
			}
		})
	} else {
		lastTable = $("#suggest").val();
	}

}

function getTableInfoByName(tableName) {
	hbaseTableList = tableInfo.hbase;
	for (var i = 0; i < hbaseTableList.length; i++) {
		if (hbaseTableList[i].tableName == tableName) {
			return tableName + "---" + $.trim(hbaseTableList[i].table_desc);
		}
	}
	return null;
}

function imageClick() {
	$("#imageModal").modal('show');
}

// //////图片操作JS///////////////////////////
/**
 * 图片操作管理
 */
function fileUpload() {
	$.ajaxFileUpload({
		url : ctx + '/file/save',
		data : {
			rowkey : "123"
		},
		secureuri : false,
		fileElementId : 'fileup',
		dataType : "json",
		success : function(response) {
			alert('custom handler for file:');
		}
	})
}

// 建立一個可存取到該file的url
function getObjectURL(file) {
	var url = null;
	if (window.createObjectURL != undefined) { // basic
		url = window.createObjectURL(file);
	} else if (window.URL != undefined) { // mozilla(firefox)
		url = window.URL.createObjectURL(file);
	} else if (window.webkitURL != undefined) { // webkit or chrome
		url = window.webkitURL.createObjectURL(file);
	}
	return url;
}

function showModel(that) {
	var info = '';
	for (var i = 0; i < udata.length; i++) {
		if (udata[i].ID == that.id || "u" + udata[i].ID == that.id) {
			info = udata[i].MODEL_INFO;
		}
	}

	$("#handbook").modal("hide");
	$("#sqlInput").val(info);
}

function modelClick(obj) {
	var ishide = $("#u" + obj.id).hasClass('hide');

	$("#model_madal_body").find(".datailInfo").addClass('hide');
	if (ishide) {
		$("#u" + obj.id).removeClass('hide');
	}
	// $("#u"+obj.id).removeClass('hide');
}

/**
 * 模板选择--->鼠标移开事件，
 * 
 * @param obj
 */
function mouseOut(obj) {
	setTimeout(function() {
		var thisdata;
		for (var i = 0; i < udata.length; i++) {
			if (udata[i].ID == obj.id) {
				thisdata = udata[i];
			}
		}
		var html = thisdata.MODEL_NAME + " : " + thisdata.MODEL_DESC;
		obj.innerHTML = html;
	}, 200)

}

/**
 * 点击显示服务地址详情
 * 
 * @param obj
 */
function showServiceUrl(obj) {
	var url = obj.attr("title");

	$("#service_url_input").val(url);

	$("#service_url").modal('show');
}

/**
 * 点击拷贝 将服务地址拷贝在剪切板
 */
function copyServiceUrl(txt) {
	if (null == txt || undefined == txt) {
		txt = $("#service_url_input").val();
	}
	if (window.clipboardData) {
		window.clipboardData.clearData();
		window.clipboardData.setData("Text", txt);
		alert("已经成功复制到剪帖板上！");
	} else if (navigator.userAgent.indexOf("Opera") != -1) {
		window.location = txt;
	} else if (window.netscape) {
		try {
			netscape.security.PrivilegeManager
					.enablePrivilege("UniversalXPConnect");
		} catch (e) {
			alert("被浏览器拒绝！\n请在浏览器地址栏输入'about:config'并回车\n然后将'signed.applets.codebase_principal_support'设置为'true'");
		}
		var clip = Components.classes['@mozilla.org/widget/clipboard;1']
				.createInstance(Components.interfaces.nsIClipboard);
		if (!clip)
			return;
		var trans = Components.classes['@mozilla.org/widget/transferable;1']
				.createInstance(Components.interfaces.nsITransferable);
		if (!trans)
			return;
		trans.addDataFlavor('text/unicode');
		var str = new Object();
		var len = new Object();
		var str = Components.classes["@mozilla.org/supports-string;1"]
				.createInstance(Components.interfaces.nsISupportsString);
		var copytext = txt;
		str.data = copytext;
		trans.setTransferData("text/unicode", str, copytext.length * 2);
		var clipid = Components.interfaces.nsIClipboard;
		if (!clip)
			return false;
		clip.setData(trans, null, clipid.kGlobalClipboard);
		alert("已经成功复制到剪帖板上！");
	}
}

/**
 * 组装服务类型下拉列表
 */
var apiType;
function assembleServiceType() {

	$.ajax({
		url : ctx + "/service/getAPIType",
		data : "",
		dataType : "json",
		type : "post",
		success : function(data) {
			if (!data.hasError) {
				apiType = data.data;
				show(1);
				var html = '';
				for (var i = 0; i < data.data.length; i++) {
					html += '<option value="' + data.data[i].CODE + '"> '
							+ data.data[i].NAME + ' </option>';
				}
				$("#service_type").html(html);
				$("#qry_md_model").html('<option value="">全部</option>' + html);
			} else {
				swal("初始化出错，请刷新", "", "error");
			}
		}

	})
}

/**
 * 组装创建人
 */
function assembleOwner(){
	var user = GetURLRequest().loginName;
	$.ajax({
		url : ctx + "/service/getAllCreator",
		data : {user:user},
		dataType : "json",
		type : "post",
		success:function(data){
			if(!data.hasError){
				var html = '';
				for(var i=0;i<data.data.length;i++){
					html += '<option value="' + data.data[i].LOGIN_NAME + '">' + data.data[i].NAME + '</option>';
				}
				$("#service_owner_search").append(html);
			}
			else{
				swal("初始化失败，请刷新页面",data.errorMsg,"error");
			}
		}
	})
}


/**
 * 提出公用方法---获得选择字段类型的表格列
 */
var dbType = null;
function getDBType() {

	$.ajax({
		url : ctx + "/service/getDBType",
		type : "post",
		data : "",
		dataType : "json",
		success : function(data) {
			if (!data.hasError) {
				dbType = data.data;
			}
		}
	})
}

/**
 * 组装公用的表格列里面的数据类型选择
 */
function getDBTypeTr() {
	var html = '<td><select class="form-control">';
	if (null != dbType) {
		for (var i = 0; i < dbType.length; i++) {
			html += '<option value=' + dbType[i].CODE + '> ' + dbType[i].NAME
					+ '</option>';
		}
	}
	html += '</select></td>';
	return html;
}



/**
 * admin权限下，点击修改创建人
 * @param obj
 */
var oldUser;
function modifyCreator(obj){
	var resid = obj.attr("resid");
	userList();
	oldUser = resid;
	$("#modifyCreatorModal").modal('show');
	
}


function userList(startPage){
	if(startPage == null){
		startPage = 0;
	}
	var condition = $("#searchCondition_user").val();
	$.ajax({
		url:ctx + "/service/getPageUser",
		type:"post",
		data:{startPage:startPage,condition:condition},
		dataType:"json",
		success:function(data){
			if(!data.hasError){
				var html = '';
				for(var i=0;i<data.data.length;i++){
					html += '<tr>';
					html += '<td><input class="user_list" type="checkbox" value="' + data.data[i].LOGIN_NAME + '"/></td>';
					html += '<td>' + data.data[i].LOGIN_NAME + '</td>';
					html += '<td>' + data.data[i].NAME + '</td>';
					var remark = data.data[i].USER_REMARK == null?"-":data.data[i].USER_REMARK;
					html += '<td>' + remark + '</td>';
					html += '</tr>';
				}
				$("#userTable tbody").html(html);
				
				$('#count_user').html(
						"第 " + data.currentPage + "/"
								+ data.totalPage);
				$('#prePage_user,#nextPage_user').removeAttr("pageNum");
				$('#prePage_user').attr("pageNum", data.previousPage);
				$('#nextPage_user').attr("pageNum", data.nextPage);

				$('#prePage_user,#nextPage_user').removeClass("disabled");
				if (data.currentPage == data.totalPage) {
					$('#nextPage_user').addClass("disabled");
				}
				if (data.currentPage == 1) {
					$('#prePage_user').addClass("disabled");
				}
			}
			else{
				swal("获取联系人失败，请重试","","error");
			}
			
		}
	})
}


/**
 * 点击确定修改服务创建人
 */
function changeUser(){
	var userName = null;
	$(".user_list").each(function(){
		var checked = $(this).is(':checked');
		if(checked){
			userName = $(this).val();
		}
	})
	
	if(null == userName){
		return;
	}
	
	$.ajax({
		url:ctx + "/service/modifyUser",
		data:{resid:oldUser,newUser:userName},
		dataType:"json",
		type:"post",
		success:function(data){
			if(!data.hasError){
				swal("更改成功","","success");
				list(1);
				$("#modifyCreatorModal").modal('hide');
			}
			else{
				swal("更改失败",data.errorMsg,"error");
			}
		}
	})
}



function GetURLRequest() {
	var url = location.search; //获取url中"?"符后的字串 
	var theRequest = new Object();
	if (url.indexOf("?") != -1) {
		var str = url.substr(1);
		strs = str.split("&");
		for (var i = 0; i < strs.length; i++) {
			theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
		}
	}
	return theRequest;
}