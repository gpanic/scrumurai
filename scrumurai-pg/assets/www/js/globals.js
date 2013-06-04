var _ws = "http://svizec.informatika.uni-mb.si:8080/scrumurai-ws-1/rest";
//var _ws = "http://localhost:7659/scrumurai-ws/rest";

/* PROJECTS */

// Selected project on all the pages displayed in the header
var _selectedProject = [-1, "No projects yet"];
// Currently viewed project, set when user views the details of a project
var _currentProject = null;

//is set when user is logged in
var _user = null;

// Currently viewed release, set in #relese, used in #viewrelease
var _currentRelease = null;


//currently selected user story
var _currentTask = null;