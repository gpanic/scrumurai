$(document).ready(function() {

	$(document).on("pagebeforeshow","#addrelease",  function() {
		console.log("pagebeforeshow #addrelease");
		if(_selectedProject[0] == -1)
			redirectError("First you need to create a project.");
	});
	
	$("#addReleaseForm").submit(function(){
		console.log("addrelease");
		addRelease();
		return false;
	});
});

var addRelease = function(){
	$("#submitAddRelease").button("disable");
	var release = $("#addReleaseForm").serializeObject();

	if(isEmpty(release.name)){
		failedAddRelease();
		return false;
	}

	release.project = {"id" : _selectedProject[0]};
	console.log(JSON.stringify(release));
	$.ajax({
		url: _ws+"/releases/",
		type: "POST",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(release),
		async: false,
		beforeSend: function ( xhr ) {
			$.mobile.loading('show');
		}
	}).done(function(data){
		$.mobile.changePage("#release");
	}).fail(function(xhr, textStatus, errorThrown){
		console.log("add release fail");
	}).always(function(){
		$.mobile.loading('hide');
	});
}

function failedAddRelease(){
	$("#addRelease_fail").show();
	enableButton("#submitAddRelease");
}