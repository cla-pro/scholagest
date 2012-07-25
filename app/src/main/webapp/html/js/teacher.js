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
				            		 clearDOM("teacher-data");

				            		 var base = dojo.byId('teacher-data');
				            		 dojo.create("h1", {
				            			 innerHTML: ''}, base);
				            		 var table = dojo.create("table", {
				            			 style: "width: 100%"}, base);
				            		 table.id = "teacher-data-table";

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
				            				 {type: "button", onclick:"setTeacherInfo(\"" + teacherKey + "\")"}, base);
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
	var keyValues = getKeyValues('teacher-data-table');
	var xhrArgs = {
			url: "http://localhost:8080/scholagest-app/services/teacher/setProperties",
			preventCash: true,
			content: {token: dojo.cookie("scholagest-token"),
				teacherKey: teacherKey,
				names: keyValues.keys,
				values: keyValues.values},
				             handleAs: "json",
				             load: function(data) {
				            	 if (data.errorCode == null) {
				            		 alert("Ok");
				            	 }
				            	 else {
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

		getTeacherInfo(teacherKey)
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
			url: "http://localhost:8080/scholagest-app/services/teacher/createTeacher",
			preventCash: true,
			content: {token: dojo.cookie("scholagest-token"),
				keys: keys, values: values},
				handleAs: "json",
				load: function(data) {
					if (data.errorCode == null) {
						//alert("Teacher created with id: " + data.teacherKey);
						loadTeachers();
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

	if (closeId != null) {
		dijit.byId(closeId).hide();
	}
}