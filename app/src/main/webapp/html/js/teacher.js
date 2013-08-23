function getTeacherInfo(teacherKey) {
	sendGetRequest("../teacher/getProperties", { teacherKey: teacherKey }, function(info) {
		var domId = "teacher-data";
		clearDOM(domId);

		var base = dojo.byId(domId);
		createInfoHtmlTable(base, info.properties);

		if (info.writable) {
			if (teacherKey == myOwnTeacherKey) {
				dojo.create("button", {type: "button", onclick:"openChangePasswordDialog(\"" + teacherKey + "\")", innerHTML: "Changer le mot de passe"}, base);
			} else {
				dojo.create("button", {type: "button", onclick:"resetPassword(\"" + teacherKey + "\")", innerHTML: "Réinitialiser le mot de passe"}, base);
			}
			
			dojo.create("button",
					{type: "button", onclick:"setTeacherInfo(\"" + teacherKey + "\")", innerHTML: "Enregistrer"}, base);
		}
	});
};
function resetPassword(teacherKey) {
	if (window.confirm('Etes vous sûr de vouloir réinitialiser le mot de passe?')) {
		sendPostRequest("../user/resetPassword", { teacherKey : teacherKey }, function(info) { alert("Mot de passe réinitialisé"); });
    }
};
function openChangePasswordDialog(teacherKey) {
	var changePasswordDialog = dijit.byId("changePasswordDialog");
	changePasswordDialog.teacherKey = teacherKey;
	changePasswordDialog.show();
};
function saveNewPassword(newPassword, repeatedNewPassword) {
	var changePasswordDialog = dijit.byId("changePasswordDialog");
	var teacherKey = changePasswordDialog.teacherKey;

	if (newPassword != repeatedNewPassword) {
		alert("Le mot de passe n'a pas été répété correctement");
	} else {
		resetDiv(changePasswordDialog.containerNode);
		changePasswordDialog.hide();
		sendPostRequest("../user/setPassword", { teacherKey : teacherKey, password: newPassword }, function(info) { alert("Mot de passe modifié"); });
	}
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

function createTeacher(closeId, teacherTypeSelectId, txtIds) {
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
	
	var teacherTypeSelect = dojo.byId(teacherTypeSelectId);
	var teacherType = teacherTypeSelect.options[teacherTypeSelect.selectedIndex].value;

	sendGetRequest("../teacher/create", { keys: keys, values: values, teacherType: teacherType }, function(info) { loadTeachers(); })

	if (closeId != null) {
		var dialog = dijit.byId(closeId);
		resetDiv(dialog.containerNode);
		dialog.hide();
	}
};