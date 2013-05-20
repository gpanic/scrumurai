$(document).ready(function() {
	$(document).on("pagebeforeshow", "#mytasks", function() {
		console.log("pagebeforeshow #mytasks");
		if(_selectedProject[0] != -1)
			getMyTasks(_selectedProject[0]);
		
	});
});

var getMyTasks = function(project_id){
	$.ajax({
		url: _ws + "/userstories/todo/"+ project_id,
		type: "GET",
		dataType: "json",
		beforeSend: function ( xhr ) {
			$.mobile.loading('show');
		}
	}).done(function(json) {
		console.log(json);
		if(json.length > 0){
			$.each(json,function(){
				$("#mytasks_list").append('<li data-corners="false" data-shadow="false" data-iconshadow="true" data-wrapperels="div" data-icon="arrow-r" data-iconpos="right" data-theme="c" class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-btn-hover-c"><div class="ui-btn-inner ui-li"><div class="ui-btn-text">'+
					'<a href="#" class="ui-link-inherit">'+
					'<h2 class="ui-li-heading">'+this.name+'</h2>'+
					'<p class="ui-li-desc"><strong>Description: </strong>'+((this.description != null) ? this.description:"/")+'</p>'+
					'<p class="ui-li-desc"><strong>Effort: </strong> '+this.effort+'</p>'+
					'<p class="ui-li-desc"><strong>Business Value: </strong>'+this.business_value+'</p>'+
					'<p class="ui-li-desc"><strong>Sprint: </strong>'+this.sprint.name+'</p>'+
					'</a>'+
					'</div><span class="ui-icon ui-icon-arrow-r ui-icon-shadow">&nbsp;</span></div></li>');
			});
		}
	}).fail(function() {
		console.log("fail");
	}).always(function() {
		$.mobile.loading('hide');
	});
}