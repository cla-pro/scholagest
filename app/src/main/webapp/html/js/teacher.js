function getTeacherInfo(teacherKey) {
	var xhrArgs = {
			url: "http://localhost:8080/scholagest-app/services/teacher/getProperties",
			preventCash: true,
			content: {token: dojo.cookie("scholagest-token"),
				teacherKey: teacherKey,
				properties: ["pTeacherFirstName", "pTeacherLastName",
				             "pTeacherMail", "pTeacherPhone", "pTeacherAddress"]},
				             handleAs: "json",
				             load: function(data) {
				            	 if (data.errorCode == null) {
				            		var domId = "teacher-data";
									clearDOM(domId);
									
									var base = dojo.byId(domId);
									createInfoHtml(base, data.info);

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
}
function setTeacherInfo(teacherKey) {
	var keyValues = getKeyValues('teacher-data');
	var xhrArgs = {
			url: "http://localhost:8080/scholagest-app/services/teacher/setProperties",
			preventCash: true,
			content: {token: dojo.cookie("scholagest-token"),
				teacherKey: teacherKey,
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
}
function loadTeachers() {
	var xhrArgs = {
			url: "http://localhost:8080/scholagest-app/services/teacher/getTeachers",
			preventCash: true,
			content: {token: dojo.cookie("scholagest-token"),
				properties: ["pTeacherLastName", "pTeacherFirstName"] },
				handleAs: "json",
				load: function(data) {
					if (data.errorCode == null) {
						clearDOM("teacher-search-list");

						var base = dojo.byId("teacher-search-list");
						var list = data.teachers;
						var ul = dojo.create("ul", {
							id: 'teacher-search-list',
							className: 'search-list'}, base);

						var teachers = data.teachers;
						for (var d in teachers) {
							var t = teachers[d];
							var text = d;
							if (t.pTeacherFirstName != null || t.pTeacherLastName != null) {
								text = '';
								if (t.pTeacherFirstName != null) {
									text = t.pTeacherFirstName;
								}

								if (t.pTeacherLastName != null) {
									if (text != '')
										text += ' ';
									text += t.pTeacherLastName;
								}
							}

							dojo.create("li",
									{ innerHTML: text,
								className: 'search-list-item',
								onclick: selectTeacher(d)}, ul);
						}
					}
					else
						alert("Error during getTeachers: ("
								+ data.errorCode + ") " + data.message);
				},
				error: function(error) {
					alert("error = " + error);
				}
	}

	var deferred = dojo.xhrGet(xhrArgs);
}
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
			preventCash: true,
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
}