$(document).ready(function() {

	$(document).on("pagebeforeshow","#addrelease",  function() {
		console.log("pagebeforeshow #addrelease");
		if(_selectedProject[0] == -1)
			redirectError("First you need to create a project.");
	});
	
	$(document).on("pageshow", "#release", function() {
		populateReleases();
	});

	$("#addReleaseForm").submit(function(){
		console.log("addrelease");
		addRelease();
		return false;
	});

	$(".release_link").click(function(){
		console.log()
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

var populateReleases = function() {
	$.ajax({
		url: _ws + "/releases/currentdone/" + _selectedProject[0],
		type: "GET",
		dataType: "json",
		beforeSend: function ( xhr ) {
			$.mobile.loading('show');
		}
	}).done(function(json) {
		var releasesCurrent = 0;
		var releasesDone = 0;
		$("#release_coll_current_list").empty();
		$("#release_coll_done_list").empty();
		$.each(json, function(i, release) {
			var release_html = "<li><a class='release_link' data-releaseid='" + release.id + "' href='javascript:selectRelease(" + release.id + ")'>" +
								"<h2>" + release.name +"</h2>" +
								"<p><strong>Start date:</strong> " + release.start_date + "</p>" +
								"<p><strong>End date:</strong> " + release.end_date + "</p>";
			if(release.current) {
				$("#release_coll_current_list").append(release_html);
				releasesCurrent++;
			} else {
				$("#release_coll_done_list").append(release_html);
				releasesDone++;
			} 
		});

		$("#release_coll_current_count").html(releasesCurrent);
		$("#release_coll_done_count").html(releasesDone);

		$("#release_coll_current_list").listview("refresh");
		$("#release_coll_done_list").listview("refresh");
	}).fail(function() {
		alert("fail");
	}).always(function() {
		$.mobile.loading('hide');
	});
}

var selectRelease = function(release_id){
	_currentRelease = release_id;
	$.mobile.changePage("#viewrelease");
}