//文本框字数限制 20
function wordLimitInput(field, rules, i, options) {
	var length=getByteLen($(field).val());
	if (length > 40) {
		return options.allrules.customInputWordLimt.alertText;
	}
}

//文本框字数限制 40
function wordLimitInputLong(field, rules, i, options) {
	var length=getByteLen($(field).val());
	if (length > 80) {
		return options.allrules.customInputWordLimtLong.alertText;
	}
}

//textarea框字数限制 200
function wordLimitArea(field, rules, i, options) {
	var length=getByteLen($(field).val());
	console.log(length);
	if (length > 400) {
		return options.allrules.customAreaWordLimt.alertText;
	}
}


//验证同一个parent下的checkbox 必选一个
function checkboxRequiredP(field, rules, i, options){
	if($(field).parent().find("input[type=checkbox]:checked").length==0){
		return "* 请选择共享方式!";
	}
}

//验证同一个parent下的radio必选一个
function radioRequiredP(field, rules, i, options){
	if($(field).parent().find("input[type=radio]:checked").length==0){
		return "* 请选择共享方式!";
	}
}

// 返回val的字节长度
function getByteLen(val) {
	var len = 0;
	for (var i = 0; i < val.length; i++) {
		if (val[i].match(/[^\x00-\xff]/ig) != null) // 全角
			len += 2;
		else
			len += 1;
	}
	return len;
}