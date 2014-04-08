var selectedStudentKey = null;
function callGetStudentGrades(students, exams, yearKey, callback) {
	sendPostRequest("../student/getStudentsGrades", { studentKeys: students, examKeys: exams, yearKey: yearKey }, callback);
};
function createStudent(closeId, txtIds) {
	if (checkRequiredFieldsAndMarkAsMissing(closeId, txtIds)) {
		return;
	}
	
	var keyValues = getKeysAndValues(txtIds);

	sendPostRequest("../student/create", { keys: keyValues.keys, values: keyValues.values }, function(info) { loadStudents(); });

	if (closeId != null) {
		var dialog = dijit.byId(closeId);
		resetDiv(dialog.containerNode);
		dialog.hide();
	}
};
function getStudentList(callback) {
	sendPostRequest("../student/getStudents", { properties: ["pStudentLastName", "pStudentFirstName"] }, callback);
};
function getStudentsInfo(studentList, properties, callback) {
	sendPostRequest("../student/getStudentsInfo", { keys: studentList, properties: properties }, callback);
};
function loadStudents() {
	getStudentList(function(students) {
		clearDOM("student-search-list");

		var base = dojo.byId("student-search-list");
		createHtmlListFromList(students, "student-search-list", base,
				buildListItemTextClosure(["pStudentLastName", "pStudentFirstName"]), selectStudent, selectedStudentKey);
	});
};
function selectStudent(studentKey) {
	sendPostRequest("../student/getProperties", { key: studentKey }, function(info) {
		selectedStudentKey = studentKey;
		
		var domId = 'student-data';
		clearDOM(domId);
		var base = dojo.byId(domId);

		//Create doms for the groups and request for further info for these groups.
		var properties = info.properties;
		for (var propertyName in properties) {
			var property = properties[propertyName];
			if (property.isHtmlGroup != null && property.isHtmlGroup == true) {
				if (propertyName == 'pStudentPersonalInfo') {
					createHtmlBaseGroup(base, property.displayText, "student-personal-info");
					getStudentPersonalInfo(studentKey, info.writable);
				}
				else {
					createHtmlBaseGroup(base, property.displayText, "student-medical-info");
					getStudentMedicalInfo(studentKey, info.writable);
				}
			}
		}
	});
};
function getStudentPersonalInfo(studentKey, writable) {
	getStudentSubInfo(studentKey, "getPersonalProperties", "setPersonalProperties", "student-personal-info", writable);
}
function getStudentMedicalInfo(studentKey, writable) {
	getStudentSubInfo(studentKey, "getMedicalProperties", "setMedicalProperties", "student-medical-info", writable);
}
function getStudentSubInfo(studentKey, getInfoServiceName, setInfoServiceName, domId, writable) {
	sendPostRequest("../student/" + getInfoServiceName, { key: studentKey }, function(info) {
		var base = dojo.byId(domId + "-content-table");
		createInfoHtmlTable(base, info.properties, true);
		
		if (writable) {
			var save = dojo.create("button",
					{type: "button", onclick:setStudentInfo(studentKey, domId + "-content-table", setInfoServiceName), innerHTML: "Enregistrer"}, base);
		}
	});
}
function setStudentInfo(studentKey, domId, webServiceName) {
	return function() {
		var keyValues = getKeyValues(domId);
		sendPostRequest("../student/" + webServiceName, { key: studentKey, properties: keyValues }, function(info) {});
	}
}