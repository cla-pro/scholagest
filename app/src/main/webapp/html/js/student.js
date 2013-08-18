function callGetStudentGrades(students, exams, yearKey, callback) {
	sendGetRequest("../student/getStudentsGrades", { studentKeys: students, examKeys: exams, yearKey: yearKey }, callback);
};
function createStudent(closeId, txtIds) {
	var keyValues = getKeysAndValues(txtIds);

	sendGetRequest("../student/create", { keys: keyValues.keys, values: keyValues.values }, function(info) { loadStudents(); });

	if (closeId != null) {
		var dialog = dijit.byId(closeId);
		resetDiv(dialog.containerNode);
		dialog.hide();
	}
};
function getStudentList(callback) {
	sendGetRequest("../student/getStudents", { properties: ["pStudentLastName", "pStudentFirstName"] }, callback);
};
function getStudentsInfo(studentList, properties, callback) {
	sendGetRequest("../student/getStudentsInfo", { students: studentList, properties: properties }, callback);
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
				url: "../student/getProperties",
				preventCache: true,
				content: {token: dojo.cookie("scholagest_token"),
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
						handleServiceError(data);
					}
				},
				error: function(error) {
					alert("error = " + error);
				}
		}

		var deferred = dojo.xhrGet(xhrArgs);
	};
};
function getStudentPersonalInfo(studentKey) {
	getStudentSubInfo(studentKey, "getPersonalProperties", "setPersonalProperties", "student-personal-info");
}
function getStudentMedicalInfo(studentKey) {
	getStudentSubInfo(studentKey, "getMedicalProperties", "setMedicalProperties", "student-medical-info");
}
function getStudentSubInfo(studentKey, getInfoServiceName, setInfoServiceName, domId) {
	sendGetRequest("../student/" + getInfoServiceName, { studentKey: studentKey }, function(info) {
		var base = dojo.byId(domId + "-content-table");
		createInfoHtmlTable(base, info.properties);
		
		var save = dojo.create("button",
				{type: "button", onclick:setStudentInfo(studentKey, domId + "-content-table", setInfoServiceName), innerHTML: "Enregistrer"}, base);
	});
}
function setStudentInfo(studentKey, domId, webServiceName) {
	return function() {
		var keyValues = getKeyValues(domId);
		sendPostRequest("../student/" + webServiceName, { key: studentKey, properties: keyValues }, function(info) {});
	}
}