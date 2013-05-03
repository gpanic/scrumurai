var _ws = "http://localhost:7659/scrumurai-ws/rest";

$(document).on("pagebeforeshow", "#mytasks", function() {
	populateProjectsPopup();
});

$(document).on("pagebeforeshow", "#myprojects", function() {
	populateProjects();
});

var populateProjectsPopup = function() {
	$.ajax({
		url: _ws + "/projects",
		type: "GET",
		dataType: "json"
	}).done(function(json) {
		var projects = []
		$(".projectPopupList li:not(:first):not(:last)").remove();
		$.each(json, function(i, project) {
			projects.push("<li><a href='#'>" + project.name + "</a></li>");
		});
		$.each(projects.reverse(), function(i, project_html) {
			$(".projectPopupDivider").after(project_html);
		});
		$(".projectPopupList").listview("refresh");
	}).fail(function() {
		alert("fail");
	}).always(function() {
	});
};

var populateProjects = function() {
	$.ajax({
		url: _ws + "/projects",
		type: "GET",
		dataType: "json"
	}).done(function(json) {
		var projects = []
		$("#myprojects > div[data-role=content] > ul[data-role=listview]").empty();
		$.each(json, function(i, project) {
			projects.push(
					"<li><a href='#'>" +
					"<h2>" + project.name + "</h2>" +
					"<p><strong>Description:</strong> " + project.description + "</p>" +
					"<p><strong>Velocity:</strong> " + project.velocity + "</p>" +
					"<p><strong>Owner:</strong> " + project.product_owner + "</p>" +
					"</a><a href=#>Remove</a></li>"
			);
		});
		$.each(projects.reverse(), function(i, project_html) {
			$("#myprojects > div[data-role=content] > ul[data-role=listview]").append(project_html);
		});
		$("#myprojects > div[data-role=content] > ul[data-role=listview]").listview("refresh");
	}).fail(function() {
		alert("fail");
	}).always(function() {
	});
}