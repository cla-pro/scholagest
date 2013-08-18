var gradePageYearKey = null;
var gradePageClassKey = null;

function selectBranchWrapper(classKey, yearKey) {
	return function (key) {
		return function(e) {
			getBranchInfo(key, ["pBranchName", "pBranchPeriods", "pBranchType"], displayBranchWrapper(classKey, yearKey));
		};
	};
};

function displayBranchWrapper(classKey, yearKey) {
	return function (branchInfo) {
		var periodKeys = branchInfo.properties["pBranchPeriods"].value.values;
		var branchType = branchInfo.properties["pBranchType"].value;
		var isBranchNumerical = branchType == 'NUMERICAL';
		getPeriodsInfo(periodKeys, branchInfo.key, classKey, yearKey, "periods", isBranchNumerical);
	};
};

function loadBranches() {
	sendGetRequest("../year/getCurrent", {}, function(yearInfo) {
		gradePageYearKey = yearInfo.key;
		
		sendGetRequest("../teacher/getClass", { yearKey: gradePageYearKey, teacherKey: myOwnTeacherKey }, function(classInfo) {
			var classesList = classInfo.properties["pTeacherClasses"].value;
			
			clearDOM("branch-search-list");
			var base = dojo.byId("branch-search-list");
			if (classesList.length > 0) {
				gradePageClassKey = classesList[0];
				callGetClassInfo(gradePageClassKey, "pClassBranches", function(info) {
					var parameters = { branchKeys: info.properties["pClassBranches"].value, properties: ["pBranchName"] };
					sendGetRequest("../branch/getPropertiesForList", parameters, function(branchInfo) {
						createHtmlListFromList(branchInfo, "branch-search-list", base,
								buildListItemTextClosure(["pBranchName"]), selectBranchWrapper(gradePageClassKey, gradePageYearKey));
					});
				});
			} else {
				base.innerHTML = 'Aucune classe assignée';
			}
		});
	});
	
	
	
//	var classKey = "http://scholagest.net/class/2012-2013#1P A";
//	var yearKey = "http://scholagest.net/year#2012-2013";
//	
//	callGetClassInfo(classKey, "pClassBranches", function(info) {
//		var parameters = { branchKeys: info.properties["pClassBranches"].value, properties: ["pBranchName"] };
//		sendGetRequest("../branch/getPropertiesForList", parameters, function(branchInfo) {
//			clearDOM("branch-search-list");
//
//			var base = dojo.byId("branch-search-list");
//			createHtmlListFromList(branchInfo, "branch-search-list", base,
//					buildListItemTextClosure(["pBranchName"]), selectBranchWrapper(classKey, yearKey));
//		});
//	});
};

function getBranchInfo(branchKey, properties, callback) {
	sendGetRequest("../branch/getProperties", { branchKey: branchKey, properties: properties }, callback);
}

function createBranch(closeId, txtNameId, gradesFlagChkId) {
	var branchName = dojo.byId(txtNameId).value;
	var branchType = "";
	if (dojo.byId(gradesFlagChkId).checked) {
		branchType = "NUMERICAL";
	} else {
		branchType = "ALPHA_NUMERICAL";
	}
	
	sendGetRequest("../branch/create", {
		classKey: gradePageClassKey,
		keys: ['pBranchName', 'pBranchType'],
		values: [branchName, branchType]
	}, function(info) {
		dijit.byId(closeId).hide(); 
		loadBranches();
	}, function(errorJson) {
		if (errorJson.errorCode == errorCodesMap.OBJECT_ALREADY_EXISTS) {
			alert('Une branche avec le même nom existe déjà dans cette classe');
		}
	});
};