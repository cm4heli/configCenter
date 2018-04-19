$(function(){
	
	//---------->> 滑动页面头部变化
	$(window).scroll(function(){
	    var top = $(window).scrollTop();   //设置变量top,表示当前滚动条到顶部的值
        if (top > 136)// 当滚动条到顶部的值大于136（原head+nav）时
        {
			$(".top").addClass("small");
			$(".totop").fadeIn();
        }
		else
		{
		    $(".top").removeClass("small");
			$(".totop").fadeOut();
		}
	});	
	$(".totop").click(function(){
		$('body,html').animate({scrollTop:0},1000);
		return false;
	});
	
	//收起展开
	$(".res_mod").click(function(){
		$(this).siblings(".res_mod").removeClass("res_collapse");
		$(this).addClass("res_collapse");
	});
	$(".res_mod_del").click(function(){
		$(this).parents(".res_mod").hide();
	});
	
	//---------->> 单选多选
	//多选
	$("input[type='checkbox']").click(function(){ 
		if($(this).is(':checked')){ 
			$(this).attr("checked","checked"); 
			$(this).parent().removeClass("c_off").addClass("c_on"); 
		}else{ 
			$(this).removeAttr("checked"); 
			$(this).parent().removeClass("c_on").addClass(" c_off"); 
		} 
	}); 
	//单选
	$("input[type='radio']").click(function(){ 
		$("input[type='radio']").removeAttr("checked"); 
		$(this).attr("checked","checked"); 
		$(this).parent().removeClass("r_off").addClass("r_on").siblings().removeClass("r_on").addClass("r_off"); 
	}); 
	
	//---------->> 字段筛选
	$(".res_field").click(function(){
		$(this).toggleClass("res_field_sel");
	});
	
});

//---------->> 选项卡
function setTabs(name,cursel,n){
    for(i=1;i<=n;i++){
        var menu=document.getElementById(name+i);
        var con=document.getElementById(name+"-con"+"-"+i);
        var cur = name + "-" + "cur";
        menu.className=i==cursel?cur:"";
        con.style.display=i==cursel?"block":"none";
    }
}
