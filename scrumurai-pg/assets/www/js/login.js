var _user;

$(document).ready(function() {
	$.mobile.loading('show');
	var loginData = window.localStorage.getItem("login");
	if(notEmpty(loginData)){
		$.ajax({
			url: _ws+"/users/loginremember",
			type: "POST",
			contentType: "text/plain; charset=utf-8",
			data: JSON.stringify(loginData)
		}).done(function(data){
			if(data.id > 0){
				$.mobile.loading('hide');
				_user = data;
				window.location = "#mytasks";
			}
		}).fail(function(){
			$.mobile.loading('hide');
			$("#login_logo").removeClass("centered").addClass("login_logo");
			$("#loginPage").show();
		})
	}
});

$(function(){
	$("#loginForm").submit(function(){
		$("#submitLogin").button("disable");

		var user = $("#loginForm").serializeObject();

		if(notEmpty(user.username) && notEmpty(user.password)){
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
				window.location = "#mytasks";
			}).fail(function(xhr, textStatus, errorThrown){
				failedLogin();
			}).always(function(){
				$.mobile.loading('hide');
			});
		}else{
			failedLogin();
		}

		return false;
	});

	function failedLogin(){
		$("#login_fail").show();
		enableButton("#submitLogin");
	}


	$("#registerForm").submit(function(){
		$("#submitRegister").prop("disabled", true);

		var user = $("#registerForm").serializeObject();
		if(notEmpty(user.username) && notEmpty(user.password) && notEmpty(user.email)){
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
				window.location = "#mytasks";
			}).fail(function(xhr, textStatus, errorThrown){
				enableButton("#submitRegister");
			}).always(function(){
				$.mobile.loading('hide');
			});
		}else{
			enableButton("#submitRegister");
		}

		return false;
	});

	function loginNewUser(user,id){
		user.id = id;
		_user = user;
	}

});
