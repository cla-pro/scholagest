function getTeacherInfo(teacherKey) {
	var xhrArgs = {
			url: "http://localhost:8080/scholagest-app/services/teacher/getProperties",
			preventCache: true,
			content: {token: dojo.cookie("scholagest-token"),
				teacherKey: teacherKey},
				handleAs: "json",
				load: function(data) {
					if (data.errorCode == null) {
						var domId = "teacher-data";
						clearDOM(domId);

						var base = dojo.byId(domId);
						createInfoHtmlTable(base, data.info.properties);

						var save = dojo.create("button",
								{type: "button", onclick:"setTeacherInfo(\"" + teacherKey + "\")", innerHTML: "Enregistrer"}, base);
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
};

function setTeacherInfo(teacherKey) {
	var keyValues = getKeyValues('teacher-data');
	var xhrArgs = {
			url: "http://localhost:8080/scholagest-app/services/teacher/setProperties",
			preventCache: true,
			postData: dojo.toJson({
				token: dojo.cookie("scholagest-token"),
				object: {
					key: teacherKey,
					properties: keyValues
				}
			}),
			handleAs: "json",
			load: function(data) {
				if (data.errorCode != null) {
					alert("Error during setProperties: ("
							+ data.errorCode + ") " + data.message);
				}
			},
			error: function(error) {
				alert("error = " + error);
			}
	}

	var deferred = dojo.xhrPost(xhrArgs);
};

function selectTeacher(teacherKey) {
	return function(e) {
		var list = dojo.byId('teacher-search-list');
		var old = list.selectedTeacher;
		if (old != null)
			old.className = 'search-list-item';

		this.className = 'search-list-item-selected';
		list.selectedTeacher = this;

		getTeacherInfo(teacherKey);
	};
};

function getTeacherList(callback) {
	var xhrArgs = {
			url: "http://localhost:8080/scholagest-app/services/teacher/getTeachers",
			preventCache: true,
			content: {token: dojo.cookie("scholagest-token"),
				properties: ["pTeacherLastName", "pTeacherFirstName"] },
				handleAs: "json",
				load: function(data) {
					if (data.errorCode == null) {
						callback(data.info);
					}
					else {
						alert("Error during getTeachers: ("
								+ data.errorCode + ") " + data.message);
					}
				},
				error: function(error) {
					alert("error = " + error);
				}
	}

	var deferred = dojo.xhrGet(xhrArgs);
};

function loadTeachers() {
	getTeacherList(function(teachers) {
		clearDOM("teacher-search-list");

		var base = dojo.byId("teacher-search-list");
		createHtmlListFromList(teachers, "teacher-search-list", base,
				buildListItemTextClosure(["pTeacherLastName", "pTeacherFirstName"]), selectTeacher);
	});
};

function getTeachersInfo(teacherList, properties, callback) {
	var xhrArgs = {
			url: "http://localhost:8080/scholagest-app/services/teacher/getTeachersInfo",
			preventCache: true,
			content: {token: dojo.cookie("scholagest-token"),
				teachers: teacherList,
				properties: properties},
			handleAs: "json",
			load: function(data) {
				if (data.errorCode == null) {
					callback(data.info);
				}
				else {
					alert("Error during getTeachersInfo: ("
							+ data.errorCode + ") " + data.message);
				}
			},
			error: function(error) {
				alert("error = " + error);
			}
	}

	var deferred = dojo.xhrGet(xhrArgs);
};

function createTeacher(closeId, txtIds) {
	var keys = [];
	var values = [];
	for (var id in txtIds) {
		var node = dojo.byId(txtIds[id]);

		var value = node.value;
		var name = node.attributes.getNamedItem('propertyName').nodeValue;

		if (name != null && value != null) {
			keys.push(name);
			values.push(value);
		}
	}

	var xhrArgs = {
			url: "http://localhost:8080/scholagest-app/services/teacher/create",
			preventCache: true,
			content: {token: dojo.cookie("scholagest-token"),
				keys: keys, values: values},
				handleAs: "json",
				load: function(data) {
					if (data.errorCode == null) {
						loadTeachers();
					}
					else
						alert("Error during createTeachers: ("
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