function jqueryAlert(content){
	$.dialog({
	    content: content,
	    boxWidth: '500px',
	    useBootstrap: false, 
	    icon: 'fa fa-warning',
	    type: 'blue'
	});
}