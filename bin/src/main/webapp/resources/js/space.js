
$(function() {
	$.ajax({
		url : ctx + "/system/getData",
		data : "",
		dataType : "json",
		type : "post",
		async : false,
		success : function(data) {
			if (null != data.data) {
				$("#ip_input").val(data.data.IP);
				$("#port_input").val(data.data.PORT);
			}
		},
		error : function(data) {
			alert("初始化失败，请刷新重试！", "", 0);
		}
	})
	$("#sys_init").modal('show');

	$('#sys_init').on('hidden.bs.modal', function() {
		$("#sys_init").modal('show');
	})

	$('#cancel').click(function() {
		window.location = '/cloudservice';
	})
	$("#confirm")
			.click(
					function() {
						var ip = $('#ip_input').val();
						if (ip == "" || ip == null) {
							alert("ip不能为空");
							$('#ip_input').focus();
							return;
						}
						var ip_reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
						if (!ip_reg.test(ip) && "localhost" != ip) {
							alert("ip格式错误");
							$('#ip_input').focus();
							return;
						}
						var port = $('#port_input').val();
						if (port == null || port == "") {
							alert("端口不能为空");
							$('#port_input').focus();
							return;
						}
						var port_reg = /^[0-9]*$/;
						if (!port_reg.test(port)) {
							alert("端口只能为数字");
							$('#port_input').focus();
							return;
						}
						$.ajax({
							url : ctx + '/system/setData',
							data : {
								ip : ip,
								port : port
							},
							dataType : "json",
							type : "post",
							success : function(data) {
								if (data.hasError) {
									alert(data.errorMsg);
								} else {
									alert('修改成功');
									location.reload() ;
								}
							}
						})

					})
})