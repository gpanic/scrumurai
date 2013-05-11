var _ws = "http://localhost:7659/scrumurai-ws/rest";
var _selectedProject = [-1, "No projects yet"];

$(document).ready(function() {
	
	$(document).on("pagebeforeshow", "#mytasks", function() {
		populateProjectsPopup();
	});
	
	$(document).on("pagebeforeshow", "#myprojects", function() {
		populateProjects();
	});
	
	$("#addProjectForm").submit(function() {
		createProject();
		return false;
	});
	
	$("#projectPopup ul").on("tap", "li", function() {
		if($(this).index() != 0 && $(this).index() != $(this).siblings().length) {
			_selectedProject[0] = $(this).attr("data-projectid");
			_selectedProject[1] = $(this).text();
			updateSelectedProject();
			$("#projectPopup").popup("close");
		}
	});
	
	$("#myProjectsList").on("tap", "a.removeProjectLink", function() {
		var pid = $(this).attr("data-projectid");
		deleteProject(pid);
		populateProjects();
	});

	$(".removeProjectLink").tap(function() {
		var pid = $(this).attr("data-projectid");
		deleteProject(pid);
		$.mobile.changePage("#myprojects");
		populateProjects();
	});

	$("#myProjectsList").on("tap", "a.viewProjectLink", function() {
		var pid = $(this).attr("data-projectid");
		$.mobile.changePage("#viewproject");
		populateViewProjectDrig(pid);
	});
	
	$(".editProjectLink").tap(function() {
		var pid = $(this).attr("data-projectid");
		$.mobile.changePage("#editproject");
		var project = getProject(pid);
		if(project != null) {
			$("#editProjectForm input[name='edit_project_name']").val(project.name);
			$("#editProjectForm textarea[name='edit_project_description']").val(project.description);
			$("#editProjectForm input[name='edit_project_velocity']").val(project.velocity);
			$("#editProjectForm").append("<input type='hidden' name='edit_project_id' value='" + pid + "'/>");
		}
	});

	$("#editProjectForm").submit(function() {
		editProject();
		return false;
	});

});

var populateProjectsPopup = function() {
	$.ajax({
		url: _ws + "/projects",
		type: "GET",
		dataType: "json",
		beforeSend: function ( xhr ) {
			$.mobile.loading('show');
		}
	}).done(function(json) {
		var projects = []
		$(".projectPopupList li:not(:first):not(:last)").remove();
		$.each(json, function(i, project) {
			projects.push("<li data-projectid='" + project.id + "'><a href='#'>" + project.name + "</a></li>");
		});
		$.each(projects.reverse(), function(i, project_html) {
			$(".projectPopupDivider").after(project_html);
		});
		$(".projectPopupList").listview("refresh");
		if(json[0] != undefined && json[0] != null && json[0].length != 0 && _selectedProject[0] == -1) {
			_selectedProject[0] = json[0].id;
			_selectedProject[1] = json[0].name;
		}
		updateSelectedProject();
	}).fail(function() {
		alert("fail");
	}).always(function() {
		$.mobile.loading('hide');
	});
};

var populateProjects = function() {
	$.ajax({
		url: _ws + "/projects",
		type: "GET",
		dataType: "json",
		beforeSend: function ( xhr ) {
			$.mobile.loading('show');
		}
	}).done(function(json) {
		var projects = []
		$("#myProjectsList").empty();
		$.each(json, function(i, project) {
			projects.push(
					"<li><a href='#' class='viewProjectLink' data-projectid='" + project.id + "'>" +
					"<h2>" + project.name + "</h2>" +
					"<p><strong>Description:</strong> " + project.description + "</p>" +
					"<p><strong>Velocity:</strong> " + project.velocity + "</p>" +
					"<p><strong>Owner:</strong> " + project.product_owner + "</p>" +
					"</a><a href=# class='removeProjectLink' data-projectid='" + project.id + "'>Remove</a></li>"
			);
		});
		$.each(projects.reverse(), function(i, project_html) {
			$("#myProjectsList").append(project_html);
		});
		$("#myProjectsList").listview("refresh");
	}).fail(function() {
		alert("fail");
	}).always(function() {
		$.mobile.loading('hide');
	});
}

var populateViewProjectDrig = function(id) {
	var project = getProject(id);
	if(project != null) {
		$(".viewProjectId").text(project.id);
		$(".viewProjectName").text(project.name);
		$(".viewProjectDescription").text(project.description);
		$(".viewProjectVelocity").text(project.velocity);
		$("#viewproject .removeProjectLink").attr("data-projectid", id);
		$("#viewproject .editProjectLink").attr("data-projectid", id);
	}
}

var createProject = function() {
	$("#submitAddProject").button("disable");
	
	var formResult = $("#addProjectForm").serializeObject();
	
	if (!formResult.add_project_name || !formResult.add_project_description || !formResult.add_project_velocity) {
		enableButton("#submitAddProject");
		return false;
	}
	
	formResult["name"] = formResult["add_project_name"];
	delete formResult["add_project_name"];
	
	formResult["description"] = formResult["add_project_description"];
	delete formResult["add_project_description"];
	
	formResult["velocity"] = formResult["add_project_velocity"];
	delete formResult["add_project_velocity"];
	
	$.ajax({
		url: _ws + "/projects",
		type: "POST",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(formResult),
		beforeSend: function ( xhr ) {
			$.mobile.loading('show');
		}
	}).done(function(json) {
		$("#addProjectForm").each (function() {
			this.reset();
		});
		enableButton("#submitAddProject");
		$.mobile.changePage("#myprojects");
	}).fail(function() {
		alert("fail");
	}).always(function() {
		$.mobile.loading('hide');
	});
}

var updateSelectedProject = function() {
	$(".selectedProject .ui-btn-text").text(_selectedProject[1]);
}

var deleteProject = function(id) {
	$.ajax({
		url: _ws + "/projects/" + id,
		type: "DELETE",
		async: false,
		beforeSend: function ( xhr ) {
			$.mobile.loading('show');
		}
	}).done(function() {
	}).fail(function() {
		alert("fail");
	}).always(function() {
		$.mobile.loading('hide');
	});
}

var getProject = function(id) {	
	var project = null;
	$.ajax({
	url: _ws + "/projects/" + id,
	type: "GET",
	async: false,
	dataType: "json",
	beforeSend: function ( xhr ) {
		$.mobile.loading('show');
	}
	}).done(function( json 	) {
		project = json;
	}).fail(function() {
		alert("fail");
	}).always(function() {
		$.mobile.loading('hide');
	});
	return project;
}

var editProject = function() {
	$("#submitEditProject").button("disable");
	
	var formResult = $("#editProjectForm").serializeObject();

	if (!formResult.edit_project_name || !formResult.edit_project_description || !formResult.edit_project_velocity) {
		enableButton("#submitEditProject");
		return false;
	}
	
	formResult["name"] = formResult["edit_project_name"];
	delete formResult["edit_project_name"];
	
	formResult["description"] = formResult["edit_project_description"];
	delete formResult["edit_project_description"];
	
	formResult["velocity"] = formResult["edit_project_velocity"];
	delete formResult["edit_project_velocity"];

	var id = formResult.edit_project_id;
	delete formResult["edit_project_id"];
	
	$.ajax({
		url: _ws + "/projects/" + id,
		type: "PUT",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(formResult),
		beforeSend: function ( xhr ) {
			$.mobile.loading('show');
		}
	}).done(function(json) {
		$("#editProjectForm").each (function() {
			this.reset();
		});
		enableButton("#submitEditProject");
		$.mobile.changePage("#viewproject");
		populateViewProjectDrig(id);
	}).fail(function() {
		alert("fail");
	}).always(function() {
		$.mobile.loading('hide');
	});

}
