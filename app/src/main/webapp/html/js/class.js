function mergeAndDisplayYearAndClassLists(yearList, classList) {
	var liItemsList = [];
	for (var yearKey in yearList) {
		var classes = classList[yearKey];
		var yearInfo = yearList[yearKey];
		
		liItemsList.push(createLIItem(yearKey, yearInfo['pYearName'], undefined, 'search-list-item-group-header'));
		
		for (var classKey in classes) {
			var classInfo = classes[classKey];
			liItemsList.push(createLIItem(classKey, classInfo['pClassName'], undefined, 'search-list-item'));
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
				properties: ["pClassName"], yearList: yearKeyList },
				handleAs: "json",
				load: function(data) {
					if (data.errorCode == null) {
						clearDOM("year-search-list-div");
						mergeAndDisplayYearAndClassLists(yearList, data.classes);
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