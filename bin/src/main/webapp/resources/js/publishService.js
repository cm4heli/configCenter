


$(function(){
	
	
//	$('.modal-body .prev').focus(function(){
//		$('.modal-body .prev').removeClass('hide');
//	})
//	
//	$('.modal-body .next').focus(function(){
//		$('.modal-body .next').removeClass('hide');
//	})
//	$('.modal-body .prev').click(function(){
//		showTopic();
//	})
//	$('.modal-body .next').click(function(){
//		showField();
//	})
//	
	$.ajax({
		url:ctx + "/service/serviceAttr",
		type:"post",
		data:"",
		dataType:"json",
		success:function(data){
			if(!data.hasError){
				$("#topicName h2").html(data.data.topicName);
				$("#fieldName h2").html(data.data.fieldName);
				var topicHtml = '';
				for(var i=0;i<data.data.topic.length;i++){
					topicHtml += '<div><input class="topic_check" id="' + data.data.topic[i].CODE + '" value="' + data.data.topic[i].CODE + '" type="checkbox" class="topic"><label for=' + data.data.topic[i].CODE + '>' +  data.data.topic[i].NAME +'</label></div>';
				}
				$("#service_topic").html(topicHtml);
				
				
				var fieldHtml = '';
				for(var i=0;i<data.data.field.length;i++){
					fieldHtml += '<div><input class="field_check" id="' + data.data.field[i].CODE + '" value="' + data.data.field[i].CODE + '" type="checkbox" class="topic"><label for=' + data.data.field[i].CODE + '>' +  data.data.field[i].NAME +'</label></div>';
				}
				$("#service_field").html(fieldHtml);
			}
		}
	})
	
})


/**
 * 点击发布服务
 */
function publishServiceConfirm(){
	
	var topicList = [];
	$(".topic_check:checked").each(function(){
			topicList.push($(this).val());
	})
	
	var fieldList = [];
	
	$(".field_check:checked").each(function(){
		fieldList.push($(this).val());
	})
	
	if(topicList.length == 0){
		alert('请选择主题！！');
		return;
	}
	
	if(fieldList.length == 0){
		alert('请选择应用领域！！');
		return;
	}
	var data = {
			id:oprate_id,
			topic:JSON.stringify(topicList),
			field:JSON.stringify(fieldList)
		}
	$.ajax({
		url:ctx + "/service/publishService",
		type:"post",
		data:data,
		dataType:"json",
		success:function(data){
			if(!data.hasError){
				var obj = $(".publi[resid=" + oprate_id + "]");
				obj.parent().parent().parent().find(".publish_type").html("已发布");
				obj.parent().find(".service_edit").addClass("hide");
				obj.parent().find(".del").addClass("hide");
				obj.attr("title","取消发布");
				html = "<i class='icon icon-lock mr5'></i>";
				obj.attr("mode",1);
				obj.html(html);
				$('#pub_modal').modal('hide');
				list(0);
				alert("发布成功！");
				
			}
			else{
				alert("发布失败：" + data.errorMsg);
			}
		}
	})
}



function showTopic(){
//	$('.modal-body .prev').addClass('hide');
//	$('.modal-body .next').removeClass('hide');
//	$('.pub_left').css("margin-left",'20px');
//	$('.pub_right').css("margin-left",'600px');
}


function showField(){
//	$('.modal-body .prev').removeClass('hide');
//	$('.modal-body .next').addClass('hide');
//	$('.pub_left').css("margin-left",'-600px');
//	$('.pub_right').css("margin-left",'20px');
}

