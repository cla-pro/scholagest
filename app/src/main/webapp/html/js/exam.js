function createExam(closeId, txtIds) {
	var dialog = dijit.byId(closeId);
	var yearKey = dialog.yearKey;
	var classKey = dialog.classKey;
	var branchKey = dialog.branchKey;
	var periodKey = dialog.periodKey;
	var keyValues = getKeysAndValues(txtIds);

	var parameters = {
			keys: keyValues.keys,
			values: keyValues.values,
			yearKey: yearKey,
			classKey: classKey,
			branchKey: branchKey,
			periodKey: periodKey
		};
	sendGetRequest("../exam/create", parameters, function(info) { loadStudents(); });

	if (closeId != null) {
		dialog.hide();
	}
};

function getExamsInfo(examList, properties, callback) {
	sendGetRequest("../exam/getExamsInfo", { exams: examList, properties: properties }, callback);
};
