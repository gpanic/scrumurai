

$(document).ready(function() {
	autoLogin();
/*
	$(document).on("pagebeforeshow",  function() {
		if(!_user && location.hash != "#register"){
			console.log("pagebeforeshow");
			$.mobile.changePage("#login");
		}
	});
*/
	$(document).on("pagebeforeshow","#login",  function() {
		console.log("pagebeforeshow #login");
		if(!_user)
			autoLogin();
		return false;
	});
	
	$("#loginForm").submit(function(){
		login();
		return false;
	});

	$("#registerForm").submit(function(){
		register();
		return false;
	});
});


var autoLogin = function(){
	if(location.hash != "#login" && location.hash != ""){
		return false;
	}
	$.mobile.loading('show');
	var loginData = window.localStorage.getItem("login");
	if(!isEmpty(loginData)){
		$.ajax({
			url: _ws+"/users/loginremember",
			type: "POST",
			contentType: "text/plain; charset=utf-8",
			data: JSON.stringify(loginData),
			async: false
		}).done(function(data){
			_user = data;
			$.mobile.changePage("#mytasks");
		}).fail(function(){
			$.mobile.loading('hide');
			$("#login_logo").removeClass("centered").addClass("login_logo");
			$("#loginPage").show();
		});
	}
}

var login = function(){
	$("#submitLogin").button("disable");
	var user = $("#loginForm").serializeObject();

	if(isEmpty(user.username) || isEmpty(user.password)){
		failedLogin();
		return false;
	}

	$.ajax({
		url: _ws+"/users/login",
		type: "POST",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(user),
		beforeSend: function ( xhr ) {
			$.mobile.loading('show');
		}
	}).done(function(data){
		window.localStorage.setItem("login", data.username+"|"+data.number);
		delete data.number;
		_user = data;
		$.mobile.changePage("#mytasks");
	}).fail(function(xhr, textStatus, errorThrown){
		failedLogin();
	}).always(function(){
		$.mobile.loading('hide');
	});
}

function failedLogin(){
	$("#login_fail").show();
	enableButton("#submitLogin");
}

var register = function(){
	$("#submitRegister").button("disable");
	var user = $("#registerForm").serializeObject();

	if(isEmpty(user.username) || isEmpty(user.password) || isEmpty(user.email)){
		enableButton("#submitRegister");
		return false;
	}

	$.ajax({
		url: _ws+"/users",
		type: "POST",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(user),
		beforeSend: function ( xhr ) {
			$.mobile.loading('show');
		}
	}).done(function(data){
		window.localStorage.setItem("login", user.username+"|"+data[1]);
		loginNewUser(user,data[0]);
		$.mobile.changePage("#mytasks");
	}).fail(function(xhr, textStatus, errorThrown){
		enableButton("#submitRegister");
	}).always(function(){
		$.mobile.loading('hide');
	});
}

function loginNewUser(user,id){
	user.id = id;
	_user = user;
}