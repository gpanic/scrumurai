$(document).ready(function() {

	$(document).on("pagebeforeshow","#addrelease",  function() {
		console.log("pagebeforeshow #addrelease");
		if(_selectedProject[0] == -1)
			redirectError("First you need to create a project.");
	});
	
	$(document).on("pageshow", "#release", function() {
		populateReleases();
	});

	$(document).on("pagebeforeshow","#viewrelease",  function() {
		console.log("pagebeforeshow #viewrelease");
		if(_currentRelease != null)
			getReleaseDetailed(_currentRelease);
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

var getReleaseDetailed = function(release_id){
	$.ajax({
		url: _ws + "/releases/detailed/"+ release_id,
		type: "GET",
		dataType: "json",
		beforeSend: function ( xhr ) {
			$.mobile.loading('show');
		}
	}).done(function(json) {
		console.log(json);
		$("#viewrelease_name").text(json.name);
		$("#viewrelease_done").text(json.effort_done+"pt");
		$("#viewrelease_total").text(json.effort_total+"pt");
		$("#viewrelease_description").text((json.description != null) ? json.description: "/");
		$("#viewrelease_progressbar" ).progressbar({value: json.effort_done * 100 / json.effort_total});
		$("#viewrelease_startdate").text(getDateFormat(json.start_date));
		$("#viewrelease_enddate").text(getDateFormat(json.end_date));
		$("#viewrelease_changelog").text((json.change_log != null) ? json.change_log:"/");

	}).fail(function() {
		console.log("fail");
	}).always(function() {
		$.mobile.loading('hide');
	});
}