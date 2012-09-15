function createStudent(closeId, txtIds) {
	var keyValues = getKeysAndValues(txtIds);

	var xhrArgs = {
			url: "http://localhost:8080/scholagest-app/services/student/create",
			preventCash: true,
			content: {token: dojo.cookie("scholagest-token"),
				keys: keyValues.keys, values: keyValues.values},
				handleAs: "json",
				load: function(data) {
					if (data.errorCode == null) {
						loadStudents();
					}
					else
						alert("Error during createStudents: ("
								+ data.errorCode + ") " + data.message);
				},
				error: function(error) {
					alert("error = " + error);
				}
	}

	var deferred = dojo.xhrGet(xhrArgs);

	if (closeId != null) {
		dijit.byId(closeId).hide();
	}
};

function loadStudents() {
	var xhrArgs = {
			url: "http://localhost:8080/scholagest-app/services/student/getStudents",
			preventCash: true,
			content: {token: dojo.cookie("scholagest-token"),
				properties: ["pStudentLastName", "pStudentFirstName"] },
				handleAs: "json",
				load: function(data) {
					if (data.errorCode == null) {
						clearDOM("student-search-list");

						var base = dojo.byId("student-search-list");
						var list = data.students;
						createHtmlListFromList(list, "student-search-list", base,
								buildListItemTextClosure(["pStudentLastName", "pStudentFirstName"]), selectStudent);
					}
					else
						alert("Error during getStudents: ("
								+ data.errorCode + ") " + data.message);
				},
				error: function(error) {
					alert("error = " + error);
				}
	}

	var deferred = dojo.xhrGet(xhrArgs);
}

function selectStudent(studentKey) {
	return function(e) {
		var list = dojo.byId('student-search-list');
		var old = list.selectedStudent;
		if (old != null)
			old.className = 'search-list-item';

		this.className = 'search-list-item-selected';
		list.selectedStudent = this;

		getStudentInfo(studentKey);
	};
}
function createHtmlGroupTitleBar(parentDOM, title) {
	var titleBar = dojo.create("div", {className: "student-part-title"}, parentDOM);
	dojo.create("a", {innerHTML: title}, titleBar);
	dojo.create("button", {className: "student-part-button", innerHTML: "Fermer"}, titleBar);
}
function createHtmlGroupContent(parentDOM, groupId, contentAsJson) {
	var div = dojo.create("div", {className: "student-part"}, parentDOM);
	var table = dojo.create("table", {style: "width: 100%"}, div);
	
	createInfoHtml(table, contentAsJson);
}
function createHtmlLabelText(parentTable, propertyName, displayText, value) {
	var tr = dojo.create("tr", {}, parentTable);

	dojo.create("td", {
		innerHTML: displayText,
		style: "width: 150px"
	}, tr);
	var cell = dojo.create("td", {}, tr);
	var txt = dojo.create("input", {
		style: "width: 100%",
		type: "text",
		value: value,
	}, cell);
	txt.key = propertyName;
}
function createHtmlGroup(parentDOM, title, contentAsJson) {
	createHtmlGroupTitleBar(parentDOM, title);
	createHtmlGroupContent(parentDOM, "", contentAsJson);
}
function createInfoHtml(parentDOM, info) {
	for (var i in info) {
		var data = info[i];
		
		if (data.isHtmlGroup != null && data.isHtmlGroup == true) {
			createHtmlGroup(parentDOM, data.displayText, data.value);
		}
		else {
			createHtmlLabelText(parentDOM, i, data.displayText, data.value);
		}
	}
}
function getStudentInfo(studentKey) {
	var xhrArgs = {
			url: "http://localhost:8080/scholagest-app/services/student/getProperties",
			preventCash: true,
			content: {token: dojo.cookie("scholagest-token"),
				studentKey: studentKey,
				properties: ["pStudentFirstName", "pStudentLastName"]},
				handleAs: "json",
				load: function(data) {
					if (data.errorCode == null) {
						var domId = "student-data";
						clearDOM(domId);
						
						var base = dojo.byId(domId);
						createInfoHtml(base, data.info);

						var save = dojo.create("button",
								{type: "button", onclick:"setStudentInfo(\"" + studentKey + "\")", innerHTML: "Enregistrer"}, base);
					}
					else {
						alert("Error during getProperties: ("
								+ data.errorCode + ") " + data.message);
					}
				},
				error: function(error) {
					alert("error = " + error);
				}
	}

	var deferred = dojo.xhrGet(xhrArgs);
}