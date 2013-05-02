function enableButton(btn){
	$(btn).button("enable");  
	$(btn).parent().removeClass("ui-btn-active");
}

function notEmpty(str){
	if(str || str.length > 0)
		return true;
	return false;
}