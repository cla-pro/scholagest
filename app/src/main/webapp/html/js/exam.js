function createExam(closeId, txtIds) {
	var dialog = dijit.byId(closeId);
	var yearKey = dialog.yearKey;
	var classKey = dialog.classKey;
	var branchKey = dialog.branchKey;
	var periodKey = dialog.periodKey;
	var keyValues = getKeysAndValues(txtIds);

	var xhrArgs = {
			url: "../exam/create",
			preventCache: true,
			content: {token: dojo.cookie("scholagest_token"),
				keys: keyValues.keys,
				values: keyValues.values,
				yearKey: yearKey,
				classKey: classKey,
				branchKey: branchKey,
				periodKey: periodKey},
				handleAs: "json",
				load: function(data) {
					if (data.errorCode == null) {
						loadStudents();
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
		dialog.hide();
	}
};

function getExamsInfo(examList, properties, callback) {
	var xhrArgs = {
			url: "../exam/getExamsInfo",
			preventCache: true,
			content: {token: dojo.cookie("scholagest_token"),
				exams: examList,
				properties: properties},
			handleAs: "json",
			load: function(data) {
				if (data.errorCode == null) {
					callback(data.info);
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
};
