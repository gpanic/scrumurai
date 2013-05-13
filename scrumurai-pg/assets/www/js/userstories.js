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
		console.log(json)
		var userstories = []
		$("#userstoriesList").empty();
		$.each(json, function(i, userstory) {
			var assignee = "n/a";
			if(userstory.assignee != null) {
				assignee = userstory.assignee.username;
			}
			userstories.push(
					"<li><a href='#' class='' data-userstoryid='" + userstory.id + "'>" +
					"<h2>" + userstory.name + "</h2>" +
					"<p><strong>Business value:</strong> " + userstory.business_value + "</p>" +
					"<p><strong>Requested by:</strong> " + userstory.author.username + "</p>" +
					"<p><strong>Asigned to:</strong> " + assignee + "</p>" +
					"<span class='ui-li-count'>" + userstory.effort + "</span>" +
					"</a><a href=# class='' data-userstoryid='" + userstory.id + "'>Remove</a></li>"
			);
		});
		$.each(userstories.reverse(), function(i, userstory_html) {
			$("#userstoriesList").append(userstory_html);
		});
		$("#userstoriesList").listview("refresh");
	}).fail(function() {
		alert("fail");
	}).always(function() {
		$.mobile.loading('hide');
	});
}