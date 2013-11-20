var gradePageYearKey = null;
var gradePageClassKey = null;
var selectedBranchKey = null;

function selectBranchWrapper(classKey, yearKey) {
	return function (key) {
		selectedBranchKey = key;
		getBranchInfo(key, ["pBranchName", "pBranchPeriods", "pBranchType"], displayBranchWrapper(classKey, yearKey));
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
	var base = dojo.byId("branch-search-list");
	sendPostRequest("../year/getCurrent", {}, function(yearInfo) {
		if (yearInfo == null) {
			base.innerHTML = 'Aucune classe assignée';
			dojo.byId('btnNewBranch').style.visibility = 'hidden';
		} else {
			dojo.byId('btnNewBranch').style.visibility = '';
			gradePageYearKey = yearInfo.key;
			
			sendPostRequest("../teacher/getClass", { teacherKey: myOwnTeacherKey }, function(classInfo) {
				var classesList = classInfo.properties["pTeacherClasses"].value;
				
				clearDOM("branch-search-list");
				if (classesList.length > 0) {
					gradePageClassKey = classesList[0];
					callGetClassInfo(gradePageClassKey, [ "pClassBranches" ], function(info) {
						var parameters = { keys: info.properties["pClassBranches"].value, properties: ["pBranchName"] };
						sendPostRequest("../branch/getBranchesInfo", parameters, function(branchInfo) {
							createHtmlListFromList(branchInfo, "branch-search-list", base,
									buildListItemTextClosure(["pBranchName"]), selectBranchWrapper(gradePageClassKey, gradePageYearKey), selectedBranchKey);
						});
					});
				} else {
					dojo.byId('btnNewBranch').style.visibility = 'hidden';
					base.innerHTML = 'Aucune classe assignée';
				}
			});
		}
	});
};

function getBranchInfo(branchKey, properties, callback) {
	sendPostRequest("../branch/getProperties", { key: branchKey, properties: properties }, callback);
}

function createBranch(closeId, txtNameId, gradesFlagChkId) {
	if (checkRequiredFieldsAndMarkAsMissing(closeId, [txtNameId])) {
		return;
	}
	
	var branchName = dojo.byId(txtNameId).value;
	var branchType = "";
	if (dojo.byId(gradesFlagChkId).checked) {
		branchType = "NUMERICAL";
	} else {
		branchType = "ALPHA_NUMERICAL";
	}
	
	sendPostRequest("../branch/create", {
		classKey: gradePageClassKey,
		keys: ['pBranchName', 'pBranchType'],
		values: [branchName, branchType]
	}, function(info) {
		dijit.byId(closeId).hide(); 
		loadBranches();
	}, function(errorJson) {
		if (errorJson.errorCode == errorCodesMap.OBJECT_ALREADY_EXISTS) {
			displayMessageDialog('Une branche avec le même nom existe déjà dans cette classe');
		}
	});
};