var userid = "1";
var _selectedUserstory = -1;

$(document).ready(function() {

	$(document).on("pageshow", "#backlog", function() {
		populateUserStories();
	});

	$(document).on("pageshow", "#adduserstory", function() {
		$("#addUserstoryForm").each (function() {
			this.reset();
		});
		populateSprintSelect();
	});

	$("#addUserstoryForm").submit(function () {
		createUserStory();
		return false;
	});

	$(document).on("pagebeforeshow", "#userstorystatedialog", function() {
		var userstory = getUserstory(_selectedUserstory);
		console.log(userstory.state);
		$("#userStoryStateDialogList li").show();
		$("#userStoryStateDialogList li").each(function() {
			var child = $(this).find("a");
			if(userstory.state == child.html().toLowerCase()) {
				$(this).hide();
			}
		});
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
		$("#userstoriesToDoList").empty();
		$("#userstoriesAssignedList").empty();
		$("#userstoriesInProgressList").empty();
		$("#userstoriesCompletedList").empty();
		$.each(json, function(i, userstory) {
			var assignee = "n/a";
			if(userstory.assignee != null) {
				assignee = userstory.assignee.username;
			}
			var sprint = "n/a"; 
			if(userstory.sprint != null) {
				sprint = userstory.sprint.name;
			}
			var userstory_html = "<li data-userstoryid='" + userstory.id + "'><a href='javascript:selectUserstory(" + userstory.id + ")' class=''>" +
								"<h2>" + userstory.name + "</h2>" +
								"<p><strong>Business value:</strong> " + userstory.business_value + "</p>" +
								"<p><strong>Requested by:</strong> " + userstory.author.username + "</p>" +
								"<p><strong>Asigned to:</strong> " + assignee + "</p>" +
								"<p><strong>Sprint:</strong> " + sprint + "</p>" +
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

var populateSprintSelect = function() {
	$.ajax({
		url: _ws + "/sprints/project/" + _selectedProject[0],
		type: "GET",
		dataType: "json",
		beforeSend: function ( xhr ) {
			$.mobile.loading('show');
		}
	}).done(function(json) {
		$("#add_userstory_sprint").empty();
		$.each(json, function(i, sprint) {
			$("#add_userstory_sprint").append("<option value='" + sprint.id + "'>" + sprint.name + "</option>");
		});
		$("#add_userstory_sprint").selectmenu("enable");
		$("#add_userstory_sprint").selectmenu("refresh");
	}).fail(function() {
		$("#add_userstory_sprint").empty();
		$("#add_userstory_sprint").append("<option value='-1	'>No sprints</option>");
		$("#add_userstory_sprint").selectmenu("disable");
		$("#add_userstory_sprint").selectmenu("refresh");
	}).always(function() {
		$.mobile.loading('hide');
	});
}

var createUserStory = function () {
	$("#submitAddUserstory").button("disable");

	var formResult = $("#addUserstoryForm").serializeObject();
	if (!formResult.add_userstory_name || formResult.add_userstory_sprint == null) {
		enableButton("#submitAddUserstory");
		return false;
	}

	var userstory = {
		name: formResult.add_userstory_name,
		description: formResult.add_userstory_description,
		effort: formResult.add_userstory_effort,
		business_value: formResult.add_userstory_bv,
		state: "to do",
		author: {
			id: userid
		},
		project: {
			id: _selectedProject[0]
		},
		sprint: {
			id: formResult.add_userstory_sprint
		}
	};

	$.ajax({
		url: _ws + "/userstories",
		type: "POST",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(userstory),
		beforeSend: function ( xhr ) {
			$.mobile.loading('show');
		}
	}).done(function(json) {
		$("#addUserstoryForm").each (function() {
			this.reset();
		});
		enableButton("#submitAddUserstory");
		$.mobile.changePage("#backlog");
	}).fail(function() {
		alert("fail");
	}).always(function() {
		$.mobile.loading('hide');
	});
}

var selectUserstory = function(id) {
	if (id != _selectedUserstory) {
		$("#userstoriesList>div>div>ul>li").each(function() {
			$(this).attr("data-theme", "d").removeClass("ui-btn-up-b").removeClass('ui-btn-hover-b').addClass("ui-btn-up-d").addClass('ui-btn-hover-d');
		});
		$("#userstoriesList>div>div>ul>li[data-userstoryid=" + id + "]").attr("data-theme", "b").removeClass("ui-btn-up-d").removeClass('ui-btn-hover-d').addClass("ui-btn-up-b").addClass('ui-btn-hover-b');
		_selectedUserstory = id;
		$(".userstory-menu-options").show();
	} else {
		$("#userstoriesList>div>div>ul>li[data-userstoryid=" + id + "]").attr("data-theme", "d").removeClass("ui-btn-up-b").removeClass('ui-btn-hover-b').addClass("ui-btn-up-d").addClass('ui-btn-hover-d');
		_selectedUserstory = -1;
		$(".userstory-menu-options").hide();
	}
}

var getUserstory = function(id) {
	var userstory = null;
	$.ajax({
	url: _ws + "/userstories/" + id,
	type: "GET",
	async: false,
	dataType: "json",
	beforeSend: function ( xhr ) {
		$.mobile.loading('show');
	}
	}).done(function( json 	) {
		userstory = json;
	}).fail(function() {
		alert("fail");
	}).always(function() {
		$.mobile.loading('hide');
	});
	return userstory;
}

var updateUserstoryState = function(state_new) {
	if(_selectedUserstory != -1) {
		$.ajax({
			url: _ws + "/userstories/" + _selectedUserstory + "/state",
			type: "PUT",
			contentType: "application/json; charset=utf-8",
			data: JSON.stringify({
				state: state_new
			}),
			beforeSend: function ( xhr ) {
				$.mobile.loading('show');
			}
		}).done(function() {
			_selectedUserstory = -1;
			$("#userstorystatedialog").dialog("close");
		}).fail(function() {
			alert("fail");
		}).always(function() {
			$.mobile.loading('hide');
		});
	}
}