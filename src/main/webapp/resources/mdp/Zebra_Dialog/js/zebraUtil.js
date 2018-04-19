function zebraAlert(info){
//	$.Zebra_Dialog('<strong>Zebra_Dialog</strong>, a small, compact and highly configurable dialog box plugin for jQuery');
	var scr_width = document.body.clientWidth ;  
	var scr_height = document.body.clientHeight ;
	scr_width='left+'+scr_width/2;
	scr_height='top+'+scr_height/4;

	
	$.Zebra_Dialog(info, {
	    'title':    '提示'
	});
	$(".ZebraDialog_Button_0").html("确定")
	$(".ZebraDialog").css({top:"200px"});
}


//demo
/*$.Zebra_Dialog('<strong>Zebra_Dialog</strong>, a small, compact and highly configurable dialog box plugin for jQuery', {
    'type':     'question',
    'title':    'Custom buttons',
    'buttons':  [
                    {caption: 'Yes', callback: function() { alert('"Yes" was clicked')}},
                    {caption: 'No', callback: function() { alert('"No" was clicked')}},
                    {caption: 'Cancel', callback: function() { alert('"Cancel" was clicked')}}
                ]
});*/