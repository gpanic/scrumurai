function enableButton(btn){
	$(btn).button("enable");  
	$(btn).parent().removeClass("ui-btn-active");
}

function isEmpty(str){
	if(str == null || str || str.length > 0)
		return false;
	return true;
}

function redirectError(msg){
  $("#errorpage_msg").text(msg);
  $.mobile.changePage("#errorpage");
  return false;
}

function getDateFormat(date){
	var d = new Date(date);
	return d.getDate()+"."+(d.getMonth()+1)+"."+d.getFullYear();
}