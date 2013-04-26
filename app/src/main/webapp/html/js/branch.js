function selectBranchWrapper(classKey, yearKey) {
	return function (key) {
		return function(e) {
			getBranchInfo(key, ["pBranchName", "pBranchPeriods"], displayBranchWrapper(classKey, yearKey));
		};
	};
};

function displayBranchWrapper(classKey, yearKey) {
	return function (branchInfo) {
		var periodKeys = branchInfo.properties["pBranchPeriods"].value.values;
		getPeriodsInfo(periodKeys, branchInfo.key, classKey, yearKey, "periods");
	};
};

function loadBranches() {
	var classKey = "http://scholagest.net/class/2012-2013#1P A";
	var yearKey = "http://scholagest.net/year#2012-2013";
	callGetClassInfo(classKey, "pClassBranches", function(info) {
		var xhrArgs = {
				url: "../branch/getPropertiesForList",
				preventCache: true,
				content: {token: dojo.cookie("scholagest_token"),
					branchKeys: info.properties["pClassBranches"].value,
					properties: ["pBranchName"]},
				handleAs: "json",
				load: function(data) {
					if (data.errorCode == null) {
						var info = data.info;
						clearDOM("branch-search-list");

						var base = dojo.byId("branch-search-list");
						createHtmlListFromList(info, "branch-search-list", base,
								buildListItemTextClosure(["pBranchName"]), selectBranchWrapper(classKey, yearKey));
					}
					else {
						handleServiceError(data);
					}
				},
				error: function(error) {
					alert("error = " + error);
				}
		}

		var deferred = dojo.xhrGet(xhrArgs);
	});
};

function getBranchInfo(branchKey, properties, callback) {
	var xhrArgs = {
			url: "../branch/getProperties",
			preventCache: true,
			content: {token: dojo.cookie("scholagest_token"),
				branchKey: branchKey,
				properties: properties},
			handleAs: "json",
			load: function(data) {
				if (data.errorCode == null) {
					var info = data.info;
					callback(info);
				}
				else {
					handleServiceError(data);
				}
			},
			error: function(error) {
				alert("error = " + error);
			}
	}

	var deferred = dojo.xhrGet(xhrArgs);
}

function createBranch(closeId, txtNameId, gradesFlagChkId) {
	var classKey = "http://scholagest.net/class/2012-2013#1P A";
	var className = dojo.byId(txtNameId).value;
	
	var xhrArgs = {
			url: "../branch/create",
			preventCache: true,
			content: {token: dojo.cookie("scholagest_token"),
				classKey: classKey,
				keys: ['pBranchName'],
				values: [className]},
			handleAs: "json",
			load: function(data) {
				if (data.errorCode == null) {
					loadBranches();
				}
				else {
					handleServiceError(data);
				}
			},
			error: function(error) {
				alert("error = " + error);
			}
	}

	var deferred = dojo.xhrGet(xhrArgs);
	if (closeId != null) {
		dijit.byId(closeId).hide();
	}
};