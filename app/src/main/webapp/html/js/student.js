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
}

function selectStudent(studentKey) {
	return function(e) {
		var list = dojo.byId('student-search-list');
		var old = list.selectedStudent;
		if (old != null)
			old.className = 'search-list-item';

		this.className = 'search-list-item-selected';
		list.selectedStudent = this;

		getStudentPersonalInfo(studentKey);
		getStudentMedicalInfo(studentKey);
	};
}
function getStudentPersonalInfo(studentKey) {
	getStudentInfo(studentKey, "getPersonalProperties", "setPersonalProperties", "student-personal-info", "pStudentPersonalInfo");
}
function getStudentMedicalInfo(studentKey) {
	getStudentInfo(studentKey, "getMedicalProperties", "setMedicalProperties", "student-medical-info", "pStudentMedicalInfo");
}
function getStudentInfo(studentKey, getInfoServiceName, setInfoServiceName, domId, propertyName) {
	var xhrArgs = {
			url: "http://localhost:8080/scholagest-app/services/student/" + getInfoServiceName,
			preventCash: true,
			content: {token: dojo.cookie("scholagest-token"),
				studentKey: studentKey},
				handleAs: "json",
				load: function(data) {
					if (data.errorCode == null) {
						clearDOM(domId);
						
						var base = dojo.byId(domId);
						
						var save = dojo.create("button",
								{type: "button", onclick:setStudentInfo(studentKey, domId + "-content-table", setInfoServiceName), innerHTML: "Enregistrer"});
						
						var info = data.info[propertyName];
						if (info.isHtmlGroup != null && info.isHtmlGroup == true) {
							createHtmlGroup(base, info.displayText, info.value, save, domId + "-content");
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
}
function setStudentInfo(studentKey, domId, webServiceName) {
	return function() {
		var keyValues = getKeyValues(domId);
		var xhrArgs = {
				url: "http://localhost:8080/scholagest-app/services/student/" + webServiceName,
				preventCash: true,
				content: {token: dojo.cookie("scholagest-token"),
					studentKey: studentKey,
					names: keyValues.keys,
					values: keyValues.values},
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
	
		var deferred = dojo.xhrGet(xhrArgs);
	}
}