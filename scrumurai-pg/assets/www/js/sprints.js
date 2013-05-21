var _selectedSprint = -1;

$(document).ready(function() {

	$(document).on("pageshow", "#sprint", function() {
		_selectedSprint = -1;
		populateSprints();
	});

	$(document).on("pageshow", "#addsprint", function() {
		$("#addSprintForm").each (function() {
			this.reset();
		});
		populateReleaseSelect("#add_sprint_release");
	});

	$("#addSprintForm").submit(function () {
		createSprint();
		return false;
	});

	$(document).on("pageshow", "#editsprint", function() {
		$("#editSprintForm").each (function() {
			this.reset();
		});
		populateReleaseSelect("#edit_sprint_release");
		populateEditForm();
	});

	$("#editSprintForm").submit(function () {
		updateSprint();
		return false;
	})

	$("#deletesprint").tap(function() {
		deleteSprint();
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
			var start = new Date(sprint.start_date);
			var end = new Date(sprint.end_date);
			var start_str = start.getUTCMonth() + "/" + start.getUTCDate() + "/" + start.getUTCFullYear();
			var end_str = end.getUTCMonth() + "/" + end.getUTCDate() + "/" + end.getUTCFullYear();
			var sprint_html = "<li data-sprintid='" + sprint.id + "'><a href='javascript:selectSprint(" + sprint.id + ")'>" +
								"<h2>" + sprint.name + "</h2>" +
								"<p><strong>Week:</strong> " + start_str + " - " + end_str + "</p>" +
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

var populateReleaseSelect = function(field) {
	$.ajax({
		url: _ws + "/releases/proj/" + _selectedProject[0],
		type: "GET",
		dataType: "json",
		async: false,
		beforeSend: function ( xhr ) {
			$.mobile.loading('show');
		}
	}).done(function(json) {
		$(field).empty();
		$.each(json, function(i, release) {
			$(field).append("<option value='" + release.id + "'>" + release.name + "</option>");
		});
		$(field).selectmenu("enable");
		$(field).selectmenu("refresh");
	}).fail(function() {
		$(field).empty();
		$(field).append("<option value='-1'>No sprints</option>");
		$(field).selectmenu("disable");
		$(field).selectmenu("refresh");
	}).always(function() {
		$.mobile.loading('hide');
	});
}

var createSprint = function () {
	$("#submitAddSprint").button("disable");

	var formResult = $("#addSprintForm").serializeObject();
	if (!formResult.add_sprint_name || formResult.add_sprint_start == null || _user == undefined) {
		enableButton("#submitAddSprint");
		return false;
	}
	var start = new Date(formResult.add_sprint_start);
	var end = new Date(formResult.add_sprint_start);
	end.setDate(end.getDate() + 4);

	var sprint = {
		name: formResult.add_sprint_name,
		start_date: start.getUTCFullYear() + "-" + (start.getUTCMonth() + 1) + "-" + (start.getUTCDate() + 1),
		end_date: end.getUTCFullYear() + "-" + (end.getUTCMonth() + 1) + "-" + (end.getUTCDate() + 1),
		project: {
			id: _selectedProject[0]
		},
		release: {
			id: formResult.add_sprint_release
		}
	};

	$.ajax({
		url: _ws + "/sprints",
		type: "POST",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(sprint),
		beforeSend: function ( xhr ) {
			$.mobile.loading('show');
		}
	}).done(function(json) {
		$("#addSprintForm").each (function() {
			this.reset();
		});
		enableButton("#submitAddSprint");
		$.mobile.changePage("#sprint");
	}).fail(function() {
		alert("fail");
	}).always(function() {
		$.mobile.loading('hide');
	});
}

var selectSprint = function(id) {
	if (id != _selectedSprint) {
		$("#sprintsList>li:not([data-role='list-divider'])").each(function() {
			$(this).attr("data-theme", "d").removeClass("ui-btn-up-b").removeClass('ui-btn-hover-b').addClass("ui-btn-up-d").addClass('ui-btn-hover-d');
		});
		$("#sprintsList>li[data-sprintid=" + id + "]").attr("data-theme", "b").removeClass("ui-btn-up-d").removeClass('ui-btn-hover-d').addClass("ui-btn-up-b").addClass('ui-btn-hover-b');
		_selectedSprint = id;
		$(".sprint-menu-options").show();
	} else {
		$("#sprintsList>li[data-sprintid=" + id + "]").attr("data-theme", "d").removeClass("ui-btn-up-b").removeClass('ui-btn-hover-b').addClass("ui-btn-up-d").addClass('ui-btn-hover-d');
		_selectedSprint = -1;
		$(".sprint-menu-options").hide();
	}
}

var populateEditForm = function() {
	var sprint = getSprint(_selectedSprint);
	$("#edit_sprint_name").val(sprint.name);
	var start = new Date(sprint.start_date);
	var start_str = start.getUTCDate() + "/" + start.getUTCMonth() + "/" + start.getUTCFullYear();
	$("#edit_sprint_start").val(start_str);
	$('#edit_sprint_release').val($("#edit_sprint_release option[value='" + sprint.release.id + "'']").val());
}

var getSprint = function(id) {
	var sprint = null;
	$.ajax({
	url: _ws + "/sprints/" + id,
	type: "GET",
	async: false,
	dataType: "json",
	beforeSend: function ( xhr ) {
		$.mobile.loading('show');
	}
	}).done(function( json 	) {
		sprint = json;
	}).fail(function() {
		alert("fail");
	}).always(function() {
		$.mobile.loading('hide');
	});
	return sprint;
}

var updateSprint = function() {
	$("#submitEditSprint").button("disable");

	var formResult = $("#editSprintForm").serializeObject();
	if (!formResult.edit_sprint_name || formResult.edit_sprint_start == null || _user == undefined) {
		enableButton("#submitEditSprint");
		return false;
	}

	var start = new Date(formResult.edit_sprint_start);
	var end = new Date(formResult.edit_sprint_start);
	end.setDate(end.getDate() + 4);

	var sprint = {
		name: formResult.edit_sprint_name,
		start_date: start.getUTCFullYear() + "-" + (start.getUTCMonth() + 1) + "-" + (start.getUTCDate() + 1),
		end_date: end.getUTCFullYear() + "-" + (end.getUTCMonth() + 1) + "-" + (end.getUTCDate() + 1),
		release: {
			id: formResult.edit_sprint_release
		}
	};


	$.ajax({
		url: _ws + "/sprints/" + _selectedSprint + "/details",
		type: "PUT",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(sprint),
		beforeSend: function ( xhr ) {
			$.mobile.loading('show');
		}
	}).done(function(json) {
		$("#editSprintForm").each (function() {
			this.reset();
		});
		enableButton("#submitEditSprint");
		_selectedSprint = -1;
		$.mobile.changePage("#sprint");
	}).fail(function() {
		alert("fail");
	}).always(function() {
		$.mobile.loading('hide');
	});
}

var deleteSprint = function() {
	$.ajax({
		url: _ws + "/sprints/" + _selectedSprint,
		type: "DELETE",
		beforeSend: function ( xhr ) {
			$.mobile.loading('show');
		}
	}).done(function() {
		populateSprints();
	}).fail(function() {
		alert("fail");
	}).always(function() {
		$.mobile.loading('hide');
	});
}