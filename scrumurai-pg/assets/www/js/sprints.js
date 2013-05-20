$(document).ready(function() {

	$(document).on("pageshow", "#sprint", function() {
		populateSprints();
	});
});

var populateSprints = function() {
	$.ajax({
		url: _ws + "/sprints/project/" + _selectedProject[0],
		type: "GET",
		dataType: "json",
		beforeSend: function ( xhr ) {
			$.mobile.loading('show');
		}
	}).done(function(json) {
		$("#sprintsList").empty();
		var sprints = [];
		$.each(json, function(i, sprint) {
			var sprint_html = "<li data-sprintid='" + sprint.id + "'><a href='javascript:selectSprint(" + sprint.id + ")'>" +
								"<h2>" + sprint.name + "</h2>" +
								"<p><strong>Week:</strong> " + sprint.start_date + " - " + sprint.end_date + "</p>" +
								"<p><strong>Total effort:</strong> " + sprint.total_effort + "</p>" +
								"<p><strong>Progress:</strong> " + sprint.progress + "</p>" +
								"</a></li>";
			if(sprints[sprint.release.id] == undefined) {
				sprints[sprint.release.id] = [];
				sprints[sprint.release.id][1] = [];
			}
			sprints[sprint.release.id][0] = sprint.release.name;
			sprints[sprint.release.id][1].push(sprint_html);
		});
		$.each(sprints, function(i, release) {
			if(release != undefined) {
				$("#sprintsList").append("<li data-role='list-divider'>" + release[0] + "</li>");
				$.each(release[1], function(i, sprint) {
					$("#sprintsList").append(sprint);
				});
			}
		});
		$("#sprintsList").listview("refresh");
	}).fail(function() {
		alert("fail");
	}).always(function() {
		$.mobile.loading('hide');
	});
}