$(document).ready(function() {
	$(document).on("pageshow", "#backlog", function() {
		populateUserStories();
	});
});

var populateUserStories = function() {
	$.ajax({
		url: _ws + "/userstories/project/" + _selectedProject[0],
		type: "GET",
		dataType: "json",
		beforeSend: function ( xhr ) {
			$.mobile.loading('show');
		}
	}).done(function(json) {
		var userstoriesToDo = 0;
		var userstoriesAssigned = 0;
		var userstoriesInProgress = 0;
		var userstoriesCompleted = 0;
		$("#userstoriesList").empty();
		$.each(json, function(i, userstory) {
			var assignee = "n/a";
			if(userstory.assignee != null) {
				assignee = userstory.assignee.username;
			}
			var userstory_html = "<li><a href='#' class='' data-userstoryid='" + userstory.id + "'>" +
								"<h2>" + userstory.name + "</h2>" +
								"<p><strong>Business value:</strong> " + userstory.business_value + "</p>" +
								"<p><strong>Requested by:</strong> " + userstory.author.username + "</p>" +
								"<p><strong>Asigned to:</strong> " + assignee + "</p>" +
								"<span class='ui-li-count'>" + userstory.effort + "</span>" +
								"</a><a href=# class='' data-userstoryid='" + userstory.id + "'>Remove</a></li>";
			if(userstory.state == "to do") {
				$("#userstoriesToDoList").append(userstory_html);
				userstoriesToDo++;
			} else if (userstory.state == "assigned") {
				$("#userstoriesAssignedList").append(userstory_html);
				userstoriesAssigned++;
			} else if (userstory.state == "in progress") {
				$("#userstoriesInProgressList").append(userstory_html);
				userstoriesInProgress++;
			} else if (userstory.state == "completed") {
				$("#userstoriesCompletedList").append(userstory_html);
				userstoriesCompleted++;
			}
		});
		$("#userstoriesToDoCount").html(userstoriesToDo);
		$("#userstoriesAssignedCount").html(userstoriesAssigned);
		$("#userstoriesInProgressCount").html(userstoriesInProgress);
		$("#userstoriesCompletedCount").html(userstoriesCompleted);
		$("#userstoriesToDoList").listview("refresh");
		$("#userstoriesAssignedList").listview("refresh");
		$("#userstoriesInProgressList").listview("refresh");
		$("#userstoriesCompletedList").listview("refresh");
	}).fail(function() {
		alert("fail");
	}).always(function() {
		$.mobile.loading('hide');
	});
}