$(function () {
  $('div[data-role*="navbar"]').each(function () {
    $(this).addClass("navbargraph");
  });

  $(document).on("pagebeforeshow","#burndown",  function() {    

    if (_selectedProject[0] == -1){
      redirectError("You have no selected project.");
    }else if(!fillBurndownSelectRelease(_selectedProject[0])){
      redirectError("You have no releases.");
    }else{
      fillVelocity(_selectedProject[0]);

      if($("#burndown_select_release").val())
        generateGraph($("#burndown_select_release").val());
    }
    return false;
  });


  $("#burndown_select_release").change(function(){
    if($("#burndown_select_release").val())
      generateGraph($("#burndown_select_release").val());
  });

});

var fillBurndownSelectRelease = function (project_id) {
  $('#burndown_select_release').find('option').remove().end();
  var found_releases = false;
  $.ajax({
    url: _ws + "/releases/proj/" + project_id,
    type: "GET",
    dataType: "json",
    async: false,
    beforeSend: function (xhr) {
      $.mobile.loading('show');
    }
  }).done(function (data) {
    found_releases = true;
    var select_release = $("#burndown_select_release");
    //dodamo fielde
    $.each(data, function () {
      select_release.append($("<option />").val(this.id).text(this.name));
    });

  //nastavimo zadnjega kot izbranega
  $('#burndown_select_release option:last-child').prop("selected", true);
  //posodobimo select, da se prikaže izbrana vrednost
  
  select_release.selectmenu('refresh',true);
}).fail(function (xhr, textStatus, errorThrown) {
  console.log("releases_proj_fail");
}).always(function () {
  $.mobile.loading('hide');
});
return found_releases;
}



var fillVelocity = function(project_id){
  var project = getProject(project_id);
  $("#burndown_velocity").text("Velocity: "+project.velocity);
}


var getUserStories = function(release_id){
  var d;
  $.ajax({
    url: _ws + "/userstories/release/" + release_id,
    type: "GET",
    dataType: "json",
    async: false,
    beforeSend: function (xhr) {
      $.mobile.loading('show');
    }
  }).done(function (data) {
    d = data;
  }).fail(function (xhr, textStatus, errorThrown) {
    console.log("get user stories based on release fail");
  }).always(function () {
    $.mobile.loading('hide');
  });
  return d;
}

var getReleaseDataTotal = function(release_id){
  var d;
  $.ajax({
    url: _ws + "/releases/datatotal/" + release_id,
    type: "GET",
    dataType: "json",
    async: false,
    beforeSend: function (xhr) {
      $.mobile.loading('show');
    }
  }).done(function (data) {
    d = data;
  }).fail(function (xhr, textStatus, errorThrown) {
    console.log("get release data total fail");
  }).always(function () {
    $.mobile.loading('hide');
  });
  return d;
}

var getDataTotal = function(release_id){
  var release = getReleaseDataTotal(release_id);
  if(release)
    return [{"date": release.start_date, "effort": release.total_effort },{"date": release.end_date , "effort": 0 }];
  else
    return null;
}

var getData = function(release_id, data_top){
  var userstories = getUserStories(release_id);
  var json = [];
  json.push(data_top);
  total_effort = data_top.effort;
  if(!userstories)
    return false;
  for(var a = 0;a < userstories.length;a++){
   if(userstories[a].end_date){
    total_effort -= userstories[a].effort;
    json.push({date: userstories[a].end_date, effort: total_effort});
  }
}
return json;
}

var generateGraph = function(release_id){
  $("#graph").html("");
  // define dimensions of graph
  var m = [20, 5, 160, 75]; // margins
  var w = 800 - m[1] - m[3]; // width
  var h = 600 - m[0] - m[2]; // height

  var format = d3.time.format("%Y-%m-%d");

  function getDate(d) {
    return format.parse(d.date);
  }

  function daydiff(first, second) {
    d = getDate(first);
    d.setDate(d.getDate() - 1);
    return (getDate(second) - d) / (1000 * 60 * 60 * 24);
  }

  // create a simple data array that we'll plot with a line (this array represents only the Y values, X will just be the index location)
  var data_total = getDataTotal(release_id);
  if(!data_total){
    console.log("dt false");
    return false;
  }
  var data = getData(release_id,data_total[0]);

  if(!data){
    $("#graph").html("<p id='errorpage_msg'>Your sprint does not have any finished user stories.</p>");
    return false;
  }
  //grid lines
  function make_x_axis() {
    return d3.svg.axis()
    .scale(x)
    .orient("bottom")
    .ticks(5)
  }

  function make_y_axis() {
    return d3.svg.axis()
    .scale(y)
    .orient("left")
    .ticks(5)
  }

  var end_date;
  if(data[data.length-1].date > data_total[1].date)
    end_date = data[data.length-1];
  else
    end_date = data_total[1];
  // X scale starts at epoch time 1335035400000, ends at 1335294600000 with 300s increments
  var x = d3.time.scale().domain([getDate(data[0]), getDate(end_date)]).range([0, w]);

  // Y scale will fit values from 0-10 within pixels h-0 (Note the inverted domain for the y-scale: bigger is up!)
  var y = d3.scale.linear().domain([data_total[1].effort, data_total[0].effort]).range([h, 0]);

  // create a line function that can convert data[] into x and y points
  var line = d3.svg.line()
  // assign the X function to plot our line as we wish
  .x(function (d, i) {
      // verbose logging to show what's actually being done
      //console.log('Plotting X value for data point: ' + d + ' using index: ' + i + ' to be at: ' + x(i) + ' using our xScale.');
      // return the X coordinate where we want to plot this datapoint
      return x(getDate(d));
    })
  .y(function (d) {
      // verbose logging to show what's actually being done
      //console.log('Plotting Y value for data point: ' + d + ' to be at: ' + y(d) + " using our yScale.");
      // return the Y coordinate where we want to plot this datapoint
      return y(d.effort); // use the 1st index of data (for example, get 20 from [20,13])
    })

  // Add an SVG element with the desired dimensions and margin.
  var graph = d3.select("#graph").append("svg:svg")
  .attr("width", w + m[1] + m[3])
  .attr("height", h + m[0] + m[2])
  .attr('id', 'chart')
  .attr('viewBox', "0 0 800 600")
  .attr('perserveAspectRatio', 'xMinYMid')
  .append("svg:g")
  .attr("transform", "translate(" + m[3] + "," + m[0] + ")");

  // create yAxis
  var xAxis;
  if(daydiff(data[0], end_date) > 10)
    xAxis = d3.svg.axis().scale(x).ticks(d3.time.days, 2).tickFormat(d3.time.format("%d.%m.%Y"));
  else
    xAxis = d3.svg.axis().scale(x).ticks(d3.time.days, 1).tickFormat(d3.time.format("%d.%m.%Y"));

  //grid lines
  graph.append("g")
  .attr("class", "grid")
  .attr("transform", "translate(0," + h + ")")
  .call(make_x_axis()
    .tickSize(-h, 0, 0)
    .tickFormat(""))

  graph.append("g")
  .attr("class", "grid")
  .call(make_y_axis()
    .tickSize(-w, 0, 0)
    .tickFormat(""))

  // Add the x-axis.
  graph.append("svg:g")
  .attr("class", "x axis")
  .attr("transform", "translate(0," + h + ")")
  .call(xAxis)
  .selectAll("text")
  .style("text-anchor", "end")
  .attr("dx", "-.8em")
  .attr("dy", ".15em")
  .attr("transform", function (d) {
    return "rotate(-65)"
  });

  // create left yAxis
  var yAxisLeft = d3.svg.axis().scale(y).ticks(data_total[0].effort / 5).orient("left");
  // Add the y-axis to the left
  graph.append("svg:g")
  .attr("class", "y axis")
  .attr("transform", "translate(-10,0)")
  .call(yAxisLeft);

  // add lines
  // do this AFTER the axes above so that the line is above the tick-lines
  graph.append("svg:path").attr("d", line(data_total)).attr("class", "data2");

  graph.append("svg:path").attr("d", line(data)).attr("class", "data1");

  graph.selectAll("circle")
  .data(data)
  .enter().append("svg:circle")
  .attr("cx", function (d) {
    return x(getDate(d))
  })
  .attr("cy", function (d) {
    return y(parseInt(d.effort));
  })
  .attr("r", 3);

  setTimeout(function(){
   var the_chart = $("#chart"),
   aspect = the_chart.width() / the_chart.height(),
   container = the_chart.parent();

   var targetWidth = container.width();
   the_chart.attr("width", targetWidth);
   the_chart.attr("height", Math.round(targetWidth / aspect));}, 500);
}

$(window).on("resize", function () {
  var the_chart = $("#chart"),
  aspect = the_chart.width() / the_chart.height(),
  container = the_chart.parent();

  var targetWidth = container.width();
  the_chart.attr("width", targetWidth);
  the_chart.attr("height", Math.round(targetWidth / aspect));
}).trigger("resize");
