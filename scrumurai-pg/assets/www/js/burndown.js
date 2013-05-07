$(function () {
  $('div[data-role*="navbar"]').each(function () {
    $(this).addClass("navbargraph");
  });


  $(".navbargraph").find("a:eq(1)").on('click', function () {
    var project_id;
    if (!project_id) {
      $("#errorpage_msg").text("Napaka");
      $.mobile.changePage("#errorpage");
      return false;
    }
    var found_releases = fillSelectRelease(project_id);
    return false;
  });
});

var fillSelectRelease = function (project_id) {
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
  select_release.selectmenu('refresh', true);
}).fail(function (xhr, textStatus, errorThrown) {
  console.log("releases_proj_fail");
}).always(function () {
  $.mobile.loading('hide');
});
return found_releases;
}
