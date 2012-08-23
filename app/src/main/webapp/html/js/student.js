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
function createInfoHtml(info) {
	for (var i in info) {
		var data = info[i];
		
		var tr = dojo.create("tr", {}, table);

		dojo.create("td", {
			innerHTML: info[i].displayText,
			style: "width: 150px"
		}, tr);
		var cell = dojo.create("td", {}, tr);
		var txt = dojo.create("input", {
			style: "width: 100%",
			type: "text",
			value: info[i].value,
		}, cell);
		txt.key = i;
	}

	var save = dojo.create("button",
			{type: "button", onclick:"setStudentInfo(\"" + studentKey + "\")"}, base);
}
function getStudentInfo(studentKey) {
	var xhrArgs = {
			url: "http://localhost:8080/scholagest-app/services/student/getProperties",
			preventCash: true,
			content: {token: dojo.cookie("scholagest-token"),
				teacherKey: studentKey,
				properties: ["pStudentFirstName", "pStudentLastName"]},
				handleAs: "json",
				load: function(data) {
					if (data.errorCode == null) {
						clearDOM("student-data");

						var base = dojo.byId('student-data');
						dojo.create("h1", {
							innerHTML: ''}, base);
						var table = dojo.create("table", {
							style: "width: 100%"}, base);
						table.id = "student-data-table";

						var info = data.info;
						for (var i in info) {
							var tr = dojo.create("tr", {}, table);

							dojo.create("td", {
								innerHTML: info[i].displayText,
								style: "width: 150px"
							}, tr);
							var cell = dojo.create("td", {}, tr);
							var txt = dojo.create("input", {
								style: "width: 100%",
								type: "text",
								value: info[i].value,
							}, cell);
							txt.key = i;
						}

						var save = dojo.create("button",
								{type: "button", onclick:"setStudentInfo(\"" + studentKey + "\")"}, base);
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