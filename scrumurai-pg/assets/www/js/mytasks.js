$(document).ready(function() {
	$(document).on("pageshow", "#mytasks", function() {
		console.log(_selectedProject[0]);
		console.log("pagebeforeshow #mytasks");
		if(_selectedProject[0] != -1 && _user != null)
			getMyTasks(_selectedProject[0], _user.id);
	});
});

var getMyTasks = function(project_id,user_id){
	$.ajax({
		url: _ws + "/userstories/todo/"+ project_id+"/"+user_id,
		type: "GET",
		dataType: "json",
		beforeSend: function ( xhr ) {
			$.mobile.loading('show');
		}
	}).done(function(json) {
		if(json.length > 0){
			$("#mytasks_list").empty();
			$.each(json,function(){
				$("#mytasks_list").append('<li data-corners="false" data-shadow="false" data-iconshadow="true" data-wrapperels="div" data-theme="c" class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-btn-hover-c"><div class="ui-btn-inner ui-li"><div class="ui-btn-text">'+
					'<a href="#" class="ui-link-inherit" data-taskid="'+this.id+'" href="javascript:selectTask(' + this.id + ')">'+
					'<h2 class="ui-li-heading">'+this.name+'</h2>'+
					'<p class="ui-li-desc"><strong>Description: </strong>'+((this.description != null) ? this.description:"/")+'</p>'+
					'<p class="ui-li-desc"><strong>Effort: </strong> '+this.effort+'</p>'+
					'<p class="ui-li-desc"><strong>Business Value: </strong>'+this.business_value+'</p>'+
					'<p class="ui-li-desc"><strong>Sprint: </strong>'+this.sprint.name+'</p>'+
					'</a></li>');
			});
		}
	}).fail(function() {
		console.log("fail");
	}).always(function() {
		$.mobile.loading('hide');
	});
}

var selectTask = function(task_id){
	// _currentTask = task_id;
	// $.mobile.changePage("#viewtask");
}