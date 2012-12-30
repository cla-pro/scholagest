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
function getStudentList(callback) {
	var xhrArgs = {
			url: "http://localhost:8080/scholagest-app/services/student/getStudents",
			preventCash: true,
			content: {token: dojo.cookie("scholagest-token"),
				properties: ["pStudentLastName", "pStudentFirstName"] },
				handleAs: "json",
				load: function(data) {
					if (data.errorCode == null) {
						callback(data.info);
					}
					else {
						alert("Error during getStudents: ("
								+ data.errorCode + ") " + data.message);
					}
				},
				error: function(error) {
					alert("error = " + error);
				}
	}

	var deferred = dojo.xhrGet(xhrArgs);
};
function getStudentsInfo(studentList, properties, callback) {
	var xhrArgs = {
			url: "http://localhost:8080/scholagest-app/services/student/getStudentsInfo",
			preventCash: true,
			content: {token: dojo.cookie("scholagest-token"),
				students: studentList,
				properties: properties},
			handleAs: "json",
			load: function(data) {
				if (data.errorCode == null) {
					callback(data.info);
				}
				else {
					alert("Error during getStudents: ("
							+ data.errorCode + ") " + data.message);
				}
			},
			error: function(error) {
				alert("error = " + error);
			}
	}

	var deferred = dojo.xhrGet(xhrArgs);
};
function loadStudents() {
	getStudentList(function(students) {
		clearDOM("student-search-list");

		var base = dojo.byId("student-search-list");
		createHtmlListFromList(students, "student-search-list", base,
				buildListItemTextClosure(["pStudentLastName", "pStudentFirstName"]), selectStudent);
	});
};
function selectStudent(studentKey) {
	return function(e) {
		var xhrArgs = {
				url: "http://localhost:8080/scholagest-app/services/student/getProperties",
				preventCash: true,
				content: {token: dojo.cookie("scholagest-token"),
					studentKey: studentKey},
				handleAs: "json",
				load: function(data) {
					if (data.errorCode == null) {
						var domId = 'student-data';
						clearDOM(domId);
						var base = dojo.byId(domId);

						//Create doms for the groups and request for further info for these groups.
						var properties = data.info.properties;
						for (var propertyName in properties) {
							var property = properties[propertyName];
							if (property.isHtmlGroup != null && property.isHtmlGroup == true) {
								if (propertyName == 'pStudentPersonalInfo') {
									createHtmlBaseGroup(base, property.displayText, "student-personal-info");
									getStudentPersonalInfo(studentKey);
								}
								else {
									createHtmlBaseGroup(base, property.displayText, "student-medical-info");
									getStudentMedicalInfo(studentKey);
								}
							}
						}
					}
					else {
						alert("Error during " + getInfoServiceName + ": ("
								+ data.errorCode + ") " + data.message);
					}
				},
				error: function(error) {
					alert("error = " + error);
				}
		}

		var deferred = dojo.xhrGet(xhrArgs);
	};
};
//function selectStudent(studentKey) {
//	return function(e) {
//		var list = dojo.byId('student-search-list');
//		var old = list.selectedStudent;
//		if (old != null)
//			old.className = 'search-list-item';
//
//		this.className = 'search-list-item-selected';
//		list.selectedStudent = this;
//
//		getStudentPersonalInfo(studentKey);
//		getStudentMedicalInfo(studentKey);
//	};
//}
function getStudentPersonalInfo(studentKey) {
	getStudentInfo(studentKey, "getPersonalProperties", "setPersonalProperties", "student-personal-info");
}
function getStudentMedicalInfo(studentKey) {
	getStudentInfo(studentKey, "getMedicalProperties", "setMedicalProperties", "student-medical-info");
}
function getStudentInfo(studentKey, getInfoServiceName, setInfoServiceName, domId) {
	var xhrArgs = {
			url: "http://localhost:8080/scholagest-app/services/student/" + getInfoServiceName,
			preventCash: true,
			content: {token: dojo.cookie("scholagest-token"),
				studentKey: studentKey},
			handleAs: "json",
			load: function(data) {
				if (data.errorCode == null) {
					var base = dojo.byId(domId + "-content-table");

					var info = data.info;
					createInfoHtmlTable(base, info.properties);
					
					var save = dojo.create("button",
							{type: "button", onclick:setStudentInfo(studentKey, domId + "-content-table", setInfoServiceName), innerHTML: "Enregistrer"}, base);
				}
				else {
					alert("Error during " + getInfoServiceName + ": ("
							+ data.errorCode + ") " + data.message);
				}
			},
			error: function(error) {
				alert("error = " + error);
			}
	}

	var deferred = dojo.xhrGet(xhrArgs);
}
function setStudentInfo(studentKey, domId, webServiceName) {
	return function() {
		var keyValues = getKeyValues(domId);
		var xhrArgs = {
				url: "http://localhost:8080/scholagest-app/services/student/" + webServiceName,
				preventCash: true,
				postData: dojo.toJson({
					token: dojo.cookie("scholagest-token"),
					object: {
						key: studentKey,
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
	}
}