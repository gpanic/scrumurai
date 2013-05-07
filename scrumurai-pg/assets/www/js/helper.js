function enableButton(btn){
	$(btn).button("enable");  
	$(btn).parent().removeClass("ui-btn-active");
}

function isEmpty(str){
	if(str || str.length > 0)
		return false;
	return true;
}