function getTeacherInfo(teacherKey) {
	sendGetRequest("../teacher/getProperties", { teacherKey: teacherKey }, function(info) {
		var domId = "teacher-data";
		clearDOM(domId);

		var base = dojo.byId(domId);
		createInfoHtmlTable(base, info.properties);

		var save = dojo.create("button",
				{type: "button", onclick:"setTeacherInfo(\"" + teacherKey + "\")", innerHTML: "Enregistrer"}, base);
	});
};

function setTeacherInfo(teacherKey) {
	var keyValues = getKeyValues('teacher-data');
	sendPostRequest("../teacher/setProperties", { key: teacherKey, properties: keyValues }, function(info) {});
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
	sendGetRequest("../teacher/getTeachers", { properties: ["pTeacherLastName", "pTeacherFirstName"] }, callback);
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
	sendGetRequest("../teacher/getTeachersInfo", { teachers: teacherList, properties: properties }, callback);
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

	sendGetRequest("../teacher/create", { keys: keys, values: values }, function(info) { loadTeachers(); })

	if (closeId != null) {
		dijit.byId(closeId).hide();
	}
};