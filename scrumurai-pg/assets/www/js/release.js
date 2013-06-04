$(document).ready(function() {

	$(document).on("pagebeforeshow","#addrelease",  function() {
		console.log("pagebeforeshow #addrelease");
		if(_selectedProject[0] == -1)
			redirectError("First you need to create a project.");

		$("#add_release_name").val("");
		$("#add_release_description").val("");
		enableButton("#submitAddRelease");

	});

	$(document).on("pagebeforeshow","#editrelease",  function() {
		console.log("pagebeforeshow #editrelease");
		if(_currentRelease != null)
			loadEditRelease(_currentRelease);
	});
	
	$(document).on("pageshow", "#release", function() {
		populateReleases();
	});

	$(document).on("pagebeforeshow", "#deleterelease", function() {
		console.log("pagebeforeshow #deleterelease");
		if(_currentRelease != null){
			deleteRelease(_currentRelease);
		}
	});
	$(document).on("pagebeforeshow","#viewrelease",  function() {
		console.log("pagebeforeshow #viewrelease");
		if(_currentRelease != null)
			getReleaseDetailed(_currentRelease);
	});

	$("#addReleaseForm").submit(function(){
		addRelease();
		return false;
	});

	$("#editReleaseForm").submit(function(){
		editRelease();
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
			"<p><strong>Start date:</strong> " + ((release.start_date != null) ? getDateFormat(release.start_date):"/") + "</p>" +
			"<p><strong>End date:</strong> " + ((release.end_date != null) ? getDateFormat(release.end_date):"/") + "</p>";
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
		console.log("fail populateRelease");
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
		$("#viewrelease_name").text(json.name);
		$("#viewrelease_done").text(json.effort_done+"pt");
		$("#viewrelease_total").text(json.effort_total+"pt");
		$("#viewrelease_description").text((json.description != null) ? json.description: "/");
		$("#viewrelease_progressbar" ).progressbar({value: json.effort_done * 100 / json.effort_total});
		$("#viewrelease_startdate").text((json.start_date != null ) ? getDateFormat(json.start_date): "/");
		$("#viewrelease_enddate").text((json.end_date != null ) ? getDateFormat(json.end_date): "/");
		$("#viewrelease_changelog").text((json.change_log != null) ? json.change_log:"/");

		$("#viewrelease_sprints > div").empty();

		$(json.sprints).each(function(){
			$("#viewrelease_sprints > div").append('<a href="#viewsprint" data-role="button" data-corners="true" data-shadow="true" data-iconshadow="true" data-wrapperels="span" data-theme="c" class="ui-btn ui-shadow ui-btn-corner-all ui-btn-up-c">'+
				'<span class="ui-btn-inner">'+
				'<span class="ui-btn-text">'+
				this.name+
				'</span>'+
				'</span>'+
				'</a>');
		});

		$("#viewrelease_sprints > div a:first-child").addClass("ui-first-child");
		$("#viewrelease_sprints > div a:last-child").addClass("ui-last-child");
	}).fail(function() {
		console.log("fail getReleaseDetail");
	}).always(function() {
		$.mobile.loading('hide');
	});
}

var loadEditRelease = function(release_id){
	$.ajax({
		url: _ws + "/releases/"+ release_id,
		type: "GET",
		dataType: "json",
		beforeSend: function ( xhr ) {
			$.mobile.loading('show');
		}
	}).done(function(json) {
		$("#edit_release_name").val(json.name);
		$("#edit_release_description").val(json.description);
		$("#editrelease_id").remove();
		$("#editReleaseForm").append("<input type='hidden' name='id' id='editrelease_id' value='" + json.id + "'/>");
	}).fail(function() {
		console.log("fail loadEditRelease");
	}).always(function() {
		$.mobile.loading('hide');
	});
}

var editRelease = function(){
	$("#submitEditRelease").button("disable");
	var release = $("#editReleaseForm").serializeObject();

	if(isEmpty(release.name)){
		failedEditRelease();
		return false;
	}

	$.ajax({
		url: _ws+"/releases/detailed/" + release.id,
		type: "PUT",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(release),
		async: false,
		beforeSend: function ( xhr ) {
			$.mobile.loading('show');
		}
	}).done(function(data){
		enableButton("#submitEditRelease");
		$.mobile.changePage("#viewrelease");
	}).fail(function(xhr, textStatus, errorThrown){
		console.log("edit release fail");
	}).always(function(){
		$.mobile.loading('hide');
	});
}

function failedEditRelease(){
	$("#EditRelease_fail").show();
	enableButton("#submitEditRelease");
}

var deleteRelease = function(release_id){
	$.ajax({
		url: _ws+"/releases/deletedetailed/" + release_id,
		type: "DELETE",
		async: false,
		beforeSend: function ( xhr ) {
			$.mobile.loading('show');
		}
	}).done(function(data){
		$.mobile.changePage("#release");
	}).fail(function(xhr, textStatus, errorThrown){
		console.log("delete release fail");
		$.mobile.changePage("#viewrelease");
	}).always(function(){
		$.mobile.loading('hide');
	});
}