function selectClass(classKey) {
	return function(e) {
		var list = dojo.byId('year-search-list');
		var old = list.selectedClass;
		if (old != null)
			old.className = 'search-list-item';

		this.className = 'search-list-item-selected';
		list.selectedClass = this;

		getClassInfo(classKey);
	};
};
function updateClassInfo(classTree, dialogId, trId) {
	return function(e) {
		var dialogElementsList = classTree.model.store.data;
		
		var elementsList = [];
		for (var elementIndex in dialogElementsList) {
			var element = dialogElementsList[elementIndex];
			if (element.id != "root") {
				elementsList.push(element);
			}
		}
		dijit.byId(dialogId).hide();
		
		//Update the displayed class' info.
		dojo.byId(trId).info.infoSetter(elementsList);
	};
}
function moveStudents(sourceTree, destTree) {
	return function(e) {
		var destStore = destTree.model.store;
		
		var selectedItems = sourceTree.selectedItems;
		for (var itemKey in selectedItems) {
			destStore.add(selectedItems[itemKey]);
		}
	};
};
function completeAddRemoveStudents(classStudentList) {
	return function(button) {
		getStudentList(function(students) {
			var studentsRawData = [{name: "root", id: "root"}];
			for (var studentKey in students) {
				var studentData = students[studentKey];
				
				var txt = studentData.properties["pStudentLastName"].value + " " + studentData.properties["pStudentFirstName"].value;
				studentsRawData.push({
					name: txt, id: studentKey, key: studentKey, info: studentData, parent: "root" });
			}
			var classStudentsRawData = [{name: "root", id: "root"}];
			var freeTree = prepareTree(studentsRawData, "free-students");
			var classTree = prepareTree(classStudentsRawData, "class-students");
			dojo.byId('fromFreeToClassStudents').onclick = moveStudents(freeTree, classTree);
			dojo.byId('fromClassToFreeStudents').onclick = moveStudents(classTree, freeTree);
			dojo.byId('btnAddRemoveStudentsOk').onclick = updateClassInfo(classTree, "addRemoveStudents", "trpClassStudents");
		});
		
		dijit.byId("addRemoveStudents").show();
	};
};
function createListButtonsWrapper(completeAddRemoveStudentsButtonClosure) {
	return function createListButtons(propertyName, parentDOM) {
		var button = dojo.create("button", {
			innerHTML: "Ajouter/Supprimer"
		}, parentDOM);
		if (propertyName == "pClassStudents") {
			//completeAddRemoveStudentsButtonClosure(button);
			button.onclick = function(e) { completeAddRemoveStudentsButtonClosure(button); };
		}
		else if (propertyName == "pClassTeachers") {
			button.onclick = function(e) { dijit.byId("addRemoveTeachers").show(); }
		}
	};
};
function setClassInfo(classKey) {
	var keyValues = getKeyValues("class-data");
	
	var xhrArgs = {
			url: "http://localhost:8080/scholagest-app/services/class/setProperties",
			preventCash: true,
			postData: dojo.toJson({
				token: dojo.cookie("scholagest-token"),
				object: {
					key: classKey,
					properties: keyValues
				}
			}),
			handleAs: "json",
			load: function(data) {
				if (data.errorCode != null) {
					alert("Error during getProperties: ("
							+ data.errorCode + ") " + data.message);
				}
			},
			error: function(error) {
				alert("error = " + error);
			}
	}

	var deferred = dojo.xhrPost(xhrArgs);
}
function getClassInfo(classKey) {
	var xhrArgs = {
			url: "http://localhost:8080/scholagest-app/services/class/getProperties",
			preventCash: true,
			content: {token: dojo.cookie("scholagest-token"),
				classKey: classKey},
			handleAs: "json",
			load: function(data) {
				if (data.errorCode == null) {
					var domId = "class-data";
					clearDOM(domId);
					
					var base = dojo.byId(domId);
					createInfoHtmlTable(base, data.info.properties, createListButtonsWrapper(completeAddRemoveStudents([])));

					var save = dojo.create("button",
							{type: "button", onclick:"setClassInfo(\"" + classKey + "\")", innerHTML: "Enregistrer"}, base);
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
function mergeAndDisplayYearAndClassLists(yearList, classList) {
	var liItemsList = [];
	for (var yearKey in yearList) {
		var classes = classList[yearKey];
		var yearInfo = yearList[yearKey];
		
		liItemsList.push(createLIItem(yearKey, yearInfo.properties['pYearName'].value, undefined, 'search-list-item-group-header'));
		
		for (var classIndex in classes) {
			var classInfo = classes[classIndex];
			var classKey = classInfo.key;
			liItemsList.push(createLIItem(classKey, classInfo.properties['pClassName'].value, selectClass(classKey), 'search-list-item'));
		}
	}
	
	createAndFillUL('year-search-list', liItemsList, 'search-list', dojo.byId('year-search-list-div'));
}
function loadClasses(yearList) {
	var yearKeyList = [];
	for (var yearKey in yearList) {
		yearKeyList.push(yearKey);
	}
	
	var xhrArgs = {
			url: "http://localhost:8080/scholagest-app/services/class/getClasses",
			preventCash: true,
			content: {token: dojo.cookie("scholagest-token"),
				properties: ["pClassName"], years: yearKeyList },
			handleAs: "json",
			load: function(data) {
				if (data.errorCode == null) {
					clearDOM("year-search-list-div");
					mergeAndDisplayYearAndClassLists(yearList, data.info);
				}
				else
					alert("Error during getClasses: ("
							+ data.errorCode + ") " + data.message);
			},
			error: function(error) {
				alert("error = " + error);
			}
	}

	var deferred = dojo.xhrGet(xhrArgs);
}
function createClass(yearKey, className) {
	var xhrArgs = {
			url: "http://localhost:8080/scholagest-app/services/class/create",
			preventCash: true,
			content: {token: dojo.cookie("scholagest-token"),
				yearKey: yearKey,
				keys: ["pClassName"], values: [className] },
			handleAs: "json",
			load: function(data) {
				if (data.errorCode == null) {
					
				}
				else
					alert("Error during createClass: ("
							+ data.errorCode + ") " + data.message);
			},
			error: function(error) {
				alert("error = " + error);
			}
	}

	var deferred = dojo.xhrGet(xhrArgs);
}