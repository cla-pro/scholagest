var selectedClassKey = null;

function createClassAndCloseDialog(closeId, txtNewClassNameId) {
	if (checkRequiredFieldsAndMarkAsMissing([txtNewClassNameId])) {
		return;
	}
	
    var yearKey = dijit.byId(closeId).currentYearKey;
    var className = dojo.byId(txtNewClassNameId).value;
    
    createClass(yearKey, className, closeId);
};
function selectClass(classKey) {
	return function(e) {
//		var list = dojo.byId('year-search-list');
//		var old = list.selectedClass;
//		if (old != null)
//			old.className = 'search-list-item';
//
//		this.className = 'search-list-item-selected';
//		list.selectedClass = this;

		getClassInfo(classKey);
	};
};
function getListRealInfo(baseList, trDOM) {
	var propertyName = trDOM.info.propertyName;
	
	var elementsKey = extractKeys(baseList);
	if (propertyName == 'pClassStudents') {
		var properties = ['pStudentFirstName', 'pStudentLastName'];
		getStudentsInfo(baseList, properties, function(students) {
			var converted = convertList(students, properties);
			
			trDOM.info.originalData = converted;
			trDOM.info.infoSetter(converted);
		});
	}
	else if (propertyName == 'pClassTeachers') {
		var properties = ['pTeacherFirstName', 'pTeacherLastName'];
		getTeachersInfo(baseList, properties, function(teachers) {
			var converted = convertList(teachers, properties);
			
			trDOM.info.originalData = converted;
			trDOM.info.infoSetter(converted);
		});
	}
};
function updateClassInfo(freeTree, classTree, dialogId, trId) {
	return function(e) {
		var dialogElementsList = classTree.model.store.data;
		
		var elementsList = [];
		for (var elementIndex in dialogElementsList) {
			var element = dialogElementsList[elementIndex];
			if (element.id != "root") {
				elementsList.push(element);
			}
		}
		
		//Update the displayed class' info.
		dojo.byId(trId).info.infoSetter(elementsList);

		var dialog = dijit.byId(dialogId);
		resetDiv(dialog.containerNode);
		dialog.hide();
	};
};
function removeElements(list, toRemove) {
	var result = [];
	for (var i = 0; i < list.length; i++) {
		var listElement = list[i];
		var seen = false;
		for (var j = 0; j < toRemove.length && !seen; j++) {
			seen = toRemove[j].key == listElement.key;
		}
		
		if (!seen) {
			result.push(listElement);
		}
	}
	
	return result;
};
function completeAddRemoveStudents(trDOM, button) {
	getStudentList(function(students) {
		var classStudents = trDOM.info.infoGetter().value;
		var convertedStudents = convertList(students, ["pStudentFirstName", "pStudentLastName"]);
		var filteredStudents = removeElements(convertedStudents, classStudents)
		var studentsRawData = [{name: "root", id: "root"}].concat(filteredStudents);
		var classStudentsRawData = [{name: "root", id: "root"}].concat(classStudents);
		var freeTree = prepareTree(studentsRawData, "free-students");
		var classTree = prepareTree(classStudentsRawData, "class-students");
		dojo.byId('fromFreeToClassStudents').onclick = moveElements(freeTree, classTree);
		dojo.byId('fromClassToFreeStudents').onclick = moveElements(classTree, freeTree);
		dojo.byId('btnAddRemoveStudentsOk').onclick = updateClassInfo(freeTree, classTree, "addRemoveStudents", "trpClassStudents");
		dijit.byId("addRemoveStudents").on("hide", function() {
			freeTree.destroy();
			classTree.destroy();
		});
	});
	
	dijit.byId("addRemoveStudents").show();
};
function completeAddRemoveTeachers(trDOM, button) {
	getTeacherList(function(teachers) {
		var classTeachers = trDOM.info.infoGetter().value;
		var convertedTeachers = convertList(teachers, ["pTeacherFirstName", "pTeacherLastName"]);
		var filteredTeachers = removeElements(convertedTeachers, classTeachers)
		var teachersRawData = [{name: "root", id: "root"}].concat(filteredTeachers);
		var classTeachersRawData = [{name: "root", id: "root"}].concat(classTeachers);
		var freeTree = prepareTree(teachersRawData, "free-teachers");
		var classTree = prepareTree(classTeachersRawData, "class-teachers");
		dojo.byId('fromFreeToClassTeachers').onclick = moveElements(freeTree, classTree);
		dojo.byId('fromClassToFreeTeachers').onclick = moveElements(classTree, freeTree);
		dojo.byId('btnAddRemoveTeachersOk').onclick = updateClassInfo(freeTree, classTree, "addRemoveTeachers", "trpClassTeachers");
		dijit.byId("addRemoveTeachers").on("hide", function() {
			freeTree.destroy();
			classTree.destroy();
		});
	});
	
	dijit.byId("addRemoveTeachers").show();
};
function createListButtonsWrapper(completeAddRemoveStudentsButtonClosure, completeAddRemoveTeachersButtonClosure) {
	return function createListButtons(propertyName, trDOM, parentDOM) {
		var button = dojo.create("button", {
			innerHTML: "Ajouter/Supprimer"
		}, parentDOM);
		if (propertyName == "pClassStudents") {
			button.onclick = function(e) { completeAddRemoveStudentsButtonClosure(trDOM, button); };
		}
		else if (propertyName == "pClassTeachers") {
			button.onclick = function(e) { completeAddRemoveTeachersButtonClosure(trDOM, button); };
		}
	};
};
function setClassInfo(classKey) {
	var keyValues = getKeyValues("class-data");
	
	sendPostRequest("../class/setProperties", { key: classKey, properties: keyValues }, function(info) {});
}
function getClassInfo(classKey) {
	callGetClassInfo(classKey, null, function(info) {
		selectedClassKey = classKey;
		
		var domId = "class-data";
		clearDOM(domId);
		
		var base = dojo.byId(domId);
		createInfoHtmlTable(base, info.properties, true, createListButtonsWrapper(completeAddRemoveStudents, completeAddRemoveTeachers), getListRealInfo);

		var save = dojo.create("button",
				{type: "button", onclick:"setClassInfo(\"" + classKey + "\")", innerHTML: "Enregistrer"}, base);
	});
}
function callGetClassInfo(classKey, classProperties, callback) {
	var content = {//automatically added token: dojo.cookie("scholagest_token"),
			classKey: classKey};
	if (classProperties != null) {
		content["properties"] = classProperties;
	}
	
	sendGetRequest("../class/getProperties", content, callback);
}
function mergeAndDisplayYearAndClassLists(yearList, classList) {
	var base = dojo.byId('year-search-list-div');
	var liItemsList = [];
	for (var yearKey in yearList) {
		var classes = classList[yearKey];
		var yearInfo = yearList[yearKey];
		
		liItemsList.push(createLIItem(yearKey, yearInfo.properties['pYearName'].value, undefined, 'search-list-item-group-header', base, yearKey));
		
		for (var classIndex in classes) {
			var classInfo = classes[classIndex];
			var classKey = classInfo.key;
			var classNode = createLIItem(classKey, classInfo.properties['pClassName'].value, selectClass(classKey), 'search-list-item', base, classKey);
			if (classKey == selectedClassKey) {
				markAsSelectedListItem(base, classNode);
			}
			liItemsList.push(classNode);
		}
	}
	
	createAndFillUL('year-search-list', liItemsList, 'search-list', base);
}
function loadClasses(yearList) {
	var yearKeyList = [];
	for (var yearKey in yearList) {
		yearKeyList.push(yearKey);
	}
	
	sendGetRequest("../class/getClasses", { properties: ["pClassName"], years: yearKeyList }, function(info) {
		clearDOM("year-search-list-div");
		mergeAndDisplayYearAndClassLists(yearList, info);
	});
}
function createClass(yearKey, className, dialogId) {
	sendGetRequest("../class/create", { yearKey: yearKey, keys: ["pClassName"], values: [className] },
			function(info) { 
				loadClasses(dojo.byId('year-search-list-div').yearsList);
				dijit.byId(dialogId).hide();
			}, function(errorJson) {
				if (errorJson.errorCode == errorCodesMap.OBJECT_ALREADY_EXISTS) {
					displayMessageDialog('Une classe avec le même nom existe déjà dans cette année');
				}
			});
}