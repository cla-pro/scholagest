var selectedTeacherKey = null;

function getTeacherInfo(teacherKey) {
	sendPostRequest("../teacher/getProperties", { key: teacherKey }, function(info) {
		var domId = "teacher-data";
		clearDOM(domId);

		var base = dojo.byId(domId);
		createInfoHtmlTable(base, info.properties, info.writable);
		
		selectedTeacherKey = teacherKey;

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
		sendPostRequest("../user/resetPassword", { teacherKey : teacherKey }, function(info) { displayMessageDialog("Mot de passe réinitialisé"); });
    }
};
function openChangePasswordDialog(teacherKey) {
	var changePasswordDialog = dijit.byId("changePasswordDialog");
	changePasswordDialog.teacherKey = teacherKey;
	changePasswordDialog.show();
};
function saveNewPassword(dialogId, newPassword, repeatedNewPassword) {
	if (checkRequiredFieldsAndMarkAsMissing(dialogId, ["txtNewPassword", "txtNewPasswordRepeat"])) {
		return;
	}
	
	var changePasswordDialog = dijit.byId("changePasswordDialog");
	var teacherKey = changePasswordDialog.teacherKey;

	if (isEmpty(newPassword)) {
		displayMessageDialog("Le mot de passe ne peut pas être vide");
	} else if (newPassword != repeatedNewPassword) {
		displayMessageDialog("Le mot de passe n'a pas été répété correctement");
	} else {
		resetDiv(changePasswordDialog.containerNode);
		changePasswordDialog.hide();
		sendPostRequest("../user/setPassword", { teacherKey : teacherKey, password: newPassword }, function(info) { displayMessageDialog("Mot de passe modifié"); });
	}
};
function setTeacherInfo(teacherKey) {
	var keyValues = getKeyValues('teacher-data');
	sendPostRequest("../teacher/setProperties", { key: teacherKey, properties: keyValues }, function(info) {});
};

function selectTeacher(teacherKey) {
	getTeacherInfo(teacherKey);
};

function getTeacherList(callback) {
	sendPostRequest("../teacher/getTeachers", { properties: ["pTeacherLastName", "pTeacherFirstName"] }, callback);
};

function loadTeachers() {
	getTeacherList(function(teachers) {
		clearDOM("teacher-search-list");

		var base = dojo.byId("teacher-search-list");
		createHtmlListFromList(teachers, "teacher-search-list", base,
				buildListItemTextClosure(["pTeacherLastName", "pTeacherFirstName"]), selectTeacher, selectedTeacherKey);
	});
};

function getTeachersInfo(teacherList, properties, callback) {
	sendPostRequest("../teacher/getTeachersInfo", { keys: teacherList, properties: properties }, callback);
};

function createTeacher(closeId, teacherTypeSelectId, txtIds) {
	if (checkRequiredFieldsAndMarkAsMissing(closeId, txtIds)) {
		return;
	}
	
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

	sendPostRequest("../teacher/create", { keys: keys, values: values, teacherType: teacherType }, function(info) { loadTeachers(); })

	if (closeId != null) {
		var dialog = dijit.byId(closeId);
		resetDiv(dialog.containerNode);
		dialog.hide();
	}
};